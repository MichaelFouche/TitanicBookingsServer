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

// create the class
public class Flight implements Serializable
{
    
    // variablles declarations
    private int flightNumber;
    private String flightDate;
    private String departCity;
    private String arriveCity;
    private int seatsAvailable;
    private double seatPrice;
    private int seatSold;
    private boolean cancelled;
 
    public Flight() 
    {
    }

    public Flight(String flightDate, String departCity, String arriveCity) 
    {
        this.flightDate = flightDate;
        this.departCity = departCity;
        this.arriveCity = arriveCity;
        this.cancelled = false;
    }

    public Flight(String flightDate, String departCity, String arriveCity, int seatsAvailable) 
    {
        this.flightDate = flightDate;
        this.departCity = departCity;
        this.arriveCity = arriveCity;
        this.seatsAvailable = seatsAvailable;
        this.cancelled = false;
    }

    public Flight(int flightNumber, String flightDate, String departCity, String arriveCity, int seatsAvailable, int seatSold) 
    {
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.departCity = departCity;
        this.arriveCity = arriveCity;
        this.seatsAvailable = seatsAvailable;
        this.seatSold = seatSold;
        this.cancelled = false;
    }
    public Flight(int flightNumber, String flightDate, String departCity, String arriveCity, int seatsAvailable, int seatSold, double price, boolean cancel) 
    {
        this.flightNumber = flightNumber;
        this.flightDate = flightDate;
        this.departCity = departCity;
        this.arriveCity = arriveCity;
        this.seatsAvailable = seatsAvailable;
        this.seatSold = seatSold;
        this.cancelled = cancel;
        this.seatPrice = price;
    }
    
    // create SET methods
    public void setFlightNumber(int flightNumber) 
    {
        this.flightNumber = flightNumber;
    }

    public void setFlightDate(String flightDate) 
    {
        this.flightDate = flightDate;
    }

    public void setDepartCity(String departCity) 
    {
        this.departCity = departCity;
    }

    public void setArriveCity(String arriveCity) 
    {
        this.arriveCity = arriveCity;
    }

    public void setSeatsAvailable(int seatsAvailable) 
    {
        this.seatsAvailable = seatsAvailable;
    }

    public void setSeatPrice(double seatPrice) 
    {
        this.seatPrice = seatPrice;
    }

    public void setSeatSold(int seatSold) 
    {
        this.seatSold = seatSold;
    }

    public void setCancelled(boolean cancelled) 
    {
        this.cancelled = cancelled;
    }

    // create GET methods
    public int getFlightNumber() {
        return flightNumber;
    }

    public String getFlightDate() 
    {
        return flightDate;
    }

    public String getDepartCity() 
    {
        return departCity;
    }

    public String getArriveCity() 
    {
        return arriveCity;
    }

    public int getSeatsAvailable() 
    {
        return seatsAvailable;
    }

    public double getSeatPrice() 
    {
        return seatPrice;
    }

    public int getSeatSold() 
    {
        return seatSold;
    }
    
   public boolean isCancelled()
   {
       return cancelled;
   }

   // create a toString method to return the String representation of this class
    @Override
    public String toString() 
    {
        return   flightNumber + "#"+ flightDate + "#" + departCity + "#" + arriveCity +
                "#"+ seatsAvailable + "#" + seatSold +"\r\n";
    }    
}