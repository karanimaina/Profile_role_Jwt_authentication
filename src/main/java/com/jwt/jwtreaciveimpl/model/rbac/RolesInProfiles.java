package com.jwt.jwtreaciveimpl.model.rbac;

import com.jwt.jwtreaciveimpl.model.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_roles_in_profile")
public class RolesInProfiles extends BaseEntity {

    private Long profileId;
    private Long roleId;
    @Column(name = "is_active")
    private Boolean isActive = Boolean.TRUE;
    @Column(columnDefinition = "integer default 0",name = "is_pending_approval")
    private Boolean isPendingApproval;
}

