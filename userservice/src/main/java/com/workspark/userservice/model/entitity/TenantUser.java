package com.workspark.userservice.model.entitity;

import com.workspark.models.enums.Status;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;
import java.util.List;

@Entity
@Data
@Table(name = "tenant_users")
public class TenantUser {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "id")
	private Long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email")
	private String email;

	@Column(name = "phone")
	private String phoneNumber;

	@Column(name = "password_hash")
	private String password;

	@Enumerated(EnumType.STRING)
	@Column(name = "status")
	private Status status;

	@Column(name = "created_at")
	private Date createdAt;

	@Column(name = "updated_at")
	private Date updatedAt;

	@Column(name = "sso")
	private Boolean sso;

	@ManyToMany
	@JoinTable(
			name = "tenant_user_roles_map",
			joinColumns = @JoinColumn(name = "user_id"),
			inverseJoinColumns = @JoinColumn(name = "role_id")
	)
	private List<TenantUserRoles> roles;

//	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
//	private TenantUserRolesMap userRoleMap;

}
