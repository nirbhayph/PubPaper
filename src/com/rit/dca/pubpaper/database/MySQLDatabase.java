package com.rit.dca.pubpaper.database;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.util.ArrayList;
import java.util.List;

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
  public MySQLDatabase(String url, String userName, String password){
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
    catch (SQLException e){
      return false;
    }
  }

  /**
  * Prepare query statement
  * @param query - SQL Query
  * @param arguments - Parameters for variables in SQL Query
  * @return PreparedStatement - returns the formed prepared statement with arguments.
  */
  private PreparedStatement prepare(String query, List<String> arguments){
    try{
      PreparedStatement preparedStatement = this.connection.prepareStatement(query);
      for(int param = 0; param < arguments.size(); param++){
        preparedStatement.setString(param+1, arguments.get(param));
      }
      return preparedStatement;
    }
    catch(SQLException e){
      return null;
    }
  }

  /**
  * Execute Select queries using prepared statement
  * @param query - SQL Query
  * @param arguments - Parameters for variables in SQL Query
  * @return ArrayList<ArrayList<String>> - returns all rows from select query along with column headers.
  */
  public ArrayList<ArrayList<String>> getData(String query, List<String> arguments){
    ArrayList<ArrayList<String>> allRows = new ArrayList<ArrayList<String>>();
    try{
      PreparedStatement preparedStatement = prepare(query, arguments);
      ResultSet resultSet = preparedStatement.executeQuery();
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      int columnCount = resultSetMetaData.getColumnCount();
      ArrayList<String> columnNameRow = new ArrayList<String>();
      for(int counter = 1; counter <= columnCount; counter++){
        columnNameRow.add(resultSetMetaData.getColumnName(counter));
      }
      allRows.add(columnNameRow);
      while(resultSet.next()){
        ArrayList<String> row = new ArrayList<String>();
        for(int counter = 1; counter <= columnCount; counter++){
          row.add(resultSet.getString(counter));
        }
        allRows.add(row);
      }
      return allRows;
    }
    catch(Exception e){
      e.printStackTrace();
      return allRows;
    }
  }

  /**
  * Execute Update, Delete and Insert queries using preparedStatement
  * @param query - SQL Query
  * @param arguments - Parameters for variables in SQL Query
  * @return int - returns rows affected from executing the DML statements.
  */
  public int modifyData(String query, List<String> arguments){
    try{
      PreparedStatement preparedStatement = prepare(query, arguments);
      return preparedStatement.executeUpdate();
    }
    catch(SQLException sqlEx) {
      return -1;
    }
  }

}
