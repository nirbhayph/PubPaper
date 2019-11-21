package com.rit.dca.paper;

import java.util.ArrayList;

/**
 * Data Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * @link: https://nirbhay.me
 */

public class PaperDAO {
    /**
     * Gives a list of paper instances
     * @return array list of papers for userId provided
     */
    public ArrayList<Paper> getPapers(int userId){
        return null;
    }

    /**
     * Gives a paper's instance
     * @return paper's instance for paperId provided
     */
    public Paper getPaper(int paperId){
        return null;
    }

    /**
     * Creates a new paper and sets
     * the information for it
     * @return paperId for newly created paper
     */
    public int setPaper(String submissionTitle, String submissionAbstract, int submissionType, String fileName,
                        String[] subjects, String[] coAuthorsFirstNames, String[] coAuthorsLastNames){
        return -1;
    }

    /**
     * Updates an existing paper
     * with the information provided for it
     * @return successful or not
     */
    public boolean setPaper(int paperId, String submissionTitle, String submissionAbstract, int submissionType, String fileName,
                            String[] subjects, String[] coAuthorsFirstNames, String[] coAuthorsLastNames){
        return false;
    }
}
