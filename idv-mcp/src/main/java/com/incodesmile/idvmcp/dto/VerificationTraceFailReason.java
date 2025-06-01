package com.incodesmile.idvmcp.dto;

import java.util.ArrayList;
import java.util.List;

public enum VerificationTraceFailReason {
    ONBOARDING_ID_VALIDATION_FAILED(VerificationFailExternalError.ID_VALIDATION_FAILED),
    ONBOARDING_ID_SCREEN_LIVENESS_FAILED(VerificationFailExternalError.ID_SCREEN_LIVENESS_FAILED),
    ONBOARDING_ID_PAPER_LIVENESS_FAILED(VerificationFailExternalError.ID_PAPER_LIVENESS_FAILED),
    ONBOARDING_ID_SHARPNESS_FAILED(VerificationFailExternalError.ID_SHARPNESS_FAILED),
    ONBOARDING_ID_DOC_CLASSIFICATION(VerificationFailExternalError.ID_TYPE_NOT_IN_VERIFICATION_POLICY),
    ONBOARDING_ID_DOC_EXPIRED(VerificationFailExternalError.ID_EXPIRED),
    ONBOARDING_LIVENESS_FAILED(VerificationFailExternalError.PROOF_OF_PERSONHOOD_FAILED),
    ONBOARDING_FACE_MATCH_FAILED(VerificationFailExternalError.FACE_ID_MATCH_FAILED),
    PHONE_OTP_FAILED(VerificationFailExternalError.PHONE_OTP_FAILED),
    AUTH_LIVENESS_FAILED(VerificationFailExternalError.PROOF_OF_PERSONHOOD_FAILED),
    AUTH_FACE_MATCH_FAILED(VerificationFailExternalError.FACE_AUTH_FAILED),
    EMPLOYEE_NOT_FOUND(VerificationFailExternalError.EMAIL_NOT_FOUND_IN_DIRECTORY),
    EMPLOYEE_NAME_NOT_MATCHED(VerificationFailExternalError.NAME_MATCH_FAILED),
    IDENTITY_BLOCK_LISTED(VerificationFailExternalError.OTHER),
    GOVERNMENT_VALIDATION_FAILED(VerificationFailExternalError.GOVERNMENT_VALIDATION_FAILED),
    /** Time to do actual verification has expired */
    EXPIRED(VerificationFailExternalError.EXPIRED),
    /** Verification link expired */
    LINK_EXPIRED(VerificationFailExternalError.LINK_EXPIRED),
    /** User rejected consent */
    DATA_SHARING_CONSENT_DECLINED(VerificationFailExternalError.DATA_SHARING_CONSENT_NOT_GRANTED),
    /** QR code UUID invalid or expired */
    INVALID_QR(VerificationFailExternalError.INVALID_QR),
    QR_ALREADY_USED(VerificationFailExternalError.QR_ALREADY_USED),
    USER_CONFIRMATION_REJECTED(VerificationFailExternalError.USER_CONFIRMATION),
    OTHER(VerificationFailExternalError.OTHER);

    private final VerificationFailExternalError externalError;

    VerificationTraceFailReason(VerificationFailExternalError externalError) {
        this.externalError = externalError;
    }

    public VerificationFailExternalError getExternalError() {
        return externalError;
    }

    public static List<VerificationTraceFailReason> getByExternalError(VerificationFailExternalError externalError) {
        List<VerificationTraceFailReason> reasons = new ArrayList<>();
        for (VerificationTraceFailReason reason : values()) {
            if (reason.getExternalError() == externalError) {
                reasons.add(reason);
            }
        }
        return reasons;
    }

    public static VerificationFailExternalError getExternalError(VerificationTraceFailReason failReason) {
        return failReason != null ? failReason.getExternalError() : null;
    }
}
