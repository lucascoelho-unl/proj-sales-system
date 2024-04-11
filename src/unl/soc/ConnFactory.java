package unl.soc;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.logging.log4j.core.config.DefaultConfiguration;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * A factory class responsible for managing database connections.
 */
public class ConnFactory {

    private static final Logger LOGGER = LogManager.getLogger(ConnFactory.class);
    private static final BasicDataSource dataSource;

    // Configure the Logger
    static {
        Configurator.initialize(new DefaultConfiguration());
        Configurator.setRootLevel(Level.INFO);
    }

    // Initialize the data source once
    static {
        dataSource = new BasicDataSource();
        dataSource.setDriverClassName("com.mysql.cj.jdbc.Driver");
        dataSource.setUrl(DatabaseInfo.URL);
        dataSource.setUsername(DatabaseInfo.USERNAME);
        dataSource.setPassword(DatabaseInfo.PASSWORD);
        LOGGER.info("Connected to database {} at {}", DatabaseInfo.USERNAME, DatabaseInfo.SERVER);
    }

    /**
     * Creates and returns a database connection.
     *
     * @return A Connection object representing the database connection.
     * @throws RuntimeException if an error occurs while establishing the connection.
     */
    public static Connection createConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            LOGGER.error("Connection error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Closes the given ResultSet, PreparedStatement, and Connection objects.
     *
     * @param rs   The ResultSet object to close.
     * @param ps   The PreparedStatement object to close.
     * @param conn The Connection object to close.
     * @throws RuntimeException if an error occurs while closing the resources.
     */
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

    /**
     * Overloaded method to close the PreparedStatement and Connection objects.
     *
     * @param ps   The PreparedStatement object to close.
     * @param conn The Connection object to close.
     */
    public static void closeConnection(PreparedStatement ps, Connection conn) {
        closeConnection(null, ps, conn);
    }

    /**
     * Overloaded method to close the Connection object.
     *
     * @param conn The Connection object to close.
     */
    public static void closeConnection(Connection conn) {
        closeConnection(null, null, conn);
    }

    /**
     * Closes the data source, releasing all pooled connections.
     *
     * @throws RuntimeException if an error occurs while closing the data source.
     */
    public static void closeConnection() {
        try {
            dataSource.close();
        } catch (SQLException e) {
            LOGGER.error("Connection error", e);
            throw new RuntimeException(e);
        }
    }

    /**
     * Method to get the generated datasource
     *
     * @return the DataSource instance from the private variable.
     */
    public static DataSource getDataSource() {
        return dataSource;
    }
}