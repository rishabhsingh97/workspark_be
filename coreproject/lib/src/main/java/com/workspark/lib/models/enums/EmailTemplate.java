package com.workspark.lib.models.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum EmailTemplate {
    SIGN_UP("sign-up"),
    FORGOT_PASSWORD("forgot-password"),
    RESET_PASSWORD("reset-password");

    private final String templateName;

    public String getTemplateName() {
        return templateName.concat(".html");
    }

}
