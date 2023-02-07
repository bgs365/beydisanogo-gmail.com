package com.sb_jwt_secu.service.sso;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class GoogleAuthMapper {

    /**
     * Mappe un {@link GoogleIdToken.Payload} vers un {@link Optional} de
     * {@link GoogleUserDataDto}.
     *
     * @param payload objet contenant les informations d'un utilisateur
     * @return un {@link Optional} de {@link GoogleUserDataDto}
     */
    public Optional<GoogleUserDataDto> mapGoogleIdTokenPayloadToDto(GoogleIdToken.Payload payload) {
        if (payload == null) {
            return Optional.empty();
        }

        GoogleUserDataDto dto = new GoogleUserDataDto(payload.getEmail(),
                (String) payload.get("name"),
                (String) payload.get("family_name"),
                (String) payload.get("given_name"));
        return Optional.of(dto);
    }
}
