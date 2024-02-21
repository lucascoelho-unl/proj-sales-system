package unl.soc;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.thoughtworks.xstream.XStream;

import java.io.*;
import java.util.*;

public class Utils {

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

                Item item;

                if (itemsInfo.get(1).compareTo("P") == 0) {
                    item = new ProductPurchase(itemsInfo.get(0), itemsInfo.get(2), Double.parseDouble(itemsInfo.get(3)));
                } else if (itemsInfo.get(1).compareTo("S") == 0) {
                    item = new Service(itemsInfo.get(0), itemsInfo.get(2), Double.parseDouble(itemsInfo.get(3)));
                } else if (itemsInfo.get(1).compareTo("D") == 0) {
                    item = new DataPlan(itemsInfo.get(0), itemsInfo.get(2), Double.parseDouble(itemsInfo.get(3)));
                } else {
                    item = new VoicePlan(itemsInfo.get(0), itemsInfo.get(2), Double.parseDouble(itemsInfo.get(3)));
                }
                codeItemMap.put(itemsInfo.get(0), item);
            }
            s.close();
            return codeItemMap;
        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException nse){
            return new HashMap<>();
        }
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
    public static List<Item> readItemsCSVtoList(Map<String, Item> itemMap) {
        return new ArrayList<>(itemMap.values());
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
        } catch (NoSuchElementException nse){
            return new HashMap<>();
        }
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
    public static List<Person> readPersonCSVtoList(Map<String, Person> personMap) {
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
    public static Map<String, Store> readStoreCSVtoMap(String path) {
        try {
            Scanner s = new Scanner(new File(path));
            Map<String, Store> codeStoreMap = new HashMap<>();
            Map<String, Person> personMap = readPersonCSVtoMap("data/Persons.csv");

            s.nextLine();
            while (s.hasNext()) {
                String line = s.nextLine();
                List<String> storeData = Arrays.asList(line.split(","));
                Person personManager = personMap.get(storeData.get(1));

                Store store = new Store(storeData.get(0),
                        new Address(storeData.get(2), storeData.get(3), storeData.get(4), Integer.parseInt(storeData.get(5))),
                        new Manager(personManager));

                codeStoreMap.put(storeData.get(0), store);
            }
            s.close();
            return codeStoreMap;

        } catch (FileNotFoundException e) {
            throw new RuntimeException(e);
        } catch (NoSuchElementException nse){
            return new HashMap<>();
        }
    }

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
    public static List<Store> readStoreCSVtoList(Map<String, Store> storeMap) {
        return new ArrayList<>(storeMap.values());
    }

    /**
     * This method creates a JSON file from a list of objects using the Gson library.
     * The JSON file is written with pretty formatting.
     *
     * @param listOfObject The list of objects to be converted to JSON format.
     * @param filePath The file path where the JSON file will be created.
     */
    public static void createJsonFile(List<?> listOfObject, String filePath) {

        Gson gson = new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setPrettyPrinting().create();

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
            String json = gson.toJson(listOfObject);
            writer.write(json);
            writer.close();
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
    public static void createJsonFile(Map<String,?> mapOfObject, String filePath) {

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
     * @param filePath The file path where the XML file will be created.
     */
    public static void createXMLFile(List<?> listOfObject, String filePath) {
        XStream xStream = new XStream();

        if (listOfObject.isEmpty()){
            try {
                xStream.toXML(listOfObject, new FileWriter(filePath));
                return;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Class<?> listType = listOfObject.get(0).getClass();

        if (listType.getSuperclass().isInstance(listOfObject.get(0)) && listType.getSuperclass() != Object.class){
            // Process annotations from item classes, change label in XML file
            xStream.processAnnotations(ProductPurchase.class);
            xStream.processAnnotations(ProductLease.class);
            xStream.processAnnotations(Service.class);
            xStream.processAnnotations(VoicePlan.class);
            xStream.processAnnotations(DataPlan.class);

            //Changes the root of the list to the plural of the superclass name
            xStream.alias(String.format(listType.getSuperclass().getSimpleName() + "s").toLowerCase(), List.class);
        }
        else {
            xStream.processAnnotations(listType);
            //Changes the root of the list to the plural of the class name
            xStream.alias(String.format(listType.getSimpleName() + "s").toLowerCase(), List.class);
        }

        if (listType.getSimpleName().equals("Person")){
            xStream.alias("email", String.class);
        }

        try {
            xStream.toXML(listOfObject, new FileWriter(filePath));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}