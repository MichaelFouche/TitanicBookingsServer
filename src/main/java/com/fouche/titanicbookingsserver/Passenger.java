/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fouche.titanicbookingsserver;

/**
 *
 * @author foosh
 */
public class Passenger {
    private int passengerID;
    private String pName;
    private String pSurname;
    private String contactNumber;
    
    public Passenger(){
        
    }

    public Passenger(int passengerID, String pName, String pSurname, String contactNumber) {
        this.passengerID = passengerID;
        this.pName = pName;
        this.pSurname = pSurname;
        this.contactNumber = contactNumber;
    }
    

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public String getpName() {
        return pName;
    }

    public void setpName(String pName) {
        this.pName = pName;
    }

    public String getpSurname() {
        return pSurname;
    }

    public void setpSurname(String pSurname) {
        this.pSurname = pSurname;
    }

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }
    
    
    
    
}

