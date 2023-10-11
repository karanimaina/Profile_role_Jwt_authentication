package com.eclectics.esb.esb_user_management.rbac.dao.repo;

import com.eclectics.esb.esb_user_management.rbac.dao.entity.Profiles;
import com.eclectics.esb.esb_user_management.rbac.dao.entity.UsersProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UsersProfilesRepository extends JpaRepository<UsersProfile,Long> {

    Optional<List<UsersProfile>> findByUserEmailAndIsActiveTrue(String email);

    Optional<UsersProfile> findByUserEmailAndProfileId(String email, Long profileId);

    Optional<List<UsersProfile>> findAllByProfileIdAndIsActiveTrue(Long profileId);
}
