package com.sonicle.webtop.contacts.swagger.v1.api;

import com.sonicle.webtop.contacts.swagger.v1.model.SyncContact;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContactStat;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncContactUpdate;
import com.sonicle.webtop.contacts.swagger.v1.model.SyncFolder;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/eas")
@Api(description = "the eas API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2019-01-18T14:59:42.003+01:00")
public abstract class EasApi extends com.sonicle.webtop.core.sdk.BaseRestApiResource {

    @POST
    @Path("/folders/{folderId}/messages")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a message", notes = "", response = SyncContactStat.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "eas-messages",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Success", response = SyncContactStat.class) })
    public Response addMessage(@PathParam("folderId") @ApiParam("Folder ID") Integer folderId,@Valid SyncContactUpdate body) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/folders/{folderId}/messages/{id}")
    @ApiOperation(value = "Deletes a message", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "eas-messages",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class) })
    public Response deleteMessage(@PathParam("folderId") @ApiParam("Folder ID") Integer folderId,@PathParam("id") @ApiParam("Message ID") Integer id) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/folders")
    @Produces({ "application/json" })
    @ApiOperation(value = "List all folders", notes = "", response = SyncFolder.class, responseContainer = "List", authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "eas-folders",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = SyncFolder.class, responseContainer = "List") })
    public Response getFolders() {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/folders/{folderId}/messages/{id}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a single message", notes = "", response = SyncContact.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "eas-messages",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = SyncContact.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class) })
    public Response getMessage(@PathParam("folderId") @ApiParam("Folder ID") Integer folderId,@PathParam("id") @ApiParam("Message ID") Integer id,@QueryParam("picture")   @ApiParam("Determine whether to return picture data")  Boolean picture) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/folders/{folderId}/messages-stats")
    @Produces({ "application/json" })
    @ApiOperation(value = "List all messages for a specific folder", notes = "", response = SyncContactStat.class, responseContainer = "List", authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "eas-messages",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = SyncContactStat.class, responseContainer = "List") })
    public Response getMessagesStats(@PathParam("folderId") Integer folderId) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/folders/{folderId}/messages/{id}")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Updates a message", notes = "", response = SyncContactStat.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "eas-messages" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = SyncContactStat.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class) })
    public Response updateMessage(@PathParam("folderId") @ApiParam("Folder ID") Integer folderId,@PathParam("id") @ApiParam("Message ID") Integer id,@Valid SyncContactUpdate body,@QueryParam("picture")   @ApiParam("Determine whether to update picture data")  Boolean picture) {
        return Response.ok().entity("magic!").build();
    }
}
