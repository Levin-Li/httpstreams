package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TSphy entity. @author MyEclipse Persistence Tools
 */

public class TSphy implements java.io.Serializable {


    // Fields

    private String CId;
    private String CZtId;
    private String CCjrId;
    private Date DJhKssj;
    private Date DJhJssj;
    private Date DKssj;
    private Date DJssj;


    // Constructors

    /** default constructor */
    public TSphy() {}


    /** full constructor */
    public TSphy(String CZtId, String CCjrId, Date DJhKssj, Date DJhJssj, Date DKssj, Date DJssj) {
        this.CZtId = CZtId;
        this.CCjrId = CCjrId;
        this.DJhKssj = DJhKssj;
        this.DJhJssj = DJhJssj;
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

    public String getCCjrId() {
        return this.CCjrId;
    }

    public void setCCjrId(String CCjrId) {
        this.CCjrId = CCjrId;
    }

    public Date getDJhKssj() {
        return this.DJhKssj;
    }

    public void setDJhKssj(Date DJhKssj) {
        this.DJhKssj = DJhKssj;
    }

    public Date getDJhJssj() {
        return this.DJhJssj;
    }

    public void setDJhJssj(Date DJhJssj) {
        this.DJhJssj = DJhJssj;
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
