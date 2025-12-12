package com.workspark.certificateservice.validation;

import com.workspark.certificateservice.annotations.ValidateAssetMap;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Slf4j
@Component
public class CustomAssetMapValidator implements ConstraintValidator<ValidateAssetMap, Map<String, MultipartFile>> {

    @Override
    public boolean isValid(Map<String, MultipartFile> value, ConstraintValidatorContext context) {
        if (value == null) {
            return false;
        }
        log.info("value: {}", value.keySet());
        return value.containsKey("template");
    }
}
