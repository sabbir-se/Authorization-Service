package com.dsi.authorization.dao.impl;

import com.dsi.authorization.dao.UserSessionDao;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.UserSession;
import com.dsi.authorization.service.impl.CommonService;
import com.dsi.authorization.util.Constants;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/15/16.
 */
public class UserSessionDaoImpl extends CommonService implements UserSessionDao {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void saveUserSession(UserSession userSession) throws CustomException {
        try{
            session.save(userSession);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserSession", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateUserSession(UserSession userSession) throws CustomException {
        try{
            session.update(userSession);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserSession", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0003,
                    Constants.AUTHORIZATION_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteUserSession(UserSession userSession) throws CustomException {
        try{
            session.delete(userSession);

        } catch (Exception e) {
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserSession", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0004,
                    Constants.AUTHORIZATION_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public UserSession getUserSessionByUserIdAndAccessToken(String userID, String accessToken) {
        Query query = session.createQuery("FROM UserSession us WHERE us.userId =:userID AND us.accessToken =:accessToken");
        query.setParameter("userID", userID);
        query.setParameter("accessToken", accessToken);

        UserSession userSession = (UserSession) query.uniqueResult();
        if(userSession != null){
            return userSession;
        }
        return null;
    }
}
