package com.dsi.authorization.service;

import com.dsi.authorization.dto.UserInfo;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.Menu;

import java.util.List;

/**
 * Created by sabbir on 8/10/16.
 */
public interface MenuService {

    void saveMenu(Menu menu) throws CustomException;
    void updateMenu(Menu menu) throws CustomException;
    void deleteMenu(Menu menu) throws CustomException;
    UserInfo getAllMenusAndApiPermission(String userID) throws CustomException;
    List<Menu> getAllMenus(String userID) throws CustomException;
}
