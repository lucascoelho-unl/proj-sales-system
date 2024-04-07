package unl.soc;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.dbcp2.BasicDataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnFactory {
    public static Connection createConnection(String username, String password) {
        BasicDataSource dataSource = new BasicDataSource();
        Dotenv dotenv = Dotenv.configure().load();

        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + dotenv.get("SERVER_NAME") + ":"+ dotenv.get("PORT_NUMBER") + "/" + dotenv.get("DATABASE_NAME"));
        dataSource.setUsername(username);
        dataSource.setPassword(password);

        Connection conn = null;

        try {
            conn = dataSource.getConnection();
        } catch (SQLException e) {
            System.out.println("Error in the connection: " + e);
            throw new RuntimeException(e);
        }
        return conn;
    }

    public static Connection createConnection() {
        Dotenv dotenv = Dotenv.configure().load();
        return createConnection(dotenv.get("USERNAME"), dotenv.get("PASSWORD"));
    }

    public static void closeConnection(ResultSet rs, PreparedStatement ps, Connection conn){
        try {
            if (rs != null && !rs.isClosed())
                rs.close();
            if (ps != null && !ps.isClosed())
                ps.close();
            if (conn != null && !conn.isClosed())
                conn.close();
        } catch (SQLException e) {
            System.out.println("SQLException: " + e);
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
    public static void closeConnection(PreparedStatement ps, Connection conn){
        closeConnection(null, ps, conn);
    }
    public static void closeConnection(Connection conn){
        closeConnection(null, null, conn);
    }
}
