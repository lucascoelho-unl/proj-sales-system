package unl.soc;

import java.util.List;
import java.util.Map;

/**
 * The DataReporter class generates various sales reports based on the data loaded from CSV files.
 */
public class DataReporter {

    /**
     * Generates a sales report organized by total sales.
     *
     * @return A string representing the sales report.
     */
    public static String reportTotalsBySales() {
        // Load sales, items, and persons maps from CSV files
        Map<String, Sale> salesMap = DataProcessor.processedSalesWithItemsMap("data/SaleItems.csv");
        List<Sale> salesList = DataProcessor.convertSalesMapToList(salesMap);

        salesList.sort(Sale::compareSales);

        StringBuilder sb = new StringBuilder();

        // Print sales report header
        sb.append("Sales Report:\n");
        sb.append("+-----------------------------------------------------------------------------------+\n" +
                "|  Summary Report -- By Total                                                       |\n" +
                "+-----------------------------------------------------------------------------------+\n");
        sb.append("Invoice #  Store     Customer                Num Items          Tax        Total\n");

        // Initialize total sales variables
        int totalItemSales = 0;
        double totalTaxSales = 0;
        double totalPriceSales = 0;

        // Print individual sale details and update total sales variables
        for (Sale sale : salesList) {
            String saleNum = sale.getUniqueCode();
            String storeCode = sale.getStore().getStoreCode();
            String fullName = sale.getCustomer().getLastName() + ", " + sale.getCustomer().getFirstName();
            int numItems = sale.getItemsList().size();
            double tax = Math.round(sale.getTotalTax() * 100) / 100.0;
            double price = Math.round(sale.getNetPrice() * 100) / 100.0;
            sb.append(String.format("%-9s  %-9s  %-20s  %10d  $%10.2f  $%10.2f\n", saleNum, storeCode, fullName, numItems, tax, price));

            totalItemSales += numItems;
            totalTaxSales += tax;
            totalPriceSales += price;
        }

        // Print total sales summary
        sb.append("+-----------------------------------------------------------------------------------+\n");
        sb.append(String.format("%54d  $%10.2f  $%10.2f\n\n", totalItemSales, Math.round(100 * totalTaxSales) / 100.0, Math.round(100 * totalPriceSales) / 100.0));

        return sb.toString();
    }

    /**
     * Generates a sales report organized by store.
     *
     * @return A string representing the store sales report.
     */
    public static String reportTotalsByStore() {
        // Load sales, items, and persons maps from CSV files
        Map<String, Sale> salesMap = DataProcessor.processedSalesWithItemsMap("data/SaleItems.csv");
        Map<String, Store> storeMap = DataProcessor.updateStoreMapFromSalesMap(salesMap);
        List<Store> storesList = DataProcessor.convertStoreMapToList(storeMap);

        storesList.sort(Store::compareStores);

        StringBuilder sb = new StringBuilder();

        // Print store sales summary
        sb.append("+--------------------------------------------------------+\n" +
                "| Store Sales Summary Report                             |\n" +
                "+--------------------------------------------------------+\n");
        sb.append("Store      Manager              # Sales   Grand Total\n");
        double totalValue = 0;
        int salesCount = 0;
        for (Store store : storesList) {
            sb.append(store.toString()).append("\n");
            totalValue += store.getTotalSalePrice();
            salesCount += store.getSales().size();
        }
        sb.append("+--------------------------------------------------------+\n");
        sb.append(String.format("%38d %4s %9.2f\n", salesCount, "$", Math.round(totalValue * 100) / 100.0));

        return sb.toString();
    }

    /**
     * Generates a sales report containing details of individual sales.
     *
     * @return A string representing the sales report.
     */
    public static String reportSales() {
        // Load sales, items, and persons maps from CSV files
        Map<String, Sale> salesMap = DataProcessor.processedSalesWithItemsMap("data/SaleItems.csv");

        List<Sale> salesList = DataProcessor.convertSalesMapToList(salesMap);

        salesList.sort(Sale::compareSales);

        StringBuilder sb = new StringBuilder();

        // Print individual sale details
        sb.append("\n");
        for (Sale sale : salesList) {
            sb.append(sale.toString());
            sb.append("\n");
        }

        return sb.toString();
    }
}
