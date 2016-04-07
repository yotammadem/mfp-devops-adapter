package com.acme.apis.models;

/**
 * Created by yotamm on 04/04/16.
 */
public class Contact {
    public String name;
    public String phoneNumber;

    public Contact(){

    }

    public Contact(String name, String phoneNumber){
        this.name = name;
        this.phoneNumber = phoneNumber;
    }
}
