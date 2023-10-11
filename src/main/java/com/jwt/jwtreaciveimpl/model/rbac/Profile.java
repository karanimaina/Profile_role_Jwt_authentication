package com.jwt.jwtreaciveimpl.model;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Date;

@Entity
@Table(name = "tb_profiles")
public class Profiles extends BaseEntity {
    private String name;
    private String remarks;
    @Column(name = "is_active")
    private Boolean isActive;
    @JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private Date updatedOn;
}
