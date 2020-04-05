package com.sb_jwt_secu.rest.dto.user;

import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
public class ResetPasswordDTO {

    @NotBlank
    @Size(min = 6, max = 20)
    private String newPassword;

}
