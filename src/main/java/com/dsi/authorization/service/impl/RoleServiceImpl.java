package com.dsi.authorization.service.impl;

import com.dsi.authorization.dao.RoleDao;
import com.dsi.authorization.dao.impl.RoleDaoImpl;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.Role;
import com.dsi.authorization.service.RoleService;
import com.dsi.authorization.util.Constants;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 6/27/16.
 */
public class RoleServiceImpl extends CommonService implements RoleService {

    private static final RoleDao roleDao = new RoleDaoImpl();

    @Override
    public void saveRole(Role role) throws CustomException {
        validateInputForCreation(role);

        Session session = getSession();
        roleDao.setSession(session);

        Role isRoleExist = roleDao.getRoleByName(role.getName());
        if(isRoleExist != null){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Role", "Role already exist.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        roleDao.saveRole(role);

        close(session);
    }

    private void validateInputForCreation(Role role) throws CustomException {
        if(role.getName() ==  null){
            ErrorContext errorContext = new ErrorContext("Name", "Role",
                    "Role Name not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0001,
                    Constants.AUTHORIZATION_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateRole(Role role) throws CustomException {
        Session session = getSession();
        roleDao.setSession(session);

        roleDao.updateRole(role);

        close(session);
    }

    @Override
    public void deleteRole(String roleID) throws CustomException {
        Session session = getSession();
        roleDao.setSession(session);

        Role role = roleDao.getRoleByID(roleID);
        roleDao.deleteRole(role);

        close(session);
    }

    @Override
    public Role getRoleByID(String roleID) throws CustomException {
        Session session = getSession();
        roleDao.setSession(session);

        Role role = roleDao.getRoleByID(roleID);
        if(role == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(roleID, "Role", "Role not found by roleID: " + roleID);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        close(session);
        return role;
    }

    @Override
    public List<Role> getAllRoles() throws CustomException {
        Session session = getSession();
        roleDao.setSession(session);

        List<Role> roleList = roleDao.getAllRoles();
        if(roleList == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Role", "Role list not found.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        close(session);
        return roleList;
    }
}
