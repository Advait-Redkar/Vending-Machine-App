package com.techelevator;

import com.techelevator.view.MenuDrivenCLI;

import java.util.List;
import java.util.Locale;
import java.util.Scanner;

public class Application {

    private static final String MAIN_MENU_OPTION_DISPLAY_ITEMS = "Display Vending Machine Items";
    private static final String MAIN_MENU_OPTION_PURCHASE = "Purchase";
    private static final String MAIN_MENU_OPTION_HIDDEN_REPORT = "[HIDDEN] Print Report";
    private static final String[] MAIN_MENU_OPTIONS = {MAIN_MENU_OPTION_DISPLAY_ITEMS, MAIN_MENU_OPTION_PURCHASE, MAIN_MENU_OPTION_HIDDEN_REPORT};

    private static final String PURCHASE_MENU_FEED_MONEY = "Feed Money";
    private static final String PURCHASE_MENU_SELECT_PRODUCT = "Select Product";
    private static final String PURCHASE_MENU_FINISH_TRANSACTION = "Finish Transaction";
    private static final String[] PURCHASE_MENU_OPTIONS = {PURCHASE_MENU_FEED_MONEY, PURCHASE_MENU_SELECT_PRODUCT, PURCHASE_MENU_FINISH_TRANSACTION};

    private final MenuDrivenCLI ui = new MenuDrivenCLI();

    public static void main(String[] args) {
        Application application = new Application();
        application.run();
    }

    public void run() {
        VendingMachine vendingMachine = new VendingMachine();
        while (true) {
            String selection = ui.promptForSelection(MAIN_MENU_OPTIONS);

            if (selection.equals(MAIN_MENU_OPTION_DISPLAY_ITEMS)) {
                // display vending machine items
                displayItems(vendingMachine);
            } else if (selection.equals(MAIN_MENU_OPTION_PURCHASE)) {
                // do purchase
                //this will be a sub-menu
                while (true) {
                    String purchaseSelection = ui.promptForSelection(PURCHASE_MENU_OPTIONS);
                    if (purchaseSelection.equals(PURCHASE_MENU_FEED_MONEY)) {
                        feedMoney(vendingMachine);
                    } else if (purchaseSelection.equals(PURCHASE_MENU_SELECT_PRODUCT)) {
                        selectItem(vendingMachine);
                    } else if (purchaseSelection.equals(PURCHASE_MENU_FINISH_TRANSACTION)) {
                        //finish transaction
                        makeChange(vendingMachine);
                        break;
                    }
                }

            } else if(selection.equals(MAIN_MENU_OPTION_HIDDEN_REPORT)) {
                purchaseReport(vendingMachine);
            }
        }
    }

    public static void displayItems(VendingMachine vendingMachine) {
        for (String item : vendingMachine.getSlotDisplay()) {
            System.out.println(item);
        }
    }

    public static void feedMoney(VendingMachine vendingMachine) {
        Scanner moneyScanner = new Scanner(System.in);
        System.out.println("Please enter amount in whole dollars to feed: ");
        int feedMoney = Integer.parseInt(moneyScanner.nextLine());
        vendingMachine.changeBalance(feedMoney);
        System.out.println("New Balance: $" + vendingMachine.getBalance());
        vendingMachine.writeAudit("FEED MONEY", "Amount fed: " + feedMoney + " New balance: " + vendingMachine.getBalance());
    }

    public static void selectItem(VendingMachine vendingMachine) {
        if (vendingMachine.getBalance() <= 0) {
            System.out.println("Insufficient funds");
            return;
        }
        Scanner itemScan = new Scanner(System.in);
        System.out.println("Please select an item: ");
        String slotId = itemScan.nextLine().toUpperCase(Locale.ROOT);
        Slot itemChosen = vendingMachine.getSlot(slotId);
        int purchaseCode = vendingMachine.purchase(itemChosen);
        if (purchaseCode == -1) {
            System.out.println("Insufficient funds for purchase");
            System.out.println("Item cost: $" + itemChosen.getPrice());
            System.out.println("Current Balance: $" + VendingMachine.getCurrencyString(vendingMachine.getBalance()));
            return;
        } else if (purchaseCode == -2) {
            System.out.println("Item is SOLD OUT");
            return;
        }
        if (itemChosen.getType().equalsIgnoreCase("chip")) {
            System.out.println("Crunch Crunch Yum!");
        } else if (itemChosen.getType().equalsIgnoreCase("candy")) {
            System.out.println("Munch Munch Yum!");
        } else if (itemChosen.getType().equalsIgnoreCase("drink")) {
            System.out.println("Glug Glug Yum!");
        } else if (itemChosen.getType().equalsIgnoreCase("gum")) {
            System.out.println("Chew Chew Yum!");
        } else {
            System.out.println("Wrong selection");
        }
        System.out.println(VendingMachine.getCurrencyString(vendingMachine.getBalance()));
        vendingMachine.writeAudit("", itemChosen.getName() +
                " slot ID: " + slotId +
                " price: " + VendingMachine.getCurrencyString(itemChosen.getPrice()) +
                " remaining balance: " + VendingMachine.getCurrencyString(vendingMachine.getBalance())
        );
    }
//makeChange method below
    public void makeChange(VendingMachine vendingMachine) {
        int[] changeArray = vendingMachine.getChange();
        String changeStr = "Your change is:\n" +
                changeArray[3] + " quarters\n" +
                changeArray[2] + " dimes\n" +
                changeArray[1] + " nickels\n" +
                changeArray[0] + " pennies\n";
        System.out.println(changeStr);
        vendingMachine.writeAudit("GIVE CHANGE", changeStr.replace("\n", " ").replace("Your", "") + " Remaining balance: " + VendingMachine.getCurrencyString(vendingMachine.getBalance()));
    }

    public void purchaseReport(VendingMachine vendingMachine) {
        List<String> purchaseReport = vendingMachine.getPurchaseReport();
        for (int i = 0; i < purchaseReport.size(); i++) {
            if (i == purchaseReport.size() - 1) {
                System.out.println("**TOTAL SALES**\n" + VendingMachine.getCurrencyString(Double.parseDouble(purchaseReport.get(i))));
                break;
            }
            System.out.println(purchaseReport.get(i));
        }
    }

}
