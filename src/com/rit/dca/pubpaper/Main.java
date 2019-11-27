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
        User validatedUser = user.login("sgorka@pct.edu", "32c1188618d2781556c80fedaa88c849c4f41ff3");
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

            // Add type
            System.out.println("\nAdd Type STATUS : " + accessType.addTypes("Aamchi Type"));

            // Edit type
            System.out.println("\nUpdate Type Affected Rows : " + accessType.changeType(8, "John Doe Type"));

            // Delete type
            System.out.println("\nDelete Type Affected Rows : " + accessType.deleteType(8));


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
