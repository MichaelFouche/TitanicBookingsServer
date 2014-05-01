/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.fouche.titanicbookingsserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.*;
import javax.swing.*;
import java.awt.event.*;
import java.awt.*;
import java.sql.*;
/**
 * TitanicBookingsServer.
 * 
 * @author (Michael Fouche & Ryno Mayer) 
 */
public class TitanicBookingsServer
{ 
    //CLASS OBJECTS
    private Ticket[] ticket;
    private Flight[] flight;
    private Passenger[] passenger;
    private Receipt[] receipt;
    private Meal[] meal;
    
    //GUI 
    private JFrame jf;
    private JPanel jpNorth,jpSouth;
    private JTextArea txtConversation;
    //SERVER SOCKET
    private ServerSocket listener;
    private ObjectOutputStream out;
    //CLIENT CONNECTION
    private Socket client;
    ObjectInputStream in;
    
    //
    int amountReads;
    int amountAllFlightsCounted;
    String msg;
    String reply;
    //private String tableName;
    private String[] columnFlight = {"flightNumber", "flightDate", "departCity", "arriveCity", "seatsAvailable", "seatSold", "seatPrice", "cancelled"};
    private String[] columnTicket = {"ticketNumber", "flightNumber", "seatsBooked", "amountPaid", "passengerID"};
    private String[] columnReceipt = {"receiptID", "passengerID", "dateOfPayment", "amountPaid", "ticketNumber"};
    private String[] columnMeal = {"mealID", "vegetarian", "chicken", "beef", "beverage", "ticketNumber"};
    private String[] columnPassenger = {"passengerID", "pName", "pSurname", "contactNumber"};
    private String[] columnFlightType = {"INT", "STRING", "STRING", "STRING", "INT", "INT", "FLOAT", "BIT"};
    private String[] columnTicketType = {"INT", "INT", "INT", "Float", "INT"};
    private String[] columnReceiptType = {"INT", "INT", "STRING", "FLOAT", "INT"};
    private String[] columnMealType = {"INT", "BIT","BIT","BIT","BIT","INT"}; 
    private String[] columnPassengerType = {"INT", "STRING", "STRING", "STRING"};
    
    public TitanicBookingsServer()
    {
        flight = new Flight[1000];
        ticket = new Ticket[1000];
        passenger = new Passenger[1000];
        receipt = new Receipt[1000];
        meal = new Meal[1000];
        
        
        amountReads = 0;
        msg = "";
        reply = "";
        // Create server socket
        try 
        {
            listener = new ServerSocket(12345, 10);
        }
        catch (IOException ioe)
        {
          System.out.println("IO Exception: " + ioe.getMessage());
        }
    } 
    
    public void createDisplay()
    {
        jf = new JFrame("SERVER FEEDBACK");
        jpNorth = new JPanel();
        txtConversation = new JTextArea(25,30);
        jpNorth.add(txtConversation);
        jf.add(jpNorth,BorderLayout.NORTH);
        jf.setSize(400,400);
        jf.pack();
        jf.setVisible(false);        
        jf.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE); //DISPOSE_ON_CLOSE,  DISPOSE_ON_CLOSE 
        jf.addWindowListener(new WindowAdapter() 
        {
            @Override
            public void windowClosing(WindowEvent e) 
            {
                int result = JOptionPane.showConfirmDialog(jf, "Are you sure you would like to close the window?");
                if( result==JOptionPane.OK_OPTION)
                {
                    // NOW we change it to dispose on close..
                    jf.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                    jf.setVisible(false);
                    jf.dispose();
                }
                jf.setVisible(true);
            }
        });
    }
    public void openCommunication()
    {
        try
        {
            out = new ObjectOutputStream(client.getOutputStream());
            out.flush();
            in = new ObjectInputStream(client.getInputStream());
        }
        catch (IOException ioe)
        {
            System.out.println("IO Exception Open communication: " + ioe.getMessage());
        }
    }
    public void listen()
    {
        // Start listening for client connections
        try 
        {
            client = listener.accept();  
            openCommunication();
            
            while(!reply.equals("TERMINATE"))
            {
                msg = (String)in.readObject();
                
                if(msg.equals("TERMINATE"))
                {
                    
                }
                /*else if(msg.equals("Load Flights"))
                {
                    receiveFlight();
                }
                else if(msg.equals("Load Tickets"))
                {
                    receiveTicket();
                }  */              
                else if(msg.equals("Send All Flights"))
                {
                    sendAllFlights();
                }
                else if(msg.equals("Send Tickets"))
                {
                    sendTickets();
                }
                else if(msg.equals("Add Flight"))
                {
                    addFlight();
                }
                else if(msg.equals("Add Ticket"))
                {
                    addTicket();
                }
                else if(msg.equals("Delete Ticket"))
                {
                    deleteTicket();
                }
                else if(msg.equals("Delete Flight"))
                {
                    deleteFlight();
                }
                else if(msg.equals("Cancel Flight"))
                {
                    cancelFlight();
                }
                else if(msg.equals("Delete all Tickets"))
                {
                    deleteAllTickets();
                }
                else if(msg.equals("Send Filtered Flights"))
                {
                    sendFilteredFlights();
                }
            }
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception (LISTEN): " + ioe.getMessage());
        }        
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found (LISTEN): " + cnfe.getMessage());
        }
    }
    public void deleteTicket()
    {
        int currentFlightNum = 0;
        int ticketNumber = 0;
        int seatsBookedTicket = 0;
        int seatsBookedFlight = 0;
        try
        {
            currentFlightNum = Integer.parseInt((String)in.readObject());
            ticketNumber =Integer.parseInt((String)in.readObject());
            seatsBookedTicket =Integer.parseInt((String)in.readObject());
        }
        catch(Exception err) 
        {
            System.out.println("RETRIEVE INFO FROM CLIENT (DELETE TICKET): " + err);
        } 
        
        try
        {
            
            String select_seat="SELECT seatSold FROM Flight WHERE flightNumber = "+currentFlightNum+" ;";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_seat); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                while (rs.next())
                {
                    seatsBookedFlight = rs.getInt(1);
                }
            }
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it 
            
        }
        catch(Exception err) 
        {
            System.out.println("REQUEST SEATS BOOKED (DELETE TICKET): " + err);
        } 
        try
        {
            seatsBookedFlight -=seatsBookedTicket;
            int seatsAvail = 10-seatsBookedFlight;
            String insert_Values_stmt="UPDATE Flight"+" SET seatSold = "+seatsBookedFlight+",seatsAvailable = "+seatsAvail+ " WHERE flightNumber = "+currentFlightNum+";";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(insert_Values_stmt); // select the data from the table
           // ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close();
            //out.writeObject("Seats booked updated");
            out.flush();
            //verify if added with all details 
        }
        catch(Exception err) 
        {
            System.out.println("UPDATE SEATS BOOKED (DELETE TICKET): " + err);
        }
        String delete_Values_stmt="DELETE FROM Ticket"+" WHERE ticketNumber = "+ticketNumber+"; ";
        try 
        {           
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(delete_Values_stmt); 
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
            //out.writeObject("Ticket deleted From database");
            out.flush();
        }
        catch (Exception err) 
        {
            System.out.println("DELETE TICKET FROM DB: " + err);
        }
    }
    public void cancelFlight()
    {
        int currentFlightNum = 0;  
        int cancelledStatus = 0;
        try
        {
            currentFlightNum = Integer.parseInt((String)in.readObject());            
        }
        catch(Exception err) 
        {
            System.out.println("RETRIEVE INFO FROM CLIENT (CANCEL FLIGHT): " + err);
        }
        
        try
        {
            
            String select_seat="SELECT cancelled FROM Flight WHERE flightNumber = "+currentFlightNum+" ;";
            
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_seat); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                while (rs.next())
                {
                    cancelledStatus = rs.getInt(1);
                }
            }
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it 
            
        }
        catch(Exception err) 
        {
            System.out.println("REQUEST CANCELLED STATUS: " + err);
        } 
        if(cancelledStatus ==0)
        {
            cancelledStatus = -1;
        }
        else
        {
            cancelledStatus = 0;
        }
        try
        {            
            String insert_Values_stmt="UPDATE Flight"+" SET cancelled = "+cancelledStatus+ " WHERE flightNumber = "+currentFlightNum+";";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(insert_Values_stmt); // select the data from the table
           // ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close();
           // out.writeObject("Cancelled Status updated");
            out.flush();
            //verify if added with all details 
        }
        catch(Exception err) 
        {
            System.out.println("UPDATE CANCELLED STATUS: " + err);
        }
    }
    public void deleteAllTickets()
    {
        int currentActiveFlightNum = 0;
        try
        {
            currentActiveFlightNum = Integer.parseInt((String)in.readObject());
        }
        catch(Exception err) 
        {
            System.out.println("RETRIEVE INFO FROM CLIENT (DELETE TICKETS): " + err);
        } 
        try 
        {   
            String delete_Values_stmt="DELETE FROM Ticket"+" WHERE flightNumber = "+currentActiveFlightNum+"; ";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(delete_Values_stmt); 
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
           // out.writeObject("All Tickets deleted From flight "+currentActiveFlightNum+" in database");
            out.flush();
        }
        catch (Exception err) 
        {
            System.out.println("DELETE ALL TICKETS FROM A FLIGHT FROM DB: " + err);
        }
        try
        {
           
            String insert_Values_stmt="UPDATE Flight"+" SET seatSold = 0,seatsAvailable = 10 WHERE flightNumber = "+currentActiveFlightNum+";";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(insert_Values_stmt); // select the data from the table
           // ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close();
            //out.writeObject("Seats booked updated");
            out.flush();
            //verify if added with all details 
        }
        catch(Exception err) 
        {
            System.out.println("UPDATE SEATS BOOKED (DELETE TICKET): " + err);
        }
    }
    public void deleteFlight()
    {
        int currentActiveFlightNum = 0;
        try
        {
            currentActiveFlightNum = Integer.parseInt((String)in.readObject());
            
       
        }
        catch(Exception err) 
        {
            System.out.println("RETRIEVE INFO FROM CLIENT (DELETE FLIGHT): " + err);
        } 
       
        try 
        {   
            String delete_Values_stmt="DELETE FROM Ticket"+" WHERE flightNumber = "+currentActiveFlightNum+"; ";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(delete_Values_stmt); 
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
           // out.writeObject("All Tickets deleted From flight "+currentActiveFlightNum+" in database");
            out.flush();
        }
        catch (Exception err) 
        {
            System.out.println("DELETE ALL TICKETS FROM A FLIGHT FROM DB: " + err);
        }
        try 
        {   
            String delete_Values_stmt="DELETE FROM Flight"+" WHERE flightNumber = "+currentActiveFlightNum+"; ";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(delete_Values_stmt); 
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
           // out.writeObject("Flight "+currentActiveFlightNum+" deleted From database");
            out.flush();
        }
        catch (Exception err) 
        {
            System.out.println("DELETE FLIGHT FROM DB: " + err);
        }
    }
    
    public void addFlight()
    {
        int newflightNum = 0;
        try
        {
            
            String select_largest_itemNum="SELECT MAX(flightNumber) FROM Flight"+" ;";                        
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_largest_itemNum); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                while (rs.next())
                {
                    newflightNum = rs.getInt(1);
                }
            }
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it 
        }
        catch(Exception err) 
        {
            System.out.println("REQUEST FLIGHT NUM: " + err);
        } 
        try
        {            
                 
            newflightNum+=50;
            //get flight num
            
            String insert_Values_stmt="insert into Flight"+" values   ("+newflightNum+",'"+(String)in.readObject()+"','"+(String)in.readObject()+"','"+(String)in.readObject()+"', 10, 0, "+Double.parseDouble((String)in.readObject())+", 0)";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(insert_Values_stmt); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close();
          //  out.writeObject("Flight added to Database");
            out.flush();
            //verify if added with all details
        }
        catch(Exception err) 
        {
            System.out.println("ADD FLIGHT: " + err);
        }  
    }
    public void addTicket()
    {
       
        int newTicketNum = 0;
        try
        {
            
            String select_largest_itemNum="SELECT MAX(ticketNumber) FROM Ticket"+" ;";                        
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_largest_itemNum); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                while (rs.next())
                {
                    newTicketNum = rs.getInt(1);
                }
            }
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it 
            
        }
        catch(Exception err) 
        {
            System.out.println("REQUEST TICKET NUM: " + err);
        } 
        int flightNum = 0;
        int seatsBooked = 0;
        int seatsBooking = 0;
        try
        {            
                 
            newTicketNum++;
            //get flight num
            flightNum = Integer.parseInt((String)in.readObject());
            String pName = (String)in.readObject();
            String pSurname = (String)in.readObject();
            seatsBooking = Integer.parseInt((String)in.readObject());            
            
            String insert_Values_stmt="insert into Ticket"+" values   ("+newTicketNum+",'"+flightNum+"', '"+pName+"', '"+pSurname+"', "+seatsBooking+", "+Double.parseDouble((String)in.readObject())+")";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(insert_Values_stmt); // select the data from the table
           // ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close();
          //  out.writeObject("Ticket added to Database");
            out.flush();
            //verify if added with all details 
        }
        catch(Exception err) 
        {
            System.out.println("ADD Ticket: " + err);
        } 
        try
        {
            
            String select_seat="SELECT seatSold FROM Flight WHERE flightNumber = "+flightNum+" ;";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_seat); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                while (rs.next())
                {
                    seatsBooked = rs.getInt(1);
                }
            }
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it 
            
        }
        catch(Exception err) 
        {
            System.out.println("REQUEST SEATS BOOKED: " + err);
        } 
        
        try
        {
            seatsBooked +=seatsBooking;
            int seatsAvail = 10-seatsBooked;
            String insert_Values_stmt="UPDATE Flight"+" SET seatSold = "+seatsBooked+",seatsAvailable = "+seatsAvail+ " WHERE flightNumber = "+flightNum+";";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeUpdate(insert_Values_stmt); // select the data from the table
           // ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            
            s.close(); // close the Statement to let the database know we're done with it
            con.close();
            //out.writeObject("Seats booked updated");
            out.flush();
            //verify if added with all details 
        }
        catch(Exception err) 
        {
            System.out.println("UPDATE SEATS BOOKED: " + err);
        }
        
        //*/
    }
    public void changeAvailableStatus()
    {
        
    }
   
    
    
    
    public void sendTickets()
    {
        try
        {
            int flightNumber = Integer.parseInt((String)in.readObject());
            int amountTicketsCounted = 0;
            //Get tickets from flight
            String select_Flight_Table_stmt="SELECT ticketNumber,flightNumber, passengerName, passengerSurname, seatsBooked, amountPaid FROM Ticket JOIN Passenger ON Ticket.passengerID = Passenger.passengerID"+" WHERE flightNumber = "+flightNumber+" ORDER BY passengerSurname;";
                        
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_Flight_Table_stmt); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                
                ticket = new Ticket[1000];
                amountTicketsCounted = 0;
                while (rs.next())
                { 
                    ticket[amountTicketsCounted] = null;
                    ticket[amountTicketsCounted] = new Ticket();
                    ticket[amountTicketsCounted].setTicketNumber(rs.getInt(1));
                    ticket[amountTicketsCounted].setFlightNumber(rs.getInt(2));
                    ticket[amountTicketsCounted].setPassengerName(rs.getString(3));
                    ticket[amountTicketsCounted].setPassengerSurname(rs.getString(4));
                    ticket[amountTicketsCounted].setSeatsBooked(rs.getInt(5));
                    ticket[amountTicketsCounted].setAmountPaid(rs.getDouble(6));
                    amountTicketsCounted++;
                }
            }
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
            out.writeObject(amountTicketsCounted); 
            for(int i=0;i<amountTicketsCounted;i++)
            {
                out.writeObject(ticket[i]);
            }            
            out.flush();
            //out.writeObject("Sent Tickets requested");
            txtConversation.append("Read Requested Tickets from database.\nStored Tickets in Ticket Objects.\nSent Tickets to client.");
            out.flush();
            
        }
        catch (Exception err) 
        {
            System.out.println("SEND TICKETS: " + err);
        }  
    }
    public void sendFilteredFlights()
    {
        //Read all Flights from database to Flight class
        
        try 
        {
            String cityToFilter = (String)in.readObject();
            String select_Flight_Table_stmt="SELECT flightNumber,flightDate, departCity, arriveCity, seatsAvailable, seatSold, seatPrice, cancelled FROM Flight"+" WHERE departCity = '"+cityToFilter+"';";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_Flight_Table_stmt); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                flight = new Flight[1000];
                amountAllFlightsCounted = 0;
                while (rs.next())
                { 
                    flight[amountAllFlightsCounted] = null;
                    flight[amountAllFlightsCounted] = new Flight();
                    flight[amountAllFlightsCounted].setFlightNumber(rs.getInt(1));
                    flight[amountAllFlightsCounted].setFlightDate(rs.getString(2));
                    flight[amountAllFlightsCounted].setDepartCity(rs.getString(3));
                    flight[amountAllFlightsCounted].setArriveCity(rs.getString(4));
                    flight[amountAllFlightsCounted].setSeatsAvailable(rs.getInt(5));                    
                    flight[amountAllFlightsCounted].setSeatSold(rs.getInt(6));
                    flight[amountAllFlightsCounted].setSeatPrice(rs.getDouble(7));
                    flight[amountAllFlightsCounted].setCancelled(rs.getBoolean(8));
                    amountAllFlightsCounted++;
                }
            }
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
        }
        catch (Exception err) 
        {
            System.out.println("SEND FILTERED FLIGHTS: " + err);
        }  
        try
        {
            out.writeObject(amountAllFlightsCounted); 
            for(int i=0;i<amountAllFlightsCounted;i++)
            {
                out.writeObject(flight[i]);
            }            
            out.flush();
            //out.writeObject("Sent all Filtered Flights ("+amountAllFlightsCounted+")");
            txtConversation.append("Read all filtered flights into flight objects\nSent all filtered Flights to client\n");
            
        }
        catch (Exception err) 
        {
            System.out.println("SEND ALL FLIGHTS COMMUNICATE: " + err);
        }  
    }
    public void sendAllFlights()
    {
        //Read all Flights from database to Flight class
        String select_Flight_Table_stmt="SELECT flightNumber,flightDate, departCity, arriveCity, seatsAvailable, seatSold, seatPrice, cancelled FROM Flight"+";";
        try 
        {
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(select_Flight_Table_stmt); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                flight = new Flight[1000];
                amountAllFlightsCounted = 0;
                while (rs.next())
                { 
                    flight[amountAllFlightsCounted] = null;
                    flight[amountAllFlightsCounted] = new Flight();
                    flight[amountAllFlightsCounted].setFlightNumber(rs.getInt(1));
                    flight[amountAllFlightsCounted].setFlightDate(rs.getString(2));
                    flight[amountAllFlightsCounted].setDepartCity(rs.getString(3));
                    flight[amountAllFlightsCounted].setArriveCity(rs.getString(4));
                    flight[amountAllFlightsCounted].setSeatsAvailable(rs.getInt(5));                    
                    flight[amountAllFlightsCounted].setSeatSold(rs.getInt(6));
                    flight[amountAllFlightsCounted].setSeatPrice(rs.getDouble(7));
                    flight[amountAllFlightsCounted].setCancelled(rs.getBoolean(8));
                    amountAllFlightsCounted++;
                }
            }
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
        }
        catch (Exception err) 
        {
            System.out.println("SEND ALL FLIGHTS: " + err);
        }  
        //Send back amount of items
        try
        {
            out.writeObject(amountAllFlightsCounted); 
            for(int i=0;i<amountAllFlightsCounted;i++)
            {
                out.writeObject(flight[i]);
            }            
            out.flush();
            //out.writeObject("Sent all Flights ("+amountAllFlightsCounted+")");
            txtConversation.append("Read all flights into flight objects\nSent all Flights to client\n");
            
        }
        catch (Exception err) 
        {
            System.out.println("SEND ALL FLIGHTS COMMUNICATE: " + err);
        }  
        //Send back items
        
        //Report
    }
    
    /*public void createTables(String tblName)
    {        
        String create_Flight_Table_stmt="CREATE TABLE "+tblName+"_Flight"+" (flightNumber INT,flightDate STRING, departCity STRING, arriveCity STRING, seatsAvailable INT, seatSold INT, seatPrice DOUBLE, cancelled BIT);";
        String create_Ticket_Table_stmt="CREATE TABLE "+tblName+"_Ticket"+" (ticketNumber INT,flightNumber INT, passengerName STRING, passengerSurname STRING, seatsBooked INT, amountPaid DOUBLE);";
       
        try 
        {
           // String filename = "TitanicBookings.mdb";
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "","");
            Statement s = con.createStatement();           
            s.executeUpdate(create_Flight_Table_stmt); 
            s.executeUpdate(create_Ticket_Table_stmt);
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
            
           // out.writeObject("Created Table: "+tblName+"_Flight, and "+tblName+"_Ticket.");     
            txtConversation.append("Created Table: "+tblName+"_Flight, and "+tblName+"_Ticket.\n");
            
        }
        catch (Exception err) 
        {
            System.out.println("CREATE TABLE: " + err);
        }
    }*/
    
    public void getTableNames()
    {
        String count_Values_qry="SELECT NAME FROM MSYSOBJECT WHERE TYPE = 1 AND NAME NOT LIKE 'MSYS*' ORDER BY NAME;";
        try 
        {            
            String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
            String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
            dbURL+= path;
            String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";        
            Class.forName(driverName);
            Connection con = DriverManager.getConnection(dbURL, "",""); 
            Statement s = con.createStatement();
            s.executeQuery(count_Values_qry); // select the data from the table
            ResultSet rs = s.getResultSet(); // get any ResultSet that came from our query
            if (rs != null) // if rs == null, then there is no ResultSet to view  
            {
                while (rs.next())
                {                            
                    out.writeObject(rs.getString(1));
                }
                //out.writeObject("Sent table names");
                txtConversation.append("Sent table names");
                out.flush();
            }
            s.close(); // close the Statement to let the database know we're done with it
            con.close(); // close the Connection to let the database know we're done with it
        }
        catch (Exception err) 
        {
            System.out.println("GET TABLE NAMES: " + err);
        }
    }
   /* public void receiveTicket()
    {
        try
        {
            amountReads = Integer.parseInt((String)in.readObject());
            for(int i=0;i<amountReads;i++)
            {
                ticket[i] = null;
                ticket[i] = new Ticket();
                ticket[i] = (Ticket) in.readObject();
            }
            txtConversation.append("Read Ticket details and wrote it to Ticket class\n");
            //out.writeObject("Read Ticket details and wrote it to Ticket class");
            
            try 
            {
                //String filename = "publisher.mdb";
                String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
                String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
                dbURL+= path;
                String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
                Class.forName(driverName);
                Connection con = DriverManager.getConnection(dbURL, "",""); 
                Statement s = con.createStatement();
                for(int i=0;i<amountReads;i++)
                {
                    String insert_Values_stmt="insert into Ticket values   ("+t[i].getTicketNumber()+",'"+t[i].getFlightNumber() +"','"+t[i].getPassengerName() +"','"+t[i].getPassengerSurname()+"','"+t[i].getSeatsBooked() +"','"+t[i].getAmountPaid()+ "')";
                    s.executeUpdate(insert_Values_stmt);
                }
                s.close(); // close the Statement to let the database know we're done with it
                con.close(); // close the Connection to let the database know we're done with it
                //out.writeObject("Wrote Ticket Details from Ticket class to database in "+tableName+"_Ticket.");
                txtConversation.append("Wrote Ticket Details from Ticket class to database in "+tableName+"_Ticket.\n");
                out.flush(); 
            }
            catch (Exception err) 
            {
                System.out.println("Receive Ticket write to DB: " + err);
            }
             
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }        
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
    }
    public void receiveFlight()
    {
        try
        {
            amountReads = Integer.parseInt((String)in.readObject());
            
            for(int i=0;i<amountReads;i++)
            {
                f[i] = null;
                f[i] = new Flight();
                f[i] = (Flight) in.readObject();                
            }
            txtConversation.append("Read Flight details and wrote it to Flight class\n");
            //out.writeObject("Read Flight details and wrote it to Flight class");
            try 
            {
                String path = new java.io.File("TitanicBookings.mdb").getAbsolutePath();
                String dbURL = "jdbc:odbc:Driver={Microsoft Access Driver (*.mdb)};DBQ=";
                dbURL+= path;
                String driverName = "sun.jdbc.odbc.JdbcOdbcDriver";
                Class.forName(driverName);
                Connection con = DriverManager.getConnection(dbURL, "",""); 
                Statement s = con.createStatement();
                for(int i=0;i<amountReads;i++)
                {     
                    int booleanToDB = 0;
                    if(f[i].isCancelled())
                    {
                        booleanToDB = -1;
                    }
                    String insert_Values_stmt="insert into "+tableName+"_Flight values   ("+f[i].getFlightNumber()+",'"+f[i].getFlightDate() +"','"+f[i].getDepartCity() +"','"+f[i].getArriveCity()+"','"+f[i].getSeatsAvailable() +"','"+f[i].getSeatSold() +"','"+f[i].getSeatPrice()+"',"+booleanToDB+")";
                    
                    s.executeUpdate(insert_Values_stmt);
                }
                s.close(); // close the Statement to let the database know we're done with it
                con.close(); // close the Connection to let the database know we're done with it
                //out.writeObject("Wrote Flight details from Flight class to database in "+tableName+"_Flight. ("+amountReads+")");
                txtConversation.append("Wrote Flight details from Flight class to database in "+tableName+"_Flight.\n");                
                out.flush();
            }
            catch (Exception err) 
            {
                System.out.println("Receive Flight write to DB: " + err);
            } 
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }        
        catch (ClassNotFoundException cnfe)
        {
            System.out.println("Class not found: " + cnfe.getMessage());
        }
    }*/
    
    public void close()
    {
        try
        {
            out.close();
            in.close();
            client.close(); 
        }
        catch(IOException ioe)
        {
            System.out.println("IO Exception: " + ioe.getMessage());
        }
        
    }
    public static void main(String[] args)
    {
        // Create application
        TitanicBookingsServer server = new TitanicBookingsServer();
        System.out.println("Hello");
        // Start waiting for connections
        server.createDisplay();
        System.out.print(" World");
        server.listen();
        System.out.print("!!");
        
    }   
    
}
