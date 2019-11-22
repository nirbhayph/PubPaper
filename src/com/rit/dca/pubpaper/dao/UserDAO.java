package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.model.User;

import java.util.HashMap;

/**
 * Database Connectivity and Access
 * @author Dhiren Chandnani
 * Email: dc6288@rit.edu
 */
public class UserDAO {

    /**
     * Get the profile info for user object (properties like name, affiliation)
     * @return User object of the profile requested
     */
    private User getProfile() {
        return null;
    }

    /**
     * Set/Create profile info for existing/new user
     * @param profileDetails HashMap (key-value pairs) of profile parameters
     * @return User object of the profile which is updated/created
     */
    private User setProfile(HashMap<String, Object> profileDetails) {
        //String lastName, String firstName, String email, String password, String affiliation
        return null;
    }

    /**
     * Resets user's password
     * @param email emailId of the user
     * @return Boolean value indicating if password reset was successful or no
     */
    private boolean resetPassword(String email) {
        return false;
    }

    /**
     * Login a particular user into the system
     * @param email    emailId of the user
     * @param password password of the user
     * @return Token - userId
     */
    private int login(String email, String password) {
        return -1;
    }

    /**
     * Set user's password for the first time
     * @param password password of the user
     * @return Boolean value indicating if setting the password was successful
     */
    private boolean setPassword(String password) {
        return false;
    }
}
