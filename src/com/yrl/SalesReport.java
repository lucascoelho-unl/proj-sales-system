package com.yrl;

import unl.soc.DataReporter;

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
        DataReporter.loadDataFromDB();

        String totalsReport = DataReporter.reportTotalsBySales();
        String storeTotals = DataReporter.reportTotalsByStore();
        String salesReport = DataReporter.reportSales();

        System.out.println(totalsReport);
        System.out.println(storeTotals);
        System.out.println(salesReport);
    }
}