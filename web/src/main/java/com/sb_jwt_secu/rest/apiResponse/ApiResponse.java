package com.sb_jwt_secu.rest.apiResponse;

import lombok.*;

@AllArgsConstructor
@Getter
@Setter
public class ApiResponse {

    private Boolean success;
    private String message;

}