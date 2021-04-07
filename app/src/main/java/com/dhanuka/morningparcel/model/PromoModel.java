package com.dhanuka.morningparcel.model;

import java.io.Serializable;

public class PromoModel implements Serializable {

    String CatCodeID;

    String CompanyID;
    String CodeDescription;
    String CodeID;
    String Val1;
    String Val2;
    String applied;
    Boolean isUsed = false;

    public Boolean getUsed() {
        return isUsed;
    }

    public void setUsed(Boolean used) {
        isUsed = used;
    }

    public String getCatCodeID() {
        return CatCodeID;
    }

    public void setCatCodeID(String catCodeID) {
        CatCodeID = catCodeID;
    }

    public String getCompanyID() {
        return CompanyID;
    }

    public void setCompanyID(String companyID) {
        CompanyID = companyID;
    }

    public String getCodeDescription() {
        return CodeDescription;
    }

    public void setCodeDescription(String codeDescription) {
        CodeDescription = codeDescription;
    }

    public String getCodeID() {
        return CodeID;
    }

    public void setCodeID(String codeID) {
        CodeID = codeID;
    }

    public String getVal1() {
        return Val1;
    }

    public void setVal1(String val1) {
        Val1 = val1;
    }

    public String getVal2() {
        return Val2;
    }

    public void setVal2(String val2) {
        Val2 = val2;
    }

    public String getApplied() {
        return applied;
    }

    public void setApplied(String applied) {
        this.applied = applied;
    }
}
