package com.workspark.userservice.model.entity;

import com.workspark.models.enitity.BaseAuditFields;
import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "user_role_mapping")
public class UserRoleMappingEntity extends BaseAuditFields {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private UserEntity user;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private UserRolesEntity role;

    @Column(name = "assigned_at")
    private Date assignedAt;
}
