package com.incodelabs.alignedexecutionengine.integration.email;

import com.incodelabs.alignedexecutionengine.integration.email.dto.EmailRequest;
import com.incodelabs.alignedexecutionengine.integration.email.dto.EmailResponse;

public interface EmailClientApi {
    EmailResponse sendEmail(EmailRequest emailRequest);
}