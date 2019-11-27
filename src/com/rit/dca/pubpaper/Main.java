package com.rit.dca.pubpaper;

import com.rit.dca.pubpaper.dao.*;
import com.rit.dca.pubpaper.model.*;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;

public class Main {

    public static void main(String[] args) {
	    // App executes from here

        // Authenticate User
        UserDAO user = new UserDAO();
        User validatedUser = user.login("stevez@cssconsult.com", "c704e98e5f46b4afd32682cf53d740524b4f6910");
        if(validatedUser != null){
            System.out.println("Logged In Successfully - " + validatedUser.getUserId());

            // Get all papers for validated user
            PaperDAO accessPapers = new PaperDAO(user);
            ArrayList<Paper> userPapers = accessPapers.getPapers(validatedUser.getUserId());

            for(Paper paper : userPapers){
                System.out.println(paper.getTitle());
            }

            // Get a paper for paperId requested
            Paper paper = accessPapers.getPaper(47);
            if(paper != null){
                System.out.println("Retrieved paper - "+paper.getTitle().replace('+', ' '));
            }

            // Get affiliation instance for validated user
            AffiliationDAO accessAffiliation = new AffiliationDAO(user);
            Affiliation affiliation = accessAffiliation.getAffiliation(validatedUser.getUserId());
            if(affiliation != null){
                System.out.println(affiliation.getAffiliationId() + " - " + affiliation.getAffiliationName());
            }

            // Get all affiliations
            ArrayList<Affiliation> affiliations = accessAffiliation.getAffiliations();
            for(Affiliation aff : affiliations){
                System.out.println(aff.getAffiliationId() + " - " + aff.getAffiliationName());
            }

            // Get all types
            TypeDAO accessType = new TypeDAO(user);

            ArrayList<Type> types = accessType.getTypes();
            for(Type type : types){
                System.out.println(type.getTypeId() + " - " + type.getTypeName());
            }

            // Get type instance for a paper id
            Type type = accessType.getType(9995);
            if(type != null){
                System.out.println("\nPaper's Type Is : " + type.getTypeId() + " - " + type.getTypeName());
            }

            // Get all users
            ArrayList<User> userList = user.getAllUsers(validatedUser.getUserId());
            for(User iUser: userList){
                System.out.println("\nFirstName : " + iUser.getFirstName() + "\t|\tLastName : " + iUser.getLastName());
            }

            // Get a user
            User rUser = user.getUser(validatedUser.getUserId(), 3);
            System.out.println("\nA USER :- FirstName : " + rUser.getFirstName() + "\t|\tLastName : " + rUser.getLastName());

            /*
            // Make user admin
            User aUser = user.changeAdminStatus(validatedUser.getUserId(), 3, true);
            System.out.println("\nAdmin USER :- isAdmin : " + aUser.getIsAdmin() + "\t|\tLastName : " + aUser.getLastName());

            // Give user review status
            rUser = user.changeReviewStatus(validatedUser.getUserId(), 3, false);
            System.out.println("\nAdmin USER :- canReview : " + rUser.getCanReview() + "\t|\tLastName : " + rUser.getLastName());


            // Add affiliation
            System.out.println("\nAdd Affiliation STATUS : " + accessAffiliation.addAffiliations("Aamchi University"));

            // Edit Affiliation
            System.out.println("\nUpdate Affiliation Affected Rows : " + accessAffiliation.changeAffiliation(275, "John Doe University"));

            // Delete Affiliation
            System.out.println("\nDelete Affiliation Affected Rows : " + accessAffiliation.deleteAffiliation(275));
            */

            /*
            // Add type
            System.out.println("\nAdd Type STATUS : " + accessType.addTypes("Aamchi Type"));

            // Edit type
            System.out.println("\nUpdate Type Affected Rows : " + accessType.changeType(8, "John Doe Type"));

            // Delete type
            System.out.println("\nDelete Type Affected Rows : " + accessType.deleteType(8));
            */

            /*
            SubjectDAO accessSubject = new SubjectDAO(user);
            // Add subject
            System.out.println("\nAdd Subject STATUS : " + accessSubject.addSubject("Aamchi Subject"));

            // Edit subject
            System.out.println("\nUpdate Subject Affected Rows : " + accessSubject.changeSubject(27, "John Doe Subject"));

            // Delete subject
            System.out.println("\nDelete Subject Affected Rows : " + accessSubject.deleteSubject(27));

            */

            /*
            PaperSubjectDAO accessPaperSubject = new PaperSubjectDAO(user);
            // Add paper subject
            int subjectIds[] = {25, 23};
            System.out.println("\nAdd PaperSubject: " + accessPaperSubject.addPaperSubject(99, subjectIds));

            // Update paper subject
            System.out.println("\nUpdate PaperSubject: " + accessPaperSubject.updatePaperSubject(3, 25, 23));
            */

            /*
            PaperAuthorDAO accessPaperAuthor = new PaperAuthorDAO(user);

            // Get Display Order
            System.out.println("Paper Author Display Order: " + accessPaperAuthor.getDisplayOrder(52, 149));

            // Add Paper Author
            System.out.println("Add Paper Author: " + accessPaperAuthor.addPaperAuthor(52, 159,5));

            // Update Paper Author
            System.out.println("Update Paper Author: " + accessPaperAuthor.updatePaperAuthor(52, 159,154));

            // Update Display Order
            System.out.println("Update Paper Author Display Order: " + accessPaperAuthor.updateDisplayOrder(52, 6,154));

            // Delete a single Paper Author
            System.out.println("Update Paper Author Display Order: " + accessPaperAuthor.deleteSinglePaperAuthor(52, 154));

            */


            /*
            // See a User's Paper's Subjects
            System.out.println("VALIDATED USER's ALL PAPERS SUBJECTS");
            ArrayList<Integer> authorsPapersIds = validatedUser.getAllPapers();

            for(int paperId : authorsPapersIds){
                PaperDAO paperDAO = new PaperDAO(user);
                ArrayList<Subject> subjects = paperDAO.getPaper(paperId).getSubjects();
                for(Subject subject : subjects){
                    System.out.println(subject.getSubjectName());
                }
                System.out.println(paperDAO.getPaper(paperId).getPaperId() + " ENDS HERE ");
            }

            // See a User's Papers Authors
            System.out.println("VALIDATED USER's ALL PAPERS AUTHORS");
            ArrayList<Integer> authorPapersIds = validatedUser.getAllPapers();

            for(int paperId : authorPapersIds){
                PaperDAO paperDAO = new PaperDAO(user);
                ArrayList<Integer> authorsIds = paperDAO.getPaper(paperId).getAuthors();
                for(int authorId : authorsIds){
                    System.out.println(user.getPublicProfile(authorId).getFirstName());
                }
                System.out.println(paperDAO.getPaper(paperId).getPaperId() + " ENDS HERE ");
            }

             */

            // Delete Paper
            PaperDAO paperDAO = new PaperDAO(user);
            System.out.println("DELETED PAPER ROWS: "+ paperDAO.deletePaper(2));



            // Update Paper
            HashMap<String, Object> updatePaperData = new HashMap<String, Object>();
            updatePaperData.put("paperId", 103);
            updatePaperData.put("title", "Mixed Reality");
            updatePaperData.put("abstract", "This is my paper abstract");
            updatePaperData.put("submissionType", 2);
            updatePaperData.put("fileId", "ZZX22");
            int subjectIdArr[] = {5, 9, 10, 13, 16};
            updatePaperData.put("subjects", subjectIdArr);
            int coAuthorsIdArr[] = {1};
            updatePaperData.put("coAuthors", coAuthorsIdArr);

            //PaperDAO paperDAO = new PaperDAO(user);
            //paperDAO.setPaper(updatePaperData);


            // Create New Paper

            HashMap<String, Object> newPaperData = new HashMap<String, Object>();
            //newPaperData.put("paperId", 9910);
            newPaperData.put("title", "nirbhay rules");
            newPaperData.put("abstract", "This is the boss");
            newPaperData.put("submissionType", 1);
            newPaperData.put("fileId", "NQAA");
            int _subjectIdArr[] = {5, 9, 10};
            newPaperData.put("subjects", _subjectIdArr);
            int _coAuthorsIdArr[] = {389};
            newPaperData.put("coAuthors", _coAuthorsIdArr);

            //paperDAO.setPaper(newPaperData);


            //Delete a User
            /*int users[] = {247, 450, 405, 10};
            System.out.println("DELETED: " + user.deleteUsers(validatedUser.getUserId(), users));*/


            // update a user
            /*
            HashMap<String, Object> userData = new HashMap<String, Object>();
            userData.put("userId", 2);
            userData.put("lastName", "Stark");
            userData.put("firstName", "Tony");
            userData.put("email", "luntb@byu.edu");
            userData.put("affiliationId", 77);

            User updatedUser = user.setProfile(userData);
            System.out.println("\nUPDATED USER:- FirstName : " + updatedUser.getFirstName() + "\t|\tLastName : " + updatedUser.getLastName());
            */

        }

        // Register a user
        /*
        UserDAO newReg = new UserDAO();
        HashMap<String, Object> newUserData = new HashMap<String, Object>();
        newUserData.put("lastName", "Stark");
        newUserData.put("firstName", "Tony");
        newUserData.put("email", "ironman@avengers.com");
        newUserData.put("password", "google12");
        newUserData.put("affiliationId", 77);

        User insertedUser = newReg.setProfile(newUserData);
        System.out.println("\nINSERTED USER:- FirstName : " + insertedUser.getFirstName() + "\t|\tLastName : " + insertedUser.getLastName());
        */

    }
}
