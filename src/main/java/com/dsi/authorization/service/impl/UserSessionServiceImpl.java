package com.dsi.authorization.service.impl;


import com.dsi.authorization.dao.UserSessionDao;
import com.dsi.authorization.dao.impl.UserSessionDaoImpl;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.UserSession;
import com.dsi.authorization.service.UserSessionService;
import com.dsi.authorization.util.Constants;
import com.dsi.authorization.util.Utility;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/15/16.
 */
public class UserSessionServiceImpl extends CommonService implements UserSessionService {

    private static final UserSessionDao dao = new UserSessionDaoImpl();

    @Override
    public void saveUserSession(UserSession userSession) throws CustomException {
        validateInputForCreation(userSession);

        Session session = getSession();
        dao.setSession(session);

        userSession.setCreatedDate(Utility.today());
        userSession.setModifiedDate(Utility.today());
        userSession.setVersion(1);
        dao.saveUserSession(userSession);

        close(session);
    }

    private void validateInputForCreation(UserSession userSession) throws CustomException {
        if(userSession.getAccessToken() == null){
            ErrorContext errorContext = new ErrorContext("AccessToken", "UserSession", "AccessToken not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0001,
                    Constants.AUTHORIZATION_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
        if(userSession.getUserId() == null){
            ErrorContext errorContext = new ErrorContext("UserID", "UserSession", "UserID not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0001,
                    Constants.AUTHORIZATION_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateUserSession(JSONObject userSessionObject) throws CustomException{
        Session session = getSession();
        dao.setSession(session);

        String userID = Utility.validation(userSessionObject, "userId");
        String accessToken = Utility.validation(userSessionObject, "accessToken");
        String newAccessToken = Utility.validation(userSessionObject, "newAccessToken");

        UserSession userSession = dao.getUserSessionByUserIdAndAccessToken(userID, accessToken);

        userSession.setAccessToken(newAccessToken);
        userSession.setModifiedBy(userID);
        userSession.setModifiedDate(Utility.today());
        dao.updateUserSession(userSession);

        close(session);
    }

    @Override
    public void deleteUserSession(JSONObject userSessionObject) throws CustomException{
        Session session = getSession();
        dao.setSession(session);

        String userID = Utility.validation(userSessionObject, "userId");
        String accessToken = Utility.validation(userSessionObject, "accessToken");

        UserSession userSession = dao.getUserSessionByUserIdAndAccessToken(userID, accessToken);
        dao.deleteUserSession(userSession);

        close(session);
    }

    @Override
    public UserSession getUserSessionByUserIdAndAccessToken(String userID, String accessToken) throws CustomException {
        Session session = getSession();
        dao.setSession(session);

        UserSession userSession = dao.getUserSessionByUserIdAndAccessToken(userID, accessToken);
        if(userSession == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserSession", "User session not found by userID: "
                    + userID +" & accessToken: "+ accessToken);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        close(session);
        return userSession;
    }
}
