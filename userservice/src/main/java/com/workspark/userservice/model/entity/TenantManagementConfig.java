package com.workspark.userservice.model.entitity;

import java.util.Date;

import jakarta.persistence.*;
import lombok.Data;


@Entity
@Data
@Table(name = "tenant_config")
public class TenantManagementConfig {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "tenant_id")
	private Long tenantId;

	@Column(name = "tenant_name")
	private String tenantName;

	@Column(name = "tenant_domain")
	private String tenantDomain;

	@Column(name = "tenant_db_schema")
	private String tenantDbSchemaName;

	@Column(name = "status")
	private boolean status;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;

}
