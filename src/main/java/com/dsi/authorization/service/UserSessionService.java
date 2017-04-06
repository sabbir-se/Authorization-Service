package com.dsi.authorization.service;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.UserSession;
import org.codehaus.jettison.json.JSONObject;

/**
 * Created by sabbir on 7/11/16.
 */
public interface UserSessionService {

    void saveUserSession(UserSession userSession) throws CustomException;
    void updateUserSession(JSONObject userSessionObject) throws CustomException;
    void deleteUserSession(JSONObject userSessionObject) throws CustomException;
    UserSession getUserSessionByUserIdAndAccessToken(String userID, String accessToken) throws CustomException;
}
