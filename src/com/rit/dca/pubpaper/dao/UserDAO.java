package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.model.User;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Database Connectivity and Access
 *
 * @author Dhiren Chandnani
 * Email: dc6288@rit.edu
 */
public class UserDAO {

    /**
     * Get the profile info for user object (properties like name, affiliation)
     *
     * @param userId - user id for which profile data is requested.
     * @return User object of the profile requested
     */
    private User getProfile(int userId) {
        return null;
    }

    /**
     * Set/Create profile info for existing/new user
     *
     * @param profileDetails HashMap (key-value pairs) of profile parameters
     * @return User object of the profile which is updated/created
     */
    private User setProfile(HashMap<String, Object> profileDetails) {


        //String lastName, String firstName, String email, String password, String affiliation
        return null;
    }

    /**
     * Resets user's password
     *
     * @param email emailId of the user
     * @return Boolean value indicating if password reset was successful or no
     */
    private boolean resetPassword(String email) {
        return false;
    }

    /**
     * Login a particular user into the system
     *
     * @param email    emailId of the user
     * @param password password of the user
     * @return User - user instance
     */
    public User login(String email, String password) {
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        if (connection.connect()) {

            // setup parameters for login query
            List<String> loginParams = new ArrayList<String>();
            loginParams.add(email);
            loginParams.add(password);

            // call get data on login query
            ArrayList<ArrayList<String>> validation = connection.getData(DAOUtil.CHECK_LOGIN, loginParams);

            // check if validated successfully
            if (validation.size() == 2) {
                // setup validated user's object
                User user = new User();
                user.setUserId(Integer.parseInt(validation.get(1).get(0)));
                user.setEmail(validation.get(1).get(3));
                user.setPwd(validation.get(1).get(4));
                user.setFirstName(validation.get(1).get(2));
                user.setLastName(validation.get(1).get(1));
                user.setAffiliationId(Integer.parseInt(validation.get(1).get(5)));
                user.setIsAdmin(Integer.parseInt(validation.get(1).get(8)));
                user.setCanReview(validation.get(1).get(6));
                user.setExpiration(validation.get(1).get(7));

                // close connection to database
                connection.close();

                // return validated user's instance
                return user;
            } else {
                connection.close();
            }
        }

        // return null if unable to validate user
        return null;

        // TODO : add exception handling in this method
    }

    /**
     * Set user's password for the first time
     *
     * @param password password of the user
     * @return Boolean value indicating if setting the password was successful
     */
    private boolean setPassword(String password) {
        return false;
    }

    /**
     * Check whether a user is an admin user
     *
     * @param connection MYSQLDatabase connection object
     * @param adminUserId id of the user
     * @return boolean status of validation
     */
    private boolean checkAdmin(MySQLDatabase connection, int adminUserId) {
        // setup parameters for admin query
        List<String> adminParams = new ArrayList<String>();
        adminParams.add(Integer.toString(adminUserId));

        // call get data on admin query
        ArrayList<ArrayList<String>> adminValidation = connection.getData(DAOUtil.CHECK_ADMIN, adminParams);

        // check if result contains single record and user is admin
        if (adminValidation.size() == 2) {
            return Integer.parseInt(adminValidation.get(1).get(0)) == 1;
        }
        return false;
    }

    /**
     * Get all users (Admin only)
     *
     * @param adminUserId id of the user
     * @return ArrayList<User> Arraylist of all users in the database
     */
    public ArrayList<User> getAllUsers(int adminUserId) {
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        ArrayList<User> userList = null;

        if (connection.connect()) {

            // check if user is admin
            if (checkAdmin(connection, adminUserId)) {

                // setup parameters for get all users query
                List<String> userParams = new ArrayList<String>();

                // call get data on get all users query
                ArrayList<ArrayList<String>> userData = connection.getData(DAOUtil.GET_ALL_USERS, userParams);

                // initialize returning user list
                userList = new ArrayList<User>();

                int iCount = 1;
                for (ArrayList<String> iUser : userData) {

                    // skip first row as meta data
                    if (iCount == 1) {
                        ++iCount;
                        continue;
                    }

                    // setup retrieved user's instance
                    User user = new User();
                    user.setUserId(Integer.parseInt(iUser.get(0)));
                    user.setEmail(iUser.get(3));
                    user.setPwd(iUser.get(4));
                    user.setFirstName(iUser.get(2));
                    user.setLastName(iUser.get(1));
                    user.setAffiliationId(Integer.parseInt(iUser.get(5)));
                    user.setIsAdmin(Integer.parseInt(iUser.get(8)));
                    user.setCanReview(iUser.get(6));
                    user.setExpiration(iUser.get(7));

                    // add user instance to returning array list
                    userList.add(user);
                }
            }

            // close connection to database
            connection.close();
        }

        // return null if unable to validate if user is admin to database
        // otherwise returns the populated userList
        return userList;
    }

    /**
     * Get a particular user information (Admin Only)
     *
     * @param adminUserId admin requesting for user information
     * @param userId requested user
     * @return User - user instance
     */
    public User getUser(int adminUserId, int userId) {
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        User user = null;

        if (connection.connect()) {

            // check if user is admin
            if (checkAdmin(connection, adminUserId)) {

                // setup parameters for get a single user query
                List<String> userParams = new ArrayList<String>();
                userParams.add(Integer.toString(userId));

                // call get data on get a single user query
                ArrayList<ArrayList<String>> userData = connection.getData(DAOUtil.GET_SINGLE_USER, userParams);

                if(userData.size() == 2){

                    // setup requested user's object
                    user = new User();
                    user.setUserId(Integer.parseInt(userData.get(1).get(0)));
                    user.setEmail(userData.get(1).get(3));
                    user.setPwd(userData.get(1).get(4));
                    user.setFirstName(userData.get(1).get(2));
                    user.setLastName(userData.get(1).get(1));
                    user.setAffiliationId(Integer.parseInt(userData.get(1).get(5)));
                    user.setIsAdmin(Integer.parseInt(userData.get(1).get(8)));
                    user.setCanReview(userData.get(1).get(6));
                    user.setExpiration(userData.get(1).get(7));
                }
            }

            // close connection to database
            connection.close();
        }

        // return null if unable to find user / not admin,
        // otherwise return the requested user
        return user;

        // TODO : add exception handling in this method
    }

    /**
     * Delete a particular user (Admin only)
     *
     * @param adminUserId id of the user
     * @param deletionIds integer array of userIds to delete
     * @return integer number of rowsAffected for the delete operation
     */
    public int deleteUsers(int adminUserId, int[] deletionIds) {
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if (connection.connect()) {

            // check if user is admin
            if(checkAdmin(connection, adminUserId)) {

                // setup parameters for delete user query
                List<String> userParams = new ArrayList<String>();
                userParams.add(convertToCommaIds(deletionIds));

                // call modify data on delete users query
                rowsAffected = connection.modifyData(DAOUtil.DELETE_USERS, userParams);

            }

            // close connection to database
            connection.close();
        }

        // return number of rows affected
        return rowsAffected;
    }

    /**
     * Return integer array to comma separated values
     *
     * @param userIdList integer array of userIds
     * @return String comma separated string of userIds
     */
    private String convertToCommaIds(int[] userIdList) {
        String result = "";

        for(int id: userIdList){
            result += ",";
        }

        return result.substring(0, result.lastIndexOf(","));
    }



}
