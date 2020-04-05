package com.sb_jwt_secu.dao.user;

import com.sb_jwt_secu.model.user.PasswordResetToken;
import com.sb_jwt_secu.model.user.CustomUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface PasswordResetTokenDAO extends JpaRepository<PasswordResetToken, Long> {
    Optional<PasswordResetToken> findByToken(String email);
    void deleteAllByCustomUser(CustomUser customUser);
}
