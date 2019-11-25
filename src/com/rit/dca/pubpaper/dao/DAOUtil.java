package com.rit.dca.pubpaper.dao;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class DAOUtil {

    // MYSQL credentials
    protected static final String userName = "root";
    protected static final String password = "google12";
    protected static final String host = "jdbc:mysql://localhost:3306/CSM?useSSL=false";

    // Authentication
    protected  static final String checkLogin = "SELECT * FROM Users WHERE email=? AND pswd=?";


}
