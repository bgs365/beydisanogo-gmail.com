package com.sb_jwt_secu.rest.dto.user;

import com.sb_jwt_secu.model.user.RoleName;
import com.sb_jwt_secu.utils.Gender;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
public class CustomUpdateUserDTO {

    private String firstName;

    private String lastName;

    private LocalDate birthDate;

    private Gender gender;

    private Set<RoleName> roles;

}