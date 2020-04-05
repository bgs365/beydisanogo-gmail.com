package com.sb_jwt_secu.service.user;

import com.sb_jwt_secu.dao.user.RoleDAO;
import com.sb_jwt_secu.model.user.Role;
import com.sb_jwt_secu.model.user.RoleName;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class RoleService {

    private RoleDAO roleDAO;

    public RoleService(RoleDAO roleDAO) {
        this.roleDAO = roleDAO;
    }

    public List<Role> getRoles(){
        return roleDAO.findAll();
    }

    public Optional<Role> findRole(RoleName roleUser){
        return roleDAO.findByName(roleUser);
    }

}
