package com.incodelabs.alignedexecutionengine.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

@Configuration
public class RestTemplateConfig {

    @Bean
    public RestTemplate restTemplate() {
        // JD - only supports HTTP/1.1
        return new RestTemplate(new SimpleClientHttpRequestFactory());
    }


//    @Bean
//    public RestTemplate restTemplate(RestTemplateBuilder builder) {
//        return builder
//                .connectTimeout(Duration.ofSeconds(350))
//                .readTimeout(Duration.ofSeconds(350))
//                .build();
//    }

}

