package com.workspark.certificateservice.service;

import com.workspark.certificateservice.model.dto.request.TemplateReq;
import com.workspark.certificateservice.model.dto.response.TemplateRes;
import com.workspark.models.response.BasePageRes;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * Service class for managing certificate templates and generating certificates.
 * Handles CRUD operations for templates and integrates with JasperReports for PDF generation.
 */
@Service
public interface TemplateService {

    /**
     * Uploads a new certificate template.
     *
     * @param templateReq   the request containing the template details
     * @param assets        the map of assets used in the template
     * @return the saved Template entity
     */
    TemplateRes uploadTemplate(@RequestParam TemplateReq templateReq, Map<String, MultipartFile> assets);

    /**
     * Retrieves all uploaded templates.
     * This method is paginated and returns a list of TemplateDto objects.
     *
     * @param page the page number to retrieve
     * @param size the number of templates per page
     * @return a list of all templates
     */
    BasePageRes<TemplateRes> getAllTemplates(int page, int size);

    /**
     * Downloads a certificate template by its ID.
     *
     * @param id the ID of the template
     * @return the template file as a byte array
     */
    TemplateRes getTemplateById(Long id);

    /**
     * Downloads a certificate template by its ID.
     *
     * @param id the ID of the template
     * @return the template file as a byte array
     */
    byte[] downloadTemplateById(Long id);

}
