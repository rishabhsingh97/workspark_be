package com.workspark.models.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

import java.io.Serializable;


@Data
public class BaseTenantDto implements Serializable {

    @JsonInclude(JsonInclude.Include.NON_EMPTY)
    private String id;

}
