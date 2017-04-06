package com.dsi.authorization.service;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.Role;

import java.util.List;

/**
 * Created by sabbir on 6/27/16.
 */
public interface RoleService {

    void saveRole(Role role) throws CustomException;
    void updateRole(Role role) throws CustomException;
    void deleteRole(String roleID) throws CustomException;
    Role getRoleByID(String roleID) throws CustomException;
    List<Role> getAllRoles() throws CustomException;
}
