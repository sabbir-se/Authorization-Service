package com.dsi.authorization.resource;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.User;
import com.dsi.authorization.model.UserRole;
import com.dsi.authorization.service.UserService;
import com.dsi.authorization.service.impl.UserServiceImpl;
import com.dsi.authorization.util.Constants;
import com.google.gson.Gson;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.*;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

/**
 * Created by sabbir on 6/24/16.
 */

@Path("/v1/user")
@Api(value = "/User", description = "Operations about User")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserResource {

    private static final Logger logger = Logger.getLogger(UserResource.class);

    private static final UserService userService = new UserServiceImpl();

    @Context
    HttpServletRequest request;

    @POST
    @ApiOperation(value = "User Create With Role / Without Role", notes = "User Create With Role / Without Role", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User create success"),
            @ApiResponse(code = 500, message = "User create failed, unauthorized.")
    })
    public Response createUser(User user) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            logger.info("Request body: " + new Gson().toJson(user));

            logger.info("User create:: Start");
            UserRole userRole = userService.saveUser(user);
            logger.info("User create:: End");

            responseObj.put("user_id", userRole.getUser().getUserId());
            responseObj.put("role_name", userRole.getRole().getName());
            responseObj.put(Constants.MESSAGE, "Create user success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @PUT
    @Path("/{user_id}")
    @ApiOperation(value = "User Update", notes = "User Update", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User update success"),
            @ApiResponse(code = 500, message = "User update failed, unauthorized.")
    })
    public Response updateUser(@PathParam("user_id") String userId, User user) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            user.setUserId(userId);
            userService.updateUser(user);
            logger.info("User update successfully.");

            responseObj.put(Constants.MESSAGE, "Update user success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @DELETE
    @Path("/{user_id}")
    @ApiOperation(value = "User Delete", notes = "User Delete", position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User delete success"),
            @ApiResponse(code = 500, message = "User delete failed, unauthorized.")
    })
    public Response deleteUser(@PathParam("user_id") String userID) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            userService.deleteUser(userID);
            logger.info("User delete successfully.");

            responseObj.put(Constants.MESSAGE, "Delete user success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @GET
    @ApiOperation(value = "Read All User", notes = "Read All User", position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Read all user success"),
            @ApiResponse(code = 500, message = "Read all user failed, unauthorized.")
    })
    public Response readAllUserByRole(@QueryParam("type") String type) throws CustomException {

        return Response.ok().entity(userService.getAllUserByRole(type)).build();
    }

    @GET
    @Path("/{user_id}")
    @ApiOperation(value = "Read User", notes = "Read User", position = 5)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Read user success"),
            @ApiResponse(code = 500, message = "Read user failed, unauthorized.")
    })
    public Response readUser(@PathParam("user_id") String userId) throws CustomException {

        return Response.ok().entity(userService.getUserByID(userId)).build();
    }

    @GET
    @Path("/role")
    @ApiOperation(value = "Read All User", notes = "Read All User", position = 6)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Read all user success"),
            @ApiResponse(code = 500, message = "Read all user failed, unauthorized.")
    })
    public Response readUserByRole() throws CustomException {

        return Response.ok().entity(userService.getUsersByRoleType()).build();
    }
}
