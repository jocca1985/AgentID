package com.mcpexample.demo;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.concurrent.ConcurrentHashMap;

@Service
public class BankMcpService {
    private static final Logger logger = LoggerFactory.getLogger(BankMcpService.class);
    private static final ConcurrentHashMap<String, String> tokenCache = new ConcurrentHashMap<>();

    @Autowired
    private BankApi bankApi;

    @Autowired
    private IdentityVerificationApi idvApi;

    @Tool(name = "check-balance", description = "Checks balance in Acme bank account")
    public BalanceResponse getBalance() {
        logger.info("mcp - getting balance...");
        return bankApi.getBalance();
    }




    @Tool(name = "deposit", description = "Deposit money into your bank account")
    public TransactionToolResponse deposit(@ToolParam Double amount, @ToolParam String token) {
        logger.info("mcp - depositing {}...", amount);
        if (!StringUtils.hasText(token)) {
            return new TransactionToolResponse(
                    false,
                    0.0,
                    0.0,
                    "To make a deposit, valid token is required. Check if there is existing token obtained from identity verification. If not, you need to verify your identity first. If token is not present, Please use the verify-identity-for-banking tool with your email."
            );
        }

        var validation = idvApi.validateToken(token);
        if (!validation.valid()) {
            return new TransactionToolResponse(
                    false,
                    0.0,
                    0.0,
                    "Your token has expired. You need to verify your identity again to make a deposit."
            );
        }

        try {
            var txResponse = bankApi.deposit(amount, token);
            logger.info("mcp - deposit response: {}", txResponse);
            return new TransactionToolResponse(
                    true,
                    txResponse.amount(),
                    txResponse.newBalance(),
                    "Deposit successful."
            );
        } catch (Exception e) {
            logger.error("mcp - deposit error: {}", e.getMessage());
            return new TransactionToolResponse(
                    false,
                    0.0,
                    0.0,
                    "Deposit failed. Error: " + e.getMessage()
            );
        }
    }

    @Tool(name = "withdraw", description = "Withdraw money from your bank account")
    public TransactionToolResponse withdraw(@ToolParam Double amount, @ToolParam String token) {
        logger.info("mcp - withdrawing {}...", amount);
        if (!StringUtils.hasText(token)) {
            return new TransactionToolResponse(
                    false,
                    0.0,
                    0.0,
                    "To make a withdrawal, valid token is required. Check if there is existing token obtained from identity verification. If not, you need to verify your identity first. If token is not present, Please use the verify-identity-for-banking tool with your email."
            );
        }

        var validation = idvApi.validateToken(token);
        if (!validation.valid()) {
            return new TransactionToolResponse(
                    false,
                    0.0,
                    0.0,
                    "Your token has expired. You need to verify your identity again to make a withdrawal."
            );
        }

        try {
            var txResponse = bankApi.withdraw(amount, token);
            logger.info("mcp - withdraw response: {}", txResponse);
            return new TransactionToolResponse(
                    true,
                    txResponse.amount(),
                    txResponse.newBalance(),
                    "Withdrawal successful."
            );
        } catch (Exception e) {
            logger.error("mcp - withdraw error: {}", e.getMessage());
            return new TransactionToolResponse(
                    false,
                    0.0,
                    0.0,
                    "Withdrawal failed. Error: " + e.getMessage()
            );
        }
    }

    @Tool(name = "store-token", description = "Store JWT auth token for deposits and withdrawals")
    public StoreTokenToolResponse storeToken(@ToolParam String token) {
        logger.info("mcp - store token {}", token);
        var validation = idvApi.validateToken(token);
        logger.info("mcp - store token validation: {}", validation);
        if (validation.valid()) {
            tokenCache.put(validation.userId(), token);
            return new StoreTokenToolResponse(
                    true,
                    false,
                    "Token stored successfully."
            );
        } else {
            return new StoreTokenToolResponse(
                    false,
                    true,
                    "Failed to store token. Please obtain a valid token through identity verification."
            );
        }
    }
}