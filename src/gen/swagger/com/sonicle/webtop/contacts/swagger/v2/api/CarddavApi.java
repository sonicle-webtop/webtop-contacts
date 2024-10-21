package com.sonicle.webtop.contacts.swagger.v2.api;

import com.sonicle.webtop.contacts.swagger.v2.model.ApiDavAddressBook;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiDavAddressBookNew;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiDavAddressBookUpdate;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiDavCard;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiDavCardNew;
import com.sonicle.webtop.contacts.swagger.v2.model.ApiDavCardsChanges;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import java.io.InputStream;
import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/carddav/addressbooks")
@Api(description = "the carddav API")
@javax.annotation.Generated(value = "org.openapitools.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2024-10-21T11:44:32.049+02:00[Europe/Berlin]")
public abstract class CarddavApi extends com.sonicle.webtop.core.sdk.BaseRestApiResource {

    @POST
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new address-book", notes = "Creates new AddressBook.", response = ApiDavAddressBook.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Success", response = ApiDavAddressBook.class)
    })
    public Response addAddressBook(@Valid @NotNull ApiDavAddressBookNew body) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/{addressBookUid}/cards")
    @Consumes({ "application/json" })
    @ApiOperation(value = "Adds a new card", notes = "Creates new Card into specified AddressBook.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Success", response = Void.class)
    })
    public Response addCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@Valid @NotNull ApiDavCardNew body) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/{addressBookUid}")
    @ApiOperation(value = "Deletes an address-book", notes = "Deletes specified AddressBook.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Address book not found", response = Void.class),
        @ApiResponse(code = 405, message = "Delete operation is not allowed", response = Void.class)
    })
    public Response deleteAddressBook(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/{addressBookUid}/cards/{href}")
    @ApiOperation(value = "Deletes a card", notes = "Deletes specified Card.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Card not found", response = Void.class)
    })
    public Response deleteCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@PathParam("href") @ApiParam("Card reference URI") String href) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/{addressBookUid}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Gets a single address-book", notes = "Gets the specified AddressBook.", response = ApiDavAddressBook.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiDavAddressBook.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Address book not found", response = Void.class)
    })
    public Response getAddressBook(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Produces({ "application/json" })
    @ApiOperation(value = "List all address-books", notes = "List available AddressBooks.", response = ApiDavAddressBook.class, responseContainer = "List", authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiDavAddressBook.class, responseContainer = "List")
    })
    public Response getAddressBooks() {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/{addressBookUid}/cards/{href}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a single card", notes = "Gets specified Card.", response = ApiDavCard.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiDavCard.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Card not found", response = Void.class)
    })
    public Response getCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@PathParam("href") @ApiParam("Card reference URI") String href) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/{addressBookUid}/cards")
    @Produces({ "application/json" })
    @ApiOperation(value = "List all cards for a specific address-book", notes = "List all Cards of specified AddressBook.", response = ApiDavCard.class, responseContainer = "List", authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiDavCard.class, responseContainer = "List")
    })
    public Response getCards(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@QueryParam("hrefs")   List<String> hrefs) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/{addressBookUid}/cards/changes")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get card changes", notes = "Returns changed cards (added/modified/deleted) since the specified syncToken. If token is not provided, the initial sync configuration will be returned.", response = ApiDavCardsChanges.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = ApiDavCardsChanges.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class)
    })
    public Response getCardsChanges(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@QueryParam("syncToken")  @ApiParam("Marks changes starting point")  String syncToken,@QueryParam("limit")  @ApiParam("Limits the number of returned results")  Integer limit) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/{addressBookUid}")
    @Consumes({ "application/json" })
    @ApiOperation(value = "Updates an address-book", notes = "Updates specified AddressBook.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Address book not found", response = Void.class)
    })
    public Response updateAddressBook(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@Valid @NotNull ApiDavAddressBookUpdate body) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/{addressBookUid}/cards/{href}")
    @Consumes({ "text/vcard" })
    @ApiOperation(value = "Updates a card", notes = "Updates specified Card.", response = Void.class, authorizations = {
        
        @Authorization(value = "auth-basic"),
        
        @Authorization(value = "auth-bearer")
         }, tags={ "dav" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Card not found", response = Void.class)
    })
    public Response updateCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@PathParam("href") @ApiParam("Card reference URI") String href,@Valid @NotNull String body) {
        return Response.ok().entity("magic!").build();
    }
}
