package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
import com.rit.dca.pubpaper.exception.CustomExceptionUtil;
import com.rit.dca.pubpaper.exception.PubPaperException;
import com.rit.dca.pubpaper.model.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class TypeDAO {
    private UserDAO userAccess;

    public TypeDAO(UserDAO userAccess){
        this.userAccess = userAccess;
    }

    /**
     * Gives a type's instance
     * @param paperId for which type instance is required
     * @return Type instance for paper id provided
     * @throws PubPaperException
     */
    public Type getType(int paperId) throws PubPaperException{
        Type type = null;

        // Check user is logged in
        if(this.userAccess.getLoggedInId() != -1) {
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
            try {
                if (connection.connect()) {

                    // setup parameters for get paper type query
                    List<String> typeParams = new ArrayList<String>();
                    typeParams.add(Integer.toString(paperId));

                    // call get data on get paper type query
                    ArrayList<ArrayList<String>> typeData = connection.getData(DAOUtil.GET_PAPER_TYPE_ID, typeParams);

                    if (typeData.size() == 2) {
                        String typeId = typeData.get(1).get(0);

                        // setup parameters for get paper type with id query
                        typeParams = new ArrayList<String>();
                        typeParams.add(typeId);

                        // call get data on get paper type with id query
                        typeData = connection.getData(DAOUtil.GET_PAPER_TYPE_WITH_ID, typeParams);

                        if (typeData.size() == 2) {

                            // set type instance values
                            type = new Type();
                            type.setTypeId(Integer.parseInt(typeId));
                            type.setTypeName(typeData.get(1).get(1));
                        }
                    }

                    // close connection to database
                    connection.close();
                }
            }
            catch(PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_TYPE, CustomExceptionUtil.getCaller(e));
            }
        }

        return type;
    }

    /**
     * Gives a list of all type instances
     * @return ArrayList of type instances
     * @throws PubPaperException
     */
    public ArrayList<Type> getTypes() throws PubPaperException{
        ArrayList<Type> types = new ArrayList<Type>();

        // Check user is logged in
        if(this.userAccess.getLoggedInId() != -1) {
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

            try {
                if (connection.connect()) {

                    // setup parameters for types query
                    List<String> typeParams = new ArrayList<String>();

                    // call get data on types query
                    ArrayList<ArrayList<String>> typesData = connection.getData(DAOUtil.GET_ALL_TYPES, typeParams);

                    int iCount = 1;
                    for (ArrayList<String> iType : typesData) {

                        // skip first row as meta data
                        if (iCount == 1) {
                            ++iCount;
                            continue;
                        }

                        // setup retrieved type's instance
                        Type type = new Type();
                        type.setTypeName(iType.get(1));
                        type.setTypeId(Integer.parseInt(iType.get(0)));

                        // add type instance to returning array list
                        types.add(type);
                    }

                    // close connection to database
                    connection.close();
                }
            }
            catch(PubPaperException e){
                throw new PubPaperException(e, DAOUtil.UNABLE_TO_GET_TYPES, CustomExceptionUtil.getCaller(e));
            }
        }
        return types;
    }


    /**
     * Get the new typeId to insert new type
     * @param connection MYSQLDatabase connection parameter
     * @return int next type id or -1
     */
    private int nextTypeId(MySQLDatabase connection){
        try {
            int nextTypeId = -1;

            // create params list for next type id query
            List<String> typeParams = new ArrayList<String>();

            // call get data on check type query
            ArrayList<ArrayList<String>> lastType = connection.getData(DAOUtil.GET_LAST_TYPE_ID, typeParams);

            if (lastType.size() == 2) {
                nextTypeId = Integer.parseInt(lastType.get(1).get(0)) + 1;
            }

            return nextTypeId;
        }
        catch(PubPaperException e){
            return -1;
        }
    }

    /**
     * Add a new type to the database (only ADMIN)
     * @param typeName typeName to be added to the database
     * @return boolean success or failure to add a new type
     * @throws PubPaperException
     */
    public boolean addTypes(String typeName) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        boolean addStatus = false;
        try {
            if (connection.connect()) {
                // check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    connection.startTransaction();
                    // setup parameters for add type query
                    List<String> typeParams = new ArrayList<String>();

                    // Get the next type id to be inserted
                    int nextTId = nextTypeId(connection);
                    if (nextTId != -1) {
                        typeParams.add(Integer.toString(nextTId));
                        typeParams.add(typeName);

                        // call modify data to add new type query
                        int rowsAffected = connection.modifyData(DAOUtil.INSERT_TYPE, typeParams);
                        if (rowsAffected == 1) {
                            addStatus = true;
                            connection.endTransaction();
                        } else {
                            connection.rollbackTransaction();
                        }
                    } else {
                        connection.rollbackTransaction();
                    }
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_ADD_TYPE, CustomExceptionUtil.getCaller(e));
        }

        return addStatus;
    }


    /**
     * Delete a type from the database (only ADMIN)
     * @param typeId typeId to be deleted from the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int deleteType(int typeId) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        try {
            if (connection.connect()) {
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                    // setup parameters for delete type query
                    List<String> typeParams = new ArrayList<String>();
                    typeParams.add(Integer.toString(typeId));

                    // call modify data to delete type query
                    rowsAffected = connection.modifyData(DAOUtil.DELETE_TYPE, typeParams);
                    if (rowsAffected == 1) {
                        // call modify data to set paper type null of the deleted type
                        connection.modifyData(DAOUtil.SET_PAPER_TYPE_NULL, typeParams);
                    }
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_DELETE_TYPE, CustomExceptionUtil.getCaller(e));
        }

        return rowsAffected;
    }

    /**
     * Update a type in the database (only ADMIN)
     * @param typeId typeId to be update in the database
     * @param newTypeName typeName to be to be replaced with in the database
     * @return int number of rows affected
     * @throws PubPaperException
     */
    public int changeType(int typeId, String newTypeName) throws PubPaperException{
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        try {
            if (connection.connect()) {
                // check if user is admin
                if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {

                    // setup parameters for update type query
                    List<String> typeParams = new ArrayList<String>();
                    typeParams.add(newTypeName);
                    typeParams.add(Integer.toString(typeId));

                    // call modify data to update type
                    rowsAffected = connection.modifyData(DAOUtil.UPDATE_TYPE, typeParams);
                }
                // close connection to database
                connection.close();
            }
        }
        catch(PubPaperException e){
            throw new PubPaperException(e, DAOUtil.UNABLE_TO_CHANGE_TYPE, CustomExceptionUtil.getCaller(e));
        }
        return rowsAffected;
    }

}