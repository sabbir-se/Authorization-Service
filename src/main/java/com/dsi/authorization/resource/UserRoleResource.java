package com.dsi.authorization.resource;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.User;
import com.dsi.authorization.model.UserRole;
import com.dsi.authorization.service.UserRoleService;
import com.dsi.authorization.service.impl.UserRoleServiceImpl;
import com.dsi.authorization.util.Constants;
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
 * Created by sabbir on 6/24/16.
 */

@Path("/v1/user_role")
@Api(value = "/UserRole", description = "Operations about User Role")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserRoleResource {

    private static final Logger logger = Logger.getLogger(UserRoleResource.class);

    private static final UserRoleService userRoleService = new UserRoleServiceImpl();

    @POST
    @ApiOperation(value = "User Role Create", notes = "User Role Create", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User create success"),
            @ApiResponse(code = 500, message = "User create failed, unauthorized.")
    })
    public Response createUserRole(UserRole userRole) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            userRoleService.saveUserRole(userRole);
            logger.info("User role create successfully.");

            responseObj.put(Constants.MESSAGE, "Create user role success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @PUT
    @ApiOperation(value = "User Role Update", notes = "User Role Update", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User role update success"),
            @ApiResponse(code = 500, message = "User role update failed, unauthorized.")
    })
    public Response updateUserRole(UserRole userRole) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            userRoleService.updateUserRole(userRole);
            logger.info("User role update successfully.");

            responseObj.put(Constants.MESSAGE, "Update user role success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @DELETE
    @Path("/{user_role_id}")
    @ApiOperation(value = "User Role Delete", notes = "User Role Delete", position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User role delete success"),
            @ApiResponse(code = 500, message = "User role delete failed, unauthorized.")
    })
    public Response deleteUser(@PathParam("user_role_id") String userRoleID) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            userRoleService.deleteUserRole(userRoleID);
            logger.info("User role delete successfully.");

            responseObj.put(Constants.MESSAGE, "Delete user role success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @GET
    @Path("/{user_id}")
    @ApiOperation(value = "Read User Role By User Id", notes = "Read User Role By User Id", position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Read user role success"),
            @ApiResponse(code = 500, message = "Read user role failed, unauthorized.")
    })
    public Response readUser(@PathParam("user_id") String userID) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            UserRole userRole = userRoleService.getUserRoleByUserID(userID);
            logger.info("Read userRole success.");

            responseObj.put("role_id", userRole.getRole().getRoleId());
            responseObj.put("role_name", userRole.getRole().getName());
            responseObj.put(Constants.MESSAGE, "Read user role success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }
}
