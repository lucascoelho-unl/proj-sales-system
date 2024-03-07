package com.yrl;

import unl.soc.DataWriter;

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
        DataWriter.createSaleReport("data/output.txt");
    }
}
