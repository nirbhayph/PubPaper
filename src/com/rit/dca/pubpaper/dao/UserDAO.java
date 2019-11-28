package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;
import com.rit.dca.pubpaper.mail.Email;
import com.rit.dca.pubpaper.model.Paper;
import com.rit.dca.pubpaper.model.User;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.apache.commons.codec.digest.DigestUtils;

/**
 * Database Connectivity and Access
 *
 * @author Dhiren Chandnani
 * Email: dc6288@rit.edu
 */
public class UserDAO {

    private int loggedInId = -1;

    public int getLoggedInId() {
        return loggedInId;
    }

    /**
     * Get the profile info for user object (all properties)
     *
     * @param userId - user id for which profile data is requested.
     * @return User object of the profile requested
     * @throws PubPaperException
     */
    public User getProfile(int userId) throws PubPaperException {
        User user = null;
        if (loggedInId == userId) {
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
                if (connection.connect()) {
                    // setup parameters for get a single user query
                    List<String> userParams = new ArrayList<String>();
                    userParams.add(Integer.toString(userId));

                    // call get data on get a single user query
                    ArrayList<ArrayList<String>> userData = connection.getData(DAOUtil.GET_SINGLE_USER, userParams);
                    if (userData.size() == 2) {

                        // setup requested user's object
                        user = new User();
                        int uid = Integer.parseInt(userData.get(1).get(0));
                        user.setUserId(uid);
                        user.setEmail(userData.get(1).get(3));
                        user.setFirstName(userData.get(1).get(2));
                        user.setLastName(userData.get(1).get(1));
                        user.setAffiliationId(Integer.parseInt(userData.get(1).get(8)));
                        user.setIsAdmin(Integer.parseInt(userData.get(1).get(7)));
                        user.setCanReview(userData.get(1).get(5));

                        // setup papers for this user
                        PaperAuthorDAO paperAuthorDAO = new PaperAuthorDAO(this);
                        user.setAllPapers(paperAuthorDAO.getAuthorPapers(uid));
                    }

                    // close the connection
                    connection.close();
                }
            }
            catch(PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_USER_PROFILE, CustomExceptionUtil.getCaller(e));
            }
        }
        return user;
    }

    /**
     * Set/Create profile info for existing/new user
     *
     * @param userData HashMap (key-value pairs) of profile parameters
     * @return User object of the profile which is updated/created
     * @throws PubPaperException
     */
    public User setProfile(HashMap<String, Object> userData) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        List<String> userParams = null;
        User user = null;
        int rowsAffected = -1;

        try {
            if (connection.connect()) {

                if (userData.containsKey("userId") && Integer.parseInt(userData.get("userId").toString()) == loggedInId) {
                    // get userId from hash map
                    int userId = Integer.parseInt(userData.get("userId").toString());

                    if (userId == loggedInId || checkAdmin(connection, loggedInId)) {
                        // create params list for check user query
                        userParams = new ArrayList<String>();
                        userParams.add(Integer.toString(userId));

                        // call get data on check user query
                        ArrayList<ArrayList<String>> validateUser = connection.getData(DAOUtil.CHECK_USER_EXIST, userParams);

                        // check if user exists
                        if (validateUser.size() == 2) {
                            userParams = new ArrayList<String>();
                            userParams.add(userData.get("lastName").toString());
                            userParams.add(userData.get("firstName").toString());
                            userParams.add(userData.get("email").toString());
                            userParams.add(userData.get("affiliationId").toString());
                            userParams.add(setPassword(userData.get("password").toString()));
                            userParams.add(Integer.toString(userId));

                            rowsAffected = connection.modifyData(DAOUtil.UPDATE_USER_PROFILE, userParams);

                            if (rowsAffected == 1) {
                                user = getProfile(userId);
                            }
                        }
                    }
                } else if (!(userData.containsKey("userId")) && loggedInId == -1) {
                    // Insert new user
                    userParams = new ArrayList<String>();
                    int newUserId = nextUserId(connection);
                    userParams.add(Integer.toString(newUserId));
                    userParams.add(userData.get("lastName").toString());
                    userParams.add(userData.get("firstName").toString());
                    userParams.add(userData.get("email").toString());
                    userParams.add(setPassword(userData.get("password").toString()));
                    userParams.add(userData.get("affiliationId").toString());

                    rowsAffected = connection.modifyData(DAOUtil.INSERT_NEW_USER, userParams);

                    if (rowsAffected == 1) {
                        loggedInId = newUserId;
                        user = getProfile(newUserId);
                        loggedInId = -1;
                    }
                }
                //Close the connection
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_SET_USER_PROFILE, CustomExceptionUtil.getCaller(e));
        }
        return user;
    }

    /**
     * Get the new user id to be inserted
     *
     * @param connection MySQLDatabase connection object
     * @return int new user id to use
     */
    private int nextUserId(MySQLDatabase connection) {
        try {
            int newUserId = -1;

            // create params list for next user id query
            List<String> userParams = new ArrayList<String>();

            // call get data on check user query
            ArrayList<ArrayList<String>> lastUser = connection.getData(DAOUtil.GET_NEXT_USER_ID, userParams);

            if (lastUser.size() == 2) {
                newUserId = Integer.parseInt(lastUser.get(1).get(0)) + 1;
            }

            return newUserId;
        }
        catch(PubPaperException e){
            return -1;
        }
    }

    /**
     * Resets user's password
     *
     * @param email emailId of the user
     * @return Boolean value indicating if password reset was successful or no
     * @throws PubPaperException
     */
    public boolean resetPassword(String email) throws PubPaperException{
        /* Was working, isn't now because of some deprecation issue javax/activation/DataHandler */
        /* Would like to hear your feedback on the same */
        /*
        try {
            if (this.loggedInId == -1) {
                MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
                if (connection.connect()) {
                    new Email().sendMail(email, "121disandkansdka");
                    connection.close();
                }
            }
            return false;
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_RESET_PASSWORD, CustomExceptionUtil.getCaller(e));
        }
        */
        return false;
    }

    /**
     * Login a particular user into the system
     *
     * @param email    emailId of the user
     * @param password password of the user
     * @return User - user instance
     * @throws PubPaperException
     */
    public User login(String email, String password) throws PubPaperException{
        if (loggedInId == -1) {
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
                if (connection.connect()) {

                    // setup parameters for login query
                    List<String> loginParams = new ArrayList<String>();
                    loginParams.add(email);

                    // call get data on login query
                    ArrayList<ArrayList<String>> validation = connection.getData(DAOUtil.CHECK_LOGIN, loginParams);

                    boolean userValidated = false;
                    if (validation.size() == 2) {
                        String hash = validation.get(1).get(4);
                        if (hash.equals(DigestUtils.shaHex(password))) {
                            if (!(new SimpleDateFormat("yyyyMMddhhmmss").parse(validation.get(1).get(6)).before(new Date()))) {
                                userValidated = true;
                            }
                        }
                    }

                    // check if validated successfully
                    if (userValidated) {

                        // setup validated user's object
                        User user = new User();

                        int userId = Integer.parseInt(validation.get(1).get(0));
                        user.setUserId(userId);
                        user.setEmail(validation.get(1).get(3));
                        user.setFirstName(validation.get(1).get(2));
                        user.setLastName(validation.get(1).get(1));
                        user.setAffiliationId(Integer.parseInt(validation.get(1).get(8)));
                        user.setIsAdmin(Integer.parseInt(validation.get(1).get(7)));
                        user.setCanReview(validation.get(1).get(5));

                        // setup papers for this user
                        PaperAuthorDAO paperAuthorDAO = new PaperAuthorDAO(this);
                        user.setAllPapers(paperAuthorDAO.getAuthorPapers(userId));

                        // close connection to database
                        connection.close();

                        loggedInId = user.getUserId();

                        // return validated user's instance
                        return user;
                    } else {
                        connection.close();
                        throw new PubPaperException(new Exception(), DAOUtil.UNABLE_TO_LOGIN, CustomExceptionUtil.getCaller(new Exception()));
                    }
                }
            }
            catch (ParseException p){
                throw new PubPaperException(p, DAOUtil.UNABLE_TO_LOGIN_TRY_LATER, CustomExceptionUtil.getCaller(p));
            }
            catch(PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_LOGIN, CustomExceptionUtil.getCaller(e));
            }
        }

        // return null if unable to validate user
        return null;
    }

    /**
     * Returns hashed password
     *
     * @param password password of the user
     * @return String hashed password string
     */
    private String setPassword(String password) {
        String hashedPassword = DigestUtils.shaHex(password);
        return hashedPassword;
    }

    /**
     * Check whether a user is an admin user
     *
     * @param connection  MYSQLDatabase connection object
     * @param adminUserId id of the user
     * @return boolean status of validation
     * @throws PubPaperException
     */
    protected boolean checkAdmin(MySQLDatabase connection, int adminUserId) throws PubPaperException{
        try {
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
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_CHECK_ADMIN, CustomExceptionUtil.getCaller(e));
        }
    }

    /**
     * Get all users (Admin only)
     *
     * @return ArrayList<User> Arraylist of all users in the database
     * @throws PubPaperException
     */
    public ArrayList<User> getAllUsers() throws PubPaperException{
        ArrayList<User> userList = null;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        try {
            if (connection.connect()) {
                // check if user is admin
                if (loggedInId != -1 && checkAdmin(connection, loggedInId)) {
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

                        int userId = Integer.parseInt(iUser.get(0));
                        user.setUserId(userId);
                        user.setEmail(iUser.get(3));
                        user.setFirstName(iUser.get(2));
                        user.setLastName(iUser.get(1));
                        user.setAffiliationId(Integer.parseInt(iUser.get(8)));
                        user.setIsAdmin(Integer.parseInt(iUser.get(7)));
                        user.setCanReview(iUser.get(5));

                        // setup paper authors array for this user
                        PaperAuthorDAO paperAuthorDAO = new PaperAuthorDAO(this);
                        user.setAllPapers(paperAuthorDAO.getAuthorPapers(userId));

                        // add user instance to returning array list
                        userList.add(user);
                    }
                }

                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_ALL_USERS, CustomExceptionUtil.getCaller(e));
        }
        // return null if unable to validate if user is admin to database
        // otherwise returns the populated userList
        return userList;
    }

    /**
     * Get a particular user information (Admin Only)
     *
     * @param userId requested user
     * @return User - user instance
     * @throws PubPaperException
     */
    public User getUser(int userId) throws PubPaperException{
        User user = null;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        try {
            if (connection.connect()) {
                if (loggedInId != -1 && checkAdmin(connection, loggedInId)) {
                    // setup parameters for get a single user query
                    List<String> userParams = new ArrayList<String>();
                    userParams.add(Integer.toString(userId));

                    // call get data on get a single user query
                    ArrayList<ArrayList<String>> userData = connection.getData(DAOUtil.GET_SINGLE_USER, userParams);
                    if (userData.size() == 2) {

                        // setup requested user's object
                        user = new User();
                        user.setUserId(Integer.parseInt(userData.get(1).get(0)));
                        user.setEmail(userData.get(1).get(3));
                        user.setFirstName(userData.get(1).get(2));
                        user.setLastName(userData.get(1).get(1));
                        user.setAffiliationId(Integer.parseInt(userData.get(1).get(8)));
                        user.setIsAdmin(Integer.parseInt(userData.get(1).get(7)));
                        user.setCanReview(userData.get(1).get(5));

                        // setup paper author for this user
                        PaperAuthorDAO paperAuthorDAO = new PaperAuthorDAO(this);
                        user.setAllPapers(paperAuthorDAO.getAuthorPapers(userId));
                    }

                    // close connection to database
                    connection.close();
                }
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_USER, CustomExceptionUtil.getCaller(e));
        }

        // return null if unable to find user / not admin,
        // otherwise return the requested user
        return user;
    }

    /**
     * Delete a particular user (Admin only)
     *
     * @param deletionIds integer array of userIds to delete
     * @return integer number of rowsAffected for the delete operation
     * @throws PubPaperException
     */
    public int deleteUsers(int[] deletionIds) throws PubPaperException{
        int userRowsAffected = 0;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                if (loggedInId != -1 && checkAdmin(connection, loggedInId)) {
                    // pass through each user id
                    for (int id : deletionIds) {
                        connection.startTransaction();
                        int rowsAffected = 0;

                        //variable to check if rollback should be done or not
                        boolean rollbackCheck = false;

                        // setup parameters to get all papers query
                        List<String> queryParams = new ArrayList<String>();
                        queryParams.add(Integer.toString(id));

                        // call get data on get all paperIds of a user
                        PaperDAO accessPapers = new PaperDAO(this);
                        ArrayList<Paper> papersData = accessPapers.getPapers(id);

                        //Check if the user has any papers associated
                        if (papersData.size() >= 1) {
                            // Pass through each paper id
                            for (Paper paper : papersData) {
                                int paperId = paper.getPaperId();

                                PaperSubjectDAO accessPaperSubjects = new PaperSubjectDAO(this);
                                // call modify data to delete papers from paper subjects
                                rowsAffected = accessPaperSubjects.deletePaperSubjects(paperId);

                                if (rowsAffected >= 1) {

                                    PaperAuthorDAO accessPaperAuthors = new PaperAuthorDAO(this);
                                    // call modify data to delete papers from paper subjects
                                    rowsAffected = accessPaperAuthors.deletePaperAuthors(paperId);

                                    if (rowsAffected < 1) {
                                        rollbackCheck = true;
                                        break;
                                    }
                                } else {
                                    rollbackCheck = true;
                                    break;
                                }
                            }

                            // setup parameters to delete users
                            queryParams = new ArrayList<String>();
                            queryParams.add(Integer.toString(id));

                            //Check if previous commands ran perfectly then continue
                            if (!rollbackCheck) {

                                rowsAffected = accessPapers.deleteUserPaper(id);

                                if (rowsAffected >= 1) {
                                    // call modify data to delete users
                                    userRowsAffected += connection.modifyData(DAOUtil.DELETE_USERS, queryParams);
                                    if (userRowsAffected < 1) {
                                        rollbackCheck = true;
                                    }
                                } else {
                                    rollbackCheck = true;
                                }
                            }
                        }
                        //if no associated paper only delete from user table
                        else {
                            userRowsAffected += connection.modifyData(DAOUtil.DELETE_USERS, queryParams);
                            if (userRowsAffected < 1) {
                                rollbackCheck = true;
                            }
                        }

                        //Check if everything ran successfully else rollback
                        if (!rollbackCheck) {
                            connection.endTransaction();
                        } else {
                            connection.rollbackTransaction();
                        }
                    }
                    // close connection to database
                    connection.close();
                }
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_USER, CustomExceptionUtil.getCaller(e));
        }

        // return number of rows affected
        return userRowsAffected;
    }

    /**
     * Make a particular user Admin (Admin Only)
     *
     * @param userId      requested user
     * @param adminStatus admin status to be given to the user
     * @return User - user instance
     * @throws PubPaperException
     */
    public User changeAdminStatus(int userId, boolean adminStatus) throws PubPaperException{
        ArrayList<String> userParams = null;
        User user = null;
        int rowsAffected = 0;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

        try {
            if (connection.connect()) {
                if (loggedInId != -1 && checkAdmin(connection, loggedInId)) {

                    // create params list for check user query
                    userParams = new ArrayList<String>();
                    userParams.add(Integer.toString(userId));

                    // call get data on check user query
                    ArrayList<ArrayList<String>> validateUser = connection.getData(DAOUtil.CHECK_USER_EXIST, userParams);

                    // check if user exists
                    if (validateUser.size() == 2) {

                        // create params list for set admin query
                        userParams = new ArrayList<String>();
                        if (adminStatus) {
                            userParams.add(Integer.toString(1));
                        } else {
                            userParams.add(Integer.toString(0));
                        }
                        userParams.add(Integer.toString(userId));

                        // call modify data on set user as admin query
                        rowsAffected = connection.modifyData(DAOUtil.SET_USER_AS_ADMIN, userParams);
                        if (rowsAffected == 1) {
                            user = getUser(userId);
                        }
                    }
                    connection.close();
                }
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_CHANGE_ADMIN_STATUS, CustomExceptionUtil.getCaller(e));
        }
        return user;
    }

    /**
     * Change review status of a particular user (Admin Only)
     *
     * @param userId       requested user
     * @param reviewStatus review status to be given to the user
     * @return User - user instance
     * @throws PubPaperException
     */
    public User changeReviewStatus(int userId, boolean reviewStatus) throws PubPaperException{
        ArrayList<String> userParams = null;
        User user = null;
        int rowsAffected = 0;
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        try {
            if (connection.connect()) {
                if (loggedInId != -1 && checkAdmin(connection, loggedInId)) {
                    // create params list for check user query
                    userParams = new ArrayList<String>();
                    userParams.add(Integer.toString(userId));

                    // call get data on check user query
                    ArrayList<ArrayList<String>> validateUser = connection.getData(DAOUtil.CHECK_USER_EXIST, userParams);

                    // check if user exists
                    if (validateUser.size() == 2) {

                        // create params list for set admin query
                        userParams = new ArrayList<String>();
                        if (reviewStatus) {
                            userParams.add(Integer.toString(1));
                        } else {
                            userParams.add(Integer.toString(0));
                        }
                        userParams.add(Integer.toString(userId));

                        // call modify data on set user as admin query
                        rowsAffected = connection.modifyData(DAOUtil.SET_USER_CAN_REVIEW, userParams);
                        if (rowsAffected == 1) {
                            user = getUser(userId);
                        }
                    }
                    connection.close();
                }
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_CHANGE_REVIEW_STATUS, CustomExceptionUtil.getCaller(e));
        }
        return user;
    }

    public boolean logout() {
        if (loggedInId != -1) {
            loggedInId = -1;
            return true;
        }
        return false;
    }
}
