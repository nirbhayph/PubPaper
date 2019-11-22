package com.rit.dca.pubpaper.dao;

import com.rit.dca.pubpaper.model.Type;
import java.util.ArrayList;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class TypeDAO {
    /**
     * Gives a list of type instances
     * @param paperId for which types are required
     * @return array list of types for paperId provided
     */
    public ArrayList<Type> getTypes(int paperId){
        return null;
    }

    /**
     * Gives a type's instance for typeId provided
     * @param typeId - id for which type information is required
     * @return type's instance
     */
    public Type getType(int typeId){
        return null;
    }
}