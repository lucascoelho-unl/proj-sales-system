package unl.soc;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class DataOasis {

    private static final DataOasis INSTANCE = new DataOasis();

    private static List<Person> personsList;
    private static List<Item> itemsList;
    private static List<Store> storesList;
    private static List<Sale> salesList;

    private DataOasis() {
        loadDataFromDB();
    }

    public static DataOasis getInstance() {
        return INSTANCE;
    }

    /**
     * Loads data from a database into private variables.
     */
    public static void loadDataFromDB() {
        Map<Integer, Sale> salesMap = DatabaseLoader.loadAllSales();
        salesList = new ArrayList<>(salesMap.values());

        Map<Integer, Person> personMap = DatabaseLoader.loadAllPersons();
        personsList = new ArrayList<>(personMap.values());

        Map<Integer, Item> itemMap = DatabaseLoader.loadAllItems();
        itemsList = new ArrayList<>(itemMap.values());

        Map<Integer, Store> storeMap = DatabaseLoader.loadAllStores();
        storesList = new ArrayList<>(storeMap.values());
    }

    public static List<Item> getItemsList() {
        return itemsList;
    }

    public static List<Person> getPersonsList() {
        return personsList;
    }

    public static List<Sale> getSalesList() {
        return salesList;
    }

    public static List<Store> getStoresList() {
        return storesList;
    }
}
