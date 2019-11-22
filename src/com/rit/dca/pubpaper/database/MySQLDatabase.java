package com.rit.dca.pubpaper.database;

import java.sql.Connection;
import java.sql.DriverManager;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class MySQLDatabase {
    private Connection connection;
    private String connectionURL;
    private String userName;
    private String password;

    /**
     * Parameterized Constructor that takes in the database url, username and password
     * @param url - contains the connection url
     * @param userName - contains the username
     * @param password - contains the password
     */
    MySQLDatabase(String url, String userName, String password){
        this.connectionURL = url;
        this.userName = userName;
        this.password = password;
    }

    /**
     * Connects to MYSQL DB using the MYSQL Driver
     * @return boolean - returns true or false based on ability to connect.
     */
    public boolean connect(){
        try{
            Class.forName(MySQLUtil.MYSQL_DRIVER);
            this.connection = DriverManager.getConnection(connectionURL, userName, password);
            return true;
        }
        catch (Exception e){
            return false;
        }
    }

    /**
     * Closes the connection
     * @return boolean - returns true or false based on ability to close connection.
     */
    public boolean close(){
        try{
            if(this.connection != null){
                connection.close();
                return true;
            }
            return false;
        }
        catch (Exception e){
            return false;
        }
    }
}
