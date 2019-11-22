package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.model.Paper;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class PaperDAO {
    /**
     * Gives a list of paper instances
     * @param userId for which papers are required
     * @return array list of papers for userId provided
     */
    public ArrayList<Paper> getPapers(int userId){
        return null;
    }

    /**
     * Gives a paper's instance for paperId provided
     * @param paperId - id for which paper information is required
     * @return paper's instance
     */
    public Paper getPaper(int paperId){
        return null;
    }

    /**
     * Creates a new paper or updates an existing
     * paper and sets the information for it
     * @param paperDetails key value pairs containing paper information
     * @return Paper instance for  created / updated paper
     */
    public Paper setPaper(HashMap<String, Object> paperDetails){
        //int paperId, String submissionTitle, String submissionAbstract, int submissionType, String fileName,
        //String[] subjects, String[] coAuthorsFirstNames, String[] coAuthorsLastNames
        return null;
    }

}
