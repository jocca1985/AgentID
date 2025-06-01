package com.incodesmile.idvmcp.dto;

import lombok.Getter;

@Getter
public enum VerificationFailExternalError {
    ID_VALIDATION_FAILED(VerificationTraceFailReasonGroup.ID_DOC_VALIDATION),
    ID_SCREEN_LIVENESS_FAILED(VerificationTraceFailReasonGroup.ID_DOC_VALIDATION),
    ID_PAPER_LIVENESS_FAILED(VerificationTraceFailReasonGroup.ID_DOC_VALIDATION),
    ID_SHARPNESS_FAILED(VerificationTraceFailReasonGroup.ID_DOC_VALIDATION),
    ID_TYPE_NOT_IN_VERIFICATION_POLICY(VerificationTraceFailReasonGroup.ID_DOC_VALIDATION),
    ID_EXPIRED(VerificationTraceFailReasonGroup.ID_DOC_VALIDATION),
    PROOF_OF_PERSONHOOD_FAILED(VerificationTraceFailReasonGroup.PROOF_OF_PERSONHOOD),
    FACE_ID_MATCH_FAILED(VerificationTraceFailReasonGroup.FACE_RECOGNITION),
    FACE_AUTH_FAILED(VerificationTraceFailReasonGroup.FACE_RECOGNITION),
    PHONE_OTP_FAILED(VerificationTraceFailReasonGroup.PHONE_NUMBER_VERIFICATION),
    DATA_SHARING_CONSENT_NOT_GRANTED(VerificationTraceFailReasonGroup.DATA_CONSENT),
    GOVERNMENT_VALIDATION_FAILED(VerificationTraceFailReasonGroup.GOVERNMENT_VALIDATION),
    EMAIL_NOT_FOUND_IN_DIRECTORY(VerificationTraceFailReasonGroup.EMP_DIR_USER_NOT_FOUND),
    NAME_MATCH_FAILED(VerificationTraceFailReasonGroup.NAME_MATCH),
    EXPIRED(VerificationTraceFailReasonGroup.EXPIRED),
    LINK_EXPIRED(VerificationTraceFailReasonGroup.EXPIRED),
    INVALID_QR(VerificationTraceFailReasonGroup.INVALID_QR),
    QR_ALREADY_USED(VerificationTraceFailReasonGroup.INVALID_QR),
    USER_CONFIRMATION(VerificationTraceFailReasonGroup.USER_CONFIRMATION),
    OTHER(VerificationTraceFailReasonGroup.OTHER);

    private final VerificationTraceFailReasonGroup group;

    VerificationFailExternalError(VerificationTraceFailReasonGroup group) {
        this.group = group;
    }

}
