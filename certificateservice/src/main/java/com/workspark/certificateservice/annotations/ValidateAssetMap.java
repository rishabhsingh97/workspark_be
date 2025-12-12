package com.workspark.certificateservice.annotations;

import com.workspark.certificateservice.validation.CustomAssetMapValidator;
import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CustomAssetMapValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidateAssetMap {

    String message() default "The 'template' key is missing from the dynamicFieldData map.";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}

