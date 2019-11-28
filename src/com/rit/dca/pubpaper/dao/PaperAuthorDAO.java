package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */
public class PaperAuthorDAO {

    private UserDAO userAccess;

    public PaperAuthorDAO(UserDAO userAccess){
        this.userAccess = userAccess;
    }

    /**
     * Gives a list of author ids for a paper id ordered according to displayOrder
     * @param paperId for which paper authors are required
     * @return array list of author ids for paperId provided
     * @throws PubPaperException
     */
    public ArrayList<Integer> getPaperAuthors(int paperId) throws PubPaperException {
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        ArrayList<Integer> authors = new ArrayList<Integer>();

        try {
            if (connection.connect()) {
                // setup parameters for paper authors query
                List<String> paperAuthorParams = new ArrayList<String>();
                paperAuthorParams.add(Integer.toString(paperId));

                // call get data on paper authors query
                ArrayList<ArrayList<String>> paperAuthorsData = connection.getData(DAOUtil.GET_PAPER_AUTHORS, paperAuthorParams);

                int iCount = 1;
                for (ArrayList<String> iPaperAuthor : paperAuthorsData) {

                    // skip first row as meta data
                    if (iCount == 1) {
                        ++iCount;
                        continue;
                    }

                    int authorId = Integer.parseInt(iPaperAuthor.get(1));

                    // add to returning authors ids array list
                    authors.add(authorId);
                }

                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_PAPER_AUTHORS, CustomExceptionUtil.getCaller(e));
        }

        return authors;
    }

    /**
     * Gives a list of paper ids for user id provided
     * @param userId for which papers are required
     * @return array list of paper ids for userId provided
     * @throws PubPaperException
     */
    public ArrayList<Integer> getAuthorPapers(int userId) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        ArrayList<Integer> papers = new ArrayList<Integer>();
        try {
            if (connection.connect()) {

                // setup parameters for author's papers query
                List<String> authorPaperParams = new ArrayList<String>();
                authorPaperParams.add(Integer.toString(userId));

                // call get data on author's paper query
                ArrayList<ArrayList<String>> authorPapersData = connection.getData(DAOUtil.GET_AUTHOR_PAPERS, authorPaperParams);


                int iCount = 1;
                for (ArrayList<String> iAuthorPaper : authorPapersData) {

                    // skip first row as meta data
                    if (iCount == 1) {
                        ++iCount;
                        continue;
                    }
                    int paperId = Integer.parseInt(iAuthorPaper.get(0));

                    // add to returning papers ids array list
                    papers.add(paperId);
                }

                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_AUTHOR_PAPERS, CustomExceptionUtil.getCaller(e));
        }
        return papers;
    }

    /**
     * Gives the display order for a combination of paper and user ids
     * @param paperId paperId to be searched
     * @param userId userId to be searched
     * @return int displayOrder of the combination
     * @throws PubPaperException
     */
    public int getDisplayOrder(int paperId, int userId) throws PubPaperException{
        int displayOrder = 0;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        try {
            if (connection.connect()) {
                // prepare params to get display order
                List<String> displayOrderParams = new ArrayList<>();
                displayOrderParams.add(Integer.toString(paperId));
                displayOrderParams.add(Integer.toString(userId));

                // call get data to get display order query
                ArrayList<ArrayList<String>> displayOrderData = connection.getData(DAOUtil.GET_DISPLAY_ORDER, displayOrderParams);
                if (displayOrderData.size() == 2) {
                    displayOrder = Integer.parseInt(displayOrderData.get(1).get(0));
                }
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_DISPLAY_ORDER, CustomExceptionUtil.getCaller(e));
        }
        return displayOrder;
    }

    /**
     * Adds new paper author in the database (submitter and ADMIN)
     * @param paperId paperId to be added
     * @param userId userId to be added
     * @param displayOrder displayOrder to be added
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int addPaperAuthor(int paperId, int userId, int displayOrder) throws PubPaperException{
        int rowsAffected = 0;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                PaperDAO accessPaper = new PaperDAO(this.userAccess);
                int submitterId = accessPaper.getPaper(paperId).getSubmitterId();
                // Check if user is submitter or Admin
                if (submitterId == this.userAccess.getLoggedInId() || this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    List<String> paperAuthorParams = new ArrayList<>();
                    paperAuthorParams.add(Integer.toString(paperId));
                    paperAuthorParams.add(Integer.toString(userId));
                    paperAuthorParams.add(Integer.toString(displayOrder));

                    rowsAffected = connection.modifyData(DAOUtil.INSERT_PAPER_AUTHOR, paperAuthorParams);

                    if (rowsAffected == 1) {
                        connection.endTransaction();
                    } else {
                        connection.rollbackTransaction();
                    }
                }
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_ADD_PAPER_AUTHORS, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Updates existing paper author in the database (submitter and ADMIN)
     * @param paperId paperId to be updated
     * @param userId userId to be updated
     * @param displayOrder displayOrder to be updated
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int updatePaperAuthor(int paperId, int userId, int displayOrder) throws PubPaperException{
        int rowsAffected = 0;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                PaperDAO accessPaper = new PaperDAO(this.userAccess);
                int submitterId = accessPaper.getPaper(paperId).getSubmitterId();
                // Check if user is submitter or Admin
                if (submitterId == this.userAccess.getLoggedInId() || this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    // setting params to update query
                    List<String> paperAuthorParams = new ArrayList<>();
                    paperAuthorParams.add(Integer.toString(paperId));
                    paperAuthorParams.add(Integer.toString(userId));
                    paperAuthorParams.add(Integer.toString(displayOrder));
                    paperAuthorParams.add(Integer.toString(displayOrder));

                    // calling update paper author query
                    rowsAffected = connection.modifyData(DAOUtil.INSERT_UPDATE_PAPER_AUTHOR, paperAuthorParams);
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
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_UPDATE_PAPER_AUTHORS, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }


    /**
     * Delete a paper author from the database (only ADMIN)
     * @param paperId paperId of the paper author to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deletePaperAuthors(int paperId) throws PubPaperException{
        int rowsAffected = -1;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        try {
            if (connection.connect()) {
                // Check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    // setup parameters for paper authors query
                    List<String> paperAuthorParams = new ArrayList<String>();
                    paperAuthorParams.add(Integer.toString(paperId));

                    // call modify data on paper authors delete query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_PAPER_AUTHORS, paperAuthorParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_PAPER_AUTHORS, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Delete an author's paper from the database (only ADMIN)
     * @param userId userId of the author to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deleteAuthorPapers(int userId) throws PubPaperException{
        int rowsAffected = -1;

        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                // Check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    // setup parameters for author papers query
                    List<String> authorPapersParams = new ArrayList<String>();
                    authorPapersParams.add(Integer.toString(userId));

                    // call modify data on author papers delete query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_AUTHOR_PAPERS, authorPapersParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_AUTHOR_PAPERS, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

    /**
     * Delete a single paper author from the database (only ADMIN)
     * @param paperId paperId of the paper to be deleted from the database
     * @param userId userId of the user to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deleteSinglePaperAuthor(int paperId, int userId) throws PubPaperException{
        int rowsAffected = -1;

        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    // setup parameters for single paper author delete query
                    List<String> paperAuthorParams = new ArrayList<String>();
                    paperAuthorParams.add(Integer.toString(paperId));
                    paperAuthorParams.add(Integer.toString(userId));

                    // call modify data on single paper author delete query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_SINGLE_PAPER_AUTHOR, paperAuthorParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_SINGLE_PAPER_AUTHOR, CustomExceptionUtil.getCaller(e));
        }

        return rowsAffected;
    }
}
