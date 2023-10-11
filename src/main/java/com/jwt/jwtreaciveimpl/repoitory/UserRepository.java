package com.jwt.jwtreaciveimpl.repoitory;

import com.jwt.jwtreaciveimpl.model.SystemUser;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<SystemUser,Long> {
    List<SystemUser> findAllByEmail(String userEmail);
}
