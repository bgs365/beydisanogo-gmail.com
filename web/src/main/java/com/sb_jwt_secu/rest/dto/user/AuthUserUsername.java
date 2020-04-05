package com.sb_jwt_secu.rest.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class AuthUserUsername {
    @NotBlank
    @Size(min = 3, max = 15)
    private String username;
}
