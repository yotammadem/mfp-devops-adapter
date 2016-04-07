package com.acme.apis;

import com.acme.apis.models.Contact;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.io.IOException;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = IntegrationTestConfig.class)
public class ContactListApiIT {

    /**
     * By default, MFP adapters are protected by the default scope.
     */
    private static final String DEFAULT_SCOPE = "";

    @Autowired
    RESTUtil restUtil;

    /**
     * - Test that adding a new contact returns status 201 and the "Location" header contains the link to the added contact
     * - Make sure that calling to get contact after adding contact will return the same contact info
     * - Delete the contact and make sure that calling to get contact after deleting will fail.
     */
    @Test
    public void testAddContact() throws IOException {
        //delete the contact (and ignore the result) just in case the DB was dirty
        restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);

        Contact contact = new Contact();
        contact.name = "tedy";
        contact.phoneNumber = "12345";
        //Test add new contact
        ResponseEntity operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, "");
        Assert.assertEquals(201, operationResult.getStatusCode().value());
        String location = operationResult.getHeaders().get("Location").toString();
        Assert.assertTrue(location.contains("/tedy"));

        //Make sure the contact is there
        ResponseEntity<Contact> contactResp = restUtil.performRequest("/tedy", Contact.class, null, HttpMethod.GET, "");
        Assert.assertEquals(200, contactResp.getStatusCode().value());
        Assert.assertEquals("tedy", contactResp.getBody().name);
        Assert.assertEquals("12345", contactResp.getBody().phoneNumber);

        //Try to add the same contact twice (expect conflict)
        operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, "");
        Assert.assertEquals(409, operationResult.getStatusCode().value());

        //Just clean up
        ResponseEntity deleteResp = restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, "");
        Assert.assertEquals(200, deleteResp.getStatusCode().value());

        //Make sure contact is not there after delete
        contactResp = restUtil.performRequest("/tedy", Contact.class, null, HttpMethod.GET, "");
        Assert.assertEquals(404, contactResp.getStatusCode().value());
    }


    /**
     * - Add contact
     * - Delete the contact we just added (expected HTTP status: 200)
     * - Make sure the contact is not there after deletion (status: 404)
     * - Try to delete the same contact one more time and expect 404
     */
    @Test
    public void testDeleteContact(){
        //TODO implement
    }


    /**
     * - Add contact
     * - Then update the contact's phone number using PUT, (expect status 200)
     * - Get the contact and make sure the phone number was updated
     * - Delete the contact
     * - Try to update again and make sure we got 404
     */
    @Test
    public void testUpdateContact() {
        //TODO implement
    }

    /**
     * - Add 2 contacts
     * - Get all contacts and make sure the 2 contacts we've added are part of the list
     */
    @Test
    public void testGetAllContacts(){
        //TODO implement
    }

}
