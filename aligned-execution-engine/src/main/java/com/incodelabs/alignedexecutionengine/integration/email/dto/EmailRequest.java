package com.incodelabs.alignedexecutionengine.integration.email.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EmailRequest {
    private String toEmail;
    private String fromEmail;
    private String subject;
    private String content;
}