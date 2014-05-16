package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TSmsWorkgroup entity. @author MyEclipse Persistence Tools
 */

public class TSmsWorkgroup implements java.io.Serializable {


    // Fields

    private String CId;
    private String CMc;
    private String CSyrId;
    private String CSyrName;
    private Date DCjsj;


    // Constructors

    /** default constructor */
    public TSmsWorkgroup() {}


    /** full constructor */
    public TSmsWorkgroup(String CMc, String CSyrId, String CSyrName, Date DCjsj) {
        this.CMc = CMc;
        this.CSyrId = CSyrId;
        this.CSyrName = CSyrName;
        this.DCjsj = DCjsj;
    }


    // Property accessors

    public String getCId() {
        return this.CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getCMc() {
        return this.CMc;
    }

    public void setCMc(String CMc) {
        this.CMc = CMc;
    }

    public String getCSyrId() {
        return this.CSyrId;
    }

    public void setCSyrId(String CSyrId) {
        this.CSyrId = CSyrId;
    }

    public String getCSyrName() {
        return this.CSyrName;
    }

    public void setCSyrName(String CSyrName) {
        this.CSyrName = CSyrName;
    }

    public Date getDCjsj() {
        return this.DCjsj;
    }

    public void setDCjsj(Date DCjsj) {
        this.DCjsj = DCjsj;
    }



}
