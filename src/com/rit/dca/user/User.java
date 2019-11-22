package com.rit.dca.user;

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

    public int getUserId() {
        return userId;
    }

    protected void setUserId(int userId) {
        this.userId = userId;
    }

    public String getLastName() {
        return lastName;
    }

    protected void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    protected void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getEmail() {
        return email;
    }

    protected void setEmail(String email) {
        this.email = email;
    }

    protected String getPwd() {
        return pwd;
    }

    protected void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public String getExpiration() {
        return expiration;
    }

    protected void setExpiration(String expiration) {
        this.expiration = expiration;
    }

    public int getIsAdmin() {
        return isAdmin;
    }

    protected void setIsAdmin(int isAdmin) {
        this.isAdmin = isAdmin;
    }

    public int getAffiliationId() {
        return affiliationId;
    }

    protected void setAffiliationId(int affiliationId) {
        this.affiliationId = affiliationId;
    }

    public String getCanReview() {
        return canReview;
    }

    protected void setCanReview(String canReview) {
        this.canReview = canReview;
    }
}
