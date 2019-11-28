package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;
import com.rit.dca.pubpaper.model.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Connectivity and Access
 *
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class PaperSubjectDAO {
    private UserDAO userAccess;

    public PaperSubjectDAO(UserDAO userAccess) {
        this.userAccess = userAccess;
    }

    /**
     * Gives a list of subject instances for paper id
     *
     * @param paperId for which subjects are required
     * @return array list of subjects for paperId provided
     * @throws PubPaperException
     */
    public ArrayList<Subject> getPaperSubjects(int paperId) throws PubPaperException {
        ArrayList<Subject> subjects = new ArrayList<Subject>();

        // Check if user is logged in
        if (this.userAccess.getLoggedInId() != -1) {
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
                if (connection.connect()) {

                    // setup parameters for paper subjects query
                    List<String> paperSubjectsParams = new ArrayList<String>();
                    paperSubjectsParams.add(Integer.toString(paperId));

                    // call get data on paper subjects query
                    ArrayList<ArrayList<String>> paperSubjectsData = connection.getData(DAOUtil.GET_PAPER_SUBJECTS, paperSubjectsParams);

                    int iCount = 1;
                    for (ArrayList<String> iPaperSubject : paperSubjectsData) {

                        // skip first row as meta data
                        if (iCount == 1) {
                            ++iCount;
                            continue;
                        }

                        // get subject details for subject id
                        SubjectDAO subjectDAO = new SubjectDAO(this.userAccess);

                        // setup new subject instance
                        Subject subject = subjectDAO.getSubject(Integer.parseInt(iPaperSubject.get(1)));


                        // add to returning subjects array list
                        subjects.add(subject);
                    }

                    // close connection to database
                    connection.close();
                }
            }
            catch(PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_PAPER_SUBJECTS, CustomExceptionUtil.getCaller(e));
            }
        }
        return subjects;
    }

    /**
     * Add a new paper subject to the database (submitter or ADMIN)
     * @param paperId paperId to be added to the database
     * @param subjectIds subjectIds to be paired with paper id to insert
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int addPaperSubject(int paperId, int[] subjectIds) throws PubPaperException{
        int rowsAffected = 0;
        boolean rollbackCheck = false;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                PaperDAO accessPaper = new PaperDAO(this.userAccess);
                int submitterId = accessPaper.getPaper(paperId).getSubmitterId();

                // Check if user adding the paper subject is the submitter of that paper or If it is an admin
                if (submitterId == this.userAccess.getLoggedInId() || this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    for (int subjectId : subjectIds) {
                        // prepare params to add new paper subject
                        List<String> paperSubjectParams = new ArrayList<>();
                        paperSubjectParams.add(Integer.toString(paperId));
                        paperSubjectParams.add(Integer.toString(subjectId));

                        int paperSubjectRowsAffected = connection.modifyData(DAOUtil.INSERT_PAPER_SUBJECT, paperSubjectParams);

                        // check if paper subject was added successfully
                        if (paperSubjectRowsAffected == 1) {
                            rowsAffected += 1;
                        } else {
                            // Rollback if error adding
                            connection.rollbackTransaction();
                            rollbackCheck = true;
                            break;
                        }
                    }
                    if (!rollbackCheck) {
                        connection.endTransaction();
                    }

                }
                // Close the connection
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_ADD_PAPER_SUBJECT, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Update a paper subject in the database (submitter or ADMIN)
     * @param paperId paperId to be update in the database
     * @param subjectId subjectId to be to be replaced with in the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int updatePaperSubject(int paperId, int subjectId) throws PubPaperException{
        int rowsAffected = 0;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                PaperDAO accessPaper = new PaperDAO(this.userAccess);
                int submitterId = accessPaper.getPaper(paperId).getSubmitterId();
                // Check if user adding the paper subject is the submitter of that paper or If it is an admin
                if (submitterId == this.userAccess.getLoggedInId() || this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    // prepare params to add new paper subject
                    List<String> paperSubjectParams = new ArrayList<>();
                    paperSubjectParams.add(Integer.toString(paperId));
                    paperSubjectParams.add(Integer.toString(subjectId));

                    rowsAffected = connection.modifyData(DAOUtil.INSERT_UPDATE_PAPER_SUBJECT, paperSubjectParams);
                    // check if paper subject was updated successfully
                    if (rowsAffected == 1) {
                        connection.endTransaction();
                    } else {
                        connection.rollbackTransaction();
                    }
                }
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_UPDATE_PAPER_SUBJECT, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Delete a paper subject from the database (only ADMIN)
     * @param paperId paperId of the paper to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deletePaperSubjects(int paperId) throws PubPaperException{
        int rowsAffected = -1;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                //Check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {

                    // setup parameters for paper subjects delete query
                    List<String> paperSubjectsParams = new ArrayList<String>();
                    paperSubjectsParams.add(Integer.toString(paperId));

                    // call modify data on paper subjects delete query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_PAPER_SUBJECTS, paperSubjectsParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_PAPER_SUBJECT, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Delete a subject's paper from the database (only ADMIN)
     * @param subjectId subjectId of the subject to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deleteSubjectPapers(int subjectId) throws PubPaperException{
        int rowsAffected = -1;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                //Check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {

                    // setup parameters for subject's paper delete query
                    List<String> paperSubjectsParams = new ArrayList<String>();
                    paperSubjectsParams.add(Integer.toString(subjectId));

                    // call modify data on paper subject's paper delete query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_SUBJECT_PAPERS, paperSubjectsParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_SUBJECT_PAPERS, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Delete a single paper subject from the database (only ADMIN)
     * @param subjectId subjectId of the subject to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deleteSinglePaperSubject(int paperId, int subjectId) throws PubPaperException{
        int rowsAffected = -1;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {

                // Check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {

                    // setup parameters for single paper subject delete query
                    List<String> paperSubjectParams = new ArrayList<String>();
                    paperSubjectParams.add(Integer.toString(paperId));
                    paperSubjectParams.add(Integer.toString(subjectId));

                    // call modify data on single paper subject delete query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_SINGLE_PAPER_SUBJECT, paperSubjectParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_SINGLE_PAPER_SUBJECT, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }
}
