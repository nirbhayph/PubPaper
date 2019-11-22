package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.model.Affiliation;
import java.util.ArrayList;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class AffiliationDAO {
    /**
     * Gives a list of affiliation instances
     * @param userId for which affiliations are required
     * @return array list of affiliations for userId provided
     */
    public ArrayList<Affiliation> getAffiliations(int userId){
        return null;
    }

    /**
     * Gives a affiliation's instance for affiliationId provided
     * @param affiliationId - id for which affiliation information is required
     * @return affiliation's instance
     */
    public Affiliation getAffiliation(int affiliationId){
        return null;
    }
}
