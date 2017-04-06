package com.dsi.authorization.dao;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.UserSession;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/15/16.
 */
public interface UserSessionDao {

    void setSession(Session session);
    void saveUserSession(UserSession userSession) throws CustomException;
    void updateUserSession(UserSession userSession) throws CustomException;
    void deleteUserSession(UserSession userSession) throws CustomException;
    UserSession getUserSessionByUserIdAndAccessToken(String userID, String accessToken);
}
