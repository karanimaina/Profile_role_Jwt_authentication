package com.jwt.jwtreaciveimpl.model.rbac;

import com.jwt.jwtreaciveimpl.model.BaseEntity;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Table(name = "tb_users_profile")
public class UsersProfile extends BaseEntity {
    private String userEmail;
    private Long profileId;
    private Boolean isActive = Boolean.TRUE;
}
