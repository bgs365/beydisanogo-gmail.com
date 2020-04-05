package com.sb_jwt_secu.dao.user;

import com.sb_jwt_secu.model.user.Role;
import com.sb_jwt_secu.model.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleDAO extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName roleUser);
}
