package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.Affiliation;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class AffiliationDAO {
    private UserDAO userAccess;

    public AffiliationDAO(UserDAO userAccess){
        this.userAccess = userAccess;
    }

    /**
     * Gives affiliation
     * @param userId for which affiliation is required
     * @return affiliation name for userId provided
     */
    public Affiliation getAffiliation(int userId){
        Affiliation affiliation = null;

        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            if (connection.connect()) {

                // setup parameters for affiliations query
                List<String> affiliationParams = new ArrayList<String>();
                affiliationParams.add(Integer.toString(userId));

                // call get data on affiliations query
                ArrayList<ArrayList<String>> affiliationsData = connection.getData(DAOUtil.GET_USER_AFFILIATION, affiliationParams);

                if (affiliationsData.size() == 2) {
                    int affiliationId = Integer.parseInt(affiliationsData.get(1).get(0));

                    affiliationParams = new ArrayList<String>();
                    affiliationParams.add(Integer.toString(affiliationId));

                    affiliationsData = connection.getData(DAOUtil.GET_AFFILIATION_WITH_ID, affiliationParams);

                    if (affiliationsData.size() == 2) {
                        affiliation = new Affiliation();
                        affiliation.setAffiliationName(affiliationsData.get(1).get(1));
                        affiliation.setAffiliationId(affiliationId);
                    }
                }

                // close connection to database
                connection.close();
            }
        }
        return affiliation;

        // TODO : add exception handling in this method
    }

    /**
     * Gives a list of all affiliation instances
     * @return array list of all affiliation instances
     */
    public ArrayList<Affiliation> getAffiliations() {

        ArrayList<Affiliation> affiliations = new ArrayList<Affiliation>();

        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            if (connection.connect()) {

                // setup parameters for affiliations query
                List<String> affiliationParams = new ArrayList<String>();

                // call get data on affiliations query
                ArrayList<ArrayList<String>> affiliationsData = connection.getData(DAOUtil.GET_ALL_AFFILIATIONS, affiliationParams);

                int iCount = 1;
                for (ArrayList<String> iAffiliation : affiliationsData) {

                    // skip first row as meta data
                    if (iCount == 1) {
                        ++iCount;
                        continue;
                    }

                    // setup retrieved affiliation's instance
                    Affiliation affiliation = new Affiliation();
                    affiliation.setAffiliationName(iAffiliation.get(1));
                    affiliation.setAffiliationId(Integer.parseInt(iAffiliation.get(0)));

                    // add affiliation instance to returning array list
                    affiliations.add(affiliation);
                }

                // close connection to database
                connection.close();
            }
        }

        return affiliations;

        // TODO : Manage exceptions for this method
    }

    /**
     * Get the new affiliation id to be inserted
     *
     * @param connection MySQLDatababase connection object
     * @return int new affiliation id to use
     */
    private int nextAffiliationId(MySQLDatabase connection) {

        int newAffiliationId = -1;

        // create params list for next user id query
        List<String> userParams = new ArrayList<String>();

        // call get data on check user query
        ArrayList<ArrayList<String>> lastAffiliation = connection.getData(DAOUtil.GET_LAST_AFFILIATION_ID, userParams);

        if (lastAffiliation.size() == 2) {
            newAffiliationId = Integer.parseInt(lastAffiliation.get(1).get(0)) + 1;
        }

        return newAffiliationId;
    }

    /**
     * Adds new affiliation to the database
     * @param affiliationName affiliation name to be added
     * @return boolean status of affiliation insert
     */
    public boolean addAffiliations(String affiliationName){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        boolean addStatus = false;

        if(connection.connect()) {
            if(this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                connection.startTransaction();
                int nextAId = nextAffiliationId(connection);
                if(nextAId != -1){
                    // setup parameters for add affiliation query
                    List<String> affiliationParams = new ArrayList<String>();
                    affiliationParams.add(Integer.toString(nextAId));
                    affiliationParams.add(affiliationName);

                    // call modify data to add affiliation query
                    int rowsAffected = connection.modifyData(DAOUtil.INSERT_AFFILIATION, affiliationParams);
                    if(rowsAffected == 1){
                        addStatus = true;
                        connection.endTransaction();
                    }
                    else{
                        connection.rollbackTransaction();
                    }
                }
                else{
                    connection.rollbackTransaction();
                }
            }
            // close connection to database
            connection.close();
        }

        return addStatus;
    }

    /**
     * Deletes existing affiliation from the database
     * @param affiliationId affiliation id to be deleted
     * @return int rows affected on deleting affiliation
     */
    public int deleteAffiliation(int affiliationId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if(connection.connect()) {

            if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                // setup parameters for delete affiliation query
                List<String> affiliationParams = new ArrayList<String>();
                affiliationParams.add(Integer.toString(affiliationId));

                // call modify data to add affiliation query
                rowsAffected = connection.modifyData(DAOUtil.DELETE_AFFILIATION, affiliationParams);
                if(rowsAffected == 1){
                    int userRowsAffected = connection.modifyData(DAOUtil.SET_USER_AFFILIATION_NULL, affiliationParams);
                }
            }
            // close connection to database
            connection.close();
        }

        return rowsAffected;
    }

    /**
     * Change affiliation name in the database
     * @param affiliationId affiliation id to be changed
     * @param newAffiliationName affiliation name to be changed to
     * @return int rows affected on deleting affiliation
     */
    public int changeAffiliation(int affiliationId, String newAffiliationName){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if(connection.connect()) {

            if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                // setup parameters for set affiliation query
                List<String> affiliationParams = new ArrayList<String>();
                affiliationParams.add(newAffiliationName);
                affiliationParams.add(Integer.toString(affiliationId));

                // call modify data to update affiliation
                rowsAffected = connection.modifyData(DAOUtil.UPDATE_AFFILIATION, affiliationParams);
            }
            // close connection to database
            connection.close();
        }

        return rowsAffected;
    }

}
