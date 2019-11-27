package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.database.MySQLDatabase;
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
     */
    public Type getType(int paperId){
        Type type = null;

        if(this.userAccess.getLoggedInId() != -1) {
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
            if (connection.connect()) {

                // setup parameters for types query
                List<String> typeParams = new ArrayList<String>();
                typeParams.add(Integer.toString(paperId));

                // call get data on types query
                ArrayList<ArrayList<String>> typesData = connection.getData(DAOUtil.GET_PAPER_TYPE_ID, typeParams);

                if (typesData.size() == 2) {
                    String typeId = typesData.get(1).get(0);

                    typeParams = new ArrayList<String>();
                    typeParams.add(typeId);

                    typesData = connection.getData(DAOUtil.GET_PAPER_TYPE_WITH_ID, typeParams);

                    if (typesData.size() == 2) {
                        type = new Type();
                        type.setTypeId(Integer.parseInt(typeId));
                        type.setTypeName(typesData.get(1).get(1));
                    }
                }

                // close connection to database
                connection.close();
            }
        }

        return type;

        // TODO : add exception handling in this method
    }

    /**
     * Gives a list of all type instances
     * @return ArrayList of type instances
     */
    public ArrayList<Type> getTypes(){
        ArrayList<Type> types = new ArrayList<Type>();

        if(this.userAccess.getLoggedInId() != -1) {
            MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);

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

        return types;

        // TODO : Manage exceptions for this method
    }


    private int nextTypeId(MySQLDatabase connection) {

        int nextTypeId = -1;

        // create params list for next user id query
        List<String> typeParams = new ArrayList<String>();

        // call get data on check type query
        ArrayList<ArrayList<String>> lastType = connection.getData(DAOUtil.GET_LAST_TYPE_ID, typeParams);

        if (lastType.size() == 2) {
            nextTypeId = Integer.parseInt(lastType.get(1).get(0)) + 1;
        }

        return nextTypeId;
    }


    public boolean addTypes(String typeName){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        boolean addStatus = false;

        if(connection.connect()) {
            if(this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                // setup parameters for add type query
                List<String> typeParams = new ArrayList<String>();
                typeParams.add(Integer.toString(nextTypeId(connection)));
                typeParams.add(typeName);

                // call modify data to add type query
                int rowsAffected = connection.modifyData(DAOUtil.INSERT_TYPE, typeParams);
                if(rowsAffected == 1){
                    addStatus = true;
                }
            }
            // close connection to database
            connection.close();
        }

        return addStatus;
    }


    public int deleteType(int typeId){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if(connection.connect()) {
            if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                // setup parameters for delete type query
                List<String> typeParams = new ArrayList<String>();
                typeParams.add(Integer.toString(typeId));

                // call modify data to delete type query
                rowsAffected = connection.modifyData(DAOUtil.DELETE_TYPE, typeParams);
                if(rowsAffected == 1){
                    int papersRowsAffected = connection.modifyData(DAOUtil.SET_PAPER_TYPE_NULL, typeParams);
                }
            }
            // close connection to database
            connection.close();
        }
        return rowsAffected;
    }

    public int changeType(int typeId, String newTypeName){
        MySQLDatabase connection = new MySQLDatabase(DAOUtil.HOST, DAOUtil.USER_NAME, DAOUtil.PASSWORD);
        int rowsAffected = 0;

        if(connection.connect()) {

            if (this.userAccess.checkAdmin(connection, this.userAccess.getLoggedInId())) {
                // setup parameters for set type query
                List<String> typeParams = new ArrayList<String>();
                typeParams.add(newTypeName);
                typeParams.add(Integer.toString(typeId));

                // call modify data to update type
                rowsAffected = connection.modifyData(DAOUtil.UPDATE_TYPE, typeParams);
            }
            // close connection to database
            connection.close();
        }

        return rowsAffected;
    }

}