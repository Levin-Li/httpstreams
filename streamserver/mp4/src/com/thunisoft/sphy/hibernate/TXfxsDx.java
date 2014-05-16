package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TXfxsDx entity. @author MyEclipse Persistence Tools
 */

public class TXfxsDx implements java.io.Serializable {


    // Fields

    private String CId;
    private String CXfxsId;
    private String CDhhm;
    private String CNr;
    private Date DCjsj;
    private Date DFssj;
    private Integer NFscs;


    // Constructors

    /** default constructor */
    public TXfxsDx() {}


    /** full constructor */
    public TXfxsDx(String CXfxsId, String CDhhm, String CNr, Date DCjsj, Date DFssj, Integer NFscs) {
        this.CXfxsId = CXfxsId;
        this.CDhhm = CDhhm;
        this.CNr = CNr;
        this.DCjsj = DCjsj;
        this.DFssj = DFssj;
        this.NFscs = NFscs;
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

    public String getCDhhm() {
        return this.CDhhm;
    }

    public void setCDhhm(String CDhhm) {
        this.CDhhm = CDhhm;
    }

    public String getCNr() {
        return this.CNr;
    }

    public void setCNr(String CNr) {
        this.CNr = CNr;
    }

    public Date getDCjsj() {
        return this.DCjsj;
    }

    public void setDCjsj(Date DCjsj) {
        this.DCjsj = DCjsj;
    }

    public Date getDFssj() {
        return this.DFssj;
    }

    public void setDFssj(Date DFssj) {
        this.DFssj = DFssj;
    }

    public Integer getNFscs() {
        return this.NFscs;
    }

    public void setNFscs(Integer NFscs) {
        this.NFscs = NFscs;
    }



}
