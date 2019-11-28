package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;
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
     * @throws PubPaperException
     */
    public Subject getSubject(int subjectId) throws PubPaperException {
        Subject subject = null;

        //Check if user is logged in
        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
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
            catch(PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_SUBJECT, CustomExceptionUtil.getCaller(e));
            }
        }

        return subject;
    }

    /**
     * Gives a list of all subject instances
     * @return ArrayList of subject instances
     * @throws PubPaperException
     */
    public ArrayList<Subject> getSubjects() throws PubPaperException{

        ArrayList<Subject> subjects = new ArrayList<Subject>();

        // Check if user is logged in
        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
                if (connection.connect()) {

                    // setup parameters to get subjects query
                    List<String> subjectParams = new ArrayList<String>();

                    // call get data on get subjects query
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
            catch(PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_SUBJECTS, CustomExceptionUtil.getCaller(e));
            }
        }
        return subjects;
    }

    /**
     * Get the new subject id to be inserted
     *
     * @param connection MySQLDatababase connection object
     * @return int new subject id to use
     */
    private int nextSubjectId(MySQLDatabase connection) {

        try {
            int newSubjectId = -1;

            // create params list for next subject id query
            List<String> subjectParams = new ArrayList<String>();

            // call get data on check subject query
            ArrayList<ArrayList<String>> lastSubject = connection.getData(DAOUtil.GET_LAST_SUBJECT_ID, subjectParams);

            if (lastSubject.size() == 2) {
                newSubjectId = Integer.parseInt(lastSubject.get(1).get(0)) + 1;
            }

            return newSubjectId;
        }
        catch(PubPaperException e){
            return -1;
        }
    }

    /**
     * Add a new subject to the database (only ADMIN)
     * @param subjectName to be added to the database
     * @return boolean success or failure to add a new subject
     * @throws PubPaperException
     */
    public boolean addSubject(String subjectName) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        boolean addStatus = false;

        try {
            if (connection.connect()) {
                // check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    // get new subject id
                    int nextSId = nextSubjectId(connection);
                    if (nextSId != -1) {
                        // setup parameters for add subject query
                        List<String> subjectParams = new ArrayList<String>();
                        subjectParams.add(Integer.toString(nextSId));
                        subjectParams.add(subjectName);

                        // call modify data to add subject query
                        int rowsAffected = connection.modifyData(DAOUtil.INSERT_SUBJECT, subjectParams);
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
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_ADD_SUBJECT, CustomExceptionUtil.getCaller(e));
        }

        return addStatus;
    }

    /**
     * Delete a subject from the database (only ADMIN)
     * @param subjectId to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deleteSubject(int subjectId) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        try {
            if (connection.connect()) {

                //Check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    // setup parameters for delete subject query
                    List<String> subjectParams = new ArrayList<String>();
                    subjectParams.add(Integer.toString(subjectId));

                    // call modify data to delete subject query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_SUBJECT, subjectParams);
                    if (rowsAffected == 1) {
                        PaperSubjectDAO accessPaperSubjectDAO = new PaperSubjectDAO(this.userAccess);

                        int paperSubjectRowsAffected = accessPaperSubjectDAO.deleteSubjectPapers(subjectId);
                        if (paperSubjectRowsAffected < 0) {
                            connection.rollbackTransaction();
                        } else {
                            connection.endTransaction();
                        }
                    } else {
                        connection.rollbackTransaction();
                    }
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_SUBJECT, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Change subject name in the database
     * @param subjectId subject id to be changed
     * @param newSubjectName subject name to be changed to
     * @return int rows affected on updating subject
     * @throws PubPaperException
     */
    public int changeSubject(int subjectId, String newSubjectName) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        try {
            if (connection.connect()) {

                //Check if user is admin
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
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_CHANGE_SUBJECT, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }
}
