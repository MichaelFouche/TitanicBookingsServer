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
public class Meal {
    private int mealID;
    private boolean vegetarian;
    private boolean chicken;
    private boolean beef;
    private boolean beverage;
    private int ticketNumber;

    public Meal(int mealID, boolean vegetarian, boolean chicken, boolean beef, boolean beverage, int ticketNumber) {
        this.mealID = mealID;
        this.vegetarian = vegetarian;
        this.chicken = chicken;
        this.beef = beef;
        this.beverage = beverage;
        this.ticketNumber = ticketNumber;
    }

    public int getMealID() {
        return mealID;
    }

    public void setMealID(int mealID) {
        this.mealID = mealID;
    }

    public boolean isVegetarian() {
        return vegetarian;
    }

    public void setVegetarian(boolean vegetarian) {
        this.vegetarian = vegetarian;
    }

    public boolean isChicken() {
        return chicken;
    }

    public void setChicken(boolean chicken) {
        this.chicken = chicken;
    }

    public boolean isBeef() {
        return beef;
    }

    public void setBeef(boolean beef) {
        this.beef = beef;
    }

    public boolean isBeverage() {
        return beverage;
    }

    public void setBeverage(boolean beverage) {
        this.beverage = beverage;
    }

    public int getTicketNumber() {
        return ticketNumber;
    }

    public void setTicketNumber(int ticketNumber) {
        this.ticketNumber = ticketNumber;
    }
    
    
}

