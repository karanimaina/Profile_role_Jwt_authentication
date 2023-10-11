package com.jwt.jwtreaciveimpl.model.rbac;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.jwt.jwtreaciveimpl.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

import java.util.Date;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_roles")
public class Roles extends BaseEntity {
    @Column(name = "role_name")
    private String name; // create-user
    @Column(name = "is_active")
    private Boolean isActive;
    private String remarks;
    private Boolean isSystemRole;
}
