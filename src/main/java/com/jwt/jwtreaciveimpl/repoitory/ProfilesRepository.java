package com.eclectics.esb.esb_user_management.rbac.dao.repo;


import com.eclectics.esb.esb_user_management.rbac.dao.entity.Profiles;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProfilesRepository extends JpaRepository<Profiles,Long> {

    Page<Profiles> findAllByIsActiveTrue(Pageable pageable);
    List<Profiles> findByNameIgnoreCase(String name);
}
