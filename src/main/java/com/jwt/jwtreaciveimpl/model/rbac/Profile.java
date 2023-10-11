package com.jwt.jwtreaciveimpl.model.rbac;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "tb_profiles")
public class Profile extends BaseEntity {
    private String name;
    private String remarks;
    @Column(name = "is_active")
    private Boolean isActive;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;
}
