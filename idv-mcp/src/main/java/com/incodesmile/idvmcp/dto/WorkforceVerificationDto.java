package com.incodesmile.idvmcp.dto;


public record WorkforceVerificationDto(
        String id,
        String fullName,
        String loginFactorValue,
        Long createdAt,
        Long lastUpdate,
        WorkforceVerificationStatus status,
        VerificationTraceFailReason failReason,
        VerificationFailExternalError externalFailReason,
        String integrationName,
        EnrollmentType verificationType,
        String location
) {}