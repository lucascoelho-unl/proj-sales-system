package unl.soc;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Data oasis is a singleton that is a helper class for the data loaders classes.
 * It stores the maps loaded from the database to use in the database loader and CSV classes.
 */
public class DataOasis {

    /**
     * Static variable instance of DataOasis for DataLoading
     */
    private static final DataOasis INSTANCE = new DataOasis();

    private Map<Integer, Address> addressMap;
    private Map<Integer, Person> personMap;
    private Map<Integer, Store> storeMap;
    private Map<Integer, Sale> salesMap;
    private Map<Integer, Item> itemMap;
    private Map<Integer, Item> itemSoldMap;

    private DataOasis() {
        this.addressMap = new HashMap<>();
        this.personMap = new HashMap<>();
        this.storeMap = new HashMap<>();
        this.salesMap = new HashMap<>();
        this.itemMap = new HashMap<>();
        this.itemSoldMap = new HashMap<>();

        loadDataFromDB();
    }

    public static DataOasis getInstance() {
        return INSTANCE;
    }

    /**
     * Loads data from a database into private variables.
     */
    public void loadDataFromDB() {
        if (this.addressMap.isEmpty() || this.personMap.isEmpty() || this.storeMap.isEmpty() || this.salesMap.isEmpty() || this.itemMap.isEmpty() || this.itemSoldMap.isEmpty()) {
            this.itemSoldMap = DatabaseLoader.loadAllItemSold();

            this.addressMap = DatabaseLoader.loadAllAddress();

            this.personMap = DatabaseLoader.loadAllPersons();

            this.itemMap = DatabaseLoader.loadAllItems();

            this.salesMap = DatabaseLoader.loadAllSales();

            this.storeMap = DatabaseLoader.loadAllStores();


        }
    }

    public Map<Integer, Address> getAddressMap() {
        return new HashMap<>(this.addressMap);
    }

    public Map<Integer, Person> getPersonMap() {
        return new HashMap<>(this.personMap);
    }

    public Map<Integer, Store> getStoreMap() {
        return new HashMap<>(this.storeMap);
    }

    public Map<Integer, Sale> getSalesMap() {
        return new HashMap<>(this.salesMap);
    }

    public Map<Integer, Item> getItemMap() {
        return new HashMap<>(this.itemMap);
    }

    public Map<Integer, Item> getItemSoldMap() {
        return new HashMap<>(this.itemSoldMap);
    }

    public List<Address> getAddressList() {
        return new ArrayList<>(this.addressMap.values());
    }

    public List<Item> getItemsSoldList() {
        return new ArrayList<>(this.itemSoldMap.values());
    }

    public List<Item> getItemsList() {
        return new ArrayList<>(this.itemMap.values());
    }

    public List<Person> getPersonsList() {
        return new ArrayList<>(this.personMap.values());
    }

    public List<Sale> getSalesList() {
        return new ArrayList<>(this.salesMap.values());
    }

    public List<Store> getStoresList() {
        return new ArrayList<>(this.storeMap.values());
    }
}
