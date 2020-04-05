package com.sb_jwt_secu.rest.apiResponse.user;

import com.sb_jwt_secu.rest.apiResponse.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseUserUsername extends ApiResponse {
    private String username;

    @Builder
    public ApiResponseUserUsername(Boolean success, String message, String username) {
        super(success, message);
        this.username = username;
    }
}
