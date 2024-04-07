package unl.soc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class DatabaseLoader {
    public static void main(String[] args) {
        Address test = loadAddress(100);
        System.out.println(test);
    }

    public static Address loadAddress(int addressId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps;
        ResultSet rs;
        Address address = null;

        String query = """
                        select zipcode, state, city, street from Address a
                        left join Zipcode z on a.zipcodeId = z.zipcodeId
                        left join lguedesdecarvalhon2.State S on z.stateId = S.stateId
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
        }
        ConnFactory.closeConnection(rs, ps, conn);
         return address;
    }

    public static Map<Integer, Address> loadAllAddress(){
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps;
        ResultSet rs;
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
        }

        ConnFactory.closeConnection(rs, ps, conn);
        return addressMap;
    }

    public static Store loadStore(int storeId) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps;
        ResultSet rs;
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
                int storeCode = rs.getInt("storeCode");
                int managerId = rs.getInt("managerId");
                int addressId = rs.getInt("addressId");
                Address address = loadAddress(addressId);
                //TODO: Create a Person loader class to use here
//                Person manager = loadPerson(managerId);
//                store = new Store(storeId, storeCode, address, manager);
            }
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        }

        return store;
    }

    //TODO: Create the load person class.
}
