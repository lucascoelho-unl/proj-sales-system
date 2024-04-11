package unl.soc;

import java.sql.*;

/**
 * This class provides methods to interact with the database for data persistence operations.
 * It is still under implementation.
 */
public class DataPersist {

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
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
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
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
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
    public static int selectOrInsertAddress(Address address) {
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
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        } finally {
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return 0;
    }
}

