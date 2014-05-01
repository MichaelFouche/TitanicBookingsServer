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
import java.text.SimpleDateFormat;
import java.util.Date;
import java.io.*;

public class Ticket implements Serializable
{
    private int ticketNumber;
    private int flightNumber;
    private String passengerName;
    private String passengerSurname;
    private int seatsBooked;
    private double amountPaid;
    
    public Ticket() 
    {
        
    }

    public Ticket(int ticketNumber, String passengerName, String passengerSurname, int seatsBooked, double amountPaid) {
        this.ticketNumber = ticketNumber;
        this.passengerName = passengerName;
        this.passengerSurname = passengerSurname;
        this.seatsBooked = seatsBooked;
        this.amountPaid = amountPaid;
    }

    public Ticket(int ticketNumber, int flightNumber, String passengerName, String passengerSurname, int seatsBooked, double amountPaid) {
        this.ticketNumber = ticketNumber;
        this.flightNumber = flightNumber;
        this.passengerName = passengerName;
        this.passengerSurname = passengerSurname;
        this.seatsBooked = seatsBooked;
        this.amountPaid = amountPaid;
    }

    // create GET methods
    public int getTicketNumber() 
    {
        return ticketNumber;
    }
        
    public int getFlightNumber() 
    {
        return flightNumber;
    }

    public String getPassengerName() 
    {
        return passengerName;
    }

    public String getPassengerSurname() 
    {
        return passengerSurname;
    }

    public int getSeatsBooked() 
    {
        return seatsBooked;
    }

    public double getAmountPaid() 
    {
        return amountPaid;
    }

    // create SET methods
    public void setTicketNumber(int ticketNumber) 
    {
        this.ticketNumber = ticketNumber;
    }
    
    public void setFlightNumber(int flightNumber) 
    {
        this.flightNumber = flightNumber;
    }

    public void setPassengerName(String passengerName) 
    {
        this.passengerName = passengerName;
    }

    public void setPassengerSurname(String passengerSurname) 
    {
        this.passengerSurname = passengerSurname;
    }

    public void setSeatsBooked(int seatsBooked) 
    {
        this.seatsBooked = seatsBooked;
    }

    public void setAmountPaid(double amountPaid) 
    {
        this.amountPaid = amountPaid;
    }
    
    // create a toString method to return the String representation of this class
    @Override
    public String toString() 
    {
        return  ticketNumber +  "#" + flightNumber + "#" +  passengerName + "#" + passengerSurname + "#" + seatsBooked + "#" + amountPaid + "\r\n";
    }
}