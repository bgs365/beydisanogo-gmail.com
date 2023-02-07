package com.sb_jwt_secu.service.sso;

import com.sb_jwt_secu.dao.user.RoleDAO;
import com.sb_jwt_secu.model.user.CustomUser;
import com.sb_jwt_secu.model.user.Role;
import com.sb_jwt_secu.model.user.RoleName;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Set;

@Component
public class UtilisateurService {

    private final UtilisateurPersistence utilisateurPersistence;
    private final RoleDAO roleDAO;

    public UtilisateurService(UtilisateurPersistence utilisateurPersistence, RoleDAO roleDAO) {
        this.utilisateurPersistence = utilisateurPersistence;
        this.roleDAO = roleDAO;
    }

    public void createUtilisateurIfNotExist(GoogleUserDataDto googleUser) {
        if (this.utilisateurPersistence.isNewByEmail(googleUser.getEmail())) {
            CustomUser newCUstomUser = new CustomUser();
            newCUstomUser.setEmail(googleUser.getEmail());
            newCUstomUser.setUsername(googleUser.getName());
            newCUstomUser.setFirstName(googleUser.getGivenName());
            newCUstomUser.setLastName(googleUser.getFamilyName());
            newCUstomUser.setRoles(Set.of(roleDAO.findByName(RoleName.ROLE_USER).get()));
            newCUstomUser.setSubscriptionDate(LocalDateTime.now());
            newCUstomUser.setEnabled(true);
            newCUstomUser.setAccountNonExpired(true);
            newCUstomUser.setAccountNonLocked(true);
            newCUstomUser.setCredentialsNonExpired(true);
            newCUstomUser.setSSOUser(true);

            this.utilisateurPersistence.saveOrUpdate(newCUstomUser);
        }
    }

    public CustomUser findById(Integer id) {
        return this.utilisateurPersistence.findById(id);
    }
    
    public CustomUser findByEmail(String email) {
        return this.utilisateurPersistence.findByEmail(email);
    }

    public Role getRoleByUtilisateurEmail(String email) {
        return this.utilisateurPersistence.findByUtilisateurEmail(email);
    }

    public boolean isRole(String email, String role) {
        return role.equals(this.utilisateurPersistence.findByUtilisateurEmail(email));
    }
}
