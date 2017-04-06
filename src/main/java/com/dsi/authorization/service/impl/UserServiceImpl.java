package com.dsi.authorization.service.impl;

import com.dsi.authorization.dao.RoleDao;
import com.dsi.authorization.dao.UserDao;
import com.dsi.authorization.dao.UserRoleDao;
import com.dsi.authorization.dao.impl.RoleDaoImpl;
import com.dsi.authorization.dao.impl.UserDaoImpl;
import com.dsi.authorization.dao.impl.UserRoleDaoImpl;
import com.dsi.authorization.dto.UserContextDto;
import com.dsi.authorization.dto.UserDto;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.*;
import com.dsi.authorization.model.System;
import com.dsi.authorization.service.UserService;
import com.dsi.authorization.util.Constants;
import com.dsi.authorization.util.Utility;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;
import org.hibernate.Session;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sabbir on 6/24/16.
 */
public class UserServiceImpl extends CommonService implements UserService {

    private static final Logger logger = Logger.getLogger(UserServiceImpl.class);

    private static final UserDao userDao = new UserDaoImpl();
    private static final RoleDao roleDao = new RoleDaoImpl();
    private static final UserRoleDao userRoleDao = new UserRoleDaoImpl();

    @Override
    public UserRole saveUser(User user) throws CustomException {
        Session session = getSession();
        userDao.setSession(session);
        roleDao.setSession(session);
        userRoleDao.setSession(session);

        User currentUser = userDao.getUserByID(user.getCreateBy());
        user.setTenantId(currentUser.getTenantId());

        if(user.getTenantId() == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "User", "TenantID not defined.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0001,
                    Constants.AUTHORIZATION_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        if(userDao.getUserByEmail(user.getEmail()) != null){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "User", "User already exist.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0002,
                    Constants.AUTHORIZATION_SERVICE_0002_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        user.setCreatedDate(Utility.today());
        user.setModifiedDate(Utility.today());
        userDao.saveUser(user);
        logger.info("User create successfully.");

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleDao.getRoleByID(user.getRoleId()));
        userRole.setSystem(userDao.getSystemByUserID(user.getCreateBy()));
        userRole.setCreateBy(user.getCreateBy());
        userRole.setModifiedBy(user.getModifiedBy());
        userRole.setCreatedDate(Utility.today());
        userRole.setModifiedDate(Utility.today());
        userRole.setActive(true);
        userRole.setVersion(1);
        userRoleDao.saveUserRole(userRole);
        logger.info("User role create successfully.");

        close(session);
        return userRole;
    }

    @Override
    public void updateUser(User user) throws CustomException {
        Session session = getSession();
        roleDao.setSession(session);
        userDao.setSession(session);
        userRoleDao.setSession(session);

        User existUser = userDao.getUserByID(user.getUserId());
        if(existUser != null) {
            existUser.setFirstName(user.getFirstName());
            existUser.setLastName(user.getLastName());
            existUser.setGender(user.getGender());
            existUser.setPhone(user.getPhone());
            existUser.setModifiedBy(user.getModifiedBy());
            existUser.setModifiedDate(Utility.today());
            userDao.updateUser(existUser);
            logger.info("User update success.");

            if(user.getRoleId() != null) {
                UserRole existRole = userRoleDao.getUserRoleByUserID(user.getUserId());
                if (existRole != null) {
                    existRole.setModifiedBy(user.getModifiedBy());
                    existRole.setModifiedDate(Utility.today());
                    existRole.setRole(roleDao.getRoleByID(user.getRoleId()));
                    userRoleDao.updateUserRole(existRole);
                    logger.info("User role update success.");
                }
            }
        }

        close(session);
    }

    @Override
    public void deleteUser(String userID) throws CustomException {
        Session session = getSession();
        userDao.setSession(session);

        User user = userDao.getUserByID(userID);
        if(user != null) {
            userDao.deleteUserContext(user.getUserId());
            userDao.deleteUserSession(user.getUserId());
            userDao.deleteUserRole(user.getUserId());
            userDao.deleteUser(user);
        }

        close(session);
    }

    @Override
    public UserDto getUserByID(String userID) throws CustomException {
        Session session = getSession();
        userRoleDao.setSession(session);

        UserRole userRole = userRoleDao.getUserRoleByUserID(userID);
        if (userRole == null) {
            close(session);
            ErrorContext errorContext = new ErrorContext(userID, "UserRole",
                    "UserRole not found by userID: " + userID);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        UserDto user = new UserDto();
        user.setUserId(userID);
        user.setFirstName(userRole.getUser().getFirstName());
        user.setLastName(userRole.getUser().getLastName());
        user.setRoleId(userRole.getRole().getRoleId());
        user.setRoleName(userRole.getRole().getName());
        user.setMessage("User role info.");

        close(session);
        return user;
    }

    @Override
    public List<UserDto> getAllUserByRole(String roleType) throws CustomException {

        Session session = getSession();
        userRoleDao.setSession(session);

        List<UserRole> userRoleList = userRoleDao.getAllUserByRole(roleType);
        if(userRoleList == null){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, "UserRole", "User role list not found by roleType: " + roleType);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
        logger.info("User role list size: " + userRoleList.size());

        List<UserDto> userDtoList = new ArrayList<>();
        for(UserRole userRole : userRoleList){
            UserDto userDto = new UserDto();
            userDto.setUserId(userRole.getUser().getUserId());
            userDto.setFirstName(userRole.getUser().getFirstName());
            userDto.setLastName(userRole.getUser().getLastName());

            userDtoList.add(userDto);
        }

        close(session);
        return userDtoList;
    }

    @Override
    public String getUsersByRoleType() throws CustomException {
        Session session = getSession();
        userRoleDao.setSession(session);

        JSONObject roleObj = new JSONObject();
        JSONArray emailArray;
        try {
            List<UserRole> userRoleList = userRoleDao.getAllUserByRoleType(RoleName.HR.getValue());
            if (userRoleList == null) {
                close(session);
                ErrorContext errorContext = new ErrorContext(null, null,
                        "User role list not found by HR RoleType");
                ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                        Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
                throw new CustomException(errorMessage);
            }
            logger.info("HR role list size: " + userRoleList.size());

            emailArray = new JSONArray();
            for (UserRole userRole : userRoleList) {
                emailArray.put(userRole.getUser().getEmail());
            }
            roleObj.put(RoleName.HR.getValue(), emailArray);

            userRoleList = userRoleDao.getAllUserByRoleType(RoleName.MANAGER.getValue());
            if (userRoleList == null) {
                close(session);
                ErrorContext errorContext = new ErrorContext(null, null,
                        "User role list not found by Manager RoleType");
                ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                        Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
                throw new CustomException(errorMessage);
            }
            logger.info("Manager role list size: " + userRoleList.size());

            emailArray = new JSONArray();
            for(UserRole userRole : userRoleList){
                emailArray.put(userRole.getUser().getEmail());
            }
            roleObj.put(RoleName.MANAGER.getValue(), emailArray);

            close(session);
            return roleObj.toString();

        } catch (JSONException je){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public void saveOrUpdateUserContext(List<UserContextDto> userContextDtoList) throws CustomException {

        if(Utility.isNullOrEmpty(userContextDtoList)){
            ErrorContext errorContext = new ErrorContext(null, "UserContext", "User Context list is empty.");
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0001,
                    Constants.AUTHORIZATION_SERVICE_0001_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        Session session = getSession();
        userDao.setSession(session);

        UserContext userContext;
        try {
            for (UserContextDto userContextDto : userContextDtoList) {

                userContext = new UserContext();
                User user = userDao.getUserByID(userContextDto.getUserId());
                UserContext existContext = userDao.getUserContextByUserId(userContextDto.getUserId());

                if (userContextDto.getActivity() == 1) {

                    if (existContext == null) {
                        userContext.setContext(getContextObjForEmployee(userContextDto));
                        userContext.setUser(user);
                        userContext.setVersion(1);
                        userDao.saveUserContext(userContext);
                        logger.info("User context save success.");

                    } else {
                        logger.info("User context already exist.");
                        if(userContextDto.getTeamId() != null) {
                            existContext.setContext(getContextObjFromExist(userContextDto.getTeamId(),
                                    existContext.getContext()));
                            userDao.updateUserContext(existContext);
                            logger.info("User context update success.");
                        }
                    }

                } else if (userContextDto.getActivity() == 2) {

                    if(existContext != null){
                        String context = getContextObjAfterRemove(existContext.getContext(),
                                userContextDto.getTeamId());
                        existContext.setContext(context);
                        userDao.updateUserContext(existContext);
                        logger.info("Update user context.");
                    }
                }
            }
            close(session);

        } catch (JSONException je){
            close(session);
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @Override
    public UserContext getUserContextByUserId(String userId) {
        logger.info("Read user context by user id: " + userId);
        UserContext userContext;

        Session session = getSession();
        userDao.setSession(session);
        userRoleDao.setSession(session);

        UserRole userRole = userRoleDao.getUserRoleByUserID(userId);
        if(userRole.getRole().getName().equals(RoleName.MEMBER.getValue())
                || userRole.getRole().getName().equals(RoleName.LEAD.getValue())){

            userContext = userDao.getUserContextByUserId(userId);
            if(userContext != null){
                close(session);
                return userContext;
            }
        }

        close(session);
        return null;
    }

    @Override
    public System getSystemByUserID(String userID) throws CustomException {
        Session session = getSession();
        userDao.setSession(session);

        System system = userDao.getSystemByUserID(userID);
        if (system == null) {
            close(session);
            ErrorContext errorContext = new ErrorContext(userID, "System", "System not found by userID: " + userID);
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0005,
                    Constants.AUTHORIZATION_SERVICE_0005_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }

        close(session);
        return system;
    }

    private String getContextObjForEmployee(UserContextDto contextDto) throws JSONException {
        JSONArray contextArray = new JSONArray();
        JSONObject contextObj = new JSONObject();

        if(contextDto.getEmployeeId() != null){
            contextArray.put(contextDto.getEmployeeId());
            contextObj.put("employeeId", contextArray);
        }

        return contextObj.toString();
    }

    private String getContextObjFromExist(String teamId, String context) throws JSONException {
        JSONObject existContextObj = new JSONObject(context);
        if(existContextObj.has("teamId")) {
            JSONArray existContextArray = existContextObj.getJSONArray("teamId");
            existContextArray.put(teamId);

        } else {
            JSONArray contextArray = new JSONArray();
            contextArray.put(teamId);

            existContextObj.put("teamId", contextArray);
        }

        return existContextObj.toString();
    }

    private String getContextObjAfterRemove(String context, String teamId) throws JSONException {
        JSONObject existContextObj = new JSONObject(context);
        JSONArray existContextArray = existContextObj.getJSONArray("teamId");
        existContextArray.remove(teamId);

        if(existContextArray.length() <= 0){
            existContextObj.remove("teamId");
        }

        return existContextObj.toString();
    }
}
