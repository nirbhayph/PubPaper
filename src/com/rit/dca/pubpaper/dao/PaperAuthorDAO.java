package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.Paper;
import com.rit.dca.pubpaper.model.User;

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
     * Gives a list of author ids for
     * paper id ordered according to displayOrder
     * @param paperId for which paper authors are required
     * @return array list of author ids for paperId provided
     */
    public ArrayList<Integer> getPaperAuthors(int paperId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        ArrayList<Integer> authors = new ArrayList<Integer>();

        if(connection.connect()) {

            // setup parameters for paper authors query
            List<String> paperAuthorParams = new ArrayList<String>();
            paperAuthorParams.add(Integer.toString(paperId));

            // call get data on paper authors query
            ArrayList<ArrayList<String>> paperAuthorsData = connection.getData(DAOUtil.GET_PAPER_AUTHORS, paperAuthorParams);

            int iCount = 1;
            for(ArrayList<String> iPaperAuthor : paperAuthorsData){

                // skip first row as meta data
                if(iCount == 1){
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

        return authors;

        // TODO : add exception handling in this method
    }

    /**
     * Gives a list of paper ids for user id provided
     * @param userId for which papers are required
     * @return array list of paper ids for userId provided
     */
    public ArrayList<Integer> getAuthorPapers(int userId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        ArrayList<Integer> papers = new ArrayList<Integer>();

        if(connection.connect()) {

            // setup parameters for author's papers query
            List<String> authorPaperParams = new ArrayList<String>();
            authorPaperParams.add(Integer.toString(userId));

            // call get data on author's paper query
            ArrayList<ArrayList<String>> authorPapersData = connection.getData(DAOUtil.GET_AUTHOR_PAPERS, authorPaperParams);


            int iCount = 1;
            for(ArrayList<String> iAuthorPaper : authorPapersData){

                // skip first row as meta data
                if(iCount == 1){
                    ++iCount;
                    continue;
                }

                PaperDAO paperDAO = new PaperDAO(this.userAccess);
                int paperId = Integer.parseInt(iAuthorPaper.get(0));

                // add to returning papers ids array list
                papers.add(paperId);
            }

            // close connection to database
            connection.close();
        }

        return papers;

        // TODO : add exception handling in this method
    }

    public int deletePaperAuthors(int paperId){
        int rowsAffected = -1;

        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        if (connection.connect()) {
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

        return rowsAffected;
    }
}
