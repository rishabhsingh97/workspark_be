package com.workspark.userservice.model.entity;

import com.workspark.models.enitity.BaseAuditFields;
import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Table(name = "users")
public class UserEntity extends BaseAuditFields {

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "password_hash")
	private String passwordHash;

	@ManyToMany
	@JoinTable(
			name = "tenant_user_roles_map",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private List<RoleEntity> roles;


}
