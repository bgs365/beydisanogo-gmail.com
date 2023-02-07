package com.sb_jwt_secu.service.sso;

import java.util.NoSuchElementException;

public class RoleNotFoundException extends NoSuchElementException {

    private static final String message = "Role non trouvé pour l'utilisateur : ";

    public RoleNotFoundException(String email) {
        super(message + email);
    }
}
