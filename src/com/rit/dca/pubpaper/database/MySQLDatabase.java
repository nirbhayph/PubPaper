package com.rit.dca.pubpaper.database;

import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;

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
  * @throws PubPaperException
  */
  public boolean connect() throws PubPaperException {
    try{
      Class.forName(MySQLUtil.MYSQL_DRIVER);
      this.connection = DriverManager.getConnection(connectionURL, userName, password);
      return true;
    }
    catch (Exception e){
      throw new PubPaperException(e, MySQLUtil.UNABLE_TO_CONNECT, CustomExceptionUtil.getCaller(e));
    }
  }

  /**
  * Closes the connection
  * @return boolean - returns true or false based on ability to close connection.
  * @throws PubPaperException
  */
  public boolean close() throws PubPaperException{
    try{
      // Check if connection is open
      if(this.connection != null){
        connection.close();
        return true;
      }
      throw new PubPaperException(new SQLException(), MySQLUtil.UNABLE_TO_CLOSE_NO_CONNECTION_EXIST, CustomExceptionUtil.getCaller(new SQLException()));
    }
    catch (SQLException e){
       throw new PubPaperException(e, MySQLUtil.UNABLE_TO_CLOSE, CustomExceptionUtil.getCaller(e));
    }
  }

  /**
  * Prepare query statement
  * @param query - SQL Query
  * @param arguments - Parameters for variables in SQL Query
  * @return PreparedStatement - returns the formed prepared statement with arguments.
  * @throws PubPaperException
  */
  private PreparedStatement prepare(String query, List<String> arguments) throws PubPaperException{
    try{
      PreparedStatement preparedStatement = this.connection.prepareStatement(query);
      for(int param = 0; param < arguments.size(); param++){
        preparedStatement.setString(param+1, arguments.get(param));
      }
      return preparedStatement;
    }
    catch(Exception e){
      throw new PubPaperException(e, MySQLUtil.UNABLE_TO_PREPARE, CustomExceptionUtil.getCaller(e));
    }
  }

  /**
  * Execute Select queries using prepared statement
  * @param query - SQL Query
  * @param arguments - Parameters for variables in SQL Query
  * @return ArrayList<ArrayList<String>> - returns all rows from select query along with column headers.
  * @throws PubPaperException
  */
  public ArrayList<ArrayList<String>> getData(String query, List<String> arguments) throws PubPaperException{
    ArrayList<ArrayList<String>> allRows = new ArrayList<ArrayList<String>>();
    try{
      PreparedStatement preparedStatement = prepare(query, arguments);
      // Executing the preparedstatement
      ResultSet resultSet = preparedStatement.executeQuery();
      ResultSetMetaData resultSetMetaData = resultSet.getMetaData();
      // getting column count of the result
      int columnCount = resultSetMetaData.getColumnCount();
      ArrayList<String> columnNameRow = new ArrayList<String>();
      for(int counter = 1; counter <= columnCount; counter++){
        columnNameRow.add(resultSetMetaData.getColumnName(counter));
      }
      allRows.add(columnNameRow);
      // converting result set into ArrayList<ArrayList<String>>
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
      throw new PubPaperException(e, MySQLUtil.UNABLE_TO_RETRIEVE_DATA, CustomExceptionUtil.getCaller(e));
    }
  }

  /**
  * Execute Update, Delete and Insert queries using preparedStatement
  * @param query - SQL Query
  * @param arguments - Parameters for variables in SQL Query
  * @return int - returns rows affected from executing the DML statements.
  * @throws PubPaperException
  */
  public int modifyData(String query, List<String> arguments) throws PubPaperException{
    try{
      PreparedStatement preparedStatement = prepare(query, arguments);
      return preparedStatement.executeUpdate();
    }
    catch(SQLException e) {
      throw new PubPaperException(e, MySQLUtil.UNABLE_TO_MODIFY_DATA, CustomExceptionUtil.getCaller(e));
    }
  }

    /**
     * Start Transaction
     * @return boolean - returns true if transaction started successfully.
     * @throws PubPaperException
     */
    public boolean startTransaction() throws PubPaperException{
        try{
            this.connection.setAutoCommit(false);
            return true;
        }
        catch(SQLException e) {
          throw new PubPaperException(e, MySQLUtil.UNABLE_TO_BEGIN_TRANSACTION, CustomExceptionUtil.getCaller(e));
        }
    }

    /**
     * End Transaction
     * @return boolean - returns true if transaction committed successfully.
     * @throws PubPaperException
     */
    public boolean endTransaction() throws PubPaperException{
        try{
            this.connection.setAutoCommit(true);
            return true;
        }
        catch(SQLException e) {
          throw new PubPaperException(e, MySQLUtil.UNABLE_TO_END_TRANSACTION, CustomExceptionUtil.getCaller(e));
        }
    }

    /**
     * Rollback Transaction
     * @return boolean - returns true if transaction is rollbacked successfully.
     * @throws PubPaperException
     */
    public boolean rollbackTransaction() throws PubPaperException{
        try{
            this.connection.rollback();
            return true;
        }
        catch(SQLException e) {
          throw new PubPaperException(e, MySQLUtil.UNABLE_TO_ROLLBACK_TRANSACTION, CustomExceptionUtil.getCaller(e));
        }
    }



}
