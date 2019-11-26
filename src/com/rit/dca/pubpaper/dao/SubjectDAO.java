package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.Paper;
import com.rit.dca.pubpaper.model.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class SubjectDAO {

    /**
     * Gives a subjects's instance for subjectId provided
     * @param subjectId - id for which type information is required
     * @return subject's instance
     */
    public Subject getSubject(int subjectId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        Subject subject = null;

        if(connection.connect()) {

            // setup parameters for subject query
            List<String> subjectParams = new ArrayList<String>();
            subjectParams.add(Integer.toString(subjectId));

            // call get data on subject query
            ArrayList<ArrayList<String>> subjectData = connection.getData(DAOUtil.GET_SUBJECT, subjectParams);

            if(subjectData.size() == 2){
                subject = new Subject();
                subject.setSubjectId(subjectId);
                subject.setSubjectName(subjectData.get(1).get(1));
            }

            // close connection to database
            connection.close();
        }

        return subject;

        // TODO : add exception handling in this method
    }
}
