package com.rit.dca.pubpaper.model;

import java.util.ArrayList;

/**
 * Class : Database Connectivity and Access (DCA)
 * @author Dhiren Chandnani
 * Email: dc6288@rit.edu
 */
public class User {
    private int userId;
    private String lastName;
    private String firstName;
    private String email;
    private String pwd;
    private String expiration;
    private int isAdmin;
    private int affiliationId;
    private String canReview;
    private ArrayList<Integer> allPapers;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getExpiration() {
        return expiration;
    }

    public void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    public void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getAffiliationId() {
        return affiliationId;
    }

    public void setAffiliationId(int affiliationId) {
        this.affiliationId = affiliationId;
    }

    public String getCanReview() {
        return canReview;
    }

    public void setCanReview(String canReview) {
        this.canReview = canReview;
    }


    public ArrayList<Integer> getAllPapers() {
        return allPapers;
    }

    public void setAllPapers(ArrayList<Integer> allPapers) {
        this.allPapers = allPapers;
    }
}
