package unl.soc;

import java.sql.*;

public class DataPersist {

    public static void main(String[] args) {
        System.out.println(selectOrInsertState("Florida"));
        Address p = new Address("8 Eliot Circle","Huntsville", "Alabama", 35815);
        System.out.println(selectOrInsertAddress(p));
    }

    private static int selectOrInsertState(String state){
        if (state.isEmpty()){
            throw new RuntimeException("Invalid state");
        }

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = """
                        select stateId from State
                        where state = ?
                        """;

        try{
            //Return id if already exist
            ps = conn.prepareStatement(query);
            ps.setString(1, state);
            rs = ps.executeQuery();
            if(rs.next()){
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
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return -1;
    }

    private static int selectOrInsertZipcode(int zipcode, int stateId){
        if (zipcode % 10000 < 0){
            throw new RuntimeException("Invalid zipcode");
        }

        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        String query = """
                        select zipcodeId from Zipcode
                        where zipcode = ? and stateId = ?;
                        """;

        try{
            //Return id if already exist
            ps = conn.prepareStatement(query);
            ps.setInt(1, zipcode);
            ps.setInt(2, stateId);
            rs = ps.executeQuery();
            if(rs.next()){
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
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }

        return -1;
    }

    public static int selectOrInsertAddress(Address address) {
        Connection conn = ConnFactory.createConnection();
        PreparedStatement ps = null;
        ResultSet rs = null;

        //Query for existence address
        String query = """
                        select addressId from Address
                        where street = ? and zipcodeId = ? and city = ?;
                        """;

        try{
            //Return id if already exist
            ps = conn.prepareStatement(query);
            ps.setString(1, address.getStreet());
            int stateId = selectOrInsertState(address.getState());
            int zipcodeId = selectOrInsertZipcode(address.getZipCode(), stateId);
            ps.setInt(2, zipcodeId);
            ps.setString(3, address.getCity());

            rs = ps.executeQuery();
            if(rs.next()){
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
            if(rs.next()){
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return 0;
    }
}

