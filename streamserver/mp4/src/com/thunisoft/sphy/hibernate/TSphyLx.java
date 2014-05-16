package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TSphyLx entity. @author MyEclipse Persistence Tools
 */

public class TSphyLx implements java.io.Serializable {


    // Fields

    private String CId;
    private String CZtId;
    private String CSphyId;
    private String CWjlj;
    private Date DKssj;
    private Date DJssj;


    // Constructors

    /** default constructor */
    public TSphyLx() {}


    /** full constructor */
    public TSphyLx(String CZtId, String CSphyId, String CWjlj, Date DKssj, Date DJssj) {
        this.CZtId = CZtId;
        this.CSphyId = CSphyId;
        this.CWjlj = CWjlj;
        this.DKssj = DKssj;
        this.DJssj = DJssj;
    }


    // Property accessors

    public String getCId() {
        return this.CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getCZtId() {
        return this.CZtId;
    }

    public void setCZtId(String CZtId) {
        this.CZtId = CZtId;
    }

    public String getCSphyId() {
        return this.CSphyId;
    }

    public void setCSphyId(String CSphyId) {
        this.CSphyId = CSphyId;
    }

    public String getCWjlj() {
        return this.CWjlj;
    }

    public void setCWjlj(String CWjlj) {
        this.CWjlj = CWjlj;
    }

    public Date getDKssj() {
        return this.DKssj;
    }

    public void setDKssj(Date DKssj) {
        this.DKssj = DKssj;
    }

    public Date getDJssj() {
        return this.DJssj;
    }

    public void setDJssj(Date DJssj) {
        this.DJssj = DJssj;
    }



}
