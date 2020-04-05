package com.sb_jwt_secu.rest.apiResponse.user;

import com.sb_jwt_secu.model.user.CustomUser;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class JwtAuthenticationResponse {
    private String accessToken;
    private String tokenType = "Bearer";
    private CustomUser user;

    public JwtAuthenticationResponse(String accessToken) {
        this.accessToken = accessToken;
    }

}
