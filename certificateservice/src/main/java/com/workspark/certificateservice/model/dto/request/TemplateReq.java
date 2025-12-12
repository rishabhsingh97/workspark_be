package com.workspark.certificateservice.model.dto.request;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * DTO representing a certificate template.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "DTO representing a certificate template, including template details and dynamic fields.")
public class TemplateReq implements Serializable {

    @NotBlank(message = "Template name can not be blank")
    @Schema(description = "Name of the certificate template.")
    private String name;

    private List<String> dynamicFields = Collections.emptyList();

    @NotBlank(message = "Template code can not be blank")
    @Schema(description = "Unique code for the template.")
    private String templateCode;

}
