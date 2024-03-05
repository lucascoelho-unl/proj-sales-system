package unl.soc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataWriter {
    /**
     * This method creates a JSON file from a list of objects using the Gson library.
     * The JSON file is written with pretty formatting.
     *
     * @param listOfObject The list of objects to be converted to JSON format.
     * @param filePath     The file path where the JSON file will be created.
     */
    public static void createJsonFile(List<?> listOfObject, String header, String filePath) {
        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            // Add the header to the JSON string
            StringBuilder jsonBuilder = new StringBuilder();
            jsonBuilder.append("{\n");
            jsonBuilder.append("  \"" + header + "\": ");

            // Convert the list of objects to JSON
            String jsonData = gson.toJson(listOfObject);
            jsonBuilder.append(jsonData);
            jsonBuilder.append("\n}");

            // Write the JSON string to the file
            writer.write(jsonBuilder.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates a JSON file from a map of objects using the Gson library.
     * The JSON file is written with pretty formatting.
     *
     * @param mapOfObject The map of objects to be converted to JSON format.
     * @param filePath The file path where the JSON file will be created.
     */
    public static void createJsonFile(Map<String, ?> mapOfObject, String header, String filePath) {
        Map<String, Object> dataWithHeader = new HashMap<>();
        dataWithHeader.put("header", header);
        dataWithHeader.putAll(mapOfObject);

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            String json = gson.toJson(mapOfObject);
            writer.write(json);
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * This method creates an XML file from a list of objects using the XStream library.
     * It processes annotations for specific classes and handles special cases such as empty lists and specific class names.
     *
     * @param listOfObject The list of objects to be converted to XML format.
     * @param filePath     The file path where the XML file will be created.
     */
    public static void createXMLFile(List<?> listOfObject, String filePath) {
        XStream xstream = new XStream();
        xstream.setMode(XStream.NO_REFERENCES);

        if (listOfObject.isEmpty()) {
            try {
                xstream.toXML(listOfObject, new FileWriter(filePath));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Class<?> listType = listOfObject.get(0).getClass();

        if (listType.getSuperclass().isInstance(listOfObject.get(0)) && listType.getSuperclass() != Object.class) {
            // Process annotations from item classes, change label in XML file
            xstream.processAnnotations(ProductPurchase.class);
            xstream.processAnnotations(ProductLease.class);
            xstream.processAnnotations(Service.class);
            xstream.processAnnotations(VoicePlan.class);
            xstream.processAnnotations(DataPlan.class);

            //Changes the root of the list to the plural of the superclass name
            xstream.alias(String.format(listType.getSuperclass().getSimpleName() + "s").toLowerCase(), List.class);
        } else {
            xstream.processAnnotations(listType);
            //Changes the root of the list to the plural of the class name
            xstream.alias(String.format(listType.getSimpleName() + "s").toLowerCase(), List.class);
        }

        if (listType.getSimpleName().equals("Person")) {
            xstream.alias("email", String.class);
        }

        try {
            xstream.toXML(listOfObject, new FileWriter(filePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createSaleReport(String outputPath){

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(outputPath));
            // Load sales, items, and persons maps from CSV files
            Map<String, Item> itemsMap = DataProcessor.readItemsCSVtoMap("data/Items.csv");
            Map<String, Person> personsMap = DataProcessor.readPersonCSVtoMap("data/Persons.csv");
            Map<String, Store> storeMap = DataProcessor.readStoreCSVtoMap("data/Stores.csv");

            // Read sales data, process purchased items, and create sales map
            Map<String, Sale> salesMapRaw = DataProcessor.readSalesToMap(personsMap, storeMap, "data/Sales.csv");
            Map<String, Sale> salesMap = DataProcessor.processPurchasedItemsIntoSalesMap(salesMapRaw, itemsMap, personsMap, "data/SaleItems.csv");

            // Print sales report header
            writer.write("Sales Report:");
            writer.newLine();
            writer.write("+-----------------------------------------------------------------------------------+\n" +
                    "|  Summary Report -- By Total                                                       |\n" +
                    "+-----------------------------------------------------------------------------------+");
            writer.newLine();
            writer.write("Invoice #  Store     Customer                Num Items          Tax        Total");
            writer.newLine();

            // Initialize total sales variables
            int totalItemSales = 0;
            double totalTaxSales = 0;
            double totalPriceSales = 0;

            // Print individual sale details and update total sales variables
            assert salesMap != null;
            for (Sale sale : salesMap.values()) {
                String saleNum = sale.getUniqueCode();
                String storeCode = sale.getStore().getStoreCode();
                String fullName = sale.getCustomer().getLastName() + ", " + sale.getCustomer().getFirstName();
                int numItems = sale.getItemsList().size();
                double tax = sale.getTotalTax();
                double price = sale.getNetPrice();
                writer.write(String.format("%-9s  %-9s  %-20s  %10d  $%10.2f  $%10.2f\n", saleNum, storeCode, fullName, numItems, tax, price));

                totalItemSales += numItems;
                totalTaxSales += tax;
                totalPriceSales += price;
            }

            // Print total sales summary
            writer.write("+-----------------------------------------------------------------------------------+");
            writer.newLine();
            writer.write(String.format("%54d  $%10.2f  $%10.2f\n\n", totalItemSales, totalTaxSales, totalPriceSales));

            // Print store sales summary
            writer.write("+--------------------------------------------------------+\n" +
                    "| Store Sales Summary Report                             |\n" +
                    "+--------------------------------------------------------+");
            writer.newLine();
            writer.write("Store      Manager              # Sales   Grand Total");
            writer.newLine();
            for (Store store : storeMap.values()) {
                writer.write(store.toString());
                writer.newLine();
            }

            // Print individual sale details
            writer.newLine();
            for (Sale sale : salesMap.values()){
                writer.write(sale.toString());
                writer.newLine();
            }


            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
