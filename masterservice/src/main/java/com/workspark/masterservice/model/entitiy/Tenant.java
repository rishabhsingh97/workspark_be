package com.workspark.masterservice.model.entitiy;


import com.workspark.lib.models.entity.BaseAuditFields;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "tenant")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant extends BaseAuditFields {

    private String name;
    private String domain;
    private String db;
    private String email;
    private int status;

}
