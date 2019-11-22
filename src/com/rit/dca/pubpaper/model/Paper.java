package com.rit.dca.pubpaper.model;

/**
 * Database Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * Profile: https://nirbhay.me
 */

public class Paper {
    private int paperId;
    private String title;
    private String abstractText;
    private String track;
    private String status;
    private int submissionType;
    private int submitterId;
    private String fileId;
    private String tentativeStatus;

    public int getPaperId() {
        return paperId;
    }

    public void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    public void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getTrack() {
        return track;
    }

    public void setTrack(String track) {
        this.track = track;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getSubmissionType() {
        return submissionType;
    }

    public void setSubmissionType(int submissionType) {
        this.submissionType = submissionType;
    }

    public int getSubmitterId() {
        return submitterId;
    }

    public void setSubmitterId(int submitterId) {
        this.submitterId = submitterId;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTentativeStatus() {
        return tentativeStatus;
    }

    public void setTentativeStatus(String tentativeStatus) {
        this.tentativeStatus = tentativeStatus;
    }
}
