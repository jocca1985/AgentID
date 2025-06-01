package com.mcpexample.demo;

public record TransactionResponse(boolean success, double amount, double newBalance){}
