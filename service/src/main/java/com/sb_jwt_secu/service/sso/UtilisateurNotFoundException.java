package com.sb_jwt_secu.service.sso;

import java.util.NoSuchElementException;

public class UtilisateurNotFoundException extends NoSuchElementException {

    private static final String messageId = "Utilisateur non trouvé avec l'id : ";
    private static final String messageMail = "Utilisateur non trouvé avec l'email : ";

    public UtilisateurNotFoundException(Integer id) {
        super(messageId + id);
    }
    
    public UtilisateurNotFoundException(String email) {
        super(messageMail + email);
    }
}
