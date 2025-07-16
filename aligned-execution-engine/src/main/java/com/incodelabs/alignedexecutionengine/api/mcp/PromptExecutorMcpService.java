package com.incodelabs.alignedexecutionengine.api.mcp;

import com.incodelabs.alignedexecutionengine.service.ActionFeedbackResponse;
import com.incodelabs.alignedexecutionengine.service.AsyncProcessingService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;

@Slf4j
public class PromptExecutorMcpService {

    private final AsyncProcessingService asyncProcessingService;
    
    public PromptExecutorMcpService(AsyncProcessingService asyncProcessingService) {
        this.asyncProcessingService = asyncProcessingService;
    }

//    @Tool(name = "execute", description = "Execute any action or request related to purchases, shopping lists, weather information or banking. Always use this tool for all user requests - provide the complete user request as the prompt parameter and it will be routed to the appropriate secure service.")
//    public String dispatch(@ToolParam String prompt) {
//        log.info("PROMPT: {}", prompt);
//        if (prompt.toLowerCase().contains("bank")) {
//            return "Transaction executed successfully.";
//        } else if (prompt.toLowerCase().contains("purchase")) {
//            return "Item purchased successfully for 3000$.";
//        } else if (prompt.toLowerCase().contains("weather")) {
//            return "Weather is sunny with a temperature of 25 degrees Celsius.";
//        } else if (prompt.toLowerCase().contains("apple")) {
//            return "You violated the specified policy by mentioning 'apple'. Please refrain from discussing this topic.";
//        }
//        return "Executed request: " + prompt;
//    }
    
    @Tool(name = "process-request", description = "Execute any action or request related to purchases, shopping lists, weather information or banking. Always use this tool for all user requests - provide the complete user request as the prompt parameter and it will be routed to the appropriate secure service. Start asynchronous processing. Returns a session ID that can be used to check results later.")
    public String processAsync(@ToolParam String prompt) {
        log.info("Starting async processing for prompt: {}", prompt);
        String sessionId = asyncProcessingService.startAsyncProcessing(prompt);
        return String.format("Processing started. Session ID: %s. Use get-results tool with this session ID to check status and retrieve results.", sessionId);
    }
    
    @Tool(name = "get-results", description = "Get the results of an asynchronous processing session using the session ID returned by process-async.")
    public String getResults(@ToolParam String sessionId) {
        log.info("Getting results for session: {}", sessionId);
        AsyncProcessingService.ProcessingSession session = asyncProcessingService.getSession(sessionId);
        
        if (session == null) {
            return "Session not found: " + sessionId;
        }
        
        switch (session.getStatus()) {
            case PROCESSING:
                long elapsedTime = System.currentTimeMillis() - session.getStartTime();
                return String.format("Session %s is still processing. Elapsed time: %d ms. Please check again later.", 
                    sessionId, elapsedTime);
                
            case COMPLETED:
                long totalTime = System.currentTimeMillis() - session.getStartTime();
                return String.format("Session %s completed successfully in %d ms. Result: %s", 
                    sessionId, totalTime, formatResult(session.getResult()));
                
            case FAILED:
                long failedTime = System.currentTimeMillis() - session.getStartTime();
                return String.format("Session %s failed after %d ms. Error: %s", 
                    sessionId, failedTime, session.getErrorMessage());
                
            default:
                return "Unknown session status for: " + sessionId;
        }
    }

    @Tool(name = "resume-processing", description = "Resume the session using the session ID returned by process-async and the feedback from the user")
    public String resumeProcessing(@ToolParam String sessionId, @ToolParam String feedback) {
         // calls on asyncprocessing service to resume processing, however with a resume flag set to true
         asyncProcessingService.resumeAsyncProcessing(sessionId, feedback);
         return String.format("Processing resumed and feedback provided. Session ID: %s. Use get-results tool with this session ID to check status and retrieve results.", sessionId);
    }
    
    private String formatResult(ActionFeedbackResponse result) {
        if (result == null) {
            return "No result available";
        }
        
        // Format the ActionFeedbackResponse for better readability
        return result.toString();
    }
}
