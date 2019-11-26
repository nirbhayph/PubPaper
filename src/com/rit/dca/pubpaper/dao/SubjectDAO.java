package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.Affiliation;
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

    private UserDAO userAccess;

    public SubjectDAO(UserDAO userAccess){
        this.userAccess = userAccess;
    }

    /**
     * Gives a subjects's instance for subjectId provided
     * @param subjectId - id for which type information is required
     * @return subject's instance
     */
    public Subject getSubject(int subjectId){
        Subject subject = null;

        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            if (connection.connect()) {

                // setup parameters for subject query
                List<String> subjectParams = new ArrayList<String>();
                subjectParams.add(Integer.toString(subjectId));

                // call get data on subject query
                ArrayList<ArrayList<String>> subjectData = connection.getData(DAOUtil.GET_SUBJECT, subjectParams);

                if (subjectData.size() == 2) {
                    subject = new Subject();
                    subject.setSubjectId(subjectId);
                    subject.setSubjectName(subjectData.get(1).get(1));
                }

                // close connection to database
                connection.close();
            }
        }

        return subject;

        // TODO : add exception handling in this method
    }

    public ArrayList<Subject> getSubjects() {

        ArrayList<Subject> subjects = new ArrayList<Subject>();

        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            if (connection.connect()) {

                // setup parameters for subjects query
                List<String> subjectParams = new ArrayList<String>();

                // call get data on subjects query
                ArrayList<ArrayList<String>> subjectsData = connection.getData(DAOUtil.GET_ALL_SUBJECTS, subjectParams);

                int iCount = 1;
                for (ArrayList<String> iSubject : subjectsData) {

                    // skip first row as meta data
                    if (iCount == 1) {
                        ++iCount;
                        continue;
                    }

                    // setup retrieved subject's instance
                    Subject subject = new Subject();
                    subject.setSubjectName(iSubject.get(1));
                    subject.setSubjectId(Integer.parseInt(iSubject.get(0)));

                    // add subject instance to returning array list
                    subjects.add(subject);
                }

                // close connection to database
                connection.close();
            }
        }

        return subjects;

        // TODO : Manage exceptions for this method
    }
}
