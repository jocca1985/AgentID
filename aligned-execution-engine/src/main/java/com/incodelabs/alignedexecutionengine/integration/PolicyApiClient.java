package com.incodelabs.alignedexecutionengine.integration;

import com.incodelabs.alignedexecutionengine.integration.dto.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

@Component
@Slf4j
@RequiredArgsConstructor
public class PolicyApiClient {

    private final RestTemplate restTemplate;

    @Value("${policy.api.base-url:http://localhost:8000}")
    private String baseUrl;

    @Value("${policy.api.bearer-token:}")
    private String bearerToken;

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        if (!bearerToken.isEmpty()) {
            headers.setBearerAuth(bearerToken);
        }
        return headers;
    }

    /**
     * Create or replace a policy
     * POST /policies
     */
    public Optional<PolicyResponse> createOrReplacePolicy(PolicyIn policy) {
        try {
            String url = baseUrl + "/policies";
            HttpEntity<PolicyIn> request = new HttpEntity<>(policy, createHeaders());

            log.info("Creating/replacing policy with id: {}", policy.getId());
            ResponseEntity<PolicyResponse> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, PolicyResponse.class);

            log.info("Policy created/replaced successfully: {}", response.getBody());
            return Optional.ofNullable(response.getBody());

        } catch (HttpClientErrorException e) {
            log.error("Error creating/replacing policy: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error creating/replacing policy", e);
            return Optional.empty();
        }
    }

    /**
     * Delete a policy
     * DELETE /policies/{policy_id}
     */
    public boolean deletePolicy(String policyId) {
        try {
            String url = baseUrl + "/policies/" + policyId;
            HttpEntity<?> request = new HttpEntity<>(createHeaders());

            log.info("Deleting policy with id: {}", policyId);
            ResponseEntity<Void> response = restTemplate.exchange(
                    url, HttpMethod.DELETE, request, Void.class);

            boolean success = response.getStatusCode() == HttpStatus.NO_CONTENT;
            log.info("Policy deletion {}: {}", success ? "successful" : "failed", policyId);
            return success;

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Policy not found for deletion: {}", policyId);
            } else {
                log.error("Error deleting policy: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            }
            return false;
        } catch (Exception e) {
            log.error("Unexpected error deleting policy: {}", policyId, e);
            return false;
        }
    }

    /**
     * Validate incoming prompt against policies
     * POST /check/prompt
     */
    public Optional<DecisionOut> checkPrompt(CheckPromptIn checkRequest) {
        try {
            String url = baseUrl + "/check/prompt";
            HttpEntity<CheckPromptIn> request = new HttpEntity<>(checkRequest, createHeaders());

            log.info("Checking prompt against policy: {}", checkRequest.getPolicyId());
            ResponseEntity<DecisionOut> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, DecisionOut.class);

            DecisionOut decision = response.getBody();
            log.info("Prompt check completed with alignment: {}", decision != null ? decision.getAlignment() : "null");
            return Optional.ofNullable(decision);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Policy not found for prompt check: {}", checkRequest.getPolicyId());
            } else {
                log.error("Error checking prompt: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error checking prompt", e);
            return Optional.empty();
        }
    }

    /**
     * Validate LLM output and tool actions against policies
     * POST /check/output
     */
    public Optional<DecisionOut> checkOutput(CheckOutputRequest checkRequest) {
        try {
            String url = baseUrl + "/check/output";
            HttpEntity<CheckOutputRequest> request = new HttpEntity<>(checkRequest, createHeaders());

            log.info("Checking LLM output against policy: {}", checkRequest.getPolicyId());
            ResponseEntity<DecisionOut> response = restTemplate.exchange(
                    url, HttpMethod.POST, request, DecisionOut.class);

            DecisionOut decision = response.getBody();
            log.info("Output check completed with alignment: {}", decision != null ? decision.getAlignment() : "null");
            return Optional.ofNullable(decision);

        } catch (HttpClientErrorException e) {
            if (e.getStatusCode() == HttpStatus.NOT_FOUND) {
                log.warn("Policy not found for output check: {}", checkRequest.getPolicyId());
            } else {
                log.error("Error checking output: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            }
            return Optional.empty();
        } catch (Exception e) {
            log.error("Unexpected error checking output", e);
            return Optional.empty();
        }
    }

    /**
     * Check prompt against all policies (convenience method)
     */
    public Optional<DecisionOut> checkPromptAgainstAllPolicies(String prompt) {
        return checkPrompt(CheckPromptIn.builder()
                .policyId(null) // null means check against all policies
                .prompt(prompt)
                .build());
    }

    /**
     * Check output against all policies (convenience method)
     */
    public Optional<DecisionOut> checkOutputAgainstAllPolicies(String llmOutput, java.util.List<ActionPlan> actions) {
        return checkOutput(CheckOutputRequest.builder()
                .policyId(null) // null means check against all policies
                .llmOutput(llmOutput)
                .build());
    }
}