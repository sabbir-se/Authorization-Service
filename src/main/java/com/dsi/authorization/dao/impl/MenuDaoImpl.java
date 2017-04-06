package com.dsi.authorization.dao.impl;

import com.dsi.authorization.dao.MenuDao;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.Api;
import com.dsi.authorization.model.Menu;
import com.dsi.authorization.service.impl.CommonService;
import com.dsi.authorization.util.Constants;
import org.hibernate.Query;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 8/10/16.
 */
public class MenuDaoImpl extends CommonService implements MenuDao {

    private Session session;

    @Override
    public void setSession(Session session) {
        this.session = session;
    }

    @Override
    public void saveMenu(Menu menu) throws CustomException {
        try{
            session.save(menu);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Menu", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void updateMenu(Menu menu) throws CustomException {
        try{
            session.update(menu);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Menu", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0003,
                    Constants.AUTHORIZATION_SERVICE_0003_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void deleteMenu(Menu menu) throws CustomException {
        try {
            session.delete(menu);

        } catch (Exception e){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "Menu", e.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0004,
                    Constants.AUTHORIZATION_SERVICE_0004_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public Menu getMenuByIdOrName(String menuID, String name) {
        Query query;
        if(menuID != null){
            query = session.createQuery("FROM Menu m WHERE m.menuId =:menuID");
            query.setParameter("menuID", menuID);

        } else {
            query = session.createQuery("FROM Menu m WHERE m.name =:name");
            query.setParameter("name", name);
        }

        Menu menu = (Menu) query.uniqueResult();
        if(menu != null){
            return menu;
        }
        return null;
    }

    @Override
    public List<Menu> getAllSubMenus(String menuID) {
        Query query = session.createQuery("FROM Menu m WHERE m.parentMenuId =:menuID ORDER BY m.position ASC");
        query.setParameter("menuID", menuID);

        List<Menu> menuList = query.list();
        if(menuList != null){
            return menuList;
        }
        return null;
    }

    @Override
    public List<Menu> getAllMenus(String userID) {

        Query query = session.createQuery("FROM Menu m WHERE m.parentMenuId is null AND m.menuId in " +
                "(SELECT rm.menu.menuId FROM RoleMenu rm WHERE rm.role.roleId in " +
                "(SELECT ur.role.roleId FROM UserRole ur WHERE ur.user.userId =:userID)) ORDER BY m.position ASC");
        query.setParameter("userID", userID);

        List<Menu> menuList = query.list();
        if(menuList != null){
            return menuList;
        }
        return null;
    }

    @Override
    public List<Api> getAllAPIByRole(String userId) {
        Query query = session.createQuery("FROM Api a WHERE a.apiId in (SELECT ma.api.apiId FROM MenuApi ma WHERE ma.menu.menuId in " +
                "(SELECT rm.menu.menuId FROM RoleMenu rm WHERE rm.role.roleId in " +
                "(SELECT ur.role.roleId FROM UserRole ur WHERE ur.user.userId =:userId)))");
        query.setParameter("userId", userId);

        List<Api> apiList = query.list();
        if(apiList != null){
            return apiList;
        }
        return null;
    }
}
