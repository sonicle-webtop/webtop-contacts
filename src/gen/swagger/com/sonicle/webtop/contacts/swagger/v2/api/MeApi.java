package com.sonicle.webtop.contacts.swagger.v2.api;

import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoriesResult;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategory;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiCategoryBase;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContact;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactBase;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactEx;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactPictureMeta;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactsResult;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiContactsResultDelta;
import java.io.File;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/me")
@Api(description = "the me API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2025-02-06T10:04:09.242+01:00[Europe/Berlin]")
public abstract class MeApi extends com.sonicle.webtop.core.sdk.BaseRestApiResource {

    @POST
    @Path("/categories")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a Category", notes = "Adds new category specifying the owning user ID. If no user ID is provided, the owner will be the current user.", response = ApiCategory.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Category created", response = ApiCategory.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 405, message = "Not allowed", response = Void.class)
    })
    public Response addCategory(@QueryParam("user_id")  @ApiParam("The ID of a user")  String userId,@Valid ApiCategoryBase body) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/categories/{category_id}/contacts")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a contact", notes = "Adds new contact into given category.", response = ApiContact.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Contact created", response = ApiContact.class),
        @ApiResponse(code = 404, message = "Category not Found", response = Void.class)
    })
    public Response addCategoryContact(@PathParam("category_id") @ApiParam("The ID of a category") String categoryId,@Valid ApiContactEx body) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/contacts")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Add a contact", notes = "Adds new contact into specified category.", response = ApiContact.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me", "contacts" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Contact created", response = ApiContact.class)
    })
    public Response addContact(@QueryParam("category_id") @NotNull  @ApiParam("The category ID where the contact will be added into")  String categoryId,@Valid ApiContactEx body) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/contacts/{contact_id}/picture")
    @ApiOperation(value = "Delete contact picture", notes = "Deletes the contact's picture of a given contact ID.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Contact's picture deleted", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response clearContactPicture(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/categories/{category_id}")
    @ApiOperation(value = "Delete a category", notes = "Delete a category given its ID.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Category deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Category not found", response = Void.class),
        @ApiResponse(code = 405, message = "Not allowed", response = Void.class)
    })
    public Response deleteCategory(@PathParam("category_id") @ApiParam("The ID of a category") String categoryId) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/contacts/{contact_id}")
    @ApiOperation(value = "Delete a contact", notes = "Delete a contact given its ID.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Contact deleted", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response deleteContact(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/categories/{category_id}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a category", notes = "Gets the specified category given its ID.", response = ApiCategory.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiCategory.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Category not found", response = Void.class),
        @ApiResponse(code = 405, message = "Not allowed", response = Void.class)
    })
    public Response getCategory(@PathParam("category_id") @ApiParam("The ID of a category") String categoryId) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/contacts/{contact_id}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a contact", notes = "Gets the specified contact given its ID.", response = ApiContact.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me", "contacts" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiContact.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response getContact(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId,@QueryParam("_select")  @ApiParam("List (comma-separated) of field names to include in resulting items. Optional.")  String select) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/contacts/{contact_id}/picture/$value")
    @Produces({ "application/octet-stream" })
    @ApiOperation(value = "Get contact picture binary data", notes = "Gets the contact's picture raw data (binary), if any, for a given contact ID.", response = Object.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Object.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response getContactPictureBytes(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/contacts/{contact_id}/picture")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a contact picture metadata", notes = "Gets the contact's picture metatada of a given contact ID.", response = ApiContactPictureMeta.class, authorizations = {
        
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

    @GET
    @Path("/categories")
    @Produces({ "application/json" })
    @ApiOperation(value = "List Categories", notes = "Returns a list of categories readable by the current user: this includes both personal and incoming shared categories.", response = ApiCategoriesResult.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiCategoriesResult.class),
        @ApiResponse(code = 405, message = "Not allowed", response = Void.class)
    })
    public Response listCategories(@QueryParam("_filter")  @ApiParam("A RSQL filter query to filter out resulting items. Optional.")  String filter,@QueryParam("_select")  @ApiParam("List (comma-separated) of field names to include in resulting items. Optional.")  String select,@QueryParam("_order_by")  @ApiParam("List (comma-separated) of field names and direction (ASC or DESC) to sort resulting items. Optional.")  String orderBy,@QueryParam("_page_no") @Min(1)  @ApiParam("The page number to return, providing a value actually activates pagination. Optional.")  Integer pageNo,@QueryParam("_page_size") @Min(1)  @ApiParam("How many items to return when paginating. Defaults to 50.")  Integer pageSize,@QueryParam("_return_count")  @ApiParam("Specifies whether to compute and return the full count of a list of items. Useful when dealing with paginated data. Optional.")  Boolean returnCount) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/categories/{category_id}/contacts")
    @Produces({ "application/json" })
    @ApiOperation(value = "List contacts", notes = "Returns a list of contacts from specified category.", response = ApiContactsResult.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiContactsResult.class),
        @ApiResponse(code = 404, message = "Category not found", response = Void.class),
        @ApiResponse(code = 405, message = "Not allowed", response = Void.class)
    })
    public Response listCategoryContacts(@PathParam("category_id") @ApiParam("The ID of a category") String categoryId,@QueryParam("_filter")  @ApiParam("A RSQL filter query to filter out resulting items. Optional.")  String filter,@QueryParam("_select")  @ApiParam("List (comma-separated) of field names to include in resulting items. Optional.")  String select,@QueryParam("_order_by")  @ApiParam("List (comma-separated) of field names and direction (ASC or DESC) to sort resulting items. Optional.")  String orderBy,@QueryParam("_page_no") @Min(1)  @ApiParam("The page number to return, providing a value actually activates pagination. Optional.")  Integer pageNo,@QueryParam("_page_size") @Min(1)  @ApiParam("How many items to return when paginating. Defaults to 50.")  Integer pageSize,@QueryParam("_return_count")  @ApiParam("Specifies whether to compute and return the full count of a list of items. Useful when dealing with paginated data. Optional.")  Boolean returnCount) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/categories/{category_id}/contacts/delta")
    @Produces({ "application/json" })
    @ApiOperation(value = "List changes on contacts collection", notes = "Get a set of contacts that have been added, deleted, or updated in a specified category, starting from a precise instant identified by a given syncToken.", response = ApiContactsResultDelta.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiContactsResultDelta.class)
    })
    public Response listCategoryContactsDelta(@PathParam("category_id") @ApiParam("The ID of a category") String categoryId,@QueryParam("_sync_token")  @ApiParam("Token exchanged between client and server that tracks changes from a precise state.")  String syncToken,@QueryParam("_select")  @ApiParam("List (comma-separated) of field names to include in resulting items. Optional.")  String select) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/contacts/{contact_id}/picture")
    @Consumes({ "image/*" })
    @ApiOperation(value = "Set contact picture", notes = "Assign a picture to the given contact ID. The picture should be in binary. It replaces any existing photo for that contact.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response setContactPicture(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId,@HeaderParam("Content-Type")   @ApiParam("The picture media-type.") String contentType,@Valid File body) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/categories/{category_id}")
    @Consumes({ "application/json" })
    @ApiOperation(value = "Update a category", notes = "Update the specified category given its ID. You can choose to update the entire object or only a subset of data.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Category updated", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Category not found", response = Void.class),
        @ApiResponse(code = 405, message = "Not allowed", response = Void.class)
    })
    public Response updateCategory(@PathParam("category_id") @ApiParam("The ID of a category") String categoryId,@Valid ApiCategoryBase body) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/contacts/{contact_id}")
    @Consumes({ "application/json" })
    @ApiOperation(value = "Update a contact", notes = "Update the specified contact given its ID. You can choose to update the entire object or only a subset of data.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-bearer"),
        
        @Authorization(value = "auth-basic")
         }, tags={ "me", "contacts" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Contact updated", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Contact not found", response = Void.class)
    })
    public Response updateContact(@PathParam("contact_id") @ApiParam("The ID of a contact") String contactId,@QueryParam("_update")  @ApiParam("List (comma-separated) of field names to update. Optional.")  String update,@Valid ApiContactEx body) {
        return Response.ok().entity("magic!").build();
    }
}
