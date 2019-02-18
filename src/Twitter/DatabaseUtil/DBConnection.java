package Twitter.DatabaseUtil;

import Twitter.Util.*;

import java.sql.Connection;
import java.sql.DriverManager;

@Singleton
//Singleton class that returns an active connection with the hosted database.
public class DBConnection {

    @Attributes
    //Attributes specify driver, user and pass to log into the database
    private static Connection connection;
    private static final String DRIVER = "INSERT YOUR DRIVER HERE";
    private static final String USER = "INSERT DB USER";
    private static final String PASS = "INSERT DB PASS";

    @Methods
    //Getting the connection with a lazy initialization
    public static Connection getConnection() {
        if(connection == null) {
            try {
                Class.forName(DRIVER);
                connection = DriverManager.getConnection("jdbc:mysql://db4free.net:3306/tweeter", USER, PASS);

            } catch(Exception e) {
                e.printStackTrace();

            }
        }

        //Return the connection (new or not)
        return connection;

    }

}
