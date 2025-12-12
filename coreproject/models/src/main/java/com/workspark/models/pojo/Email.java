package com.workspark.models.pojo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Email {
    private String toEmail;
    private String fromEmail;
    private String toName;
    private String fromName;
    private String subject;
    private String message;
    private String template;
    private Map<String, Object> parameters;
}