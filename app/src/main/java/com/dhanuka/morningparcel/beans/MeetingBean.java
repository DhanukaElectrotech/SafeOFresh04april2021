package com.dhanuka.morningparcel.beans;


import com.google.gson.annotations.SerializedName;

import java.io.Serializable;


public class MeetingBean  implements Serializable {

    @SerializedName("ContactId")
    String ContactId;

    @SerializedName("MeetingPersonName")
    String MeetingPersonName;

    @SerializedName("ContactNumber")
    String ContactNumber;

    @SerializedName("EmailId")
    String EmailId;

    @SerializedName("Designation")
    String Designation;

    @SerializedName("DecisionMakingPerson")
    String DecisionMakingPerson;

    @SerializedName("DecisionMakerContactNo")
    String DecisionMakerContactNo;

    @SerializedName("DecisionMakerDesignation")
    String DecisionMakerDesignation;
    @SerializedName("VisitTime")
    String VisitTime;

    @SerializedName("Reason")
    String Reason;
    @SerializedName("ProductOffered")
    String ProductOffered;
    @SerializedName("Comment")
    String Comment;

    @SerializedName("NextFollowupdate")
    String NextFollowupdate;

    @SerializedName("NextFollowupComment")
    String NextFollowupComment;
    @SerializedName("Createdby")
    String Createdby;

    @SerializedName("Createddatetime")
    String Createddatetime;


    public String getContactId() {
        return ContactId;
    }

    public void setContactId(String contactId) {
        ContactId = contactId;
    }

    public String getMeetingPersonName() {
        return MeetingPersonName;
    }

    public void setMeetingPersonName(String meetingPersonName) {
        MeetingPersonName = meetingPersonName;
    }

    public String getContactNumber() {
        return ContactNumber;
    }

    public void setContactNumber(String contactNumber) {
        ContactNumber = contactNumber;
    }

    public String getEmailId() {
        return EmailId;
    }

    public void setEmailId(String emailId) {
        EmailId = emailId;
    }

    public String getDesignation() {
        return Designation;
    }

    public void setDesignation(String designation) {
        Designation = designation;
    }

    public String getDecisionMakingPerson() {
        return DecisionMakingPerson;
    }

    public void setDecisionMakingPerson(String decisionMakingPerson) {
        DecisionMakingPerson = decisionMakingPerson;
    }

    public String getDecisionMakerContactNo() {
        return DecisionMakerContactNo;
    }

    public void setDecisionMakerContactNo(String decisionMakerContactNo) {
        DecisionMakerContactNo = decisionMakerContactNo;
    }

    public String getDecisionMakerDesignation() {
        return DecisionMakerDesignation;
    }

    public void setDecisionMakerDesignation(String decisionMakerDesignation) {
        DecisionMakerDesignation = decisionMakerDesignation;
    }

    public String getVisitTime() {
        return VisitTime;
    }

    public void setVisitTime(String visitTime) {
        VisitTime = visitTime;
    }

    public String getReason() {
        return Reason;
    }

    public void setReason(String reason) {
        Reason = reason;
    }

    public String getProductOffered() {
        return ProductOffered;
    }

    public void setProductOffered(String productOffered) {
        ProductOffered = productOffered;
    }

    public String getComment() {
        return Comment;
    }

    public void setComment(String comment) {
        Comment = comment;
    }

    public String getNextFollowupdate() {
        return NextFollowupdate;
    }

    public void setNextFollowupdate(String nextFollowupdate) {
        NextFollowupdate = nextFollowupdate;
    }

    public String getNextFollowupComment() {
        return NextFollowupComment;
    }

    public void setNextFollowupComment(String nextFollowupComment) {
        NextFollowupComment = nextFollowupComment;
    }

    public String getCreatedby() {
        return Createdby;
    }

    public void setCreatedby(String createdby) {
        Createdby = createdby;
    }

    public String getCreateddatetime() {
        return Createddatetime;
    }

    public void setCreateddatetime(String createddatetime) {
        Createddatetime = createddatetime;
    }
}
