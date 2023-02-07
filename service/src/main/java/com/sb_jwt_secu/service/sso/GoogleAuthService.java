package com.sb_jwt_secu.service.sso;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken.Payload;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.Optional;

@Service
public class GoogleAuthService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GoogleAuthService.class);
    private static final String TOKEN_BEARER_AUTH_PREFIX = "Bearer ";

    private final GoogleIdTokenVerifier verifier;
    private final GoogleAuthMapper googleAuthMapper;
    private final UtilisateurService utilisateurService;

    /**
     * Constructeur de {@link GoogleAuthService}.
     */
    @Autowired
    public GoogleAuthService(HttpTransport transport, JsonFactory factory,
            @Value("${spring.security.oauth2.client.registration.google.client-id}") String clientId,
            GoogleAuthMapper googleAuthMapper, UtilisateurService utilisateurService) {
        this(new GoogleIdTokenVerifier.Builder(transport, factory).setAudience(Collections.singletonList(clientId))
                .build(), googleAuthMapper, utilisateurService);
    }

    /**
     * Construteur de {@link GoogleAuthService}.
     */
    public GoogleAuthService(GoogleIdTokenVerifier verifier, GoogleAuthMapper googleAuthMapper,
            UtilisateurService utilisateurService) {
        this.verifier = verifier;
        this.googleAuthMapper = googleAuthMapper;
        this.utilisateurService = utilisateurService;
    }

    /**
     * Vérifie le token avec l'api Google.
     *
     * @param idTokenString le token à vérifier
     * @return un {@link Optional} {@link GoogleUserDataDto}
     */
    public Optional<GoogleUserDataDto> getAuthenticatedUserData(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);

            if (idToken != null) {
                Payload payload = idToken.getPayload();
                LOGGER.debug("Google User authenticated, userId = {}", payload.getSubject());

                if (payload.getEmailVerified()) {
                    Optional<GoogleUserDataDto> googleUser = googleAuthMapper.mapGoogleIdTokenPayloadToDto(payload);
                    
                    googleUser.ifPresent(gu -> this.utilisateurService.createUtilisateurIfNotExist(gu));
                    
                    return googleUser;
                }
            } else {
                LOGGER.warn("Invalid ID token.");
            }
        } catch (GeneralSecurityException | IOException e) {
            LOGGER.error("Error occured when trying to check Google auth token" + e);
        }

        return Optional.empty();
    }

}
