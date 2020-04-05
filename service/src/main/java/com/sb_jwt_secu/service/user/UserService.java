package com.sb_jwt_secu.service.user;

import com.sb_jwt_secu.dao.user.PasswordResetTokenDAO;
import com.sb_jwt_secu.dao.user.UserDAO;
import com.sb_jwt_secu.exception.user.PasswordTokenException;
import com.sb_jwt_secu.model.user.CustomUser;
import com.sb_jwt_secu.model.user.Role;
import com.sb_jwt_secu.model.user.PasswordResetToken;
import com.sb_jwt_secu.utils.CustomUserProperties;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class UserService implements UserDetailsService {

    private UserDAO userDAO;
    private PasswordResetTokenDAO passwordResetTokenDAO;
    private final int passwordTokenExpiration;

    public UserService(
            UserDAO userDAO,
            PasswordResetTokenDAO passwordResetTokenDAO,
            CustomUserProperties customUserProperties
    ) {
        this.userDAO = userDAO;
        this.passwordResetTokenDAO = passwordResetTokenDAO;
        this.passwordTokenExpiration = Integer.parseInt(customUserProperties.getPasswordTokenExpiration());
    }

    @Override
    public CustomUser loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {

        CustomUser customUser = userDAO.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() ->
                        new UsernameNotFoundException("User not found with username or email: " + usernameOrEmail)
                );


        return customUser;
    }

    private Collection<? extends GrantedAuthority> mapRolesToAuthorities(Collection<Role> roles) {
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());
    }

    /**
     * This method is used by JWTAuthenticationFilter
     *
     * @param id
     * @return
     */
    @Transactional
    public UserDetails loadUserById(Long id) {
        CustomUser customUser = userDAO.findById(id).orElseThrow(
                () -> new UsernameNotFoundException("User not found with id : " + id)
        );

        return new User(customUser.getEmail(),
                customUser.getPassword(),
                mapRolesToAuthorities(customUser.getRoles()));
    }

    public CustomUser save(CustomUser customUser) {
        return userDAO.save(customUser);
    }

    public List getUsers() {
        return userDAO.findAll();
    }

    public Object getUserById(Long id) {
        return userDAO.getOne(id);
    }


    public boolean existsByUsername(String username) {
        return userDAO.findByUsername(username).isPresent();
    }

    public boolean existsByEmail(String email) {
        return userDAO.findByEmail(email).isPresent();
    }

    @Transactional
    public void saveAndDeleteToken(CustomUser user) {
        save(user);
        passwordResetTokenDAO.deleteAllByCustomUser(user);
    }

    public void createPasswordResetTokenForUser(CustomUser user, String token) {
        PasswordResetToken myToken = PasswordResetToken.builder()
                .token(token)
                .customUser(user)
                .expiryDate(LocalDateTime.now().plusMinutes(passwordTokenExpiration))
                .build();
        passwordResetTokenDAO.save(myToken);
    }

    public void validatePasswordResetToken(long id, String token, boolean isGrantChangePasswordPrivilege) {
        Optional<PasswordResetToken> passToken =
                passwordResetTokenDAO.findByToken(token);
        if (!passToken.isPresent() || (passToken.get().getCustomUser().getId() != id)) {
            throw new PasswordTokenException("invalidToken");
        }

        if ((passToken.get().getExpiryDate().toEpochSecond(ZoneOffset.UTC)
                - LocalDateTime.now().toEpochSecond(ZoneOffset.UTC)) <= 0) {
            throw new PasswordTokenException( "expired" );
        }


        if(isGrantChangePasswordPrivilege){
            CustomUser user = passToken.get().getCustomUser();
            Authentication auth = new UsernamePasswordAuthenticationToken(
                    user, null, Arrays.asList(
                    new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
            SecurityContextHolder.getContext().setAuthentication(auth);
        }

    }

    public CustomUser getLoggedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return loadUserByUsername(authentication.getName());
    }

}
