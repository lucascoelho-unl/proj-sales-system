package unl.soc;

import javax.xml.crypto.Data;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;

/**
 * The Utils class provides utility methods for reading data from CSV files, parsing it into objects,
 * and converting objects to JSON or XML formats.
 * Additionally, it includes methods for creating JSON files from lists or maps of objects using the Gson library,
 * and for creating XML files from lists of objects using the XStream library.
 */
public class DataProcessor {

    /**
     * Processes purchased items from a CSV file and updates the sales map accordingly.
     *
     * @param path The path to the CSV file containing purchased items.
     */
    public static Map<String, Sale> processPurchasedItemsIntoSalesMap(Map<String, Sale> salesMap,
                                                                      Map<String, Item> itemsMap,
                                                                      Map<String, Person> personsMap,
                                                                      String path) {


        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<String> itemInSaleInfo = Arrays.asList(line.split(","));

                if (itemInSaleInfo.size() < 2) {
                    return salesMap;
                }

                String saleCode = itemInSaleInfo.get(0);
                String itemCode = itemInSaleInfo.get(1);
                Sale sale = salesMap.get(saleCode);
                Item item = itemsMap.get(itemCode);
                double basePrice = item.getBasePrice();

                // Determine the type of item and add it to the sale
                if (item instanceof ProductPurchase) {
                    if (itemInSaleInfo.size() == 2) {
                        sale.addItem(new ProductPurchase(item));
                    } else {
                        String startDate = itemInSaleInfo.get(2);
                        String endDate = itemInSaleInfo.get(3);
                        sale.addItem(new ProductLease(item, startDate, endDate));
                    }
                } else if (item instanceof Service) {
                    double totalHours = Double.parseDouble(itemInSaleInfo.get(2));
                    Person employee = personsMap.get(itemInSaleInfo.get(3));
                    sale.addItem(new Service(item, totalHours, employee));
                } else if (item instanceof DataPlan) {
                    double totalGB = Double.parseDouble(itemInSaleInfo.get(2));
                    sale.addItem(new DataPlan(item, totalGB));
                } else if (item instanceof VoicePlan) {
                    String phoneNumber = itemInSaleInfo.get(2);
                    double totalPeriod = Double.parseDouble(itemInSaleInfo.get(3));
                    sale.addItem(new VoicePlan(item, phoneNumber, totalPeriod));
                }
            }
            return salesMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Processes purchased items from a CSV file and updates the sales map accordingly.
     *
     * @param path The path to the CSV file containing purchased items.
     */
    public static Map<String, Sale> processPurchasedItemsIntoSalesMap(Map<String, Sale> salesMap, String path){

        Map<String, Item> itemsMap = DataProcessor.readItemsCSVtoMap("data/Items.csv");
        Map<String, Person> personsMap = DataProcessor.readPersonCSVtoMap("data/Persons.csv");

        try (Scanner scanner = new Scanner(new File(path))) {
            scanner.nextLine();
            while (scanner.hasNextLine()) {
                String line = scanner.nextLine();
                List<String> itemInSaleInfo = Arrays.asList(line.split(","));

                if (itemInSaleInfo.size() < 2) {
                    return salesMap;
                }

                String saleCode = itemInSaleInfo.get(0);
                String itemCode = itemInSaleInfo.get(1);
                Sale sale = salesMap.get(saleCode);
                Item item = itemsMap.get(itemCode);

                // Determine the type of item and add it to the sale
                if (item instanceof ProductPurchase) {
                    if (itemInSaleInfo.size() == 2) {
                        sale.addItem(new ProductPurchase(item));
                    } else {
                        String startDate = itemInSaleInfo.get(2);
                        String endDate = itemInSaleInfo.get(3);
                        sale.addItem(new ProductLease(item, startDate, endDate));
                    }
                } else if (item instanceof Service) {
                    double totalHours = Double.parseDouble(itemInSaleInfo.get(2));
                    Person employee = personsMap.get(itemInSaleInfo.get(3));
                    sale.addItem(new Service(item, totalHours, employee));
                } else if (item instanceof DataPlan) {
                    double totalGB = Double.parseDouble(itemInSaleInfo.get(2));
                    sale.addItem(new DataPlan(item, totalGB));
                } else if (item instanceof VoicePlan) {
                    String phoneNumber = itemInSaleInfo.get(2);
                    double totalPeriod = Double.parseDouble(itemInSaleInfo.get(3));
                    sale.addItem(new VoicePlan(item, phoneNumber, totalPeriod));
                }
            }
            return salesMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads sales data from a CSV file and stores it in a map.
     *
     * @param path The path to the CSV file containing sales data.
     * @return A map where keys are sale codes and values are Sale objects.
     */
    public static Map<String, Sale> readSaleCSVToMap(String path) {
        Map<String, Person> personMap = DataProcessor.readPersonCSVtoMap("data/Persons.csv");
        Map<String, Store> storeCodeMap = DataProcessor.readStoreCSVtoMap("data/Stores.csv");

        try {
            Scanner s = new Scanner(new File(path));

            Map<String, Sale> saleCodeMap = new HashMap<>();

            s.nextLine();
            while (s.hasNext()) {
                String line = s.nextLine();
                List<String> saleInfo = Arrays.asList(line.split(","));

                if (saleInfo.size() < 2) {
                    return saleCodeMap;
                }

                String saleCode = saleInfo.get(0);
                Store store = storeCodeMap.get(saleInfo.get(1));
                Person customer = personMap.get(saleInfo.get(2));
                Person salesman = personMap.get(saleInfo.get(3));
                String date = saleInfo.get(4);

                Sale sale = new Sale(saleCode, store, customer, salesman, date);

                store.addSale(sale);

                saleCodeMap.put(saleCode, sale);
            }
            return saleCodeMap;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Reads data from a CSV file containing information about items and converts it into a Map with item codes as keys
     * and corresponding Item objects as values.
     *
     * @param path The path to the CSV file.
     * @return A Map<String, Item> where keys are item codes and values are Item objects created from the data in the CSV file.
     * @throws RuntimeException if there is an issue reading the file or parsing the data.
     */
    public static Map<String, Item> readItemsCSVtoMap(String path) {
        try {
            Scanner s = new Scanner(new File(path));

            Map<String, Item> codeItemMap = new HashMap<>();

            s.nextLine();
            while (s.hasNext()) {
                String line = s.nextLine();
                List<String> itemsInfo = Arrays.asList(line.split(","));

                if (itemsInfo.size() < 2) {
                    return codeItemMap;
                }

                String code = itemsInfo.get(0);
                String type = itemsInfo.get(1);
                String name = itemsInfo.get(2);
                double baseCost = Double.parseDouble(itemsInfo.get(3));

                Item item = switch (type) {
                    case "P" -> new ProductPurchase(code, name, baseCost);
                    case "S" -> new Service(code, name, baseCost);
                    case "D" -> new DataPlan(code, name, baseCost);
                    case "V" -> new VoicePlan(code, name, baseCost);
                    default -> throw new IllegalStateException("Unexpected value: " + type);
                };

                codeItemMap.put(code, item);
            }
            s.close();
            return codeItemMap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException nse) {
            return new HashMap<>();
        }
    }

    /**
     * Parses a Map of items, where keys are item codes and values are Item objects, into categorized maps
     * based on their types and constructs a map containing these categorized maps for product purchases,
     * product leases, voice plans, data plans, and services.
     *
     * @param itemsMap A Map<String, Item> where keys are item codes and values are Item objects.
     * @return A Map<String, Object> where keys are item types and values are categorized maps (e.g., product purchases, services).
     */
    public static Map<String, Object> itemsMapParse(Map<String, Item> itemsMap) {
        Map<String, ProductPurchase> productPurchaseList = new HashMap<>();
        Map<String, VoicePlan> voicePlanMap = new HashMap<>();
        Map<String, DataPlan> dataPlanMap = new HashMap<>();
        Map<String, ProductLease> productLeaseMap = new HashMap<>();
        Map<String, Service> serviceMap = new HashMap<>();

        Map<String, Object> result = new HashMap<>(Map.of(
                "purchase", productPurchaseList,
                "lease", productLeaseMap,
                "voicePlan", voicePlanMap,
                "dataPlan", dataPlanMap,
                "service", serviceMap));

        for (Item item : itemsMap.values()) {
            if (item instanceof ProductPurchase) {
                productPurchaseList.put(item.getUniqueCode(), (ProductPurchase) item);
            } else if (item instanceof Service) {
                serviceMap.put(item.getUniqueCode(), (Service) item);
            } else if (item instanceof VoicePlan) {
                voicePlanMap.put(item.getUniqueCode(), (VoicePlan) item);
            } else if (item instanceof DataPlan) {
                dataPlanMap.put(item.getUniqueCode(), (DataPlan) item);
            }
        }

        result.put("purchase", productPurchaseList);
        result.put("service", serviceMap);
        result.put("voicePlan", voicePlanMap);
        result.put("dataPlan", dataPlanMap);

        return result;
    }

    /**
     * Reads data from a CSV file containing information about persons and converts it into a Map with UUIDs as keys
     * and corresponding Person objects as values.
     *
     * @param path The path to the CSV file.
     * @return A Map<String, Person> where keys are UUIDs and values are Person objects created from the data in the CSV file.
     * @throws RuntimeException if there is an issue reading the file or parsing the data.
     */
    public static Map<String, Person> readPersonCSVtoMap(String path) {
        try {
            Scanner s = new Scanner(new File(path));
            Map<String, Person> uuidPersonMap = new HashMap<>();

            s.nextLine();
            while (s.hasNext()) {
                String line = s.nextLine();
                List<String> personData = Arrays.asList(line.split(","));

                if (personData.size() < 2) {
                    return uuidPersonMap;
                }

                List<String> emailList = new ArrayList<>();
                for (int i = 7; i < personData.size(); i++) {
                    emailList.add(personData.get(i));
                }

                Person person = new Person(personData.get(0),
                        personData.get(1),
                        personData.get(2),
                        new Address(personData.get(3), personData.get(4), personData.get(5), Integer.parseInt(personData.get(6))),
                        emailList);

                uuidPersonMap.put(person.getUuid(), person);
            }
            s.close();
            return uuidPersonMap;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException nse) {
            return new HashMap<>();
        }
    }

    /**
     * Reads data from a CSV file containing information about stores and their managers,
     * and creates a map of store codes to Store objects.
     *
     * @param path The path to the CSV file.
     * @return A map of store codes to Store objects.
     * @throws RuntimeException If the file specified by 'path' is not found.
     */
    public static Map<String, Store> readStoreCSVtoMap(String path) {
        try {
            Scanner s = new Scanner(new File(path));
            Map<String, Store> codeStoreMap = new HashMap<>();
            Map<String, Person> personMap = readPersonCSVtoMap("data/Persons.csv");

            s.nextLine();
            while (s.hasNext()) {
                String line = s.nextLine();
                List<String> storeData = Arrays.asList(line.split(","));

                if (storeData.size() < 2) {
                    return codeStoreMap;
                }

                Person manager = personMap.get(storeData.get(1));

                Store store = new Store(storeData.get(0),
                        new Address(storeData.get(2), storeData.get(3), storeData.get(4), Integer.parseInt(storeData.get(5))),
                        manager);

                codeStoreMap.put(storeData.get(0), store);
            }
            s.close();
            return codeStoreMap;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException nse) {
            return new HashMap<>();
        }
    }

    public static List<Sale> convertSalesMapToList(Map<String, Sale> salesMap) {
        return new ArrayList<>(salesMap.values());
    }

    public static List<Sale> salesList(String path) {
        List<Sale> saleList = new ArrayList<>(readSaleCSVToMap(path).values());
        Collections.reverse(saleList);
        return saleList;
    }

    /**
     * Reads data from a CSV file containing information about items and converts it into a List of Item objects.
     *
     * @param path The path to the CSV file.
     * @return A List of Item objects created from the data in the CSV file.
     * @throws RuntimeException if there is an issue reading the file or parsing the data.
     */
    public static List<Item> readItemsCSVtoList(String path) {
        return new ArrayList<>(readItemsCSVtoMap(path).values());
    }

    /**
     * Converts a Map of items, where keys are item codes and values are Item objects,
     * into a List containing all the Item objects from the map.
     *
     * @param itemMap A Map<String, Item> where keys are item codes and values are Item objects.
     * @return A List of Item objects created from the values in the provided itemMap.
     */
    public static List<Item> convertItemMapToList(Map<String, Item> itemMap) {
        return new ArrayList<>(itemMap.values());
    }

    /**
     * Reads data from a CSV file containing information about persons and converts it into a List of Person objects.
     *
     * @param path The path to the CSV file.
     * @return A List of Person objects created from the data in the CSV file.
     * @throws RuntimeException if there is an issue reading the file or parsing the data.
     */
    public static List<Person> readPersonCSVtoList(String path) {
        return new ArrayList<>(readPersonCSVtoMap(path).values());
    }

    /**
     * Converts a Map of persons, where keys are UUIDs and values are Person objects,
     * into a List containing all the Person objects from the map.
     *
     * @param personMap A Map<String, Person> where keys are UUIDs and values are Person objects.
     * @return A List of Person objects created from the values in the provided personMap.
     */
    public static List<Person> convertPersonMapToList(Map<String, Person> personMap) {
        return new ArrayList<>(personMap.values());
    }

    /**
     * Reads data from a CSV file containing information about stores and converts it into a Map with store codes as keys
     * and corresponding Store objects as values. Additionally, it associates each store with a manager from the provided
     * Persons CSV file.
     *
     * @param path The path to the CSV file containing store information.
     * @return A Map<String, Store> where keys are store codes and values are Store objects created from the data in the CSV file.
     * @throws RuntimeException if there is an issue reading the file or parsing the data.
     */

    /**
     * Reads data from a CSV file containing information about stores and converts it into a List of Store objects.
     *
     * @param path The path to the CSV file.
     * @return A List of Store objects created from the data in the CSV file.
     * @throws RuntimeException if there is an issue reading the file or parsing the data.
     */
    public static List<Store> readStoreCSVtoList(String path) {
        return new ArrayList<>(readStoreCSVtoMap(path).values());
    }

    /**
     * Converts a Map of stores, where keys are store codes and values are Store objects,
     * into a List containing all the Store objects from the map.
     *
     * @param storeMap A Map<String, Store> where keys are store codes and values are Store objects.
     * @return A List of Store objects created from the values in the provided storeMap.
     */
    public static List<Store> convertStoreMapToList(Map<String, Store> storeMap) {
        return new ArrayList<>(storeMap.values());
    }
}