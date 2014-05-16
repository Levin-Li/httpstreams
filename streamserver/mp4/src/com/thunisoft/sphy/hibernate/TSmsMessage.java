package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TSmsMessage entity. @author MyEclipse Persistence Tools
 */

public class TSmsMessage implements java.io.Serializable {


    // Fields

    private String CId;
    private String CFsrId;
    private String CFsrMc;
    private Date DFssj;
    private String CJsrId;
    private String CJsrMc;
    private Date DJssj;
    private String CNr;
    private Integer NYx;


    // Constructors

    /** default constructor */
    public TSmsMessage() {}


    /** full constructor */
    public TSmsMessage(String CFsrId, String CFsrMc, Date DFssj, String CJsrId, String CJsrMc,
            Date DJssj, String CNr, Integer NYx) {
        this.CFsrId = CFsrId;
        this.CFsrMc = CFsrMc;
        this.DFssj = DFssj;
        this.CJsrId = CJsrId;
        this.CJsrMc = CJsrMc;
        this.DJssj = DJssj;
        this.CNr = CNr;
        this.NYx = NYx;
    }


    // Property accessors

    public String getCId() {
        return this.CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getCFsrId() {
        return this.CFsrId;
    }

    public void setCFsrId(String CFsrId) {
        this.CFsrId = CFsrId;
    }

    public String getCFsrMc() {
        return this.CFsrMc;
    }

    public void setCFsrMc(String CFsrMc) {
        this.CFsrMc = CFsrMc;
    }

    public Date getDFssj() {
        return this.DFssj;
    }

    public void setDFssj(Date DFssj) {
        this.DFssj = DFssj;
    }

    public String getCJsrId() {
        return this.CJsrId;
    }

    public void setCJsrId(String CJsrId) {
        this.CJsrId = CJsrId;
    }

    public String getCJsrMc() {
        return this.CJsrMc;
    }

    public void setCJsrMc(String CJsrMc) {
        this.CJsrMc = CJsrMc;
    }

    public Date getDJssj() {
        return this.DJssj;
    }

    public void setDJssj(Date DJssj) {
        this.DJssj = DJssj;
    }

    public String getCNr() {
        return this.CNr;
    }

    public void setCNr(String CNr) {
        this.CNr = CNr;
    }

    public Integer getNYx() {
        return this.NYx;
    }

    public void setNYx(Integer NYx) {
        this.NYx = NYx;
    }



}
