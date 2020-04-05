package com.sb_jwt_secu.rest.apiResponse.user;

import com.sb_jwt_secu.rest.apiResponse.ApiResponse;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApiUserConnectedDataResponse extends ApiResponse {
    private Boolean usernameTaken;
    private Boolean emailTaken;

    @Builder
    public ApiUserConnectedDataResponse(Boolean success, String message, Boolean usernameTaken, Boolean emailTaken) {
        super(success, message);
        this.usernameTaken = usernameTaken;
        this.emailTaken = emailTaken;
    }
}
