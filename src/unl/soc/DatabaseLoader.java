package unl.soc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseLoader {
    public static void main(String[] args) {
//        Address test = loadAddress(1);
//        System.out.println(test);
//
//        var itemMap = loadAllItem();
//        for (var item : itemMap.keySet()) {
//            System.out.println(item + ": " + itemMap.get(item));
//        }
//        Item i = loadItem(2);
        var it = loadItemSold(25);
        System.out.println(it);

        var itemMap = loadAllItemSold();
        for (var item : itemMap.keySet()) {
            System.out.println(item + ": " + itemMap.get(item));
        }

        var personMap = loadAllPersons();
        for (var person : personMap.keySet()) {
            System.out.println(person + ": " + personMap.get(person));
        }

    }

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
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, addressId);
            rs = ps.executeQuery();
            if (rs.next()){
                int zipcode = rs.getInt("zipcode");
                String state = rs.getString("state");
                String city = rs.getString("city");
                String street = rs.getString("street");
                address = new Address(addressId, street, city, state, zipcode);
            }

        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
         return address;
    }

    public static Map<Integer, Address> loadAllAddress(){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Address> addressMap = new HashMap<>();

        String query = """
                        select addressId from Address;
                        """;
        try{
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                Address address = loadAddress(rs.getInt("addressId"));
                addressMap.put(rs.getInt("addressId"), address);
            }

        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return addressMap;
    }

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
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, personId);
            rs = ps.executeQuery();
            if (rs.next()){
                String uuid = rs.getString("uuid");
                String firstName = rs.getString("firstName");
                String lastName = rs.getString("lastName");
                Address address = loadAddress(rs.getInt("addressId"));
                person = new Person(personId, uuid,firstName,lastName,address);
                // Adding e-mails to the person list of e-mails
                while (rs.next()) {
                    String email = rs.getString("e.address");
                    person.addEmail(email);
                }
            }
        } catch (SQLException e) {
            System.out.println("Error parsing person " + personId + ": " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return person;
    }

    public static Map<Integer, Person> loadAllPersons(){
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
            while (rs.next()){
                Person person = loadPerson(rs.getInt("personId"));
                personMap.put(rs.getInt("personId"), person);
            }
        } catch (SQLException e) {
            System.out.println("Error loading persons: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return personMap;
    }

    public static Store loadStore(int storeId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Store store = null;

        String query = """
                        select s.storeCode, s.managerId, s.addressId from Store s
                        where storeId = ?
                        """;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, storeId);
            rs = ps.executeQuery();
            if (rs.next()){
                String storeCode = rs.getString("storeCode");
                int managerId = rs.getInt("managerId");
                int addressId = rs.getInt("addressId");
                Address address = loadAddress(addressId);
                Person manager = loadPerson(managerId);
                store = new Store(storeId, storeCode, address, manager);
            }
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return store;
    }

    public static Map<Integer, Store> loadAllStores(){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Store> storeMap = new HashMap<>();

        String query = """
                        select storeId from Store;
                        """;
        try{
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                Store store = loadStore(rs.getInt("storeId"));
                storeMap.put(rs.getInt("storeId"), store);
            }

        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return storeMap;
    }

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

        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, itemId);
            rs = ps.executeQuery();
            if (rs.next()){
                String uniqueCode = rs.getString("uniqueCode");
                float basePrice = rs.getFloat("basePrice");
                String name = rs.getString("name");
                String type = rs.getString("type");

                item = switch (type) {
                    case "S" -> new Service(itemId, uniqueCode, name, basePrice);
                    case "D" -> new DataPlan(itemId, uniqueCode, name, basePrice);
                    case "V" -> new VoicePlan(itemId, uniqueCode, name, basePrice);
                    default -> null;
                };

                if (rs.getString("startDate") != null && type.equals("L")){
                    item = new ProductLease(itemId, uniqueCode, name, basePrice);
                } else if (type.equals("P")) {
                    item = new ProductPurchase(itemId, uniqueCode, name, basePrice);
                }

            }
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return item;
    }

    public static Map<Integer, Item> loadAllItems(){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Item> itemMap = new HashMap<>();

        String query = """
                        select itemId from Item;
                        """;
        try{
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                Item item = loadItem(rs.getInt("itemId"));
                itemMap.put(rs.getInt("itemId"), item);
            }

        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return itemMap;
    }

    public static Item loadItemSold(int itemSaleId){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Item item = null;

        String query = """
                       select i.itemId, type, startDate, endDate, totalGb, totalHours, employeeId, totalPeriod, phoneNumber from Item i
                       left join ItemSale its on its.itemId = i.itemId
                       where its.itemSaleId = ?;
                       """;

        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, itemSaleId);
            rs = ps.executeQuery();
            if (rs.next()){
                String type = rs.getString("type");
                item = loadItem(rs.getInt("itemId"));
                item = switch (type) {
                    case "L" ->
                            new ProductLease(itemSaleId, item, rs.getString("startDate"), rs.getString("endDate"));
                    case "V" ->
                            new VoicePlan(itemSaleId, item, rs.getString("phoneNumber"), rs.getDouble("totalPeriod"));
                    case "S" ->
                            new Service(itemSaleId, item, rs.getDouble("totalHours"), loadPerson(rs.getInt("employeeId")));
                    case "D" ->
                            new DataPlan(itemSaleId, item, rs.getDouble("totalGb"));
                    default -> item;
                };
            }

        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return item;
    }

    public static Map<Integer, Item> loadAllItemSold(){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Map<Integer, Item> itemMap = new HashMap<>();

        String query = """
                        select itemSaleId from ItemSale;
                        """;
        try{
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()){
                Item itemSold = loadItemSold(rs.getInt("itemSaleId"));
                itemMap.put(rs.getInt("itemSaleId"), itemSold);
            }

        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return itemMap;
    }

    private static Sale loadRawSale(int saleId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;
        Sale sale = null;

        String query = """
                    select * from Sale s
                    left join ItemSale its on s.saleId = ItemSale.saleId
                    left join Item i on its.itemId = i.itemId
                    where s.saleId = ?
                    """;
        try{
            ps = conn.prepareStatement(query);
            ps.setInt(1, saleId);
            rs = ps.executeQuery();
            while (rs.next()){

            }

        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return sale;
    }
    //TODO: Create the load person class.
}
