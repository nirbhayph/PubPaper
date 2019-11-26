package com.rit.dca.pubpaper;

import com.rit.dca.pubpaper.dao.AffiliationDAO;
import com.rit.dca.pubpaper.dao.PaperDAO;
import com.rit.dca.pubpaper.dao.TypeDAO;
import com.rit.dca.pubpaper.dao.UserDAO;
import com.rit.dca.pubpaper.model.Affiliation;
import com.rit.dca.pubpaper.model.Paper;
import com.rit.dca.pubpaper.model.Type;
import com.rit.dca.pubpaper.model.User;

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
            PaperDAO accessPapers = new PaperDAO();
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
            AffiliationDAO accessAffiliation = new AffiliationDAO();
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
            TypeDAO accessType = new TypeDAO();

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

            // Make user admin
            User aUser = user.changeAdminStatus(validatedUser.getUserId(), 3, true);
            System.out.println("\nAdmin USER :- isAdmin : " + aUser.getIsAdmin() + "\t|\tLastName : " + aUser.getLastName());

            // Add affiliation
            System.out.println("\nAdd Affiliation STATUS : " + accessAffiliation.addAffiliations(validatedUser.getUserId(), "Aamchi University"));

            // Edit Affiliation
            System.out.println("\nUpdate Affiliation Affected Rows : " + accessAffiliation.changeAffiliation(validatedUser.getUserId(), "Aamchi University", "John Doe University"));

            // Delete Affiliation
            System.out.println("\nDelete Affiliation Affected Rows : " + accessAffiliation.deleteAffiliation(validatedUser.getUserId(), 275));

            //Delete a User
            /*
            int users[] = {247, 450, 405, 10};
            System.out.println("DELETED: " + user.deleteUsers(validatedUser.getUserId(), users));
            */


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
