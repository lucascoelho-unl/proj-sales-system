package unl.soc;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import java.sql.*;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This is a collection of utility methods that define a general API for
 * interacting with the database supporting this application.
 *
 */
public class SalesData {
    public static void main(String[] args) {

        DatabaseLoader.loadAllItemSold();
    }
    private static final Logger LOGGER = LogManager.getLogger(SalesData.class);

    static {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.INFO);
    }

    /**
     * Removes all records from all tables in the database.
     */
    public static void clearDatabase() {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;

        try {
            // Truncate tables
            List<String> tablesToTruncate = Arrays.asList("ItemSale", "Item", "Sale", "Store", "Email", "Person", "Address", "Zipcode", "State");
            for (String table : tablesToTruncate) {
                String truncate = "delete from " + table;
                ps = conn.prepareStatement(truncate);
                ps.execute();
                LOGGER.debug("Table cleared: {}", table);
            }
        } catch (SQLException e) {
            LOGGER.error("Error cleaning database: {}", e.getMessage());
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }

    }

    /**
     * Method to add a person record to the database with the provided data.
     *
     * @param personUuid
     * @param firstName
     * @param lastName
     * @param street
     * @param city
     * @param state
     * @param zip
     */
    public static void addPerson(String personUuid, String firstName, String lastName, String street, String city, String state, String zip) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = "insert into Person (uuid, firstName, lastName, addressId) values (?, ?, ?, ?);";

        try{
            int addressId = selectOrInsertAddress(new Address(street,city,state,Integer.parseInt(zip)));
            ps = conn.prepareStatement(query);
            ps.setString(1,personUuid);
            ps.setString(2,firstName);
            ps.setString(3,lastName);
            ps.setInt(4, addressId);
            ps.executeUpdate();
            LOGGER.info("Added Person : {}", personUuid);
        } catch (SQLException e) {
            LOGGER.error("Error in the connection: {}. Could not add a personx", e.getMessage());
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
    }

    /**
     * Adds an email record corresponding person record corresponding to the
     * provided <code>personUuid</code>
     *
     * @param personUuid
     * @param email
     */
    public static void addEmail(String personUuid, String email) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        try{
            Person person = DatabaseLoader.loadPerson(personUuid);
            if(person == null) {
                throw new SQLException("Person not found");
            }
            String insert = """
                    insert into Email (address, personId) values (?, ?)
                    """;
            ps = conn.prepareStatement(insert);
            ps.setString(1, email);
            ps.setInt(2, person.getId());
            ps.executeUpdate();
            LOGGER.info("Added Email : {}", email);
        } catch (SQLException e) {
            LOGGER.error("Error Adding Email: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds a store record to the database managed by the person identified by the
     * given code.
     *
     * @param storeCode
     * @param managerCode
     * @param street
     * @param city
     * @param state
     * @param zip
     */
    public static void addStore(String storeCode, String managerCode, String street, String city, String state, String zip) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String insert = "insert into Store (storeCode, managerId, addressId) values (?, ?, ?);";

        try {
            Person manager = DatabaseLoader.loadPerson(managerCode);
            if(manager == null) {
                throw new SQLException("Manager not found");
            }
            int addressId = selectOrInsertAddress(new Address(street,city,state,Integer.parseInt(zip)));
            ps = conn.prepareStatement(insert);
            ps.setString(1,storeCode);
            ps.setInt(2, manager.getId());
            ps.setInt(3, addressId);
            ps.executeUpdate();
            LOGGER.info("Added Store : {}", storeCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding Store: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds an item record to the database of the given <code>type</code> with the
     * given <code>code</code>, <code>name</code> and <code>basePrice</code>.
     *
     * Valid values for the <code>type</code> will be <code>"Product"</code>,
     * <code>"Service"</code>, <code>"Data"</code>, or <code>"Voice"</code>.
     *
     * @param itemCode
     * @param name
     * @param type
     * @param basePrice
     */
    public static void addItem(String itemCode, String name, String type, double basePrice) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String insert = "insert into Item (uniqueCode, name, type, basePrice) values (?, ?, ?, ?);";

        try {
            ps = conn.prepareStatement(insert);
            ps.setString(1, itemCode);
            ps.setString(2, name);
            ps.setString(3, type);
            ps.setDouble(4, basePrice);
            ps.executeUpdate();
            LOGGER.info("Added Item : {}", itemCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding Item: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds an Sale record to the database with the given data.
     *
     * @param saleCode
     * @param storeCode
     * @param customerPersonUuid
     * @param salesPersonUuid
     * @param saleDate
     */
    public static void addSale(String saleCode, String storeCode, String customerPersonUuid, String salesPersonUuid, String saleDate) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;

        String insert = "insert into Sale (uniqueCode, saleDate, customerId, salesmanId, storeId) values (?, ?, ?, ?, ?);";

        try {
            Store store = DatabaseLoader.loadStore(storeCode);
            if(store == null) {
                throw new SQLException("Store not found");
            }

            Person customer = DatabaseLoader.loadPerson(customerPersonUuid);
            if(customer == null) {
                throw new SQLException("Customer not found");
            }

            Person salePerson = DatabaseLoader.loadPerson(salesPersonUuid);
            if(salePerson == null) {
                throw new SQLException("Sale person not found");
            }
            ps = conn.prepareStatement(insert);
            ps.setString(1, saleCode);
            ps.setString(2, saleDate);
            ps.setInt(3, customer.getId());
            ps.setInt(4, salePerson.getId());
            ps.setInt(5, store.getId());
            ps.executeUpdate();

            LOGGER.info("Added Sale : {}", saleCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding Sale: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds a particular product (identified by <code>itemCode</code>) to a
     * particular sale (identified by <code>saleCode</code>).
     *
     * @param saleCode
     * @param itemCode
     */
    public static void addProductToSale(String saleCode, String itemCode) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;

        Map<String, Sale> saleMap = DatabaseLoader.saleMapWithSaleCodeKey();
        Map<String, Item> itemMap = DatabaseLoader.itemMapWithItemCodeKey();

        String insert = "insert into ItemSale (itemId, saleId) values (?, ?);";

        try {
            Item item = itemMap.get(itemCode);
            if (item == null) {
                throw new SQLException("Item not found");
            }

            Sale sale = saleMap.get(saleCode);
            if(sale == null) {
                throw new SQLException("Sale not found");
            }

            ps = conn.prepareStatement(insert);
            ps.setInt(1, item.getId());
            ps.setInt(2, sale.getId());
            ps.executeUpdate();
            LOGGER.info("Added Product Purchase to ItemSale: {}", itemCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding Product Purchase to ItemSale: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds a particular leased (identified by <code>itemCode</code>) to a
     * particular sale (identified by <code>saleCode</code>) with the start/end date
     * specified.
     *
     * @param saleCode
     * @param startDate
     * @param endDate
     */
    public static void addLeaseToSale(String saleCode, String itemCode, String startDate, String endDate) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String, Sale> saleMap = DatabaseLoader.saleMapWithSaleCodeKey();
        Map<String, Item> itemMap = DatabaseLoader.itemMapWithItemCodeKey();

        String insert = "insert into ItemSale (itemId, saleId, startDate, endDate) values (?, ?, ?, ?);";

        try {
            Item item = itemMap.get(itemCode);
            if (item == null) {
                throw new SQLException("Item not found");
            }

            Sale sale = saleMap.get(saleCode);
            if(sale == null) {
                throw new SQLException("Sale not found");
            }

            if (startDate.length() != 10 || endDate.length() != 10) {
                throw new SQLException("Start date and End date must be in ISO 8601 format");
            }

            if (startDate.compareTo(endDate) > 0) {
                throw new SQLException("Start date cannot be after End date");
            }

            ps = conn.prepareStatement(insert);
            ps.setInt(1, item.getId());
            ps.setInt(2, sale.getId());
            ps.setString(3, startDate);
            ps.setString(4, endDate);
            ps.executeUpdate();
            LOGGER.info("Added Product Lease to ItemSale: {}", itemCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding Product Lease to Sale: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds a particular service (identified by <code>itemCode</code>) to a
     * particular sale (identified by <code>saleCode</code>) with the specified
     * number of hours. The service is done by the employee with the specified
     * <code>servicePersonUuid</code>
     *
     * @param saleCode
     * @param itemCode
     * @param billedHours
     * @param servicePersonUuid
     */
    public static void addServiceToSale(String saleCode, String itemCode, double billedHours, String servicePersonUuid) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String, Person> personMap = DatabaseLoader.personMapWithUuidKey();
        Map<String, Sale> saleMap = DatabaseLoader.saleMapWithSaleCodeKey();
        Map<String, Item> itemMap = DatabaseLoader.itemMapWithItemCodeKey();

        String insert = "insert into ItemSale (itemId, saleId, totalHours, employeeId) values (?, ?, ?, ?);";

        try {
            Person employee = personMap.get(servicePersonUuid);
            if(employee == null) {
                throw new SQLException("Employee not found");
            }

            Item item = itemMap.get(itemCode);
            if (item == null) {
                throw new SQLException("Item not found");
            }

            Sale sale = saleMap.get(saleCode);
            if(sale == null) {
                throw new SQLException("Sale not found");
            }

            if (billedHours <= 0) {
                throw new SQLException("Billed hours cannot be negative or 0");
            }

            ps = conn.prepareStatement(insert);
            ps.setInt(1, item.getId());
            ps.setInt(2, sale.getId());
            ps.setDouble(3, billedHours);
            ps.setInt(4, employee.getId());
            ps.executeUpdate();
            LOGGER.info("Added Service to ItemSale: {}", itemCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding Service to Sale: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds a particular data plan (identified by <code>itemCode</code>) to a
     * particular sale (identified by <code>saleCode</code>) with the specified
     * number of gigabytes.
     *
     * @param saleCode
     * @param itemCode
     * @param gbs
     */
    public static void addDataPlanToSale(String saleCode, String itemCode, double gbs) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String, Sale> saleMap = DatabaseLoader.saleMapWithSaleCodeKey();
        Map<String, Item> itemMap = DatabaseLoader.itemMapWithItemCodeKey();

        String insert = "insert into ItemSale (itemId, saleId, totalGb) values (?, ?, ?);";

        try {
            Item item = itemMap.get(itemCode);
            if (item == null) {
                throw new SQLException("Item not found");
            }

            Sale sale = saleMap.get(saleCode);
            if(sale == null) {
                throw new SQLException("Sale not found");
            }

            if (gbs <= 0) {
                throw new SQLException("Gigabytes cannot be negative or 0");
            }

            ps = conn.prepareStatement(insert);
            ps.setInt(1, item.getId());
            ps.setInt(2, sale.getId());
            ps.setDouble(3, gbs);
            ps.executeUpdate();
            LOGGER.info("Added DataPlan to ItemSale: {}", itemCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding DataPlan to Sale: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Adds a particular voice plan (identified by <code>itemCode</code>) to a
     * particular sale (identified by <code>saleCode</code>) with the specified
     * <code>phoneNumber</code> for the given number of <code>days</code>.
     *
     * @param saleCode
     * @param itemCode
     * @param phoneNumber
     * @param days
     */
    public static void addVoicePlanToSale(String saleCode, String itemCode, String phoneNumber, int days) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        Map<String, Sale> saleMap = DatabaseLoader.saleMapWithSaleCodeKey();
        Map<String, Item> itemMap = DatabaseLoader.itemMapWithItemCodeKey();

        String insert = "insert into ItemSale (itemId, saleId, totalPeriod, phoneNumber) values (?, ?, ?, ?);";

        try {
            Item item = itemMap.get(itemCode);
            if (item == null) {
                throw new SQLException("Item not found");
            }

            Sale sale = saleMap.get(saleCode);
            if(sale == null) {
                throw new SQLException("Sale not found");
            }

            if (days <= 0) {
                throw new SQLException("Days cannot be negative or 0");
            }

            ps = conn.prepareStatement(insert);
            ps.setInt(1, item.getId());
            ps.setInt(2, sale.getId());
            ps.setInt(3, days);
            ps.setString(4, phoneNumber);
            ps.executeUpdate();
            LOGGER.info("Added DataPlan to ItemSale: {}", itemCode);
        } catch (SQLException e) {
            LOGGER.error("Error Adding DataPlan to Sale: {}", e.getMessage());
        } finally {
            ConnFactory.closeConnection(ps, conn);
        }
    }

    /**
     * Selects or inserts a state into the database.
     *
     * @param state The state to select or insert.
     * @return The ID of the state in the database.
     */
    private static int selectOrInsertState(String state) {
        if (state.isEmpty()) {
            throw new RuntimeException("Invalid state");
        }

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = """
                select stateId from State
                where state = ?
                """;

        try {
            //Return id if already exist
            ps = conn.prepareStatement(query);
            ps.setString(1, state);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("stateId");
            }

            //If do not exist, create
            query = """
                    insert into State (state) values (?);
                    """;
            ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, state);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in the connection: {}", e.toString());
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return -1;
    }

    /**
     * Selects or inserts a zipcode into the database.
     *
     * @param zipcode The zipcode to select or insert.
     * @param stateId The ID of the state associated with the zipcode.
     * @return The ID of the zipcode in the database.
     */
    private static int selectOrInsertZipcode(int zipcode, int stateId) {
        if (zipcode % 10000 < 0) {
            throw new RuntimeException("Invalid zipcode");
        }

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = """
                select zipcodeId from Zipcode
                where zipcode = ? and stateId = ?;
                """;

        try {
            //Return id if already exist
            ps = conn.prepareStatement(query);
            ps.setInt(1, zipcode);
            ps.setInt(2, stateId);
            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("zipcodeId");
            }

            //If do not exist, create
            String insert = """
                    insert into Zipcode (zipcode, stateId)
                    values (?, ?);
                    """;
            ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps.setInt(1, zipcode);
            ps.setInt(2, stateId);
            ps.executeUpdate();
            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in the connection: {}", e.toString());
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return -1;
    }

    /**
     * Selects or inserts an address into the database.
     *
     * @param address The address to select or insert.
     * @return The ID of the address in the database.
     */
    private static int selectOrInsertAddress(Address address) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        //Query for existence address
        String query = """
                select addressId from Address
                where street = ? and zipcodeId = ? and city = ?;
                """;

        try {
            //Return id if already exist
            ps = conn.prepareStatement(query);
            ps.setString(1, address.getStreet());
            int stateId = selectOrInsertState(address.getState());
            int zipcodeId = selectOrInsertZipcode(address.getZipCode(), stateId);
            ps.setInt(2, zipcodeId);
            ps.setString(3, address.getCity());

            rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt("addressId");
            }

            //If do not exist, create
            String insert = """
                    insert into Address (street, city, zipcodeId)
                    values(?,?,?)
                    """;
            ps = conn.prepareStatement(insert, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setInt(3, zipcodeId);
            ps.executeUpdate();

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            LOGGER.error("Error in the connection: {}", e.toString());
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return -1;
    }

}