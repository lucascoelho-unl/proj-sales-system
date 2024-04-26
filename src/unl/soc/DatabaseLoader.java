package unl.soc;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

/**
 * This class provides methods to load data from the database into memory objects.
 */
public class DatabaseLoader {
    private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);
    private static final DataOasis instance;


    // Configure the Logger
    static {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.INFO);
        instance = DataOasis.getInstance();
    }

    /**
     * Loads an Address object from the database based on the given address ID.
     *
     * @param addressId The ID of the address to load.
     * @return The Address object loaded from the database.
     */
    public static Address loadAddress(int addressId) {
        if ((instance != null) && !instance.getAddressMap().isEmpty()) {
            return instance.getAddressMap().get(addressId);
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Address address = null;

        String query = """
                select zipcode, state, city, street from Address a
                left join Zipcode z on a.zipcodeId = z.zipcodeId
                left join State S on z.stateId = S.stateId
                where addressId = ?;
                """;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, addressId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int zipcode = rs.getInt("zipcode");
                String state = rs.getString("state");
                String city = rs.getString("city");
                String street = rs.getString("street");
                address = new Address(addressId, street, city, state, zipcode);
            }
        } catch (SQLException e) {
            LOGGER.error("Error parsing address {}: {}", addressId, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return address;
    }

    /**
     * Loads all Address objects from the database.
     *
     * @return A map of address IDs to Address objects.
     */
    public static Map<Integer, Address> loadAllAddress() {
        if ((instance != null) && !instance.getAddressMap().isEmpty()) {
            return instance.getAddressMap();
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map <Integer, Address> addressMapResult = new HashMap<>();

        String query = """
                select addressId from Address;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Address address = loadAddress(rs.getInt("addressId"));
                addressMapResult.put(rs.getInt("addressId"), address);
            }

        } catch (SQLException e) {
            LOGGER.error("Error parsing all addresses", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Loaded {} addresses", addressMapResult.size());
        return addressMapResult;
    }

    /**
     * Loads a Person object from the database based on the given person ID.
     *
     * @param personId The ID of the person to load.
     * @return The Person object loaded from the database.
     */
    public static Person loadPerson(int personId) {
        if ((instance != null) && !instance.getPersonMap().isEmpty()) {
            return instance.getPersonMap().get(personId);
        }

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Person person = null;

        String query = """
                select uuid, firstName, lastName, addressId from Person
                where personId = ?;
                """;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String uuid = rs.getString("uuid");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Address address = loadAddress(rs.getInt("addressId"));
                List<String> emails = loadEmails(personId);
                person = new Person(personId, uuid, firstName, lastName, address, emails);
            }
        } catch (SQLException e) {
            LOGGER.error("Error parsing person {}: {}", personId, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return person;
    }

    /**
     * Loads a Person object from the database based on the given person uuid.
     *
     * @param uuid The unique code of the person to load.
     * @return The Person object loaded from the database.
     */
    public static Person loadPerson(String uuid) {

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Person person = null;

        String query = """
                select personId, firstName, lastName, addressId from Person
                where uuid = ?;
                """;
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, uuid);
            rs = ps.executeQuery();
            if (rs.next()) {
                int personId = rs.getInt("personId");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Address address = loadAddress(rs.getInt("addressId"));
                List<String> emails = loadEmails(personId);
                person = new Person(personId, uuid, firstName, lastName, address, emails);
            }
        } catch (SQLException e) {
            LOGGER.error("Error parsing person {}: {}", uuid, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return person;
    }

    private static List<String> loadEmails(int personId){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        List<String> emailMap = new ArrayList<>();

        String query = "select emailId, address from Email where personId = ?;";

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()) {
                int emailId = rs.getInt("emailId");
                String address = rs.getString("address");
                emailMap.add(address);
            }
        } catch (SQLException e) {
            LOGGER.error("Error parsing person {}: {}", personId, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return emailMap;

    }

    /**
     * Loads all Person objects from the database.
     *
     * @return A map of person IDs to Person objects.
     */
    public static Map<Integer, Person> loadAllPersons() {
        if ((instance != null) && !instance.getPersonMap().isEmpty()) {
            return instance.getPersonMap();
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map <Integer, Person> personMapResult = new HashMap<>();

        String query = """
                select personId from Person;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Person person = loadPerson(rs.getInt("personId"));
                personMapResult.put(rs.getInt("personId"), person);
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading all persons: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully loaded {} persons", personMapResult.size());
        return personMapResult;
    }

    /**
     * Loads a Store object from the database based on the given store ID.
     *
     * @param storeId The ID of the store to load.
     * @return The Store object loaded from the database without its sales.
     */
    private static Store loadRawStore(int storeId) {
        if ((instance != null) && !instance.getStoreMap().isEmpty()) {
            return instance.getStoreMap().get(storeId);
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Store store = null;

        String query = """
                select s.storeCode, s.managerId, s.addressId from Store s
                where storeId = ?
                """;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, storeId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String storeCode = rs.getString("storeCode");
                int managerId = rs.getInt("managerId");
                int addressId = rs.getInt("addressId");
                Address address = loadAddress(addressId);
                Person manager = loadPerson(managerId);
                store = new Store(storeId, storeCode, address, manager);
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading store {}: ", storeId, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return store;
    }

    /**
     * Loads a Store object from the database based on the given store code.
     *
     * @param storeCode The store code of the store to load.
     * @return The Store object loaded from the database without its sales.
     */
    private static Store loadRawStore(String storeCode) {

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Store store = null;

        String query = """
                select storeId, managerId, addressId from Store
                where storeCode = ?
                """;
        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, storeCode);
            rs = ps.executeQuery();
            if (rs.next()) {
                int storeId = rs.getInt("storeId");
                int managerId = rs.getInt("managerId");
                int addressId = rs.getInt("addressId");
                Address address = loadAddress(addressId);
                Person manager = loadPerson(managerId);
                store = new Store(storeId, storeCode, address, manager);
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading store {}: ", storeCode, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return store;
    }

    /**
     * Loads a Store object from the database based on the given store ID.
     *
     * @param storeId The ID of the store to load.
     * @return The Store object loaded from the database with its sales.
     */

    public static Store loadStore(int storeId) {
        Store store = loadRawStore(storeId);
        if (store != null) {
            updateSingleStoreWithSales(store);
        }
        return store;
    }

    /**
     * Loads a Store object from the database based on the given store code.
     *
     * @param storeCode The store code of the store to load.
     * @return The Store object loaded from the database with its sales.
     */

    public static Store loadStore(String storeCode) {
        Store store = loadRawStore(storeCode);
        if (store != null) {
            updateSingleStoreWithSales(store);
        }
        return store;
    }

    /**
     * Loads all Store objects from the database.
     *
     * @return A map of store IDs to Store objects.
     */
    public static Map<Integer, Store> loadAllStores() {
        if ((instance != null) && !instance.getStoreMap().isEmpty()) {
            return instance.getStoreMap();
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map <Integer, Store> storeMapResult = new HashMap<>();

        String query = """
                select storeId from Store;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Store store = loadRawStore(rs.getInt("storeId"));
                storeMapResult.put(rs.getInt("storeId"), store);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all stores: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        updateStoreMapFromSalesMap(storeMapResult);
        LOGGER.info("Successfully loaded {} stores", storeMapResult.size());
        return storeMapResult;
    }

    /**
     * Loads an Item object from the database based on the given item ID.
     *
     * @param itemId The ID of the item to load.
     * @return The Item object loaded from the database.
     */
    public static Item loadItem(int itemId) {
        if ((instance != null) && !instance.getItemMap().isEmpty()) {
            return instance.getItemMap().get(itemId);
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Item item = null;

        String query = """
                select itemId, uniqueCode, basePrice, name, type from Item
                where itemId = ?;
                """;

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String uniqueCode = rs.getString("uniqueCode");
                double basePrice = rs.getDouble("basePrice");
                String name = rs.getString("name");
                String type = rs.getString("type");

                // Switch case to determine the type of item sale
                item = switch (type) {
                    case "S" -> new Service(itemId, uniqueCode, name, basePrice);
                    case "D" -> new DataPlan(itemId, uniqueCode, name, basePrice);
                    case "V" -> new VoicePlan(itemId, uniqueCode, name, basePrice);
                    case "P" -> new ProductPurchase(itemId, uniqueCode, name, basePrice);
                    default -> null;
                };
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading item {}: ", itemId, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return item;
    }

    /**
     * Loads all Item objects from the database.
     *
     * @return A map of item IDs to Item objects.
     */
    public static Map<Integer, Item> loadAllItems() {
        if ((instance != null) && !instance.getItemMap().isEmpty()) {
            return instance.getItemMap();
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Item> itemMapResult = new HashMap<>();

        String query = """
                select itemId from Item;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Item item = loadItem(rs.getInt("itemId"));
                itemMapResult.put(rs.getInt("itemId"), item);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all items: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully loaded {} items", itemMapResult.size());
        return itemMapResult;
    }

    /**
     * Loads an Item object sold from the database based on the given item sale ID.
     *
     * @param itemSaleId The ID of the item sale to load.
     * @return The Item object sold loaded from the database.
     */
    public static Item loadItemSold(int itemSaleId) {
        if ((instance != null) && !instance.getItemSoldMap().isEmpty()) {
            return instance.getItemSoldMap().get(itemSaleId);
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Item item = null;

        String query = """
                select i.itemId, i.type, startDate, endDate, totalGb, totalHours, employeeId, totalPeriod, phoneNumber, isLease from Item i
                left join ItemSale its on its.itemId = i.itemId
                where its.itemSaleId = ?;
                """;
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, itemSaleId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String type = rs.getString("type");
                item = loadItem(rs.getInt("itemId"));

                if (type.equals("P") && rs.getBoolean("isLease")) {
                    item = new ProductLease(itemSaleId, item, rs.getString("startDate"), rs.getString("endDate"));
                } else {
                    // Switch case to determine the type of item sale to correctly instantiate the item instance.
                    item = switch (type) {
                        case "V" -> new VoicePlan(itemSaleId, item, rs.getString("phoneNumber"), rs.getDouble("totalPeriod"));
                        case "S" -> new Service(itemSaleId, item, rs.getDouble("totalHours"), loadPerson(rs.getInt("employeeId")));
                        case "D" -> new DataPlan(itemSaleId, item, rs.getDouble("totalGb"));
                        default -> item;
                    };
                }
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading item sold {}: ", itemSaleId, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return item;
    }

    /**
     * Loads all Item objects sold from the database.
     *
     * @return A map of item sale IDs to Item objects sold.
     */
    public static Map<Integer, Item> loadAllItemSold() {
        if ((instance != null) && !instance.getItemSoldMap().isEmpty()) {
            return instance.getItemSoldMap();
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Item> itemMapResult = new HashMap<>();

        String query = """
                select itemSaleId from ItemSale;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Item itemSold = loadItemSold(rs.getInt("itemSaleId"));
                itemMapResult.put(rs.getInt("itemSaleId"), itemSold);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all item sold: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Loaded {} item sold", itemMapResult.size());
        return itemMapResult;
    }

    /**
     * Loads a Sale object from the database based on the given sale ID.
     *
     * @param saleId The ID of the sale to load.
     * @return The Sale object loaded from the database.
     */
    public static Sale loadSale(int saleId) {
        if ((instance != null) && !instance.getSalesMap().isEmpty()) {
            return instance.getSalesMap().get(saleId);
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Sale sale = null;

        String query = """
                select uniqueCode, saleDate, customerId, salesmanId, storeId from Sale
                where Sale.saleId = ?;
                """;

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, saleId);
            rs = ps.executeQuery();

            if (rs.next()) {
                String uniqueCode = rs.getString("uniqueCode");
                String saleDate = rs.getString("saleDate");
                Person customer = loadPerson(rs.getInt("customerId"));
                Person salesman = loadPerson(rs.getInt("salesmanId"));
                Store store = loadRawStore(rs.getInt("storeId"));
                sale = new Sale(saleId, uniqueCode, store, customer, salesman, saleDate);

                loadItemSale(sale);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading sale {}: ", saleId, e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return sale;
    }

    /**
     * Loads a Sale object from the database based on the given sale unique code.
     *
     * @param uniqueCode The unique Code of the sale to load.
     * @return The Sale object loaded from the database.
     */
    public static Sale loadSale(String uniqueCode) {

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Sale sale = null;

        String query = """
                select saleId, saleDate, customerId, salesmanId, storeId from Sale
                where uniqueCode = ?;
                """;

        try {
            ps = conn.prepareStatement(query);
            ps.setString(1, uniqueCode);
            rs = ps.executeQuery();

            if (rs.next()) {
                int saleId = rs.getInt("saleId");
                String saleDate = rs.getString("saleDate");
                Person customer = loadPerson(rs.getInt("customerId"));
                Person salesman = loadPerson(rs.getInt("salesmanId"));
                Store store = loadRawStore(rs.getInt("storeId"));
                sale = new Sale(saleId, uniqueCode, store, customer, salesman, saleDate);

                loadItemSale(sale);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading sale {}: ", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return sale;
    }

    private static void loadItemSale(Sale sale){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "select itemSaleId from ItemSale where saleId = ?";
        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, sale.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                Item item = loadItemSold(rs.getInt("itemSaleId"));
                sale.addItem(item);
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading item to sale {}: ", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
    }
    /**
     * Loads all Sale objects from the database.
     *
     * @return A map of sale IDs to Sale objects.
     */
    public static Map<Integer, Sale> loadAllSales() {
        if ((instance != null) && !instance.getItemSoldMap().isEmpty()) {
            return instance.getSalesMap();
        }
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Sale> saleMapResult = new HashMap<>();

        String query = """
                select saleId from Sale;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sale sale = loadSale(rs.getInt("saleId"));
                saleMapResult.put(rs.getInt("saleId"), sale);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all sales: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully loaded {} sales", saleMapResult.size());
        return saleMapResult;
    }

    /**
     * Updates the store map from the sales map.
     *
     * @param storesMap The map of store IDs to Store objects.
     */
    private static void updateStoreMapFromSalesMap(Map<Integer, Store> storesMap) {
        List<Sale> salesList = instance.getSalesList();

        for (Sale sale : salesList) {
            Store store = sale.getStore();
            storesMap.get(store.getId()).addSale(sale);
        }
        LOGGER.info("Successfully parsed sales into stores");
    }

    private static void updateSingleStoreWithSales(Store store) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = """
                    select saleId from Sale
                    where storeId = ?;
                    """;

        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, store.getId());
            rs = ps.executeQuery();
            while (rs.next()) {
                Sale sale = loadSale(rs.getInt("saleId"));
                store.addSale(sale);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading store: id {} - storeCode {}", store.getId(), store.getStoreCode());
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully filled store {} - storeCode {} with sales", store.getId(), store.getStoreCode());
    }

    public static Map<String, Sale> saleMapWithSaleCodeKey(){
//        if ((instance != null) && !instance.sak().isEmpty()) {
//            new HashMap<>(saleMap);
//        }

        Map<String, Sale> newSaleMap = new HashMap<>();

        for (Sale sale : instance.getSalesList()){
            newSaleMap.put(sale.getUniqueCode(), sale);
        }
        return newSaleMap;
    }

    public static Map<String, Item> itemMapWithItemCodeKey(){
        DataOasis instance = DataOasis.getInstance();

        Map<String, Item> newItemMap = new HashMap<>();

        for (Item item : instance.getItemSoldMap().values()){
            newItemMap.put(item.getUniqueCode(), item);
        }
        return newItemMap;
    }

    public static Map<String, Person> personMapWithUuidKey(){
//        if (!personMap.isEmpty()) {
//            new HashMap<>(personMap);
//        }

        Map<String, Person> newPersonMap = new HashMap<>();

        for (Person person : instance.getPersonsList()){
            newPersonMap.put(person.getUuid(), person);
        }
        return newPersonMap;
    }
}