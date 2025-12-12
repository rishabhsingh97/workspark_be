package com.workspark.userservice.model.entitity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "tenant_user_roles")
public class TenantUserRoles {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "role_name")
	private String roleName;

	@Column(name = "role_desc")
	private String roleDesc;

	@Column(name = "status")
	private Boolean status;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;

//	@ManyToMany(mappedBy = "roles")
//	private List<TenantUsers> users;
}
