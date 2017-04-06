package com.dsi.authorization.dao;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.Api;
import com.dsi.authorization.model.Menu;
import org.hibernate.Session;

import java.util.List;

/**
 * Created by sabbir on 8/10/16.
 */
public interface MenuDao {

    void setSession(Session session);
    void saveMenu(Menu menu) throws CustomException;
    void updateMenu(Menu menu) throws CustomException;
    void deleteMenu(Menu menu) throws CustomException;
    Menu getMenuByIdOrName(String menuID, String name);
    List<Menu> getAllSubMenus(String menuID);
    List<Menu> getAllMenus(String userID);

    List<Api> getAllAPIByRole(String userId);
}
