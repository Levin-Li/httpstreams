package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TXfxsFj entity. @author MyEclipse Persistence Tools
 */

public class TXfxsFj implements java.io.Serializable {


    // Fields

    private String CId;
    private String CXfxsId;
    private String CWjmc;
    private String CCclj;
    private Date DScsj;
    private String CScrId;


    // Constructors

    /** default constructor */
    public TXfxsFj() {}


    /** full constructor */
    public TXfxsFj(String CXfxsId, String CWjmc, String CCclj, Date DScsj, String CScrId) {
        this.CXfxsId = CXfxsId;
        this.CWjmc = CWjmc;
        this.CCclj = CCclj;
        this.DScsj = DScsj;
        this.CScrId = CScrId;
    }


    // Property accessors

    public String getCId() {
        return this.CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getCXfxsId() {
        return this.CXfxsId;
    }

    public void setCXfxsId(String CXfxsId) {
        this.CXfxsId = CXfxsId;
    }

    public String getCWjmc() {
        return this.CWjmc;
    }

    public void setCWjmc(String CWjmc) {
        this.CWjmc = CWjmc;
    }

    public String getCCclj() {
        return this.CCclj;
    }

    public void setCCclj(String CCclj) {
        this.CCclj = CCclj;
    }

    public Date getDScsj() {
        return this.DScsj;
    }

    public void setDScsj(Date DScsj) {
        this.DScsj = DScsj;
    }

    public String getCScrId() {
        return this.CScrId;
    }

    public void setCScrId(String CScrId) {
        this.CScrId = CScrId;
    }



}
