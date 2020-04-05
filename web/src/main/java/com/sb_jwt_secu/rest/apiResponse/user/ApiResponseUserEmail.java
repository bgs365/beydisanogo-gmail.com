package com.sb_jwt_secu.rest.apiResponse.user;

import com.sb_jwt_secu.rest.apiResponse.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseUserEmail extends ApiResponse {
    private String email;

    @Builder
    public ApiResponseUserEmail(Boolean success, String message, String email) {
        super(success, message);
        this.email = email;
    }
}
