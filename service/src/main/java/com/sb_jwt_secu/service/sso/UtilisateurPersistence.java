package com.sb_jwt_secu.service.sso;

import com.sb_jwt_secu.dao.user.UserDAO;
import com.sb_jwt_secu.model.user.CustomUser;
import com.sb_jwt_secu.model.user.Role;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
public class UtilisateurPersistence {

    private final UserDAO userDAO;

    public UtilisateurPersistence(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    public void saveOrUpdate(CustomUser utilisateur) {
        this.userDAO.save(utilisateur);
    }

    public CustomUser findById(Integer id) {
        return this.userDAO.findById(id.longValue()).orElseThrow(() -> new UtilisateurNotFoundException(id));
    }

    public Boolean isNewByEmail(String email) {
        return this.userDAO.findByEmail(email).isEmpty();
    }
    
    public CustomUser findByEmail(String email) {
        return this.userDAO.findByEmail(email)
                .orElseThrow(() -> new UtilisateurNotFoundException(email));
    }

    public Role findByUtilisateurEmail(String email) {
        return this.userDAO.findByEmail(email).get().getRoles().stream().collect(Collectors.toList()).get(0);
    }
}
