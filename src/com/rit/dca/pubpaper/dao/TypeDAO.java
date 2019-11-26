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
}