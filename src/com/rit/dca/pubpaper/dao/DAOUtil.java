package com.rit.dca.pubpaper.dao;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class DAOUtil {

    // MYSQL credentials
    protected static final String USER_NAME = "root";
    protected static final String PASSWORD = "google12";
    protected static final String HOST = "jdbc:mysql://localhost:3306/CSM?useSSL=false";

    // Authentication
    protected static final String CHECK_LOGIN = "SELECT * FROM Users WHERE email=? AND pswd=?";
    protected static final String CHECK_ADMIN = "SELECT isAdmin FROM Users WHERE userId=?";

    // Users
    protected static final String GET_ALL_USERS = "SELECT * FROM Users limit 3";
    protected static final String GET_SINGLE_USER = "SELECT * FROM Users WHERE userId=?";
    protected static final String DELETE_USERS = "DELETE FROM Users WHERE userId=?";
    protected static final String CHECK_USER_EXIST = "SELECT userId FROM Users WHERE userId=?";
    protected static final String UPDATE_USER_PROFILE = "UPDATE Users SET lastName=?, firstName=?, email=?, affiliationId=? WHERE userId=?";
    protected static final String INSERT_NEW_USER = "INSERT INTO Users(userId, lastName, firstName, email, pswd, canReview, expiration, isAdmin, affiliationId)" +
            " VALUES(?,?,?,?,?,'0','20250101000000',0,?)";
    protected static final String GET_NEXT_USER_ID = "SELECT userId FROM Users ORDER BY userId DESC LIMIT 1";
    protected static final String SET_USER_AS_ADMIN = "UPDATE Users SET isAdmin=? WHERE userId=?";
    protected static final String SET_USER_CAN_REVIEW = "UPDATE Users SET canReview=? WHERE userId=?";

    // Paper Authors
    protected static final String DELETE_PAPER_AUTHORS = "DELETE FROM PaperAuthors WHERE paperId=?";
    protected static final String DELETE_AUTHOR_PAPERS = "DELETE FROM PaperAuthors WHERE userId=?";
    protected static final String DELETE_SINGLE_PAPER_AUTHOR = "DELETE FROM PaperAuthors WHERE paperId=? AND userId=?";
    protected static final String GET_PAPER_AUTHORS = "SELECT * FROM PaperAuthors WHERE paperId=? ORDER BY displayOrder ASC";
    protected static final String GET_AUTHOR_PAPERS = "SELECT * FROM PaperAuthors WHERE userId=?";
    protected static final String GET_DISPLAY_ORDER = "SELECT displayOrder FROM PaperAuthors WHERE paperId=? AND userId=?";
    protected static final String INSERT_PAPER_AUTHOR = "INSERT INTO PaperAuthors VALUES (?,?,?)";
    protected static final String UPDATE_PAPER_AUTHOR = "UPDATE PaperAuthors SET userId=? WHERE paperId=? AND userId=?";
    protected static final String UPDATE_PAPER_AUTHOR_DISPLAY_ORDER = "Update PaperAuthors SET displayOrder=? WHERE paperId=? AND userId=?";


    // Paper
    protected static final String GET_USER_PAPERS = "SELECT * FROM Papers WHERE submitterId=?";
    protected static final String GET_PAPER_WITH_PAPER_ID = "SELECT * FROM Papers WHERE paperId=?";
    protected static final String GET_USER_PAPER_IDS = "SELECT paperId FROM Papers WHERE submitterId=?";
    protected static final String DELETE_USER_PAPERS = "DELETE FROM Papers WHERE submitterId=?";
    //protected static final String INSERT_PAPER = "";
    //protected static final String UPDATE_PAPER = "UPDATE Papers SET title=?, abstract=?, submissionType=?, fileId=? WHERE paperId=? AND submitterId=?";

    // Affiliations
    protected static final String GET_USER_AFFILIATION = "SELECT affiliationId FROM Users WHERE userId=?";
    protected static final String GET_AFFILIATION_WITH_ID = "SELECT * FROM _Affiliations WHERE affiliationId=?";
    protected static final String GET_ALL_AFFILIATIONS = "SELECT * FROM _Affiliations";
    protected static final String INSERT_AFFILIATION = "INSERT INTO _Affiliations VALUES (?,?)";
    protected static final String GET_LAST_AFFILIATION_ID = "SELECT affiliationId FROM _Affiliations ORDER BY affiliationId DESC LIMIT 1";
    protected static final String DELETE_AFFILIATION = "DELETE FROM _Affiliations WHERE affiliationId=?";
    protected static final String SET_USER_AFFILIATION_NULL = "UPDATE Users SET affiliationId=NULL WHERE affiliationId=?";
    protected static final String UPDATE_AFFILIATION = "UPDATE _Affiliations SET affiliationName=? WHERE affiliationId=?";


    // Types
    protected static final String GET_ALL_TYPES = "SELECT * FROM _Types";
    protected static final String GET_PAPER_TYPE_ID = "SELECT submissionType FROM Papers WHERE paperId=?";
    protected static final String GET_PAPER_TYPE_WITH_ID = "SELECT * FROM _Types WHERE typeId=?";
    protected static final String GET_LAST_TYPE_ID = "SELECT typeId FROM _Types ORDER BY typeId DESC LIMIT 1";
    protected static final String INSERT_TYPE = "INSERT INTO _Types VALUES (?,?)";
    protected static final String DELETE_TYPE = "DELETE FROM _Types WHERE typeId=?";
    protected static final String SET_PAPER_TYPE_NULL = "UPDATE Papers SET submissionType=NULL WHERE submissionType=?";
    protected static final String UPDATE_TYPE = "UPDATE _Types SET typeName=? WHERE typeId=?";


    // PaperSubjects
    protected static final String DELETE_PAPER_SUBJECTS = "DELETE FROM PaperSubjects WHERE paperId=?";
    protected static final String DELETE_SUBJECT_PAPERS = "DELETE FROM papersubjects where subjectid=?";
    protected static final String DELETE_SINGLE_PAPER_SUBJECT = "DELETE FROM papersubjects WHERE paperId=? AND subjectId=?";
    protected static final String GET_PAPER_SUBJECTS = "SELECT * FROM PaperSubjects WHERE paperId=?";
    protected static final String INSERT_PAPER_SUBJECT = "INSERT INTO PaperSubjects VALUES (?,?)";
    protected static final String UPDATE_PAPER_SUBJECT = "UPDATE PaperSubjects SET subjectId=? WHERE paperid=? AND subjectId=?";

    // Subject
    protected  static final String GET_SUBJECT = "SELECT * FROM _subjects WHERE subjectId=?";
    protected  static final String GET_ALL_SUBJECTS = "SELECT * FROM _subjects";
    protected static final String GET_LAST_SUBJECT_ID = "SELECT subjectId FROM _subjects ORDER BY subjectId DESC LIMIT 1";
    protected static final String INSERT_SUBJECT = "INSERT INTO _subjects VALUES (?,?)";
    protected static final String DELETE_SUBJECT = "DELETE FROM _subjects WHERE subjectId=?";
    protected static final String UPDATE_SUBJECT = "UPDATE _subjects SET subjectName=? WHERE subjectId=?";
}
