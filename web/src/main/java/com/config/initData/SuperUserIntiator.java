package com.config.initData;

import com.sb_jwt_secu.dao.user.RoleDAO;
import com.sb_jwt_secu.dao.user.UserDAO;
import com.sb_jwt_secu.model.user.CustomUser;
import com.sb_jwt_secu.model.user.Role;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@Transactional
public class SuperUserIntiator {

    private BCryptPasswordEncoder passwordEncoder;

    private UserDAO userRepository;


    private RoleDAO roleDAO;

    public SuperUserIntiator(BCryptPasswordEncoder passwordEncoder, UserDAO userRepository, RoleDAO roleDAO) {
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
        this.roleDAO = roleDAO;
    }

    public void createCustomUser(){

        Set<Role> roles =  roleDAO.findAll().stream().collect(Collectors.toSet());

        CustomUser customUser = CustomUser.builder()
                .firstName("Gaoussou")
                .lastName("Diarra")
                .username("custom1")
                .password(passwordEncoder.encode("custom1"))
                .roles(roles)
                .email("custom@custom.com")
                .birthDate(LocalDate.of(2015, 5,5))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        if (userRepository.findByUsernameOrEmail(customUser.getUsername(), customUser.getEmail() ).isEmpty()){
            userRepository.save(customUser);
        }

        CustomUser customUser2 = CustomUser.builder()
                .firstName("Temujin")
                .lastName("Kan")
                .username("gengiskan")
                .password(passwordEncoder.encode("gengiskan"))
                .roles(roles)
                .email("gengiskan@gengiskan.com")
                .birthDate(LocalDate.of(2015, 5,5))
                .accountNonExpired(true)
                .accountNonLocked(true)
                .credentialsNonExpired(true)
                .enabled(true)
                .build();

        if (! userRepository.findByUsernameOrEmail(customUser2.getUsername(), customUser2.getEmail() ).isEmpty()){
            userRepository.save(customUser2);
        }

    }


}
