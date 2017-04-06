package com.dsi.authorization.resource;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.Role;
import com.dsi.authorization.model.Role;
import com.dsi.authorization.service.RoleService;
import com.dsi.authorization.service.impl.RoleServiceImpl;
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
import java.util.List;

/**
 * Created by sabbir on 6/27/16.
 */

@Path("/v1/role")
@Api(value = "/Role", description = "Operations about Role")
@Produces({MediaType.APPLICATION_JSON})
@Consumes({MediaType.APPLICATION_JSON})
public class RoleResource {

    private static final Logger logger = Logger.getLogger(RoleResource.class);

    private static final RoleService roleService = new RoleServiceImpl();

    @POST
    @ApiOperation(value = "Role Create", notes = "Role Create", position = 1)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Role create success"),
            @ApiResponse(code = 500, message = "Role create failed, unauthorized.")
    })
    public Response createRole(Role role) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            roleService.saveRole(role);
            logger.info("Role create successfully.");

            responseObj.put(Constants.MESSAGE, "Create role success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @PUT
    @ApiOperation(value = "Role Update", notes = "Role Update", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Role update success"),
            @ApiResponse(code = 500, message = "Role update failed, unauthorized.")
    })
    public Response updateRole(Role role) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            roleService.updateRole(role);
            logger.info("Role update successfully.");

            responseObj.put(Constants.MESSAGE, "Update role success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @DELETE
    @Path("/{role_id}")
    @ApiOperation(value = "Role Delete", notes = "Role Delete", position = 3)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Role delete success"),
            @ApiResponse(code = 500, message = "Role delete failed, unauthorized.")
    })
    public Response deleteRole(@PathParam("role_id") String roleID) throws CustomException {
        JSONObject responseObj = new JSONObject();

        try{
            roleService.deleteRole(roleID);
            logger.info("Role delete successfully.");

            responseObj.put(Constants.MESSAGE, "Delete role success.");
            return Response.ok().entity(responseObj.toString()).build();

        } catch (JSONException je){
            ErrorContext errorContext = new ErrorContext(null, null, je.getMessage());
            ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0009,
                    Constants.AUTHORIZATION_SERVICE_0009_DESCRIPTION, errorContext);
            throw new CustomException(errorMessage);
        }
    }

    @GET
    @ApiOperation(value = "Read All Roles", notes = "Read All Roles", position = 4)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Read role success"),
            @ApiResponse(code = 500, message = "Read role failed, unauthorized.")
    })
    public Response getAllRoles() throws CustomException {
        List<Role> roleList = roleService.getAllRoles();
        logger.info("Role list size: " + roleList.size());

        return Response.ok().entity(roleList).build();
    }
}
