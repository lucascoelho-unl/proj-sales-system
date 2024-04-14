package unl.soc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * The DataReporter class generates various sales reports based on the data loaded from CSV files or a database.
 */
public class DataReporter {

    // private variables instantiation to avoid duplicate calls and improve efficiency after data is loaded.
    // loads the data ONLY ONCE instead of loading it multiple times when calling a function.
    private static List<Person> personsList;
    private static List<Item> itemsList;
    private static List<Store> storesList;
    private static List<Sale> salesList;

    /**
     * Loads data from CSV files into private variables.
     */
    public static void loadDataFromCSV() {
        Map<String, Person> personMap = DataProcessor.readPersonCSVtoMap("data/Persons.csv");
        Map<String, Item> itemsMap = DataProcessor.readItemsCSVtoMap("data/Items.csv");
        Map<String, Store> storeMap = DataProcessor.readStoreCSVtoMap("data/Stores.csv");
        Map<String, Sale> salesMap = DataProcessor.readSaleCSVToMap("data/Sales.csv");

        salesMap = DataProcessor.fillSalesWithItemsMap(salesMap, itemsMap, personMap, "data/SaleItems.csv");
        storeMap = DataProcessor.updateStoreMapFromSalesMap(storeMap, salesMap);

        personsList = new ArrayList<>(personMap.values());
        itemsList = new ArrayList<>(itemsMap.values());
        storesList = new ArrayList<>(storeMap.values());
        salesList = new ArrayList<>(salesMap.values());
    }

    /**
     * Loads data from a database into private variables.
     */
    public static void loadDataFromDB() {
        Map<Integer, Person> personMap = DatabaseLoader.loadAllPersons();
        Map<Integer, Item> itemMap = DatabaseLoader.loadAllItems();
        Map<Integer, Store> storeMap = DatabaseLoader.loadAllStores();
        Map<Integer, Sale> salesMap = DatabaseLoader.loadAllSales();

        personsList = new ArrayList<>(personMap.values());
        itemsList = new ArrayList<>(itemMap.values());
        storesList = new ArrayList<>(storeMap.values());
        salesList = new ArrayList<>(salesMap.values());
    }

    /**
     * Generates a sales report organized by total sales.
     *
     * @return A string representing the sales report.
     */
    public static String reportTotalsBySales() {

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