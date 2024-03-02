package com.yrl;

import unl.soc.*;

import java.util.Map;

/**
 * The SalesReport class generates a summary report of sales data.
 * It reads sales, items, persons, and store information from CSV files,
 * processes the data, and prints a summary report including total items sold,
 * total tax collected, and total sales revenue.
 */
public class SalesReport {

    /**
     * The main method generates and prints the summary report.
     *
     * @param args Command-line arguments (not used).
     */
    public static void main(String[] args) {
        // Load sales, items, and persons maps from CSV files
        Map<String, Item> itemsMap = DataProcessor.readItemsCSVtoMap("data/Items.csv");
        Map<String, Person> personsMap = DataProcessor.readPersonCSVtoMap("data/Persons.csv");
        Map<String, Store> storeMap = DataProcessor.readStoreCSVtoMap("data/Stores.csv");

        // Read sales data, process purchased items, and create sales map
        Map<String, Sale> salesMapRaw = DataProcessor.readSalesToMap(personsMap, storeMap, "data/Sales.csv");
        Map<String, Sale> salesMap = DataProcessor.processPurchasedItemsIntoSalesMap(salesMapRaw, itemsMap, personsMap, "data/SaleItems.csv");

        // Print sales report header
        System.out.println("Sales Report:");
        System.out.println("+-----------------------------------------------------------------------------------+\n" +
                "|  Summary Report -- By Total                                                       |\n" +
                "+-----------------------------------------------------------------------------------+");
        System.out.println("Invoice #  Store     Customer                Num Items          Tax        Total");

        // Initialize total sales variables
        int totalItemSales = 0;
        double totalTaxSales = 0;
        double totalPriceSales = 0;

        // Print individual sale details and update total sales variables
        for (Sale sale : salesMap.values()) {
            String saleNum = sale.getUniqueCode();
            String storeCode = sale.getStore().getStoreCode();
            String fullName = sale.getCustomer().getLastName() + ", " + sale.getCustomer().getFirstName();
            int numItems = sale.getItemsList().size();
            double tax = sale.getTotalTax();
            double price = sale.getNetPrice();
            System.out.printf("%-9s  %-9s  %-20s  %10d  $%10.2f  $%10.2f\n", saleNum, storeCode, fullName, numItems, tax, price);

            totalItemSales += numItems;
            totalTaxSales += tax;
            totalPriceSales += price;
        }

        // Print total sales summary
        System.out.println("+-----------------------------------------------------------------------------------+");
        System.out.printf("%54d  $%10.2f  $%10.2f\n\n", totalItemSales, totalTaxSales, totalPriceSales);

        // Print store sales summary
        System.out.println("+--------------------------------------------------------+\n" +
                "| Store Sales Summary Report                             |\n" +
                "+--------------------------------------------------------+");
        System.out.println("Store      Manager              # Sales   Grand Total");
        for (Store store : storeMap.values()) {
            System.out.println(store);
        }

        // Print individual sale details
        for (Sale sale : salesMap.values()){
            System.out.println(sale);
        }
    }
}
