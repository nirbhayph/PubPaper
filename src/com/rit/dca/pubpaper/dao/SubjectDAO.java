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


    /**
     * Get the new subject id to be inserted
     *
     * @param connection MySQLDatababase connection object
     * @return int new subject id to use
     */
    private int nextSubjectId(MySQLDatabase connection) {

        int newSubjectId = -1;

        // create params list for next user id query
        List<String> subjectParams = new ArrayList<String>();

        // call get data on check user query
        ArrayList<ArrayList<String>> lastSubject = connection.getData(DAOUtil.GET_LAST_SUBJECT_ID, subjectParams);

        if (lastSubject.size() == 2) {
            newSubjectId = Integer.parseInt(lastSubject.get(1).get(0)) + 1;
        }

        return newSubjectId;
    }

    public boolean addSubject(String subjectName){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        boolean addStatus = false;

        if(connection.connect()) {
            if(this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                connection.startTransaction();
                int nextSId = nextSubjectId(connection);
                if(nextSId != -1){
                    // setup parameters for add subject query
                    List<String> subjectParams = new ArrayList<String>();
                    subjectParams.add(Integer.toString(nextSId));
                    subjectParams.add(subjectName);

                    // call modify data to add subject query
                    int rowsAffected = connection.modifyData(DAOUtil.INSERT_SUBJECT, subjectParams);
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
     * Deletes existing subject from the database
     * @param subjectId subject id to be deleted
     * @return int rows affected on deleting affiliation
     */
    public int deleteSubject(int subjectId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if(connection.connect()) {

            if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                connection.startTransaction();
                // setup parameters for delete affiliation query
                List<String> subjectParams = new ArrayList<String>();
                subjectParams.add(Integer.toString(subjectId));

                // call modify data to add affiliation query
                rowsAffected = connection.modifyData(DAOUtil.DELETE_SUBJECT, subjectParams);
                if(rowsAffected == 1){
                    int paperSubjectRowsAffected = connection.modifyData(DAOUtil.DELETE_SUBJECT_PAPER, subjectParams);
                    if(paperSubjectRowsAffected < 0){
                        connection.rollbackTransaction();
                    }
                    else{
                        connection.endTransaction();
                    }
                }
                else{
                    connection.rollbackTransaction();
                }
            }
            // close connection to database
            connection.close();
        }

        return rowsAffected;
    }

    /**
     * Change subject name in the database
     * @param subjectId affiliation id to be changed
     * @param newSubjectName affiliation name to be changed to
     * @return int rows affected on deleting affiliation
     */
    public int changeSubject(int subjectId, String newSubjectName){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if(connection.connect()) {

            if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                // setup parameters for set subject query
                List<String> subjectParams = new ArrayList<String>();
                subjectParams.add(newSubjectName);
                subjectParams.add(Integer.toString(subjectId));

                // call modify data to update subject
                rowsAffected = connection.modifyData(DAOUtil.UPDATE_SUBJECT, subjectParams);
            }
            // close connection to database
            connection.close();
        }

        return rowsAffected;
    }
}
