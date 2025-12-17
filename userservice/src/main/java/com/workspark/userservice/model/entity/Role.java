package com.workspark.userservice.model.entity;

import com.workspark.models.enitity.BaseAuditFields;
import com.workspark.models.enums.UserRole;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.util.Date;

@Entity
@Data
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleEntity extends BaseAuditFields {

	@Enumerated(EnumType.STRING)
	private UserRole roleName;
	private String roleDesc;
	private Boolean status;

}
