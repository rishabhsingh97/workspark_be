package com.workspark.userservice.model.entitity;

import jakarta.persistence.*;
import lombok.Data;

import java.util.Date;

@Entity
@Data
@Table(name = "tenant_user_roles_map")
public class TenantUserRolesMap {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private TenantUser user;

    @ManyToOne
    @JoinColumn(name = "role_id", referencedColumnName = "id")
    private TenantUserRoles role;

    @Column(name = "assigned_at")
    private Date assignedAt;
}
