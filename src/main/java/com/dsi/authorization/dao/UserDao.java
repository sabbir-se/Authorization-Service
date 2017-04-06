package com.dsi.authorization.dao;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.System;
import com.dsi.authorization.model.User;
import com.dsi.authorization.model.UserContext;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 6/24/16.
 */
public interface UserDao {

    void setSession(Session session);
    void saveUser(User user) throws CustomException;
    void updateUser(User user) throws CustomException;
    void deleteUser(User user) throws CustomException;
    void deleteUserSession(String userID) throws CustomException;
    void deleteUserRole(String userID) throws CustomException;
    User getUserByID(String userID);
    User getUserByEmail(String email);

    void saveUserContext(UserContext userContext) throws CustomException;
    void updateUserContext(UserContext userContext) throws CustomException;
    void deleteUserContext(String userId) throws CustomException;
    UserContext getUserContextByUserId(String userId);

    System getSystemByUserID(String userID);
}
