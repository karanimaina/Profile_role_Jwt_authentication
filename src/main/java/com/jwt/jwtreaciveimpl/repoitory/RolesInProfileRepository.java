package com.eclectics.esb.esb_user_management.rbac.dao.repo;

import com.eclectics.esb.esb_user_management.rbac.dao.entity.RolesInProfiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface RolesInProfileRepository extends JpaRepository<RolesInProfiles,Long> {
    @Query("SELECT rolesInProfile from RolesInProfiles rolesInProfile WHERE rolesInProfile.profileId=:profileId AND rolesInProfile.isActive = true")
    Page<RolesInProfiles> findAllByProdfileId(@Param("profileId") Long profileId, Pageable pageable);

    @Query("SELECT rolesInProfile from RolesInProfiles rolesInProfile WHERE rolesInProfile.profileId=:profileId AND rolesInProfile.isActive = true")
    List<RolesInProfiles> findAllByProdfileId(@Param("profileId") Long profileId);

    @Query("SELECT rolesInProfile from RolesInProfiles rolesInProfile WHERE rolesInProfile.roleId=:roleId AND rolesInProfile.isActive = true")
    List<RolesInProfiles> findAllByRoles(@Param("roleId") Long roleId);

    Optional<RolesInProfiles> findAllByRoleIdAndProfileId(Long roleId,Long profileId);
}
