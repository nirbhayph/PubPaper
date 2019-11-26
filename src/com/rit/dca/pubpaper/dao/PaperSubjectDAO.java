package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.PaperAuthor;
import com.rit.dca.pubpaper.model.PaperSubject;
import com.rit.dca.pubpaper.model.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class PaperSubjectDAO {
    /**
     * Gives a list of subject instances for paper id
     * @param paperId for which subjects are required
     * @return array list of subjects for paperId provided
     */
    public ArrayList<Subject> getPaperSubjects(int paperId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        ArrayList<Subject> subjects = new ArrayList<Subject>();

        if(connection.connect()) {

            // setup parameters for paper subjects query
            List<String> paperSubjectsParams = new ArrayList<String>();
            paperSubjectsParams.add(Integer.toString(paperId));

            // call get data on paper subjects query
            ArrayList<ArrayList<String>> paperSubjectsData = connection.getData(DAOUtil.GET_PAPER_SUBJECTS, paperSubjectsParams);

            int iCount = 1;
            for(ArrayList<String> iPaperSubject : paperSubjectsData){

                // skip first row as meta data
                if(iCount == 1){
                    ++iCount;
                    continue;
                }

                // get subject details for subject id
                SubjectDAO subjectDAO = new SubjectDAO();

                // setup new subject instance
                Subject subject = subjectDAO.getSubject(Integer.parseInt(iPaperSubject.get(1)));


                // add to returning subjects array list
                subjects.add(subject);
            }

            // close connection to database
            connection.close();
        }

        return subjects;

        // TODO : add exception handling in this method
    }
}
