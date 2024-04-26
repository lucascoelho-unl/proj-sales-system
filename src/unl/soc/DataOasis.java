package unl.soc;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DataOasis {

    private static final DataOasis INSTANCE = new DataOasis();

    private static Map<Integer, Address> addressMap;
    private static Map<Integer, Person> personMap;
    private static Map<Integer, Store> storeMap;
    private static Map<Integer, Sale> salesMap;
    private static Map<Integer, Item> itemMap;
    private static Map<Integer, Item> itemSoldMap;

    private DataOasis() {
        if (addressMap == null || personMap == null || storeMap == null || salesMap == null || itemMap == null || itemSoldMap == null) {
            addressMap = new HashMap<>();
            personMap = new HashMap<>();
            storeMap = new HashMap<>();
            salesMap = new HashMap<>();
            itemMap = new HashMap<>();
            itemSoldMap = new HashMap<>();
        }
        loadDataFromDB();
    }

    public static DataOasis getInstance() {
        return INSTANCE;
    }

    /**
     * Loads data from a database into private variables.
     */
    public static void loadDataFromDB() {
        if (addressMap.isEmpty() || personMap.isEmpty() || storeMap.isEmpty() || salesMap.isEmpty() || itemMap.isEmpty() || itemSoldMap.isEmpty()) {
            itemSoldMap = DatabaseLoader.loadAllItemSold();

            addressMap = DatabaseLoader.loadAllAddress();

            personMap = DatabaseLoader.loadAllPersons();

            itemMap = DatabaseLoader.loadAllItems();

            storeMap = DatabaseLoader.loadAllStores();

            salesMap = DatabaseLoader.loadAllSales();


        }
    }

    public static Map<Integer, Address> getAddressMap() {
        return new HashMap<>(addressMap);
    }

    public static Map<Integer, Person> getPersonMap() {
        return new HashMap<>(personMap);
    }

    public static Map<Integer, Store> getStoreMap() {
        return new HashMap<>(storeMap);
    }

    public static Map<Integer, Sale> getSalesMap() {
        return new HashMap<>(salesMap);
    }

    public static Map<Integer, Item> getItemMap() {
        return new HashMap<>(itemMap);
    }

    public Map<Integer, Item> getItemSoldMap() {
        return new HashMap<>(itemSoldMap);
    }

    public static List<Address> getAddressList() {
        return new ArrayList<>(addressMap.values());
    }

    public static List<Item> getItemsSoldList() {
        return new ArrayList<>(itemSoldMap.values());
    }

    public static List<Item> getItemsList() {
        return new ArrayList<>(itemMap.values());
    }

    public static List<Person> getPersonsList() {
        return new ArrayList<>(personMap.values());
    }

    public static List<Sale> getSalesList() {
        return new ArrayList<>(salesMap.values());
    }

    public static List<Store> getStoresList() {
        return new ArrayList<>(storeMap.values());
    }
}
