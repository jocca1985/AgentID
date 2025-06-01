package com.incodesmile.idvmcp.dto;

public record StepDto(
        StepName eventName,
        boolean executed,
        boolean successful,
        Long createdAt,
        String timePassed
) {}
