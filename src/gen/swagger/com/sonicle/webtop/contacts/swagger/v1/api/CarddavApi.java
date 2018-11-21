package com.sonicle.webtop.contacts.swagger.v1.api;

import com.sonicle.webtop.contacts.swagger.v1.model.AddressBook;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBookNew;
import com.sonicle.webtop.contacts.swagger.v1.model.AddressBookUpdate;
import com.sonicle.webtop.contacts.swagger.v1.model.Card;
import com.sonicle.webtop.contacts.swagger.v1.model.CardNew;
import com.sonicle.webtop.contacts.swagger.v1.model.CardsChanges;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;

import io.swagger.annotations.*;

import java.util.Map;
import java.util.List;
import javax.validation.constraints.*;
import javax.validation.Valid;

@Path("/carddav")
@Api(description = "the carddav API")
@javax.annotation.Generated(value = "io.swagger.codegen.languages.JavaJAXRSSpecServerCodegen", date = "2018-11-21T12:36:54.502+01:00")
public abstract class CarddavApi extends com.sonicle.webtop.core.sdk.BaseRestApiResource {

    @POST
    @Path("/addressbooks")
    @Consumes({ "application/json" })
    @Produces({ "application/json" })
    @ApiOperation(value = "Adds a new address book", notes = "", response = AddressBook.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-addressbooks",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Success", response = AddressBook.class) })
    public Response addAddressBook(@Valid AddressBookNew body) {
        return Response.ok().entity("magic!").build();
    }

    @POST
    @Path("/addressbooks/{addressBookUid}/cards")
    @Consumes({ "application/json" })
    @ApiOperation(value = "Adds a new card", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-cards",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 201, message = "Success", response = Void.class) })
    public Response addCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@Valid CardNew body) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/addressbooks/{addressBookUid}")
    @ApiOperation(value = "Deletes an address book", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-addressbooks",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Address book not found", response = Void.class),
        @ApiResponse(code = 405, message = "Delete operation is not allowed", response = Void.class) })
    public Response deleteAddressBook(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid) {
        return Response.ok().entity("magic!").build();
    }

    @DELETE
    @Path("/addressbooks/{addressBookUid}/cards/{href}")
    @ApiOperation(value = "Deletes a card", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-cards",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 204, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Card not found", response = Void.class) })
    public Response deleteCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@PathParam("href") @ApiParam("Card reference URI") String href) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/addressbooks/{addressBookUid}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Gets a single address book", notes = "", response = AddressBook.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-addressbooks",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = AddressBook.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Address book not found", response = Void.class) })
    public Response getAddressBook(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/addressbooks")
    @Produces({ "application/json" })
    @ApiOperation(value = "List all address books", notes = "", response = AddressBook.class, responseContainer = "List", authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-addressbooks",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = AddressBook.class, responseContainer = "List") })
    public Response getAddressBooks() {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/addressbooks/{addressBookUid}/cards/{href}")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get a single card", notes = "", response = Card.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-cards",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Card.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Card not found", response = Void.class) })
    public Response getCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@PathParam("href") @ApiParam("Card reference URI") String href) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/addressbooks/{addressBookUid}/cards")
    @Produces({ "application/json" })
    @ApiOperation(value = "List all cards for a specific addressbook", notes = "", response = Card.class, responseContainer = "List", authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-cards",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Card.class, responseContainer = "List") })
    public Response getCards(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@QueryParam("hrefs")    List<String> hrefs) {
        return Response.ok().entity("magic!").build();
    }

    @GET
    @Path("/addressbooks/{addressBookUid}/cards/changes")
    @Produces({ "application/json" })
    @ApiOperation(value = "Get card changes", notes = "Returns changed cards (added/modified/deleted) since the specified syncToken. If token is not provided, the initial sync configuration will be returned.", response = CardsChanges.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-cards",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = CardsChanges.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class) })
    public Response getCardsChanges(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@QueryParam("syncToken")   @ApiParam("Marks changes starting point")  String syncToken,@QueryParam("limit")   @ApiParam("Limits the number of returned results")  Integer limit) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/addressbooks/{addressBookUid}")
    @Consumes({ "application/json" })
    @ApiOperation(value = "Updates an address book", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-addressbooks",  })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Address book not found", response = Void.class) })
    public Response updateAddressBook(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@Valid AddressBookUpdate body) {
        return Response.ok().entity("magic!").build();
    }

    @PUT
    @Path("/addressbooks/{addressBookUid}/cards/{href}")
    @Consumes({ "text/vcard" })
    @ApiOperation(value = "Updates a card", notes = "", response = Void.class, authorizations = {
        @Authorization(value = "Basic authentication")
    }, tags={ "dav-cards" })
    @ApiResponses(value = { 
        @ApiResponse(code = 200, message = "Success", response = Void.class),
        @ApiResponse(code = 400, message = "Invalid parameter", response = Void.class),
        @ApiResponse(code = 404, message = "Card not found", response = Void.class) })
    public Response updateCard(@PathParam("addressBookUid") @ApiParam("Address book UID") String addressBookUid,@PathParam("href") @ApiParam("Card reference URI") String href,@Valid String body) {
        return Response.ok().entity("magic!").build();
    }
}
