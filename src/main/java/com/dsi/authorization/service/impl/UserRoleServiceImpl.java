package com.dsi.authorization.service.impl;

import com.dsi.authorization.dao.UserRoleDao;
import com.dsi.authorization.dao.impl.UserRoleDaoImpl;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.UserRole;
import com.dsi.authorization.service.UserRoleService;
import com.dsi.authorization.util.Constants;
import com.dsi.authorization.util.Utility;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 6/24/16.
 */
public class UserRoleServiceImpl extends CommonService implements UserRoleService {

    private static final UserRoleDao userRoleDao = new UserRoleDaoImpl();

    @Override
    public void saveUserRole(UserRole userRole) throws CustomException {
        validateInputForCreation(userRole);

        Session session = getSession();
        userRoleDao.setSession(session);

        UserRole isUserRoleExist = userRoleDao.getUserRoleByUserIdAndSystemIdAndRoleID(userRole.getUser().getUserId(),
                userRole.getSystem().getSystemId(), userRole.getRole().getRoleId());
        if(isUserRoleExist != null){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserRole", "User role already exist.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        userRole.setCreatedDate(Utility.today());
        userRole.setModifiedDate(Utility.today());
        userRoleDao.saveUserRole(userRole);

        close(session);
    }

    private void validateInputForCreation(UserRole userRole) throws CustomException {
        if(userRole.getRole().getRoleId() ==  null){
            ErrorContext errorContext = new ErrorContext("RoleID", "UserRole",
                    "RoleID not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0001,
                    Constants.AUTHORIZATION_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateUserRole(UserRole userRole) throws CustomException {
        Session session = getSession();
        userRoleDao.setSession(session);

        userRole.setCreatedDate(Utility.today());
        userRole.setModifiedDate(Utility.today());
        userRoleDao.updateUserRole(userRole);

        close(session);
    }

    @Override
    public void deleteUserRole(String userRoleID) throws CustomException {
        Session session = getSession();
        userRoleDao.setSession(session);

        UserRole userRole = userRoleDao.getUserRoleByIdOrRoleID(userRoleID, null);
        userRoleDao.deleteUserRole(userRole);

        close(session);
    }

    @Override
    public UserRole getUserRoleByIdOrRoleID(String userRoleID, String roleID) throws CustomException {
        Session session = getSession();
        userRoleDao.setSession(session);

        UserRole userRole = userRoleDao.getUserRoleByIdOrRoleID(userRoleID, roleID);
        if(userRole == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(userRoleID, "UserRole", "User role not found by userRoleID: " + userRoleID);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        close(session);
        return userRole;
    }

    @Override
    public UserRole getUserRoleByUserID(String userID) throws CustomException {
        Session session = getSession();
        userRoleDao.setSession(session);

        UserRole userRole = userRoleDao.getUserRoleByUserID(userID);
        if(userRole == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(userID, "UserRole", "User role not found by userID: " + userID);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        close(session);
        return userRole;
    }

    @Override
    public List<UserRole> getUserRoleListBySystemID(String systemID) throws CustomException {
        Session session = getSession();
        userRoleDao.setSession(session);

        List<UserRole> userRoleList = userRoleDao.getUserRoleListBySystemID(systemID);
        if(userRoleList == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(systemID, "UserRole", "User role list not found by systemID: " + systemID);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        close(session);
        return userRoleList;
    }
}
