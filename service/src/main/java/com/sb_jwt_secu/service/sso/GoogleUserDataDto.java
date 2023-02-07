package com.sb_jwt_secu.service.sso;

import lombok.*;

@Setter
@Getter
@RequiredArgsConstructor
@Builder
@AllArgsConstructor
public class GoogleUserDataDto {
    private String email;
    private String name;
    private String familyName;
    private String givenName;
}
