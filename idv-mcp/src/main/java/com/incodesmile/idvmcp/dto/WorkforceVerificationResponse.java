package com.incodesmile.idvmcp.dto;

public record WorkforceVerificationResponse(
        WorkforceVerificationDto verification,
        DeviceFingerprintInfoDto deviceFingerprint,
        StepsTakenDto stepsTaken
) {}
