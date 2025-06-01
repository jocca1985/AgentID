package com.mcpexample.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


@Component
public class BankApi {
    private static final Logger logger = LoggerFactory.getLogger(BankApi.class);

    @Value("${bank.app.url}")
    private String bankAppUrl;

    private final RestTemplate restTemplate;

    @Autowired
    public BankApi(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public BalanceResponse getBalance() {
        logger.info("Trying to get balance...");
        try {
            ResponseEntity<BalanceResponse> response = restTemplate.getForEntity(
                    bankAppUrl + "/api/balance",
                    BalanceResponse.class
            );
            logger.info("Balance response: {}", response.getBody());
            return response.getBody();
        } catch (Exception error) {
            logger.error("Error getting balance: {}", error.getMessage());
            throw error;
        }
    }

    public TransactionResponse deposit(double amount, String token) {
        logger.info("Trying to deposit {}... with token {}", amount, token);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("amount", amount);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(
                    bankAppUrl + "/api/deposit",
                    entity,
                    TransactionResponse.class
            );

            return response.getBody();
        } catch (Exception error) {
            logger.error("Error making deposit: {}", error.getMessage());
            throw error;
        }
    }

    public TransactionResponse withdraw(double amount, String token) {
        logger.info("Trying to withdraw {}... with token {}", amount, token);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setBearerAuth(token);

            Map<String, Object> requestBody = new HashMap<>();
            requestBody.put("amount", amount);

            HttpEntity<Map<String, Object>> entity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<TransactionResponse> response = restTemplate.postForEntity(
                    bankAppUrl + "/api/withdraw",
                    entity,
                    TransactionResponse.class
            );

            return response.getBody();
        } catch (Exception error) {
            logger.error("Error making withdrawal: {}", error.getMessage());
            throw error;
        }
    }
}
