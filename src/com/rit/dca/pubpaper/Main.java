package com.rit.dca.pubpaper;

import com.rit.dca.pubpaper.dao.PaperDAO;
import com.rit.dca.pubpaper.dao.UserDAO;
import com.rit.dca.pubpaper.model.Paper;
import com.rit.dca.pubpaper.model.User;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
	    // App executes from here

        // Authenticate User
        UserDAO user = new UserDAO();
        User validatedUser = user.login("np5318@rit.edu", "google12");
        if( user != null){
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
        }
    }
}
