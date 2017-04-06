package com.dsi.authorization.dao;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.UserRole;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 6/24/16.
 */
public interface UserRoleDao {

    void setSession(Session session);
    void saveUserRole(UserRole userRole) throws CustomException;
    void updateUserRole(UserRole userRole) throws CustomException;
    void deleteUserRole(UserRole userRole) throws CustomException;
    UserRole getUserRoleByIdOrRoleID(String userRoleID, String roleID);
    UserRole getUserRoleByUserIdAndSystemIdAndRoleID
            (String userID, String systemID, String roleID);
    UserRole getUserRoleByUserID(String userID);
    List<UserRole> getAllUserByRole(String roleType);
    List<UserRole> getAllUserByRoleType(String roleType);
    List<UserRole> getUserRoleListBySystemID(String systemID);
}
