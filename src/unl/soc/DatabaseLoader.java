package unl.soc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

public class DatabaseLoader {
    public static void main(String[] args) {
        Address test = loadAddress(1);
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


}
