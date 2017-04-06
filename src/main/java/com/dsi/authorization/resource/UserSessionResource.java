package com.dsi.authorization.resource;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.UserRole;
import com.dsi.authorization.model.UserSession;
import com.dsi.authorization.service.UserRoleService;
import com.dsi.authorization.service.UserSessionService;
import com.dsi.authorization.service.impl.UserRoleServiceImpl;
import com.dsi.authorization.service.impl.UserSessionServiceImpl;
import com.dsi.authorization.util.Constants;
import com.dsi.authorization.util.Utility;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sabbir on 7/11/16.
 */

@Path("/v1/user_session")
@Api(value = "/UserSession", description = "Operations about UserSession")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserSessionResource {

    private static final Logger logger = Logger.getLogger(UserSessionResource.class);

    private static final UserSessionService userSessionService = new UserSessionServiceImpl();
    private static final UserRoleService userRoleService = new UserRoleServiceImpl();

    @POST
    @ApiOperation(value = "User Session Create", notes = "User Session Create", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User session create success"),
            @ApiResponse(code = 500, message = "User session create failed, unauthorized.")
    })
    public Response createUserSession(UserSession userSession) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            logger.info("Access Token: " + userSession.getAccessToken());

            userSessionService.saveUserSession(userSession);
            logger.info("User session save successfully.");

            UserRole userRole = userRoleService.getUserRoleByUserID(userSession.getUserId());
            if(userRole != null){
                responseObj.put("roleName", userRole.getRole().getName());
            }

            responseObj.put(Constants.MESSAGE, "Create user session success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @PUT
    @ApiOperation(value = "User Session Update", notes = "User Session Update", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User session update success"),
            @ApiResponse(code = 500, message = "User session update failed, unauthorized.")
    })
    public Response updateUserSession(String requestBody) throws CustomException {
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj;
        try{
            logger.info("Request Body:: " + requestBody);
            requestObj = new JSONObject(requestBody);

            userSessionService.updateUserSession(requestObj);
            logger.info("User session update successfully.");

            responseObj.put(Constants.MESSAGE, "Update user session success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @DELETE
    @ApiOperation(value = "Delete User Session", notes = "Delete User Session", position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Delete user session success"),
            @ApiResponse(code = 500, message = "Delete user session failed, unauthorized.")
    })
    public Response deleteUserSession(String requestBody) throws CustomException {
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj;
        try {
            logger.info("Request Body:: " + requestBody);
            requestObj = new JSONObject(requestBody);

            userSessionService.deleteUserSession(requestObj);
            logger.info("Delete user session successfully.");

            responseObj.put(Constants.MESSAGE, "Delete user session success");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @POST
    @Path("/is_valid")
    @ApiOperation(value = "Get Valid User Session", notes = "Get Valid User Session", position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Get valid user session success"),
            @ApiResponse(code = 500, message = "Get valid user session failed, unauthorized.")
    })
    public Response getLoginSession(String requestBody) throws CustomException {
        JSONObject responseObj = new JSONObject();
        JSONObject requestObj;
        try {
            logger.info("Request Body:: " + requestBody);
            requestObj = new JSONObject(requestBody);
            String userID = Utility.validation(requestObj, "userId");
            String accessToken = Utility.validation(requestObj, "accessToken");

            userSessionService.getUserSessionByUserIdAndAccessToken(userID, accessToken);
            logger.info("Valid user session found.");

            responseObj.put(Constants.MESSAGE, "Success");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }
}
