package com.jwt.jwtreaciveimpl.repoitory;


import com.jwt.jwtreaciveimpl.model.rbac.Profile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProfilesRepository extends JpaRepository<Profile,Long> {

    Page<Profile> findAllByIsActiveTrue(Pageable pageable);
    List<Profile> findByNameIgnoreCase(String name);
}
