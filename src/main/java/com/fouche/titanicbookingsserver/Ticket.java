/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


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
    private double  amountPaid;
    private int passengerID;
    
    public Ticket() 
    {
        
    }

    public Ticket(int ticketNumber, int flightNumber, String passengerName, String passengerSurname, int seatsBooked, double amountPaid, int passengerID) {
        this.ticketNumber = ticketNumber;
        this.flightNumber = flightNumber;
        this.passengerName = passengerName;
        this.passengerSurname = passengerSurname;
        this.seatsBooked = seatsBooked;
        this.amountPaid = amountPaid;
        this.passengerID = passengerID;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }

    public int getFlightNumber() {
        return flightNumber;
    }

    public void setFlightNumber(int flightNumber) {
        this.flightNumber = flightNumber;
    }

    public String getPassengerName() {
        return passengerName;
    }

    public void setPassengerName(String passengerName) {
        this.passengerName = passengerName;
    }

    public String getPassengerSurname() {
        return passengerSurname;
    }

    public void setPassengerSurname(String passengerSurname) {
        this.passengerSurname = passengerSurname;
    }

    public int getSeatsBooked() {
        return seatsBooked;
    }

    public void setSeatsBooked(int seatsBooked) {
        this.seatsBooked = seatsBooked;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    @Override
    public String toString() {
        return "Ticket{" + "ticketNumber=" + ticketNumber + ", flightNumber=" + flightNumber + ", passengerName=" + passengerName + ", passengerSurname=" + passengerSurname + ", seatsBooked=" + seatsBooked + ", amountPaid=" + amountPaid + ", passengerID=" + passengerID + '}';
    }
    
}