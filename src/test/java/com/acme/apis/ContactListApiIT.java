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
import java.util.HashMap;
import java.util.Map;

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
        ResponseEntity operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, DEFAULT_SCOPE);
        Assert.assertEquals(201, operationResult.getStatusCode().value());
        String location = operationResult.getHeaders().get("Location").toString();
        Assert.assertTrue(location.contains("/tedy"));

        //Make sure the contact is there
        ResponseEntity<Contact> contactResp = restUtil.performRequest("/tedy", Contact.class, null, HttpMethod.GET, DEFAULT_SCOPE);
        Assert.assertEquals(200, contactResp.getStatusCode().value());
        Assert.assertEquals("tedy", contactResp.getBody().name);
        Assert.assertEquals("12345", contactResp.getBody().phoneNumber);

        //Try to add the same contact twice (expect conflict)
        operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, DEFAULT_SCOPE);
        Assert.assertEquals(409, operationResult.getStatusCode().value());

        //Just clean up
        ResponseEntity deleteResp = restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);
        Assert.assertEquals(200, deleteResp.getStatusCode().value());

        //Make sure contact is not there after delete
        contactResp = restUtil.performRequest("/tedy", Contact.class, null, HttpMethod.GET, DEFAULT_SCOPE);
        Assert.assertEquals(404, contactResp.getStatusCode().value());
    }

    @Test
    public void testDeleteContact() throws IOException {
        //delete the contact (and ignore the result) just in case the DB was dirty
        restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);

        Contact contact = new Contact();
        contact.name = "tedy";
        contact.phoneNumber = "12345";
        //Test add new contact
        ResponseEntity operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, DEFAULT_SCOPE);
        Assert.assertEquals(201, operationResult.getStatusCode().value());

        //Delete the contact
        ResponseEntity deleteResp = restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);
        Assert.assertEquals(200, deleteResp.getStatusCode().value());

        //Make sure the contact is not there
        ResponseEntity<Contact> contactResp = restUtil.performRequest("/tedy", Contact.class, null, HttpMethod.GET, DEFAULT_SCOPE);
        Assert.assertEquals(404, contactResp.getStatusCode().value());

        //Make sure cannot delete twice
        deleteResp = restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);
        Assert.assertEquals(404, deleteResp.getStatusCode().value());
    }


    @Test
    public void testUpdateContact() throws IOException {
        //delete the contact (and ignore the result) just in case the DB was dirty
        restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);

        Contact contact = new Contact();
        contact.name = "tedy";
        contact.phoneNumber = "12345";
        //Test add new contact
        ResponseEntity operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, DEFAULT_SCOPE);
        Assert.assertEquals(201, operationResult.getStatusCode().value());

        contact.phoneNumber = "111";

        operationResult = restUtil.performRequest("/", null, contact, HttpMethod.PUT, DEFAULT_SCOPE);
        Assert.assertEquals(200, operationResult.getStatusCode().value());

        //Make sure the contact is updated
        ResponseEntity<Contact> contactResp = restUtil.performRequest("/tedy", Contact.class, null, HttpMethod.GET, DEFAULT_SCOPE);
        Assert.assertEquals(200, contactResp.getStatusCode().value());
        Assert.assertEquals("111", contactResp.getBody().phoneNumber); //assert we got the updated phone number

        //Delete the contact
        restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);

        //Make sure it is not possible to update after deletion
        operationResult = restUtil.performRequest("/", null, contact, HttpMethod.PUT, DEFAULT_SCOPE);
        Assert.assertEquals(404, operationResult.getStatusCode().value());
    }

    @Test
    public void testGetAllContacts() throws IOException {
        //delete the contact (and ignore the result) just in case the DB was dirty
        restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);
        restUtil.performRequest("/tedy2", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);

        Contact contact = new Contact();
        contact.name = "tedy";
        contact.phoneNumber = "12345";
        //Test add new contact
        ResponseEntity operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, DEFAULT_SCOPE);
        Assert.assertEquals(201, operationResult.getStatusCode().value());

        contact.name = "tedy2";
        contact.phoneNumber = "1212";
        operationResult = restUtil.performRequest("/", null, contact, HttpMethod.POST, DEFAULT_SCOPE);
        Assert.assertEquals(201, operationResult.getStatusCode().value());

        ResponseEntity<Contact[]> allContacts = restUtil.performRequest("/", Contact[].class, null, HttpMethod.GET, DEFAULT_SCOPE);
        Assert.assertEquals(200, allContacts.getStatusCode().value());

        Map<String, Contact> contactMap = new HashMap<String, Contact>();
        for (Contact con : allContacts.getBody()){
            contactMap.put(con.name, con);
        }

        Contact tedy = contactMap.get("tedy");
        Contact tedy2 = contactMap.get("tedy2");

        Assert.assertNotNull(tedy);
        Assert.assertNotNull(tedy2);

        Assert.assertEquals("12345", tedy.phoneNumber);
        Assert.assertEquals("1212", tedy2.phoneNumber);

        //Cleanup
        restUtil.performRequest("/tedy", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);
        restUtil.performRequest("/tedy2", null, null, HttpMethod.DELETE, DEFAULT_SCOPE);
    }


}
