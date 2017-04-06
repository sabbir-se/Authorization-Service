package com.dsi.authorization.dao.impl;

import com.dsi.authorization.dao.UserDao;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.System;
import com.dsi.authorization.model.User;
import com.dsi.authorization.model.UserContext;
import com.dsi.authorization.service.impl.CommonService;
import com.dsi.authorization.util.Constants;
import org.hibernate.Query;
import org.hibernate.Session;

/**
 * Created by sabbir on 6/24/16.
 */
public class UserDaoImpl extends CommonService implements UserDao {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void saveUser(User user) throws CustomException {
        try {
            session.save(user);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "User", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateUser(User user) throws CustomException {
        try{
            session.update(user);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "User", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0003,
                    Constants.AUTHORIZATION_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteUser(User user) throws CustomException {
        try {
            session.delete(user);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "User", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0004,
                    Constants.AUTHORIZATION_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteUserSession(String userID) throws CustomException {
        try {
            Query query = session.createQuery("DELETE FROM UserSession us WHERE us.userId =:userID");
            query.setParameter("userID", userID);

            query.executeUpdate();

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserSession", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0004,
                    Constants.AUTHORIZATION_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteUserRole(String userID) throws CustomException {
        try {
            Query query = session.createQuery("DELETE FROM UserRole ur WHERE ur.user.userId =:userID");
            query.setParameter("userID", userID);

            query.executeUpdate();

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserRole", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0004,
                    Constants.AUTHORIZATION_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public User getUserByID(String userID) {
        Query query = session.createQuery("FROM User u WHERE u.userId =:userID");
        query.setParameter("userID", userID);

        User user = (User) query.uniqueResult();
        if(user != null) {
            return user;
        }
        return null;
    }

    @Override
    public User getUserByEmail(String email) {
        Query query = session.createQuery("FROM User u WHERE u.email =:email");
        query.setParameter("email", email);

        User user = (User) query.uniqueResult();
        if(user != null) {
            return user;
        }
        return null;
    }

    @Override
    public void saveUserContext(UserContext userContext) throws CustomException {
        try {
            session.save(userContext);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserContext", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateUserContext(UserContext userContext) throws CustomException {
        try{
            session.update(userContext);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserContext", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0003,
                    Constants.AUTHORIZATION_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteUserContext(String userId) throws CustomException {
        try {
            Query query = session.createQuery("DELETE FROM UserContext uc WHERE uc.user.userId =:userId");
            query.setParameter("userId", userId);

            query.executeUpdate();

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserContext", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0004,
                    Constants.AUTHORIZATION_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public UserContext getUserContextByUserId(String userId) {
        Query query = session.createQuery("FROM UserContext uc WHERE uc.user.userId =:userId");
        query.setParameter("userId", userId);

        UserContext userContext = (UserContext) query.uniqueResult();
        if(userContext != null){
            return userContext;
        }
        return null;
    }

    @Override
    public System getSystemByUserID(String userID) {
        Query query = session.createQuery("FROM System s WHERE s.tenantId in " +
                "(SELECT u.tenantId FROM User u WHERE u.userId =:userID)");
        query.setParameter("userID", userID);

        System system = (System) query.uniqueResult();
        if(system != null){
            return system;
        }
        return null;
    }
}
