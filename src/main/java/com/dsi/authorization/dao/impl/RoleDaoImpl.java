package com.dsi.authorization.dao.impl;

import com.dsi.authorization.dao.RoleDao;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.Role;
import com.dsi.authorization.service.impl.CommonService;
import com.dsi.authorization.util.Constants;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 6/27/16.
 */
public class RoleDaoImpl extends CommonService implements RoleDao {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void saveRole(Role role) throws CustomException {
        try{
            session.save(role);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Role", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateRole(Role role) throws CustomException {
        try{
            session.update(role);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Role", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0003,
                    Constants.AUTHORIZATION_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteRole(Role role) throws CustomException {
        try{
            session.delete(role);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Role", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0004,
                    Constants.AUTHORIZATION_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public Role getRoleByID(String roleID) {
        Query query = session.createQuery("FROM Role r WHERE r.roleId =:roleID");
        query.setParameter("roleID", roleID);

        Role role = (Role) query.uniqueResult();
        if(role != null){
            return role;
        }
        return null;
    }

    @Override
    public Role getRoleByName(String roleName) {
        Query query = session.createQuery("FROM Role r WHERE r.name =:roleName");
        query.setParameter("roleName", roleName);

        Role role = (Role) query.uniqueResult();
        if(role != null){
            return role;
        }
        return null;
    }

    @Override
    public List<Role> getAllRoles() {
        Query query = session.createQuery("FROM Role");

        List<Role> roleList = query.list();
        if(roleList != null){
            return roleList;
        }
        return null;
    }
}
