package com.rit.dca.pubpaper.dao;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class DAOUtil {

    // MYSQL credentials
    protected static final String USER_NAME = "root";
    protected static final String PASSWORD = "google12";
    protected static final String HOST = "jdbc:mysql://localhost:3306/CSM?useSSL=false";

    // Authentication
    protected static final String CHECK_LOGIN = "SELECT * FROM Users WHERE email=? AND pswd=?";
    
    // Paper
    protected static final String GET_USER_PAPERS = "SELECT * FROM Papers WHERE submitterId=?";
    protected static final String GET_PAPER_WITH_PAPER_ID = "SELECT * FROM Papers WHERE submissionId=?";
}
