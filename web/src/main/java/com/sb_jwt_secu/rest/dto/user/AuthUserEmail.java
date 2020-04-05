package com.sb_jwt_secu.rest.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthUserEmail {
    @NotBlank
    @Size(max = 40)
    @Email
    private String email;
}
