package com.mcpexample.demo;

public record TransactionToolResponse(
        boolean success,
        double amount,
        double newBalance,
        String message
) {
    public TransactionToolResponse(TransactionResponse transactionResponse, String message) {
        this(
                transactionResponse.success(),
                transactionResponse.amount(),
                transactionResponse.newBalance(),
                message
        );
    }
}
