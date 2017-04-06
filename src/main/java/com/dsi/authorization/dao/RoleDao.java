package com.dsi.authorization.dao;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.Role;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 6/27/16.
 */
public interface RoleDao {

    void setSession(Session session);
    void saveRole(Role role) throws CustomException;
    void updateRole(Role role) throws CustomException;
    void deleteRole(Role role) throws CustomException;
    Role getRoleByID(String roleID);
    Role getRoleByName(String roleName);
    List<Role> getAllRoles();
}
