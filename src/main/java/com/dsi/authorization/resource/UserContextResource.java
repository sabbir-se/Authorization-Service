package com.dsi.authorization.resource;

import com.dsi.authorization.dto.UserContextDto;
import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.User;
import com.dsi.authorization.model.UserContext;
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
import java.util.List;

/**
 * Created by sabbir on 1/25/17.
 */
@Path("/v1/user/context")
@Api(value = "/UserContext", description = "Operations about User Context")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class UserContextResource {

    private static final Logger logger = Logger.getLogger(UserContextResource.class);

    private static final UserService userService = new UserServiceImpl();

    @Context
    HttpServletRequest request;

    @POST
    @ApiOperation(value = "User Context Create", notes = "User Context Create", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "User context create success"),
            @ApiResponse(code = 500, message = "User context create failed, unauthorized.")
    })
    public Response createUserContext(List<UserContextDto> userContextDtoList) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            logger.info("User context operation:: Start");
            userService.saveOrUpdateUserContext(userContextDtoList);
            logger.info("User context operation:: End");

            responseObj.put(Constants.MESSAGE, "Create user context success.");
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
    @ApiOperation(value = "Read User Context", notes = "Read User Context", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Read user context success"),
            @ApiResponse(code = 500, message = "Read user context failed, unauthorized.")
    })
    public Response readUserContext(@PathParam("user_id") String userId) throws CustomException {

        JSONObject responseObj = new JSONObject();
        try{
            UserContext userContext = userService.getUserContextByUserId(userId);

            responseObj.put(Constants.MESSAGE, "Success");
            if(userContext != null) {
                responseObj.put("context", userContext.getContext());
            }

            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }
}
