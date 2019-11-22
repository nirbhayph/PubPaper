package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.model.Subject;
import java.util.ArrayList;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class SubjectDAO {
    /**
     * Gives a list of subject instances
     * @param paperId for which subjects are required
     * @return array list of subjects for paperId provided
     */
    public ArrayList<Subject> getSubjects(int paperId){
        return null;
    }

    /**
     * Gives a subjects's instance for subjectId provided
     * @param subjectId - id for which type information is required
     * @return subject's instance
     */
    public Subject getSubject(int subjectId){
        return null;
    }
}
