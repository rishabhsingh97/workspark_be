package com.workspark.userservice.model.entity;

import com.workspark.lib.models.entity.BaseAuditFields;
import com.workspark.models.enums.UserRoleEnum;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@Table(name = "roles")
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Role extends BaseAuditFields {

	@Enumerated(EnumType.STRING)
	private UserRoleEnum name;
	private String description;
	private int status;
}
