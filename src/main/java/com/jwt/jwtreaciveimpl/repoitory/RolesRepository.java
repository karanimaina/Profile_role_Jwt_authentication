package com.jwt.jwtreaciveimpl.repoitory;

import com.jwt.jwtreaciveimpl.model.rbac.Roles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface RolesRepository extends JpaRepository<Roles,Long> {

    Page<Roles> findAllByIsActiveTrue(Pageable pageable);
    List<Roles> findAllByIsActiveTrue();
    List<Roles> findByNameIgnoreCase(String roleName);
    Optional<Roles> findFirstByDestinationAddressAndId(String destinationAddress , long id);

    List<Roles> findAllByCanCreateWorkflowTrue();

    Optional<Roles> findByDestinationAddress(String destinationAddress);
}
