/*
 *    Licensed Materials - Property of IBM
 *    5725-I43 (C) Copyright IBM Corp. 2015. All Rights Reserved.
 *    US Government Users Restricted Rights - Use, duplication or
 *    disclosure restricted by GSA ADP Schedule Contract with IBM Corp.
*/

package com.acme.apis;

import java.util.logging.Logger;
import java.util.*;

import com.acme.apis.models.Contact;
import com.ibm.mfp.adapter.api.ConfigurationAPI;
import com.ibm.mfp.adapter.api.MFPJAXRSApplication;

import javax.ws.rs.core.Context;

public class ContactListApiApplication extends MFPJAXRSApplication{

	static Logger logger = Logger.getLogger(ContactListApiApplication.class.getName());
	

	@Context
	ConfigurationAPI configurationAPI;

	protected void init() throws Exception {
		String db = configurationAPI.getPropertyValue("db");
		if (db.equalsIgnoreCase("db1")){
			Map<String, Contact> contacts = ContactListApiResource.contactMap;
			contacts.clear();
			contacts.put("Sagy", new Contact("Sagy","1234"));
			contacts.put("Yotam", new Contact("Yotam","1234"));
		}else{
			Map<String, Contact> contacts = ContactListApiResource.contactMap;
			contacts.clear();
			contacts.put("James", new Contact("James","5555"));
			contacts.put("Mac", new Contact("Mac","6666"));
		}
		logger.info("Adapter initialized!");
	}
	

	protected void destroy() throws Exception {
		logger.info("Adapter destroyed!");
	}
	

	protected String getPackageToScan() {
		//The package of this class will be scanned (recursively) to find JAX-RS resources. 
		//It is also possible to override "getPackagesToScan" method in order to return more than one package for scanning
		return getClass().getPackage().getName();
	}
}
