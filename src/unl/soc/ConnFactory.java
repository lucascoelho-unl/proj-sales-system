package unl.soc;

import io.github.cdimascio.dotenv.Dotenv;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ConnFactory {

    private static final Logger LOGGER = LogManager.getLogger(ConnFactory.class);

    // Configure the Logger
    static {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.INFO);
    }

    private static final BasicDataSource dataSource;

    // Initialize the data source once
    static {
        Dotenv dotenv = Dotenv.configure().load();
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl("jdbc:mysql://" + dotenv.get("SERVER_NAME") + ":" + dotenv.get("PORT_NUMBER") + "/" + dotenv.get("DATABASE_NAME"));
        dataSource.setUsername(dotenv.get("USERNAME"));
        dataSource.setPassword(dotenv.get("PASSWORD"));
        LOGGER.info("Connected to database {} at {}:{}", dotenv.get("DATABASE_NAME"), dotenv.get("SERVER_NAME"), dotenv.get("PORT_NUMBER"));
    }

    public static Connection createConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Connection error", e);
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
            LOGGER.error("Connection error", e);
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
            LOGGER.error("Connection error", e);
            throw new RuntimeException(e);
        }
    }
}
