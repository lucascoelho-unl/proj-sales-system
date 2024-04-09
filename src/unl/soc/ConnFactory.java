package unl.soc;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnFactory {
    private static final BasicDataSource dataSource;

    // Initialize the data source once
    static {
        Dotenv dotenv = Dotenv.configure().load();
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + dotenv.get("SERVER_NAME") + ":" + dotenv.get("PORT_NUMBER") + "/" + dotenv.get("DATABASE_NAME"));
        dataSource.setUsername(dotenv.get("USERNAME"));
        dataSource.setPassword(dotenv.get("PASSWORD"));
    }

    public static Connection createConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(ResultSet rs, PreparedStatement ps, Connection conn) {
        try {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            throw new RuntimeException(e);
        }
    }

    public static void closeConnection(PreparedStatement ps, Connection conn) {
        closeConnection(null, ps, conn);
    }

    public static void closeConnection(Connection conn) {
        closeConnection(null, null, conn);
    }
    public static void closeConnection() {
        try{
            dataSource.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            throw new RuntimeException(e);
        }
    }
}
