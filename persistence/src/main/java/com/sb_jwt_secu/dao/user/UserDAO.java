package com.sb_jwt_secu.dao.user;

import com.sb_jwt_secu.model.user.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserDAO extends JpaRepository<CustomUser, Long> {
    Optional<CustomUser> findByUsernameOrEmail(String username, String email);
    Optional<CustomUser> findByUsername(String username);
    Optional<CustomUser> findByEmail(String email);
}
