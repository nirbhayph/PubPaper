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
    /**
     * Gives affiliation
     * @param userId for which affiliation is required
     * @return affiliation name for userId provided
     */
    public Affiliation getAffiliation(int userId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        Affiliation affiliation = null;

        if(connection.connect()) {

            // setup parameters for affiliations query
            List<String> affiliationParams = new ArrayList<String>();
            affiliationParams.add(Integer.toString(userId));

            // call get data on affiliations query
            ArrayList<ArrayList<String>> affiliationsData = connection.getData(DAOUtil.GET_USER_AFFILIATION, affiliationParams);

            if(affiliationsData.size() == 2){
                int affiliationId = Integer.parseInt(affiliationsData.get(1).get(0));

                affiliationParams = new ArrayList<String>();
                affiliationParams.add(Integer.toString(affiliationId));

                affiliationsData = connection.getData(DAOUtil.GET_AFFILIATION_WITH_ID, affiliationParams);

                if(affiliationsData.size() == 2){
                    affiliation = new Affiliation();
                    affiliation.setAffiliationName(affiliationsData.get(1).get(1));
                    affiliation.setAffiliationId(affiliationId);
                }
            }

            // close connection to database
            connection.close();
        }

        return affiliation;

        // TODO : add exception handling in this method
    }

    /**
     * Gives a list of all affiliation instances
     * @return array list of all affiliation instances
     */
    public ArrayList<Affiliation> getAffiliations() {

        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        ArrayList<Affiliation> affiliations = new ArrayList<Affiliation>();

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

        return affiliations;

        // TODO : Manage exceptions for this method
    }


    /**
     * Check whether a user is an admin user
     *
     * @param connection  MYSQLDatabase connection object
     * @param adminUserId id of the user
     * @return boolean status of validation
     */
    protected boolean checkAdmin(MySQLDatabase connection, int adminUserId) {
        // setup parameters for admin query
        List<String> adminParams = new ArrayList<String>();
        adminParams.add(Integer.toString(adminUserId));

        // call get data on admin query
        ArrayList<ArrayList<String>> adminValidation = connection.getData(DAOUtil.CHECK_ADMIN, adminParams);

        // check if result contains single record and user is admin
        if (adminValidation.size() == 2) {
            return Integer.parseInt(adminValidation.get(1).get(0)) == 1;
        }
        return false;
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
        ArrayList<ArrayList<String>> lastAffiliation = connection.getData(DAOUtil.GET_NEXT_AFFILIATION_ID, userParams);

        if (lastAffiliation.size() == 2) {
            newAffiliationId = Integer.parseInt(lastAffiliation.get(1).get(0)) + 1;
        }

        return newAffiliationId;
    }

    /**
     * Adds new affiliation to the database
     * @param adminUserId admin user id
     * @param affiliationName affiliation name to be added
     * @return boolean status of affiliation insert
     */
    public boolean addAffiliations(int adminUserId, String affiliationName){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        boolean addStatus = false;

        if(connection.connect()) {
            if(checkAdmin(connection, adminUserId)) {
                // setup parameters for add affiliation query
                List<String> affiliationParams = new ArrayList<String>();
                affiliationParams.add(Integer.toString(nextAffiliationId(connection)));
                affiliationParams.add(affiliationName);

                // call modify data to add affiliation query
                int rowsAffected = connection.modifyData(DAOUtil.INSERT_AFFILIATION, affiliationParams);
                if(rowsAffected == 1){
                    addStatus = true;
                }
            }
            // close connection to database
            connection.close();
        }

        return addStatus;
    }

    /**
     * Deletes existing affiliation from the database
     * @param adminUserId admin user id
     * @param affiliationId affiliation id to be deleted
     * @return int rows affected on deleting affiliation
     */
    public int deleteAffiliation(int adminUserId, int affiliationId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if(connection.connect()) {

            if (checkAdmin(connection, adminUserId)) {
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

}
