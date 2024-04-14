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
import java.util.HashMap;
import java.util.Map;

/**
 * This class provides methods to load data from the database into memory objects.
 */
public class DatabaseLoader {

    private static final Logger LOGGER = LogManager.getLogger(DatabaseLoader.class);

    // Configure the Logger
    static {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.INFO);
    }

    /**
     * Loads an Address object from the database based on the given address ID.
     *
     * @param addressId The ID of the address to load.
     * @return The Address object loaded from the database.
     */
    public static Address loadAddress(int addressId) {
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
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Address> addressMap = new HashMap<>();

        String query = """
                select addressId from Address;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Address address = loadAddress(rs.getInt("addressId"));
                addressMap.put(rs.getInt("addressId"), address);
            }

        } catch (SQLException e) {
            LOGGER.error("Error parsing all addresses", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Loaded {} addresses", addressMap.size());
        return addressMap;
    }

    /**
     * Loads a Person object from the database based on the given person ID.
     *
     * @param personId The ID of the person to load.
     * @return The Person object loaded from the database.
     */
    public static Person loadPerson(int personId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Person person = null;

        String query = """
                select uuid, firstName, lastName, addressId, e.address from Person p
                left join Email e on p.personId = e.personId
                where p.personId = ?;
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
                person = new Person(personId, uuid, firstName, lastName, address);
                // Adding e-mails to the person list of e-mails
                String email;
                if ((email = rs.getString("address")) != null) {
                    person.addEmail(email);
                    while (rs.next()) {
                        email = rs.getString("address");
                        person.addEmail(email);
                    }
                }
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
     * Loads all Person objects from the database.
     *
     * @return A map of person IDs to Person objects.
     */
    public static Map<Integer, Person> loadAllPersons() {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Person> personMap = new HashMap<>();

        String query = """
                select personId from Person;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Person person = loadPerson(rs.getInt("personId"));
                personMap.put(rs.getInt("personId"), person);
            }
        } catch (SQLException e) {
            LOGGER.error("Error loading all persons: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully loaded {} persons", personMap.size());
        return personMap;
    }

    /**
     * Loads a Store object from the database based on the given store ID.
     *
     * @param storeId The ID of the store to load.
     * @return The Store object loaded from the database without its sales.
     */
    private static Store loadRawStore(int storeId) {
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
     * Loads a Store object from the database based on the given store ID.
     *
     * @param storeId The ID of the store to load.
     * @return The Store object loaded from the database with its sales.
     */

    public static Store loadStore(int storeId) {
        Store store = loadRawStore(storeId);
        updateSingleStoreWithSales(store);
        return store;
    }

    /**
     * Loads all Store objects from the database.
     *
     * @return A map of store IDs to Store objects.
     */
    public static Map<Integer, Store> loadAllStores() {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Store> storeMap = new HashMap<>();

        String query = """
                select storeId from Store;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Store store = loadRawStore(rs.getInt("storeId"));
                storeMap.put(rs.getInt("storeId"), store);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all stores: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully loaded {} stores", storeMap.size());
        updateStoreMapFromSalesMap(storeMap);
        return storeMap;
    }

    /**
     * Loads an Item object from the database based on the given item ID.
     *
     * @param itemId The ID of the item to load.
     * @return The Item object loaded from the database.
     */
    public static Item loadItem(int itemId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Item item = null;

        String query = """
                select Item.itemId, uniqueCode, basePrice, name, type, startDate from Item
                left join ItemSale on Item.itemId = ItemSale.itemId
                where Item.itemId = ?;
                """;

        try {
            ps = conn.prepareStatement(query);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();
            if (rs.next()) {
                String uniqueCode = rs.getString("uniqueCode");
                float basePrice = rs.getFloat("basePrice");
                String name = rs.getString("name");
                String type = rs.getString("type");

                // Switch case to determine the type of item sale
                item = switch (type) {
                    case "S" -> new Service(itemId, uniqueCode, name, basePrice);
                    case "D" -> new DataPlan(itemId, uniqueCode, name, basePrice);
                    case "V" -> new VoicePlan(itemId, uniqueCode, name, basePrice);
                    default -> null;
                };

                if (rs.getString("startDate") != null && type.equals("L")) {
                    item = new ProductLease(itemId, uniqueCode, name, basePrice);
                } else if (type.equals("P")) {
                    item = new ProductPurchase(itemId, uniqueCode, name, basePrice);
                }

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
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Item> itemMap = new HashMap<>();

        String query = """
                select itemId from Item;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Item item = loadItem(rs.getInt("itemId"));
                itemMap.put(rs.getInt("itemId"), item);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all items: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully loaded {} items", itemMap.size());
        return itemMap;
    }

    /**
     * Loads an Item object sold from the database based on the given item sale ID.
     *
     * @param itemSaleId The ID of the item sale to load.
     * @return The Item object sold loaded from the database.
     */
    public static Item loadItemSold(int itemSaleId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Item item = null;

        String query = """
                select i.itemId, type, startDate, endDate, totalGb, totalHours, employeeId, totalPeriod, phoneNumber from Item i
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

                // Switch case to determine the type of item sale to correctly instantiate the item instance.
                item = switch (type) {
                    case "L" -> new ProductLease(itemSaleId, item, rs.getString("startDate"), rs.getString("endDate"));
                    case "V" ->
                            new VoicePlan(itemSaleId, item, rs.getString("phoneNumber"), rs.getDouble("totalPeriod"));
                    case "S" ->
                            new Service(itemSaleId, item, rs.getDouble("totalHours"), loadPerson(rs.getInt("employeeId")));
                    case "D" -> new DataPlan(itemSaleId, item, rs.getDouble("totalGb"));
                    default -> item;
                };
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
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Item> itemMap = new HashMap<>();

        String query = """
                select itemSaleId from ItemSale;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Item itemSold = loadItemSold(rs.getInt("itemSaleId"));
                itemMap.put(rs.getInt("itemSaleId"), itemSold);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all item sold: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Loaded {} item sold", itemMap.size());
        return itemMap;
    }

    /**
     * Loads a Sale object from the database based on the given sale ID.
     *
     * @param saleId The ID of the sale to load.
     * @return The Sale object loaded from the database.
     */
    public static Sale loadSale(int saleId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Sale sale = null;

        String query = """
                select uniqueCode, saleDate, customerId, salesmanId, storeId, itemSaleId from Sale
                left join ItemSale on ItemSale.saleId = Sale.saleId
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

                //Load items of the sale
                Item item;
                if ((item = loadItemSold(rs.getInt("itemSaleId"))) != null) {
                    sale.addItem(item);
                    while (rs.next()) {
                        item = loadItemSold(rs.getInt("itemSaleId"));
                        sale.addItem(item);
                    }
                }
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
     * Loads all Sale objects from the database.
     *
     * @return A map of sale IDs to Sale objects.
     */
    public static Map<Integer, Sale> loadAllSales() {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Sale> saleMap = new HashMap<>();

        String query = """
                select saleId from Sale;
                """;
        try {
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                Sale sale = loadSale(rs.getInt("saleId"));
                saleMap.put(rs.getInt("saleId"), sale);
            }

        } catch (SQLException e) {
            LOGGER.error("Error loading all sales: ", e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        LOGGER.info("Successfully loaded {} sales", saleMap.size());
        return saleMap;
    }

    /**
     * Updates the store map from the sales map.
     *
     * @param storesMap The map of store IDs to Store objects.
     */
    private static void updateStoreMapFromSalesMap(Map<Integer, Store> storesMap) {
        Map<Integer, Sale> salesMap = loadAllSales();
        for (Sale sale : salesMap.values()) {
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
}