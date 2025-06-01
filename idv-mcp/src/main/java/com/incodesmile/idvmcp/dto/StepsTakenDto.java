package com.incodesmile.idvmcp.dto;

import java.util.List;

public record StepsTakenDto(
        Long verificationStartedAt,
        String verificationDuration,
        List<StepDto> steps
) {
}
