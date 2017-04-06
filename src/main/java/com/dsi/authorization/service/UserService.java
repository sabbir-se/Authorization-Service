package com.dsi.authorization.service;

import com.dsi.authorization.dto.UserContextDto;
import com.dsi.authorization.dto.UserDto;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.model.System;
import com.dsi.authorization.model.User;
import com.dsi.authorization.model.UserContext;
import com.dsi.authorization.model.UserRole;

import java.util.List;

/**
 * Created by sabbir on 6/24/16.
 */
public interface UserService {

    UserRole saveUser(User user) throws CustomException;
    void updateUser(User user) throws CustomException;
    void deleteUser(String userID) throws CustomException;
    UserDto getUserByID(String userID) throws CustomException;
    List<UserDto> getAllUserByRole(String roleType) throws CustomException;
    String getUsersByRoleType() throws CustomException;

    void saveOrUpdateUserContext(List<UserContextDto> userContextDtoList) throws CustomException;
    UserContext getUserContextByUserId(String userId);

    System getSystemByUserID(String userID) throws CustomException;
}
