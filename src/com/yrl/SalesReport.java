package com.yrl;

import unl.soc.DataReporter;
import unl.soc.DatabaseLoader;
import unl.soc.Sale;
import unl.soc.SortedLinkedList;


/**
 * The SalesReport class generates a summary report of sales data.
 * It reads sales, items, persons, and stores information from CSV files,
 * processes the data, and prints a summary report including total items sold,
 * total tax collected, and total sales revenue.
 */
public class SalesReport {

    /**
     * The main method generates and prints the summary reports.
     */
    public static void main(String[] args) {
        /**
         * Report for previous assignments
         *
         * SortedLinkedList<Sale> salesListSorted = DatabaseLoader.loadSaleListSorted();
         *
         * String totalsReport = DataReporter.reportTotalsBySales(salesListSorted, "Report Totals -- By Sales");
         * String storeTotals = DataReporter.reportTotalsByStore();
         * String salesReport = DataReporter.reportSales();
         *
         * System.out.println(totalsReport);
         * System.out.println(storeTotals);
         * System.out.println(salesReport);
         */

        SortedLinkedList<Sale> salesListSortedByCustomer = DatabaseLoader.loadSaleListSortedByCustomer();
        SortedLinkedList<Sale> salesListSortedByTotal = DatabaseLoader.loadSaleListSortedByTotals();
        SortedLinkedList<Sale> salesListSortedByStore = DatabaseLoader.loadSaleListSortedByStore();

        String salesByCustomersReport = DataReporter.reportTotalsBySales(salesListSortedByCustomer, "Sales by Customer");
        String salesByTotalReport = DataReporter.reportTotalsBySales(salesListSortedByTotal, "Sales by Total");
        String salesByStoreReport = DataReporter.reportTotalsBySales(salesListSortedByStore, "Sales by Store");

        System.out.println(salesByCustomersReport);
        System.out.println(salesByTotalReport);
        System.out.println(salesByStoreReport);

    }
}