package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;
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
     * @throws PubPaperException
     */
    public Affiliation getAffiliation(int userId) throws PubPaperException {
        Affiliation affiliation = null;

        // Check if user is logged in
        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
                if (connection.connect()) {

                    // setup parameters for affiliations query
                    List<String> affiliationParams = new ArrayList<String>();
                    affiliationParams.add(Integer.toString(userId));

                    // call get data on affiliation query
                    ArrayList<ArrayList<String>> affiliationsData = connection.getData(DAOUtil.GET_USER_AFFILIATION, affiliationParams);

                    if (affiliationsData.size() == 2) {
                        int affiliationId = Integer.parseInt(affiliationsData.get(1).get(0));

                        affiliationParams = new ArrayList<String>();
                        affiliationParams.add(Integer.toString(affiliationId));

                        // Get the affiliation
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
            catch (PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_AFFILIATION, CustomExceptionUtil.getCaller(e));
            }
        }
        return affiliation;
    }

    /**
     * Gives a list of all affiliation instances
     * @return array list of all affiliation instances
     * @throws PubPaperException
     */
    public ArrayList<Affiliation> getAffiliations() throws PubPaperException{

        ArrayList<Affiliation> affiliations = new ArrayList<Affiliation>();

        // Check if user is logged in
        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
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
            catch (PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_AFFILIATIONS, CustomExceptionUtil.getCaller(e));
            }
        }
        return affiliations;
    }

    /**
     * Get the new affiliation id to be inserted
     *
     * @param connection MySQLDatabase connection object
     * @return int new affiliation id to use
     */
    private int nextAffiliationId(MySQLDatabase connection) {

        try {
            int newAffiliationId = -1;

            // create params list for next affiliation id query
            List<String> userParams = new ArrayList<String>();

            // call get data on check affiliation query
            ArrayList<ArrayList<String>> lastAffiliation = connection.getData(DAOUtil.GET_LAST_AFFILIATION_ID, userParams);

            if (lastAffiliation.size() == 2) {
                newAffiliationId = Integer.parseInt(lastAffiliation.get(1).get(0)) + 1;
            }

            return newAffiliationId;
        }
        catch (PubPaperException e){
            return -1;
        }
    }

    /**
     * Adds new affiliation to the database
     * @param affiliationName affiliation name to be added
     * @return boolean status of affiliation inserted
     * @throws PubPaperException
     */
    public boolean addAffiliations(String affiliationName) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        boolean addStatus = false;

        try {
            if (connection.connect()) {
                //CHeck if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    // Get the new affiliation id
                    int nextAId = nextAffiliationId(connection);
                    if (nextAId != -1) {
                        // setup parameters for add affiliation query
                        List<String> affiliationParams = new ArrayList<String>();
                        affiliationParams.add(Integer.toString(nextAId));
                        affiliationParams.add(affiliationName);

                        // call modify data to add affiliation query
                        int rowsAffected = connection.modifyData(DAOUtil.INSERT_AFFILIATION, affiliationParams);
                        if (rowsAffected == 1) {
                            addStatus = true;
                            connection.endTransaction();
                        } else {
                            connection.rollbackTransaction();
                        }
                    } else {
                        connection.rollbackTransaction();
                    }
                }
                // close connection to database
                connection.close();
            }
        }
        catch (PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_ADD_AFFILIATION, CustomExceptionUtil.getCaller(e));
        }

        return addStatus;
    }

    /**
     * Deletes existing affiliation from the database
     * @param affiliationId affiliation id to be deleted
     * @return int rows affected on deleting affiliation
     * @throws PubPaperException
     */
    public int deleteAffiliation(int affiliationId) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        try {
            if (connection.connect()) {

                // Check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    // setup parameters for delete affiliation query
                    List<String> affiliationParams = new ArrayList<String>();
                    affiliationParams.add(Integer.toString(affiliationId));

                    // call modify data to delete affiliation query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_AFFILIATION, affiliationParams);
                    if (rowsAffected == 1) {

                        // Set user affiliation null
                        connection.modifyData(DAOUtil.SET_USER_AFFILIATION_NULL, affiliationParams);
                    }
                }
                // close connection to database
                connection.close();
            }
        }
        catch (PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_AFFILIATION, CustomExceptionUtil.getCaller(e));
        }

        return rowsAffected;
    }

    /**
     * Change affiliation name in the database
     * @param affiliationId affiliation id to be changed
     * @param newAffiliationName affiliation name to be changed to
     * @return int rows affected on deleting affiliation
     * @throws PubPaperException
     */
    public int changeAffiliation(int affiliationId, String newAffiliationName) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        try {
            if (connection.connect()) {
                // check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {

                    // setup parameters for update affiliation query
                    List<String> affiliationParams = new ArrayList<String>();
                    affiliationParams.add(newAffiliationName);
                    affiliationParams.add(Integer.toString(affiliationId));

                    // call modify data to update affiliation
                    rowsAffected = connection.modifyData(DAOUtil.UPDATE_AFFILIATION, affiliationParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch (PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_CHANGE_AFFILIATION, CustomExceptionUtil.getCaller(e));
        }

        return rowsAffected;
    }

}
