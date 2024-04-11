package unl.soc;

/**
 * Database connection configuration
 */
public class DatabaseInfo {

    /**
     * Username used to connect to the SQL server
     */
    public static final String USERNAME = "lguedesdecarvalhon2";

    /**
     * Password used to connect to the SQL server
     */
    public static final String PASSWORD = "zi9diengahXu";

    /**
     * Connection parameters that may be necessary for server configuration
     *
     */
    public static final String PARAMETERS = "";

    /**
     * SQL server to connect to
     */
    public static final String SERVER = "cse-linux-01.unl.edu";

    /**
     * Fully formatted URL for a JDBC connection
     */
    public static final String URL = String.format("jdbc:mysql://%s/%s?%s", SERVER, USERNAME, PARAMETERS);

}
