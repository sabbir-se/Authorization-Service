package com.dsi.authorization.service;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.UserRole;

import java.util.List;

/**
 * Created by sabbir on 6/24/16.
 */
public interface UserRoleService {

    void saveUserRole(UserRole userRole) throws CustomException;
    void updateUserRole(UserRole userRole) throws CustomException;
    void deleteUserRole(String userRoleID) throws CustomException;
    UserRole getUserRoleByIdOrRoleID(String userRoleID, String roleID) throws CustomException;
    UserRole getUserRoleByUserID(String userID) throws CustomException;
    List<UserRole> getUserRoleListBySystemID(String systemID) throws CustomException;
}
