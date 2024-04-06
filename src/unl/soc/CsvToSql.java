package unl.soc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.ConnectionFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import io.github.cdimascio.dotenv.Dotenv;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Objects;


public class CsvToSql {
    public static void main(String[] args) {
        try (Connection conn = getDataSource().getConnection()) {
//            tempFunction(conn);
            cleanDB(conn);
            createDB(conn);
            fillDB(conn);
        } catch (SQLException e){
           System.err.println(e);
        }
    }


    private static DataSource getDataSource() {
        BasicDataSource dataSource = new BasicDataSource();
        Dotenv dotenv = Dotenv.configure().load();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + dotenv.get("SERVER_NAME") + ":"+ dotenv.get("PORT_NUMBER") + "/" + dotenv.get("DATABASE_NAME"));
        dataSource.setUsername(dotenv.get("USERNAME"));
        dataSource.setPassword(dotenv.get("PASSWORD"));
        return dataSource;
    }

    public static void insertAddressToDB(Connection conn) throws SQLException {
        String insertStatement = "insert into Address (street, city, state, zipCode) values (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(insertStatement);

        List<Person> people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        List<Store> stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        for (Person person : people) {
            Address address = person.getAddress();
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getState());
            ps.setInt(4, address.getZipCode());
            ps.addBatch();
        }
        for (Store store : stores) {
            Address address = store.getAddress();
            ps.setString(1, address.getStreet());
            ps.setString(2, address.getCity());
            ps.setString(3, address.getState());
            ps.setInt(4, address.getZipCode());
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    public static void insertPersonToDB(Connection conn) throws SQLException {
        String statement = "insert into Person (uuid, firstName, lastName, addressId) values (?,?,?,?)";
        PreparedStatement ps = conn.prepareStatement(statement);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        List<Person> people = DataProcessor.readPersonCSVtoList("data/Persons.csv");
        for (Person person : people) {
            Address address = person.getAddress();

            ps.setString(1, person.getUuid());
            ps.setString(2, person.getFirstName());
            ps.setString(3, person.getLastName());
            try {
                String addressIdQuery = "select addressId from Address where zipCode=? and street=?";
                Integer addressId = jdbcTemplate.queryForObject(addressIdQuery, Integer.class, address.getZipCode(), address.getStreet());
                if (addressId == null) {
                    throw new NullPointerException();
                }
                ps.setInt(4, addressId);
            } catch (SQLException e) {
                ps.setNull(4, Types.INTEGER);
            }
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    public static void insertEmailToDB(Connection conn) throws SQLException {
        String emailInsert = "insert into Email(address, personId) values (?,?)";
        PreparedStatement ps = conn.prepareStatement(emailInsert);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        List<Person> people = DataProcessor.readPersonCSVtoList("data/Persons.csv");

        for (Person person : people) {
            List<String> emailList = person.getEmailList();

            //Get Person
            String personQuery = "select personId from Person where uuid=?";
            Integer personId = jdbcTemplate.queryForObject(personQuery, Integer.class, person.getUuid());

            for (String email : emailList) {
                ps.setString(1, email);
                if (personId == null) {
                    throw new NullPointerException();
                }
                ps.setInt(2, personId);
                ps.addBatch();
            }
        }
        ps.executeBatch();
        ps.close();
    }

    public static void insertStoreDB(Connection conn) throws SQLException, DataAccessException {
        String insertQuery = "insert into Store (storeCode, managerId, addressId) values (?,?,?)";
        PreparedStatement ps = conn.prepareStatement(insertQuery);
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        List<Store> stores = DataProcessor.readStoreCSVtoList("data/Stores.csv");

        for (Store store : stores) {
            String storeCode = store.getStoreCode();
            Person manager = store.getManager();
            Address address = store.getAddress();

            //Get Manager and Address
            String managerQuery = "select personId from Person where uuid=?";
            String addressQuery = "select addressId from Address where street=? and zipCode=?";

            Integer managerId = jdbcTemplate.queryForObject(managerQuery, Integer.class, manager.getUuid());
            Integer addressId = jdbcTemplate.queryForObject(addressQuery, Integer.class, address.getStreet(), address.getZipCode());

            ps.setString(1, storeCode);
            if (managerId == null) {
                throw new NullPointerException();
            }
            ps.setInt(2, managerId);
            if (addressId == null) {
                throw new NullPointerException();
            }
            ps.setInt(3, addressId);
            ps.addBatch();
        }
        ps.executeBatch();
        ps.close();
    }

    public static void itemToSql(Connection conn) {
        List<Item> itemList = DataProcessor.readItemsCSVtoList("data/Items.csv");
        try{
            String insertItem = "insert into Item(uniqueCode, name, basePrice) values (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(insertItem);
            for (Item item : itemList){
                ps.setString(1, item.getUniqueCode());
                ps.setString(2, item.getName());
                ps.setDouble(3, item.getBasePrice());
                ps.addBatch();
            }
            ps.executeBatch();
            ps.close();
        } catch (SQLException e){
            System.err.println(e);
        }
    }

    public static void saleSql(Connection conn) throws SQLException {
        PreparedStatement ps;
        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());
        List<Sale> saleList = DataProcessor.rawSalesList("data/Sales.csv");

        for (Sale sale : saleList) {
            String uniqueCode = sale.getUniqueCode();
            String saleDate = sale.getDateTime().toString();
            Person customer = sale.getCustomer();
            Person salesman = sale.getSalesman();
            Store store = sale.getStore();

            //Get customer and salesman
            String personQuery = "select personId from Person where uuid=?";
            Integer customerId = jdbcTemplate.queryForObject(personQuery, Integer.class, customer.getUuid());
            Integer salesmanId = jdbcTemplate.queryForObject(personQuery, Integer.class, salesman.getUuid());

            //Get store
            String storeQuery = "select storeId from Store where storeCode=?";
            Integer storeId = jdbcTemplate.queryForObject(storeQuery, Integer.class, store.getStoreCode());

            String insertSQL = "insert into Sale (uniqueCode, saleDate, customerId, salesmanId, storeId) values (?,?,?,?,?)";
            ps = conn.prepareStatement(insertSQL);
            ps.setString(1, uniqueCode);
            ps.setString(2, saleDate);
            if (customerId == null) {
                throw new NullPointerException();
            }
            ps.setInt(3, customerId);
            if (salesmanId == null) {
                throw new NullPointerException();
            }
            ps.setInt(4, salesmanId);
            if (storeId == null) {
                throw new NullPointerException();
            }
            ps.setInt(5, storeId);
            ps.executeUpdate();
        }
    }

    public static void itemSaleSql(Connection conn) throws SQLException {

        JdbcTemplate jdbcTemplate = new JdbcTemplate(getDataSource());

        List<Sale> saleProcessedList = DataProcessor.processedSalesList("data/SaleItems.csv");

        for (Sale sale : saleProcessedList) {
            String saleIdQuery = "select saleId from Sale where uniqueCode=?";
            Integer saleId = jdbcTemplate.queryForObject(saleIdQuery, Integer.class, sale.getUniqueCode());
            if (saleId == null) {
                throw new NullPointerException();
            }

            List<Item> itemFromSaleList = sale.getItemsList();
            for (Item item : itemFromSaleList) {
                PreparedStatement ps;
                String itemType = DataProcessor.verifyItemType(item);
                String itemIdQuery = "select itemId from Item where uniqueCode=?";
                Integer itemId = jdbcTemplate.queryForObject(itemIdQuery, Integer.class, item.getUniqueCode());

                switch (Objects.requireNonNull(itemType)) {
                    case "V" -> {
                        VoicePlan itemVoicePlan = (VoicePlan) item;

                        String insertSQL = "insert into ItemSale (itemId, saleId, type, totalPeriod, phoneNumber) values (?,?,?,?,?)";
                        ps = conn.prepareStatement(insertSQL);

                        ps.setInt(1, itemId);
                        ps.setInt(2, saleId);
                        ps.setString(3,itemType);
                        ps.setDouble(4, itemVoicePlan.getTotalPeriod());
                        ps.setString(5, itemVoicePlan.getPhoneNumber());
                        ps.executeUpdate();
                        ps.close();
                    }
                    case "L" -> {
                        ProductLease itemLease = (ProductLease) item;

                        String insertSQL = "insert into ItemSale (itemId, saleId, type, startDate, endDate) values (?,?,?,?,?)";
                        ps = conn.prepareStatement(insertSQL);

                        ps.setInt(1, itemId);
                        ps.setInt(2, saleId);
                        ps.setString(3,itemType);
                        ps.setString(4, itemLease.getStartDate().toString());
                        ps.setString(5, itemLease.getEndDate().toString());
                        ps.executeUpdate();
                        ps.close();
                    }
                    case "D" -> {
                        DataPlan itemData = (DataPlan) item;

                        String insertSQL = "insert into ItemSale (itemId, saleId,type, totalGb) values (?,?,?,?)";
                        ps = conn.prepareStatement(insertSQL);
                        if (itemId == null) {
                            throw new NullPointerException();
                        }
                        ps.setInt(1, itemId);
                        ps.setInt(2, saleId);
                        ps.setString(3,itemType);
                        ps.setDouble(4,itemData.getTotalGB());
                        ps.executeUpdate();
                        ps.close();
                    }
                    case "S" -> {
                        Service itemService = (Service) item;

                        String employeeQueryId = "select personId from Person where uuid=?";
                        Integer employeeId = jdbcTemplate.queryForObject(employeeQueryId, Integer.class, itemService.getEmployee().getUuid());

                        String insertSQL = "insert into ItemSale (itemId, saleId, type, totalHours, employeeId) values (?,?,?,?,?)";
                        ps = conn.prepareStatement(insertSQL);
                        if (itemId == null) {
                            throw new NullPointerException();
                        }
                        ps.setInt(1, itemId);
                        ps.setInt(2, saleId);
                        ps.setString(3,itemType);
                        ps.setDouble(4, itemService.getTotalHours());
                        ps.setInt(5, employeeId);
                        ps.executeUpdate();
                        ps.close();
                    }
                    default -> {

                        String insertSQL = "insert into ItemSale (itemId, saleId, type) values (?,?,?)";
                        ps = conn.prepareStatement(insertSQL);
                        ps.setInt(1, itemId);
                        ps.setInt(2, saleId);
                        ps.setString(3,itemType);
                        ps.executeUpdate();
                        ps.close();
                    }
                }

            }
        }

    }


    public static void cleanDB(Connection conn) throws SQLException {
        String drop = "drop table if exists Email, ItemSale, Item, Sale, Store, Person, Address";
        PreparedStatement ps = conn.prepareStatement(drop);
        ps.execute();
        ps.close();
    }

    public static void createDB(Connection conn) throws SQLException {
//        PreparedStatement ps = conn.prepareStatement("create table if not exists Address(addressId int primary key not null auto_increment, street varchar(255) not null, city varchar(255) not null, state varchar(255) not null, zipCode int not null)");
        Statement ps = conn.createStatement();
        ps.addBatch("create table if not exists Address(addressId int primary key not null auto_increment, street varchar(255) not null, city varchar(255) not null, state varchar(255) not null, zipCode int not null)");
        ps.addBatch(
                "create table if not exists Person(" +
                        "personId int primary key not null auto_increment," +
                        "uuid varchar(255) not null," +
                        "firstName varchar(255)," +
                        "lastName varchar(255) not null," +
                        "addressId int not null," +
                        "FOREIGN KEY (addressId) references Address(addressId))");

        ps.addBatch(
                "create table if not exists Email(" +
                        "emailId int primary key not null auto_increment," +
                        "address varchar(255) not null," +
                        "personId int not null," +
                        "FOREIGN KEY (personId) references Person(personId))"
        );
        ps.addBatch(
                "create table if not exists Store(" +
                        "storeId int primary key not null auto_increment," +
                        "storeCode varchar(255) not null," +
                        "managerId int not null," +
                        "addressId int not null," +
                        "FOREIGN KEY (managerId) references Person(personId)," +
                        "FOREIGN KEY (addressId) references Address(addressId))"
        );
        ps.addBatch(
                "create table if not exists Sale(" +
                        "saleId int primary key not null auto_increment," +
                        "uniqueCode varchar(255) not null," +
                        "saleDate varchar(255)," +
                        "customerId int not null ," +
                        "salesmanId int not null ," +
                        "storeId int not null ," +
                        "FOREIGN KEY (customerId) references Person(personId)," +
                        "FOREIGN KEY (salesmanId) references Person(personId)," +
                        "FOREIGN KEY (storeId) references Store(storeId))"
        );
        ps.addBatch(
                "create table if not exists Item(" +
                        "itemId int primary key not null auto_increment," +
                        "uniqueCode varchar(255) not null," +
                        "name varchar(255) not null," +
                        "basePrice double not null)"
        );
        ps.addBatch(
                "create table if not exists ItemSale(" +
                        "itemSaleId int PRIMARY KEY auto_increment," +
                        "itemId int," +
                        "saleId int," +
                        "type varchar(1) not null," +
                        "startDate varchar(40)," +
                        "endDate varchar(40)," +
                        "totalGb double," +
                        "totalHours double," +
                        "employeeId int," +
                        "totalPeriod double," +
                        "phoneNumber varchar(40)," +
                        "FOREIGN KEY (itemId) references Item(itemId)," +
                        "FOREIGN KEY (saleId) references Sale(saleId)," +
                        "FOREIGN KEY (employeeId) references Person(personId));"
        );
        ps.executeBatch();
        ps.close();
    }

    public static void fillDB(Connection conn) throws SQLException {
        insertAddressToDB(conn);
        insertPersonToDB(conn);
        insertEmailToDB(conn);
        insertStoreDB(conn);
        itemToSql(conn);
        saleSql(conn);
        itemSaleSql(conn);
    }

}

