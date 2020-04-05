package com.sb_jwt_secu.rest.apiResponse.user;

import com.sb_jwt_secu.rest.apiResponse.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponsePassword extends ApiResponse {
    private Boolean old_and_new_password_are_same;
    private Boolean wrong_old_password;

    @Builder
    public ApiResponsePassword(Boolean success, String message, Boolean old_and_new_password_are_same, Boolean wrong_old_password) {
        super(success, message);
        this.old_and_new_password_are_same = old_and_new_password_are_same;
        this.wrong_old_password = wrong_old_password;
    }
}
