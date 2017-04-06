package com.dsi.authorization.dto;

import com.dsi.authorization.model.Api;
import com.dsi.authorization.model.Menu;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sabbir on 1/27/17.
 */
public class UserInfo {

    private List<Menu> menuList = new ArrayList<>();

    private List<Api> apiList = new ArrayList<>();

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }

    public List<Api> getApiList() {
        return apiList;
    }

    public void setApiList(List<Api> apiList) {
        this.apiList = apiList;
    }
}
