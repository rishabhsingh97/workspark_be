package com.workspark.certificateservice.service.impl;

import com.workspark.certificateservice.exceptions.customExceptions.JasperException;
import com.workspark.certificateservice.model.entity.TemplateAsset;
import com.workspark.certificateservice.service.ReportService;
import lombok.extern.slf4j.Slf4j;
import net.sf.jasperreports.engine.*;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class implements the ReportService interface and provides methods for generating
 * certificate PDFs and images from Jasper reports.
 */
@Slf4j
@Service
public class ReportServiceJasperImpl implements ReportService {

    /**
     * Compiles a Jasper template file.
     *
     * @param file the MultipartFile containing the Jasper template
     * @return a byte array representing the compiled template
     */
    @Override
    public byte[] compileTemplateFile(MultipartFile file) {
        try (ByteArrayInputStream jrXmlStream = new ByteArrayInputStream(file.getBytes())) {

            JasperReport jasperReport = JasperCompileManager.compileReport(jrXmlStream);

            try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                 ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteArrayOutputStream)) {
                objectOutputStream.writeObject(jasperReport);
                log.info("Template file compiled successfully.");
                return byteArrayOutputStream.toByteArray();
            }

        } catch (JRException | IOException e) {
            log.error("Error compiling template file: {}", e.getMessage(), e);
            throw new JasperException("Error compiling template file: ".concat(e.getMessage()));
        }
    }

    /**
     * Generates a certificate PDF from a Jasper template and dynamic fields.
     *
     * @param pdfSourceData     the byte array representing the Jasper template
     * @param dynamicFieldsData a map containing dynamic fields and their values
     * @param assets            a list of assets to be included in the certificate
     * @return a byte array representing the generated certificate PDF
     */
    public byte[] generateCertificatePdf(byte[] pdfSourceData, Map<String, Object> dynamicFieldsData, List<TemplateAsset> assets) {
        Map<String, Object> parameters = prepareParameters(dynamicFieldsData, assets);

        try (InputStream jrXmlStream = new ByteArrayInputStream(pdfSourceData);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jrXmlStream, parameters, new JREmptyDataSource());
            JasperExportManager.exportReportToPdfStream(jasperPrint, byteArrayOutputStream);
            log.info("Certificate PDF generated successfully.");
            return byteArrayOutputStream.toByteArray();
        } catch (IOException | JRException e) {
            log.error("Error generating certificate PDF: {}", e.getMessage(), e);
            throw new JasperException("Error generating certificate PDF: " + e.getMessage());
        }
    }

    /**
     * Generates a certificate image from a Jasper template and dynamic fields.
     *
     *  @param pdfSourceData     the byte array representing the Jasper template
     * @param dynamicFieldsData     a map containing dynamic fields and their values
     * @param assets            a list of assets to be included in the certificate
     * @param type              the image type (e.g., "jpg", "png")
     * @return  a byte array representing the generated certificate image
     */
    public byte[] generateCertificateImage(byte[] pdfSourceData, Map<String, Object> dynamicFieldsData, List<TemplateAsset> assets, String type) {
        Map<String, Object> parameters = prepareParameters(dynamicFieldsData, assets);

        try (InputStream jrXmlStream = new ByteArrayInputStream(pdfSourceData);
             ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream()) {
            JasperPrint jasperPrint = JasperFillManager.fillReport(jrXmlStream, parameters, new JREmptyDataSource());

            BufferedImage image = (BufferedImage) JasperPrintManager.printPageToImage(jasperPrint, 0, 2.0f);
            ImageIO.write(image, type, byteArrayOutputStream);
            log.info("Certificate image generated successfully.");
            return byteArrayOutputStream.toByteArray();

        } catch (IOException | JRException e) {
            log.error("Error generating certificate image: {}", e.getMessage(), e);
            throw new JasperException("Error generating certificate image: " + e.getMessage());
        }
    }

    /**
     * Prepares parameters for Jasper reports by adding dynamic fields and processing assets.
     *
     * @param dynamicFieldsData the dynamic fields to be included in the parameters
     * @param assets            the list of assets to be added to the parameters
     * @return a map of parameters
     */
    private Map<String, Object> prepareParameters(Map<String, Object> dynamicFieldsData, List<TemplateAsset> assets) {
        Map<String, Object> parameters = new HashMap<>(dynamicFieldsData);
        assets.parallelStream()
                .filter(asset -> !asset.getName().equals("template"))
                .forEach(asset -> {
                    try (ByteArrayInputStream assetStream = new ByteArrayInputStream(
                            asset.getAssetData().getBytes(1, (int) asset.getAssetData().length()))) {
                        parameters.put(asset.getName(), assetStream);
                        log.debug("Asset '{}' added to parameters.", asset.getName());
                    } catch (SQLException | IOException e) {
                        log.error("Error processing asset '{}': {}", asset.getName(), e.getMessage(), e);
                        throw new JasperException("Error processing asset: " + e.getMessage());
                    }
                });
        log.info("Parameters prepared successfully with {} dynamic fields and {} assets.",
                dynamicFieldsData.size(), assets.size());
        return parameters;
    }
}
