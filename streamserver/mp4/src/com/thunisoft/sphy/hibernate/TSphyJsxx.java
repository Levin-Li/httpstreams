package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TSphyJsxx entity. @author MyEclipse Persistence Tools
 */

public class TSphyJsxx implements java.io.Serializable {


    // Fields

    private String CId;
    private String CZtId;
    private String CSphyId;
    private String CFsrId;
    private String CJsrId;
    private String CSm;
    private String CNr;
    private Date DFssj;
    private Date DJssj;


    // Constructors

    /** default constructor */
    public TSphyJsxx() {}


    /** full constructor */
    public TSphyJsxx(String CZtId, String CSphyId, String CFsrId, String CJsrId, String CSm,
            String CNr, Date DFssj, Date DJssj) {
        this.CZtId = CZtId;
        this.CSphyId = CSphyId;
        this.CFsrId = CFsrId;
        this.CJsrId = CJsrId;
        this.CSm = CSm;
        this.CNr = CNr;
        this.DFssj = DFssj;
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

    public String getCFsrId() {
        return this.CFsrId;
    }

    public void setCFsrId(String CFsrId) {
        this.CFsrId = CFsrId;
    }

    public String getCJsrId() {
        return this.CJsrId;
    }

    public void setCJsrId(String CJsrId) {
        this.CJsrId = CJsrId;
    }

    public String getCSm() {
        return this.CSm;
    }

    public void setCSm(String CSm) {
        this.CSm = CSm;
    }

    public String getCNr() {
        return this.CNr;
    }

    public void setCNr(String CNr) {
        this.CNr = CNr;
    }

    public Date getDFssj() {
        return this.DFssj;
    }

    public void setDFssj(Date DFssj) {
        this.DFssj = DFssj;
    }

    public Date getDJssj() {
        return this.DJssj;
    }

    public void setDJssj(Date DJssj) {
        this.DJssj = DJssj;
    }



}
