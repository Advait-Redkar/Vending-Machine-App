package com.techelevator;

import java.text.NumberFormat;
import java.util.Currency;
import java.util.Locale;

public class Slot {
    //Instance Vars
    private String name;
    private String slotId;
    private String type;
    private double price;
    private static final int INITIAL_QUANTITY = 5;
    private int quantity;
    //Constructor
    public Slot(String name, String type, double price, String slotId) {
        this.name = name;
        this.type = type;
        this.price = price;
        this.quantity = INITIAL_QUANTITY;
    }
    //Getters
    public double getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return this.name;
    }

    public String getSlotId() {
        return slotId;
    }

    //Setters
    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
    //Method(s)
    private double purchase() {
        //TODO handle if this.quantity == 0
        this.quantity -= 1;
        return this.price;
    }

    private String getCurrencyString(double currencyDouble) {
        return NumberFormat.getCurrencyInstance(Locale.ROOT).format(currencyDouble).replace("¤", "$");
    }

    public int getTimesPurchased() {
        return INITIAL_QUANTITY - this.quantity;
    }

    //Override method of toString
    @Override
    public String toString() {
        if (this.quantity <= 0) {
            return "SOLD OUT";
        }
        return this.name + " " + this.getCurrencyString(this.price);
    }


}
