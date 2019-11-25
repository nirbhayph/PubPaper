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
}
