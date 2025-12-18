package com.workspark.certificateservice.controller.impl;

import com.workspark.certificateservice.controller.TemplateController;
import com.workspark.certificateservice.model.dto.request.TemplateReq;
import com.workspark.certificateservice.model.dto.response.TemplateRes;
import com.workspark.certificateservice.service.TemplateService;
import com.workspark.models.response.BaseRes;
import com.workspark.models.response.BasePageRes;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

/**
 * Controller for managing certificate templates and generating certificates.
 * Provides endpoints for uploading templates, retrieving templates, and generating certificates.
 */
@Slf4j
@RequiredArgsConstructor
@RestController
@Tag(name = "Template Management", description = "APIs for managing certificate templates")
public class TemplateControllerImpl implements TemplateController {

    private final TemplateService templateService;

    /**
     * Endpoint to upload a new certificate template.
     *
     * @param templateReq the code or identifier of the template
     * @param assets      the assets (files) associated with the template
     * @return the uploaded template details
     */
    public ResponseEntity<BaseRes<TemplateRes>> uploadTemplate(TemplateReq templateReq, Map<String, MultipartFile> assets) {

        log.info("Received request to upload template with name: {}", templateReq.getName());
        log.debug("Template request details: {}", templateReq);

        log.info("Calling service to upload template...");
        TemplateRes templateDto = templateService.uploadTemplate(templateReq, assets);
        log.info("Template uploaded successfully with ID: {}", templateDto.getId());
        return BaseRes.success(templateDto, "", HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve all templates.
     * This method is paginated and returns a list of TemplateDto objects.
     *
     * @param page the page number to retrieve
     * @param size the number of templates per page
     * @return a list of all templates
     */
    public ResponseEntity<BaseRes<BasePageRes<TemplateRes>>> getAllTemplates(int page, int size) {
        log.info("Received request to fetch templates for page: {}, with size: {}", page, size);

        log.info("Fetching templates...");
        BasePageRes<TemplateRes> paginatedTemplatesList = templateService.getAllTemplates(page, size);
        log.info("Fetched {} templates for page: {}, size: {}",
                paginatedTemplatesList.getPageSize(), page, size);
        return BaseRes.success(paginatedTemplatesList, "", HttpStatus.OK);
    }

    /**
     * Endpoint to retrieve a template by its ID.
     *
     * @param id the ID of the template to retrieve
     * @return the template details
     */
    @Override
    public ResponseEntity<BaseRes<TemplateRes>> getTemplatesById(Long id) {
        log.info("Received request to fetch template with id: {}", id);
        log.info("Fetching template by id...");
        TemplateRes template = templateService.getTemplateById(id);
        return BaseRes.success(template, "", HttpStatus.OK);
    }

    /**
     * Endpoint to generate a certificate preview for a given template ID.
     *
     * @param id the ID of the template
     * @return the generated certificate preview
     */
    public ResponseEntity<byte[]> downloadById(Long id) {
        log.info("Received request to generate certificate preview for template ID: {}", id);

        log.info("Calling service to generate preview for template ID: {}", id);
        byte[] pdf = templateService.downloadTemplateById(id);
        log.info("Generated certificate preview for template ID: {}", id);

        return ResponseEntity.ok()
                .header("Content-Disposition", "attachment; filename=certificate.jpg")
                .contentType(MediaType.IMAGE_JPEG)
                .body(pdf);
    }

}
