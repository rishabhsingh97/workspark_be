package com.workspark.certificateservice.model.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * DTO representing a certificate template.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO representing a certificate template, including template details and dynamic fields.")
public class TemplateRes implements Serializable {

    @Schema(description = "ID of the template")
    private Long id;

    @Schema(description = "Name of the certificate template")
    private String name;

    @Schema(description = "Unique code for the template")
    private String templateCode;

    @Schema(description = "Base64-encoded preview image of the certificate template")
    private String base64Preview;

}
