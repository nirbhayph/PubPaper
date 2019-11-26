package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.Paper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class PaperDAO {
    private UserDAO userAccess;

    public PaperDAO(UserDAO userAccess){
        this.userAccess = userAccess;
    }

    /**
     * Gives a list of paper instances
     * @param userId for which papers are required
     * @return array list of papers for userId provided
     */
    public ArrayList<Paper> getPapers(int userId){
        ArrayList<Paper> papers = new ArrayList<Paper>();

        if(this.userAccess.getLoggedInId() != -1) {

            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            if (connection.connect()) {

                // setup parameters for papers query
                List<String> paperParams = new ArrayList<String>();
                paperParams.add(Integer.toString(userId));

                // call get data on papers query
                ArrayList<ArrayList<String>> papersData = connection.getData(DAOUtil.GET_USER_PAPERS, paperParams);

                int iCount = 1;
                for (ArrayList<String> iPaper : papersData) {

                    // skip first row as meta data
                    if (iCount == 1) {
                        ++iCount;
                        continue;
                    }

                    // set new paper instance
                    Paper paper = new Paper();

                    int paperId = Integer.parseInt(iPaper.get(0));
                    paper.setPaperId(paperId);
                    paper.setAbstractText(iPaper.get(2));
                    paper.setFileId(iPaper.get(7));
                    paper.setStatus(iPaper.get(4));
                    paper.setSubmissionType(Integer.parseInt(iPaper.get(5)));
                    paper.setSubmitterId(Integer.parseInt(iPaper.get(6)));
                    paper.setTentativeStatus(iPaper.get(8));
                    paper.setTitle(iPaper.get(1));
                    paper.setTrack(iPaper.get(3));

                    PaperSubjectDAO paperSubjectDAO = new PaperSubjectDAO(this.userAccess);
                    paper.setSubjects(paperSubjectDAO.getPaperSubjects(paperId));

                    PaperAuthorDAO paperAuthorDAO = new PaperAuthorDAO(this.userAccess);
                    paper.setAuthors(paperAuthorDAO.getPaperAuthors(paperId));

                    // add to returning papers array list
                    papers.add(paper);
                }

                // close connection to database
                connection.close();
            }
        }
        return papers;
        // TODO : add exception handling in this method
    }

    /**
     * Gives a paper's instance for paperId provided
     * @param paperId - id for which paper information is required
     * @return paper's instance
     */
    public Paper getPaper(int paperId){
        if(this.userAccess.getLoggedInId() != -1){
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            if(connection.connect()) {

                // setup parameters for paper query
                List<String> paperParams = new ArrayList<String>();
                paperParams.add(Integer.toString(paperId));

                // call get data on paper query
                ArrayList<ArrayList<String>> paperData = connection.getData(DAOUtil.GET_PAPER_WITH_PAPER_ID, paperParams);

                // check if requested paper exists
                if(paperData.size() == 2){

                    // setup retrieved paper's instance
                    Paper paper = new Paper();
                    paper.setPaperId(Integer.parseInt(paperData.get(1).get(0)));
                    paper.setAbstractText(paperData.get(1).get(2));
                    paper.setFileId(paperData.get(1).get(7));
                    paper.setStatus(paperData.get(1).get(4));
                    paper.setSubmissionType(Integer.parseInt(paperData.get(1).get(5)));
                    paper.setSubmitterId(Integer.parseInt(paperData.get(1).get(6)));
                    paper.setTentativeStatus(paperData.get(1).get(8));
                    paper.setTitle(paperData.get(1).get(1));
                    paper.setTrack(paperData.get(1).get(3));


                    PaperSubjectDAO paperSubjectDAO = new PaperSubjectDAO(this.userAccess);
                    paper.setSubjects(paperSubjectDAO.getPaperSubjects(paperId));

                    PaperAuthorDAO paperAuthorDAO = new PaperAuthorDAO(this.userAccess);
                    paper.setAuthors(paperAuthorDAO.getPaperAuthors(paperId));

                    // close connection to database
                    connection.close();

                    // return created paper instance
                    return paper;
                }
                else{
                    // close connection to database
                    connection.close();
                }
            }
        }
        return null;
        // TODO: Manage exceptions in this method
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

    public int deletePaper(int submitterId){
        int rowsAffected = -1;
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
            if (connection.connect()) {
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    // setup parameters for paper delete query
                    List<String> paperParams = new ArrayList<String>();
                    paperParams.add(Integer.toString(submitterId));

                    // call modify data on paper delete query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_USER_PAPERS, paperParams);
                }

                // close connection to database
                connection.close();
            }
        return rowsAffected;
    }

}
