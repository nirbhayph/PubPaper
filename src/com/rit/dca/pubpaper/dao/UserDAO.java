package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Database Connectivity and Access
 * @author Dhiren Chandnani
 * Email: dc6288@rit.edu
 */
public class UserDAO {

    /**
     * Get the profile info for user object (properties like name, affiliation)
     * @param userId - user id for which profile data is requested.
     * @return User object of the profile requested
     */
    private User getProfile(int userId) {
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
    public User login(String email, String password) {
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.host, DAOUtil.userName, DAOUtil.password);

        if(connection.connect()){

            // setup parameters for login query
            List<String> loginParams= new ArrayList<String>();
            loginParams.add(email);
            loginParams.add(password);

            // call get data on login query
            ArrayList<ArrayList<String>> validation = connection.getData(DAOUtil.checkLogin, loginParams);

            // check if validated successfully
            if(validation.size() == 2){
                // setup validated user's object
                User user = new User();
                user.setUserId(Integer.parseInt(validation.get(1).get(0)));
                user.setEmail(validation.get(1).get(3));
                user.setPwd(validation.get(1).get(4));
                user.setFirstName(validation.get(1).get(2));
                user.setLastName(validation.get(1).get(1));
                user.setAffiliationId(-1); //validation.get(1).get(5)
                user.setIsAdmin(Integer.parseInt(validation.get(1).get(8)));
                user.setCanReview(validation.get(1).get(6));
                user.setExpiration(validation.get(1).get(7));

                // close connection to database
                connection.close();

                // return validated user's instance
                return user;
            }
        }

        // return null if unable to validate user
        return null;

        // TODO : add exception handling in this method
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
