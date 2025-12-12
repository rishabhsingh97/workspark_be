package com.workspark.certificateservice.model.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.Map;

/**
 * DTO representing a certificate with dynamic data fields.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Schema(description = "DTO representing a certificate, including template ID, dynamic fields, and certificate type.")
public class CertificateReq {

    @NotNull(message = "Template ID cannot be null")
    @Schema(description = "ID of the certificate template")
    private Long templateId;

    @Schema(description = "Map of dynamic field data for the certificate, where keys are field names and values are the field values")
    private Map<String, Object> dynamicFieldData = new HashMap<>();

    @NotBlank(message = "type cannot be null")
    @Schema(description = "Type of the certificate, such as 'pdf', 'image/jpg', 'image/png, etc.")
    private String type;
}
