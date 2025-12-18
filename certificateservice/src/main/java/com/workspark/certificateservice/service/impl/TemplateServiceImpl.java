package com.workspark.certificateservice.service.impl;

import com.workspark.certificateservice.exceptions.customExceptions.JasperException;
import com.workspark.certificateservice.exceptions.customExceptions.TemplateException;
import com.workspark.certificateservice.model.dto.request.TemplateReq;
import com.workspark.certificateservice.model.dto.response.TemplateRes;
import com.workspark.certificateservice.model.entity.TemplateAsset;
import com.workspark.certificateservice.model.entity.Template;
import com.workspark.certificateservice.model.entity.TemplateDynamicField;
import com.workspark.certificateservice.repository.TemplateAssetRepository;
import com.workspark.certificateservice.repository.TemplateDynamicFieldRepository;
import com.workspark.certificateservice.repository.TemplateRepository;
import com.workspark.certificateservice.service.ReportService;
import com.workspark.certificateservice.service.TemplateService;
import com.workspark.models.response.BasePageRes;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.*;

/**
 * Service class for managing certificate templates and generating certificates.
 * Handles CRUD operations for templates and integrates with JasperReports for PDF generation.
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class TemplateServiceImpl implements TemplateService {

    private final ReportService reportService;
    private final TemplateRepository templateRepository;
    private final TemplateAssetRepository templateAssetRepository;
    private final TemplateDynamicFieldRepository templateDynamicFieldRepository;

    /**
     * Uploads a new certificate template.
     *
     * @param templateReq the request containing the template details
     * @param assets      the map of assets used in the template
     * @return the saved Template entity
     */
    @Transactional
    public TemplateRes uploadTemplate(TemplateReq templateReq, Map<String, MultipartFile> assets) {
        log.info("Uploading new template with name: {}", templateReq.getName());

        Blob compiledTemplateBlob = compileTemplate(assets.get("template"));

        List<TemplateAsset> assetList = processAssets(assets);

        Blob previewBlob = generatePreviewImage(templateReq, compiledTemplateBlob, assetList);

        Template template = saveTemplateToDatabase(templateReq, compiledTemplateBlob, previewBlob, assetList);

        log.info("Template uploaded successfully with ID: {}", template.getId());

        return TemplateRes.builder()
                .id(template.getId())
                .name(templateReq.getName())
                .templateCode(templateReq.getTemplateCode())
                .build();
    }

    /**
     * Retrieves all uploaded templates.
     * This method is paginated and returns a list of TemplateDto objects.
     *
     * @param page the page number to retrieve
     * @param size the number of templates per page
     * @return paginatedRes<TemplateRes> containing the paginated list of templates
     */
    public BasePageRes<TemplateRes> getAllTemplates(int page, int size) {
        log.info("Fetching all templates from the database (Page: {}, Size: {})", page, size);

        Page<Template> routeDataPage = templateRepository.findAll(PageRequest.of(page - 1, size));

        return BasePageRes.<TemplateRes>builder()
                .data(routeDataPage.getContent().stream().map(this::mapToTemplateRes).toList())
                .pageNo(page)
                .pageSize(routeDataPage.getSize())
                .totalPages(routeDataPage.getTotalPages())
                .totalCount(routeDataPage.getTotalElements())
                .build();
    }

    /**
     * Retrieves a template by its ID.
     *
     * @param id the ID of the template to retrieve
     * @return the TemplateRes object representing the template
     */
    @Override
    public TemplateRes getTemplateById(Long id) {
        log.info("Fetching template with ID: {}", id);
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Template not found with Id: {}", id);
                    return new TemplateException("Template not found", HttpStatus.NOT_FOUND);
                });
        return mapToTemplateRes(template);
    }

    /**
     * Downloads a certificate template by its ID.
     *
     * @param id the ID of the template
     * @return the template file as a byte array
     */
    public byte[] downloadTemplateById(Long id) {
        log.info("Fetching template to download with Id: {}", id);
        Template template = templateRepository.findById(id)
                .orElseThrow(() -> {
                    log.error("Template not found with Id: {}", id);
                    return new TemplateException("Template not found", HttpStatus.NOT_FOUND);
                });

        try {
            return template.getPreviewFile()
                    .getBytes(1, (int) template.getPreviewFile().length());
        } catch (SQLException e) {
            log.error("Error reading preview file for template ID {}: {}", id, e.getMessage(), e);
            throw new TemplateException(e.getMessage());
        }
    }

    /**
     * Compiles the template file.
     *
     * @param templateFile the template file to be compiled
     * @return the compiled template file as a Blob
     */
    private Blob compileTemplate(MultipartFile templateFile) {
        try {
            log.info("Compiling template file.");
            byte[] compiledData = reportService.compileTemplateFile(templateFile);
            return new SerialBlob(compiledData);
        } catch (SQLException e) {
            log.error("Error creating blob for compiled template: {}", e.getMessage(), e);
            throw new TemplateException(e.getMessage());
        }
    }

    /**
     * Processes the assets used in the template.
     *
     * @param assets the map of assets used in the template
     * @return the list of TemplateAsset entities
     */
    private List<TemplateAsset> processAssets(Map<String, MultipartFile> assets) {
        log.info("Processing assets for template.");
        List<TemplateAsset> assetList = new ArrayList<>();
        assets.forEach((key, value) -> {
            try {
                log.debug("Processing asset: {}", key);
                byte[] assetData = value.getBytes();
                assetList.add(TemplateAsset.builder()
                        .name(key)
                        .assetData(new SerialBlob(assetData))
                        .build());
            } catch (Exception e) {
                log.error("Error processing asset '{}': {}", key, e.getMessage(), e);
                throw new TemplateException("Error processing asset: " + e.getMessage());
            }
        });
        return assetList;
    }

    /**
     * Generates a preview image for the template.
     *
     * @param templateReq          the request containing the template details
     * @param compiledTemplateBlob the compiled template file
     * @param assetList            the list of assets used in the template
     * @return the preview image as a Blob
     */
    private Blob generatePreviewImage(TemplateReq templateReq, Blob compiledTemplateBlob, List<TemplateAsset> assetList) {
        log.info("Generating preview image for template.");
        try {
            Map<String, Object> dynamicFieldsData = new HashMap<>();
            templateReq.getDynamicFields().forEach(field -> dynamicFieldsData.put(field, field));

            byte[] previewData = reportService.generateCertificateImage(
                    compiledTemplateBlob.getBytes(1, (int) compiledTemplateBlob.length()),
                    dynamicFieldsData,
                    assetList,
                    "jpg");
            return new SerialBlob(previewData);
        } catch (SQLException e) {
            log.error("Error creating blob for preview image: {}", e.getMessage(), e);
            throw new TemplateException(e.getMessage());
        }
    }

    /**
     * Saves the template to the database.
     *
     * @param templateReq          the request containing the template details
     * @param compiledTemplateBlob the compiled template file
     * @param previewBlob          the preview image
     * @param assetList            the list of assets used in the template
     * @return the saved Template entity
     */
    @Transactional
    public Template saveTemplateToDatabase(TemplateReq templateReq, Blob compiledTemplateBlob, Blob previewBlob, List<TemplateAsset> assetList) {
        log.info("Saving template to the database.");

        Template template = Template.builder()
                .name(templateReq.getName())
                .fileData(compiledTemplateBlob)
                .previewFile(previewBlob)
                .templateCode(templateReq.getTemplateCode())
                .build();


        Template savedTemplate = templateRepository.save(template);

        List<TemplateDynamicField> templateDynamicFields = templateReq.getDynamicFields()
                .stream()
                .map(s ->
                        TemplateDynamicField
                                .builder()
                                .dynamicField(s)
                                .template(template)
                                .build())
                .toList();

        List<TemplateAsset> templateAssets = assetList.stream()
                .map(asset -> {
                    asset.setTemplate(template);
                    return asset;
                })
                .toList();

        templateDynamicFieldRepository.saveAll(templateDynamicFields);
        templateAssetRepository.saveAll(templateAssets);
        return savedTemplate;
    }

    /**
     * Maps a Template entity to a TemplateRes DTO.
     *
     * @param template the Template entity
     * @return the TemplateRes DTO
     */
    private TemplateRes mapToTemplateRes(Template template) {
        try {
            log.debug("Mapping template ID {} to response object.", template.getId());
            return TemplateRes.builder()
                    .id(template.getId())
                    .name(template.getName())
                    .templateCode(template.getTemplateCode())
                    .base64Preview(Base64.getEncoder().encodeToString(
                            template.getPreviewFile().getBytes(1, (int) template.getPreviewFile().length())))
                    .build();
        } catch (SQLException e) {
            log.error("Error mapping template ID {}: {}", template.getId(), e.getMessage(), e);
            throw new JasperException(e.getMessage());
        }
    }
}
