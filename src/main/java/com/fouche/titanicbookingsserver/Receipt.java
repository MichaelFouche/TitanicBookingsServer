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
public class Receipt {
    private int receiptID;
    private int passengerID;
    private String dateOfPayment;
    private double amountPaid;
    private int ticketNumber;

    public Receipt(int receiptID, int passengerID, String dateOfPayment, double amountPaid, int ticketNumber) {
        this.receiptID = receiptID;
        this.passengerID = passengerID;
        this.dateOfPayment = dateOfPayment;
        this.amountPaid = amountPaid;
        this.ticketNumber = ticketNumber;
    }

    public int getReceiptID() {
        return receiptID;
    }

    public void setReceiptID(int receiptID) {
        this.receiptID = receiptID;
    }

    public int getPassengerID() {
        return passengerID;
    }

    public void setPassengerID(int passengerID) {
        this.passengerID = passengerID;
    }

    public String getDateOfPayment() {
        return dateOfPayment;
    }

    public void setDateOfPayment(String dateOfPayment) {
        this.dateOfPayment = dateOfPayment;
    }

    public double getAmountPaid() {
        return amountPaid;
    }

    public void setAmountPaid(double amountPaid) {
        this.amountPaid = amountPaid;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    
    
    
        
}
