package com.rit.dca.pubpaper;

import com.rit.dca.pubpaper.dao.*;
import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;
import com.rit.dca.pubpaper.model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {

        /************************************* NOTE *********************************

           Please change MySQL credentials in com.rit.dca.pubpaper.dao.DAOUtil.java
           You will have to also add all the 4 jar files from the project's libraries
           directory.

         ***********************************NOTE ENDS******************************/

        /* Here we have tried to include all the function calls
        that one can to display what PubPaper is capable of displaying
        */
        try {

            /* Register New User Starts Here */
            UserDAO userDAO = new UserDAO();
            HashMap<String, Object> newUserData = new HashMap<String, Object>();
            newUserData.put("lastName", "Stark");
            newUserData.put("firstName", "Tony");
            newUserData.put("email", "ironman@avengers.com");
            newUserData.put("password", "google12");
            newUserData.put("affiliationId", 77);

            User registeredUser = userDAO.setProfile(newUserData);

            // Registered User JSON Details
            System.out.println("Registered User\n" + ModelUtil.getJson(registeredUser));

            /*******************************/

            /* Login Registered User */

            User user = userDAO.login("ironman@avengers.com", "google12");
            if (user != null) {
                System.out.println("Logged In Successfully");

                // Logged In User JSON Details
                System.out.println(ModelUtil.getJson(user));

                /* Create New Paper and Read It*/

                PaperDAO paperDAO = new PaperDAO(userDAO);

                HashMap<String, Object> newPaperData = new HashMap<String, Object>();
                newPaperData.put("title", "My Paper");
                newPaperData.put("abstract", "This is a paper I am inserting for dummy purposes");
                newPaperData.put("submissionType", 1);
                newPaperData.put("fileId", "NQAAXX");
                int _subjectIdArr[] = {5, 9, 10};
                newPaperData.put("subjects", _subjectIdArr);
                int _coAuthorsIdArr[] = {68};
                newPaperData.put("coAuthors", _coAuthorsIdArr);

                Paper createdPaper = paperDAO.setPaper(newPaperData);
                System.out.println("Inserted New Paper\n" + ModelUtil.getJson(createdPaper));

                /* Create New Paper and Read Ends Here */

                /* Update Paper */

                HashMap<String, Object> updatePaperData = new HashMap<String, Object>();
                updatePaperData.put("paperId", createdPaper.getPaperId());
                updatePaperData.put("title", "Mixed Reality");
                updatePaperData.put("abstract", "This is my mixed abstract");
                updatePaperData.put("submissionType", 2);
                updatePaperData.put("fileId", "ZZX22");
                int subjectIdArr[] = {5, 9, 10, 13, 16};
                updatePaperData.put("subjects", subjectIdArr);
                int coAuthorsIdArr[] = {1, 419, 3, 5, 420, 6};
                updatePaperData.put("coAuthors", coAuthorsIdArr);

                Paper updatedPaper = paperDAO.setPaper(updatePaperData);
                System.out.println("Updated Paper\n" + ModelUtil.getJson(updatedPaper));

                /* Update Paper Ends Here */

                /* Logging Out Now! */

                if(userDAO.logout()){
                    System.out.println("Logged Out Successfully");
                }

                /* Logging Out Ends Here */
            }

            /* Logged In Segment Ends Here */

            /*******************************/


            /* Logging in as an admin user */

            /* We changed the hash for one of the admin users according
            to our hash function, the password for that was google12.
            We are attaching our sql dump for reference. If you wish to
            login through another admin user, you will have to first update the
            hash as we did not know what's your hashing technique and neither did
            we have the salt.
            */

            User adminUser = userDAO.login("stevez@cssconsult.com", "google12");
            if (user != null) {
                System.out.println("Logged In Successfully with Admin");

                // Logged In User JSON Details
                System.out.println(ModelUtil.getJson(adminUser));

                /* See a user's papers */
                PaperDAO paperDAO = new PaperDAO(userDAO);
                ArrayList<Paper> papers = paperDAO.getPapers(247);
                System.out.println("Reading all papers");
                for(Paper paper : papers){
                    System.out.println("Paper " + paper.getTitle() + "\n" + ModelUtil.getJson(paper));
                }
                /* See a user's papers ends */

                /* Delete a paper */
                if(paperDAO.deletePaper(68) == 1){
                    System.out.println("Paper deleted successfully");
                }
                /* Delete a paper ends */

                /* See all subjects */
                SubjectDAO subjectDAO = new SubjectDAO(userDAO);
                ArrayList<Subject> subjects = subjectDAO.getSubjects();
                System.out.println("Reading all subjects");
                for(Subject subject : subjects){
                    System.out.println("Subject\n" + ModelUtil.getJson(subject));
                }
                /* See all subjects ends */

                /* Add a new subject in the subject's table */
                Subject addedSubject = subjectDAO.addSubject("DUMMY SUBJECT - MACHINE LEARNING");
                System.out.println("Added Subject\n" + ModelUtil.getJson(addedSubject));
                /* Add a new subject in the subject's table ends */

                /* Update a subject in the subject's table */
                subjectDAO.changeSubject(addedSubject.getSubjectId(), "UPDATED DUMMY - AI");
                System.out.println("Updated Subject" + ModelUtil.getJson(
                                                subjectDAO.getSubject(
                                                        addedSubject.getSubjectId()
                                                )));
                /* Update a subject in the subject's table ends */

                /* Delete a subject in the subject's table */
                if(subjectDAO.deleteSubject(addedSubject.getSubjectId()) == 1){
                    System.out.println("Deleted Subject Successfully");
                }
                /* Delete a subject in the subject's table ends */

                /*************************************NOTE******************************************
                /* Like we saw with subjects, any logged in user can read all affiliations and types
                and an admin user can read, create, update and delete an affiliation or a type
                Please refer the similar functions as SubjectDAO in AffiliationDAO and TypeDAO
                 ***********************************NOTE ENDS*************************************/

                /* Delete one or more users */
                int[] toDeleteUserIds = {247, 450, 405, 10, user.getUserId()}; // integer array of user ids
                if(userDAO.deleteUsers(toDeleteUserIds) == toDeleteUserIds.length){
                    System.out.println("User(s) deleted successfully");
                }
                /* Delete one or more users ends here */

                /* Make some other user an admin */
                User newAdminUser = userDAO.changeAdminStatus(3, true); // user id 3 becomes admin (true (1))
                System.out.println("New Admin User Details\n"+ModelUtil.getJson(newAdminUser));
                /* Make some other user an admin ends*/

            }
        } catch (PubPaperException e) { // Custom Exception used throughout the project
            System.out.println(e.getMessage());
        }
    }
}
