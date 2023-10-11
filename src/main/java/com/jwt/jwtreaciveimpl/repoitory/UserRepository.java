package com.jwt.jwtreaciveimpl.repoitory;

import com.jwt.jwtreaciveimpl.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User,Long> {
    List<User> findAllByEmail(String userEmail);
}
