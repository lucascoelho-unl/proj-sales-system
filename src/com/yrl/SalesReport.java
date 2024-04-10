package com.yrl;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;
import unl.soc.DataReporter;
import unl.soc.DatabaseLoader;

/**
 * The SalesReport class generates a summary report of sales data.
 * It reads sales, items, persons, and stores information from CSV files,
 * processes the data, and prints a summary report including total items sold,
 * total tax collected, and total sales revenue.
 */
public class SalesReport {

    //TODO add LOGGER reports for this class

    /**
     * The main method generates and prints the summary reports.
     */
    public static void main(String[] args) {
        String totalsReport = DataReporter.reportTotalsBySalesFromDB();
        String storeTotals = DataReporter.reportTotalsByStoreFromDB();
        String salesReport = DataReporter.reportSalesFromDB();

        System.out.println(totalsReport);
        System.out.println(storeTotals);
        System.out.println(salesReport);
    }
}