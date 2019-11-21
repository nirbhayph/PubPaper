package com.rit.dca.paper;

/**
 * Data Connectivity and Access
 * @author Nirbhay Ashok Pherwani
 * Email: np5318@rit.edu
 * @link: https://nirbhay.me
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

    protected void setPaperId(int paperId) {
        this.paperId = paperId;
    }

    public String getTitle() {
        return title;
    }

    protected void setTitle(String title) {
        this.title = title;
    }

    public String getAbstractText() {
        return abstractText;
    }

    protected void setAbstractText(String abstractText) {
        this.abstractText = abstractText;
    }

    public String getTrack() {
        return track;
    }

    protected void setTrack(String track) {
        this.track = track;
    }

    public String getStatus() {
        return status;
    }

    protected void setStatus(String status) {
        this.status = status;
    }

    public int getSubmissionType() {
        return submissionType;
    }

    protected void setSubmissionType(int submissionType) {
        this.submissionType = submissionType;
    }

    public int getSubmitterId() {
        return submitterId;
    }

    protected void setSubmitterId(int submitterId) {
        this.submitterId = submitterId;
    }

    public String getFileId() {
        return fileId;
    }

    protected void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public String getTentativeStatus() {
        return tentativeStatus;
    }

    protected void setTentativeStatus(String tentativeStatus) {
        this.tentativeStatus = tentativeStatus;
    }
}
