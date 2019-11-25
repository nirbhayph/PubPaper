package com.rit.dca.pubpaper;

import com.rit.dca.pubpaper.dao.UserDAO;
import com.rit.dca.pubpaper.model.User;

public class Main {

    public static void main(String[] args) {
	    // App executes from here

        // Authenticate User
        UserDAO user = new UserDAO();
        User validatedUser = user.login("np5318@rit.edu", "google12");
        if( user != null){
            System.out.println("Logged In Successfully - " + validatedUser.getUserId());
        }
    }
}
