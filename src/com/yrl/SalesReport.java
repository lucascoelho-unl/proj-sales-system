package com.yrl;

import unl.soc.DataProcessor;
import unl.soc.DataWriter;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

/**
 * The SalesReport class generates a summary report of sales data.
 * It reads sales, items, persons, and store information from CSV files,
 * processes the data, and prints a summary report including total items sold,
 * total tax collected, and total sales revenue.
 */
public class SalesReport {

    /**
     * The main method generates and prints the summary reports.
     */
    public static void main(String[] args) {
        DataWriter.printSalesReport();
    }
}