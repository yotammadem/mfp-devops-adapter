/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
 */

package com.acme.apis;

import com.acme.apis.models.Contact;
import io.swagger.annotations.*;

import java.util.*;
import java.util.logging.Logger;

import javax.ws.rs.*;
import javax.ws.rs.core.*;

import com.ibm.mfp.adapter.api.ConfigurationAPI;

@Api(value = "Contact list adapter")
@Path("/")
public class ContactListApiResource {
    /*
	 * For more info on JAX-RS see
	 * https://jax-rs-spec.java.net/nonav/2.0-rev-a/apidocs/index.html
	 */

    // Define logger (Standard java.util.Logger)
    static Logger logger = Logger.getLogger(ContactListApiResource.class.getName());

    // Inject the MFP configuration API:
    @Context
    ConfigurationAPI configApi;


    static Map<String, Contact> contactMap = new HashMap<String, Contact>();


    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Contact[].class)
    })
    @ApiOperation(value = "Returns A list of all the contacts")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Collection<Contact> getAllContacts() {
        return contactMap.values();
    }

    @ApiOperation(value = "Add contact")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Created", responseHeaders = {
                    @ResponseHeader(name = "Location", description = "Location (URL) of the created contact")
            }),
            @ApiResponse(code = 406, message = "Contact (name) already exist")
    })
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    public Response addContact(Contact contact, @Context UriInfo uriInfo) {
        if (contactMap.containsKey(contact.name)) {
            return Response.status(409).entity("Contact named: " + contact.name + " already exist").build();
        }

        contactMap.put(contact.name, contact);

        UriBuilder builder = uriInfo.getAbsolutePathBuilder();
        builder.path(contact.name);
        return Response.created(builder.build()).build();
    }

    @ApiOperation(value = "Update contact")
    @ApiResponses(value = {
            @ApiResponse(code = 201, message = "Updated successfully"),
            @ApiResponse(code = 404, message = "Contact not found")
    })
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public Response updateContact(Contact contact) {
        if (contactMap.containsKey(contact.name)) {
            contactMap.put(contact.name, contact);
        } else {
            throw new NotFoundException(contact.name);
        }
        return Response.ok().build();
    }

    @ApiOperation(value = "Get contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success", response = Contact.class),
            @ApiResponse(code = 404, message = "Contact not found")
    })
    @GET
    @Path("{name}")
    @Produces(MediaType.APPLICATION_JSON)
    public Contact getContact(@PathParam("name") String name) {
        Contact contact = contactMap.get(name);
        if (contact == null) {
            throw new NotFoundException(name);
        }
        return contact;
    }

    @ApiOperation(value = "Delete contact")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success"),
            @ApiResponse(code = 404, message = "Contact not found")
    })
    @DELETE
    @Path("{name}")
    public Response deleteContact(@PathParam("name") String name) {
        Contact contact = contactMap.remove(name);
        if (contact == null) {
            throw new NotFoundException(name);
        }
        return Response.ok().build();
    }

}
