package unl.soc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import static unl.soc.DatabaseLoader.loadAddress;

public class temp {

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
                System.out.println("loaded person" + person);
            }
        } catch (SQLException e) {
            System.out.println("Error parsing person " + personId + ": " + e);
            throw new RuntimeException(e);
        } finally {
            System.out.println("closing loading person with personId: " + personId);
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
            System.out.println("loading all person is over, closing");
            ConnFactory.closeConnection(rs, ps, conn);
        }
        return personMap;
    }
}
