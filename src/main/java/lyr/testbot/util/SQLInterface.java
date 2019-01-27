package lyr.testbot.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLInterface {

    /*
    [Prereq for Encyption]
    SET PERSIST innodb_file_per_table = 1;

    Database name: javatest

    Username: usertest
    Password: passtest

    Table: table1 (
        id,
        key VARCHAR(30),
        value VARCHAR(400)
    )
     */

    private static final String
        DB_NAME  = "javatest",
        USERNAME = "usertest",
        PASSWORD = "passtest";

    private static final String DB_URL = "jdbc:mysql://localhost/" + DB_NAME;
    //static final String JDBC_DRIVER = "com.mysql.jdbc.Driver";  // Unneeded

    private Connection dbconn;

    public SQLInterface() throws Exception {
        try {
            Log.logDebug("> Initiating database connection.");
            dbconn = DriverManager.getConnection(DB_URL,USERNAME,PASSWORD);

            Log.logDebug("> | Database connection established.");

            Runtime.getRuntime().addShutdownHook(new Thread(() -> {
                Log.log("> Shutting down.");
                try { if(dbconn!=null) dbconn.close(); } catch(SQLException se){ se.printStackTrace(); }
            }));

        } catch (Exception exc) {
            Log.logError("> Error in SQLInterface init.");
            throw exc;
        }

    }

    private ResultSet execute(String query){
        try (Statement statement = dbconn.createStatement(); ResultSet resultSet = statement.executeQuery(query)) {
            resultSet.close(); statement.close();
            return resultSet;
        } catch (Exception e) {
            Log.logError("> Error in query!");
            return null;
        }
    }
}