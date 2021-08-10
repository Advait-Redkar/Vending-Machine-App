package com.techelevator;

import java.io.*;
import java.sql.Date;
import java.text.NumberFormat;
import java.time.Clock;
import java.time.Instant;
import java.util.*;

public class VendingMachine {

    private double balance;
    private String auditFilename;
    private Map<String, Slot> slots = new HashMap<>();
    //private Map<String, Integer> slotsPurchased = new HashMap<>();
    private List<String> slotDisplay;

    public VendingMachine() {
        this.initializeInventory();
        this.balance = 0.00;
        this.auditFilename = "audit.txt";
        slotDisplay = makeSlotDisplay();
    }

    public static String getCurrencyString(double currencyDouble) {
        return NumberFormat.getCurrencyInstance(Locale.ROOT).format(currencyDouble).replace("¤", "$");
    }

    //new class field--private list of strings---purely for display
    //constructor of map of all objects available for purchase
    //method to populate slotDisplay
    private List<String> makeSlotDisplay() {
        List<String> temporaryList = new ArrayList<>();
        for (Map.Entry<String, Slot> kVP : this.slots.entrySet()) {
            temporaryList.add(kVP.getKey() + ": " + kVP.getValue().toString());
        }
        Collections.sort(temporaryList);
        return temporaryList;
    }

    public List<String> getPurchaseReport() {
        List<String> purchaseList = new ArrayList<>();
        String purchaseLineStr;
        Double totalSales = 0.00;
        for (Map.Entry<String, Slot> kVP: this.slots.entrySet()) {
            purchaseLineStr = kVP.getValue().getName() + " | " + kVP.getValue().getTimesPurchased();
            purchaseList.add(purchaseLineStr);
            totalSales += kVP.getValue().getTimesPurchased() * kVP.getValue().getPrice();
        }
        purchaseList.add(totalSales.toString());
        return purchaseList;
    }

    public double getBalance() {
        return balance;
    }

    public List<String> getSlotDisplay() {
        return this.slotDisplay;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public void changeBalance(double changeAmount) {
        this.balance += changeAmount;
    }

    public Slot getSlot(String slotId) {
        return this.slots.get(slotId);
    }

    private void initializeInventory() {
        try (Scanner inventoryScan = new Scanner(new File("C:\\Users\\Student\\workspace\\module1-capstone-java-team-7\\Capstone\\java\\inventory.txt"))) {
            while (inventoryScan.hasNextLine()) {
                makeSlot(inventoryScan.nextLine());
            }
        } catch (Exception e) {
            System.out.println("Inventory File Not Found");
        }

        //this.slots
        //put this in constructor
        //build lot obj from string
    }

    private void makeSlot(String line) {
        String[] slotArray = (line.split("\\|"));
        //A1|Potato Crisps|3.05|Chip---Format
        //public Slot(String name, String type, double price) {---Slot Constructor
        this.slots.put(slotArray[0], new Slot(slotArray[1], slotArray[3], Double.parseDouble(slotArray[2]), slotArray[0]));
        //this.slotsPurchased.put(slotArray[0], 0);
    }

    public void writeAudit(String label, String auditInfo) {
        String auditString = "";
        auditString += label + " ";
        auditString += Date.from(Instant.now(Clock.systemDefaultZone())) + " ";
        auditString += auditInfo;
        try (FileOutputStream auditFile = new FileOutputStream(this.auditFilename, true);
             PrintWriter auditWriter = new PrintWriter(auditFile)) {
            auditWriter.println(auditString);
        } catch (FileNotFoundException e) {
            System.out.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.out.println("IO exception" + e.getMessage());
        }
    }

    //Method below
    public int purchase(Slot slot) {
        if (getBalance() < slot.getPrice()) {
            return -1;
        } else if (slot.getQuantity() <= 0) {
            return -2;
        }
        changeBalance(slot.getPrice() * -1);
        slot.setQuantity(slot.getQuantity() - 1);
        //this.slotsPurchased.put(slot.getSlotId(), this.slotsPurchased.get(slot.getSlotId()) + 1);
        return 0;
    }

    public int[] getChange() {
        int[] changeArray = new int[]{0, 0, 0, 0}; //pennies, nickles, dimes, quarters
        while (this.balance >= 0.25) {
            this.changeBalance(-0.25);
            changeArray[3]++;
        }
        while (this.balance >= 0.10) {
            this.changeBalance(-0.10);
            changeArray[2]++;
        }
        while (this.balance >= 0.05) {
            this.changeBalance(-0.05);
            changeArray[1]++;
        }
        while (this.balance >= 0.00 && !(this.balance < 0.01)) {
            this.changeBalance(-0.01);
            changeArray[0]++;
        }
        return changeArray;
    }
}

