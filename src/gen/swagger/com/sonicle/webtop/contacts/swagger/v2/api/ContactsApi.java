package com.sonicle.webtop.contacts.swagger.v2.api;

import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactPictureMeta;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/contacts/{contact_id}/picture")
@Api(description = "the contacts API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-01-31T11:55:24.983+01:00[Europe/Berlin]")
public abstract class ContactsApi extends com.sonicle.webtop.core.sdk.BaseRestApiResource {

    @GET
    @Path("/$value")
    @Produces({ "application/octet-stream" })
    @ApiOperation(value = "Get contact picture binary data", notes = "Gets the contact's picture binary-data, if any, for a given contact ID.", response = Object.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Object.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response getContactPictureData(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a contact picture metadata", notes = "Gets the contact's picture metatada given a contact ID.", response = ApiContactPictureMeta.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiContactPictureMeta.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response getContactPictureMeta(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @ApiOperation(value = "Set contact picture", notes = "Assign a picture to the given contact ID. The picture should be in binary. It replaces any existing photo for that contact.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response setContactPicture(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId) {
        return Response.ok().entity("magic!").build();
    }
}
