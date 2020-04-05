package com.sb_jwt_secu.rest.apiResponse.user;

import com.sb_jwt_secu.rest.apiResponse.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiResponseUserAvatar extends ApiResponse {
    private String avatar;

    @Builder
    public ApiResponseUserAvatar(Boolean success, String message, String avatar) {
        super(success, message);
        this.avatar = avatar;
    }
}
