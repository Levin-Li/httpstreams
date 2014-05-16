package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TXfxs entity. @author MyEclipse Persistence Tools
 */

public class TXfxs implements java.io.Serializable {


    // Fields

    private String CId;
    private String CCxm;
    private String CJs;
    private String CCjrId;
    private Date DCjsj;
    private Integer NZt;
    private Integer NYx;
    private Date DYysj;
    private Integer NYysc;
    private Integer NIsSptg;
    private String CXrfMc;
    private String CXrfSfz;
    private String CXrfDhhm;
    private String CSphyId;


    // Constructors

    /** default constructor */
    public TXfxs() {}


    /** full constructor */
    public TXfxs(String CCxm, String CJs, String CCjrId, Date DCjsj, Integer NZt, Integer NYx,
            Date DYysj, Integer NYysc, Integer NIsSptg, String CXrfMc, String CXrfSfz,
            String CXrfDhhm, String CSphyId) {
        this.CCxm = CCxm;
        this.CJs = CJs;
        this.CCjrId = CCjrId;
        this.DCjsj = DCjsj;
        this.NZt = NZt;
        this.NYx = NYx;
        this.DYysj = DYysj;
        this.NYysc = NYysc;
        this.NIsSptg = NIsSptg;
        this.CXrfMc = CXrfMc;
        this.CXrfSfz = CXrfSfz;
        this.CXrfDhhm = CXrfDhhm;
        this.CSphyId = CSphyId;
    }


    // Property accessors

    public String getCId() {
        return this.CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getCCxm() {
        return this.CCxm;
    }

    public void setCCxm(String CCxm) {
        this.CCxm = CCxm;
    }

    public String getCJs() {
        return this.CJs;
    }

    public void setCJs(String CJs) {
        this.CJs = CJs;
    }

    public String getCCjrId() {
        return this.CCjrId;
    }

    public void setCCjrId(String CCjrId) {
        this.CCjrId = CCjrId;
    }

    public Date getDCjsj() {
        return this.DCjsj;
    }

    public void setDCjsj(Date DCjsj) {
        this.DCjsj = DCjsj;
    }

    public Integer getNZt() {
        return this.NZt;
    }

    public void setNZt(Integer NZt) {
        this.NZt = NZt;
    }

    public Integer getNYx() {
        return this.NYx;
    }

    public void setNYx(Integer NYx) {
        this.NYx = NYx;
    }

    public Date getDYysj() {
        return this.DYysj;
    }

    public void setDYysj(Date DYysj) {
        this.DYysj = DYysj;
    }

    public Integer getNYysc() {
        return this.NYysc;
    }

    public void setNYysc(Integer NYysc) {
        this.NYysc = NYysc;
    }

    public Integer getNIsSptg() {
        return this.NIsSptg;
    }

    public void setNIsSptg(Integer NIsSptg) {
        this.NIsSptg = NIsSptg;
    }

    public String getCXrfMc() {
        return this.CXrfMc;
    }

    public void setCXrfMc(String CXrfMc) {
        this.CXrfMc = CXrfMc;
    }

    public String getCXrfSfz() {
        return this.CXrfSfz;
    }

    public void setCXrfSfz(String CXrfSfz) {
        this.CXrfSfz = CXrfSfz;
    }

    public String getCXrfDhhm() {
        return this.CXrfDhhm;
    }

    public void setCXrfDhhm(String CXrfDhhm) {
        this.CXrfDhhm = CXrfDhhm;
    }

    public String getCSphyId() {
        return this.CSphyId;
    }

    public void setCSphyId(String CSphyId) {
        this.CSphyId = CSphyId;
    }



}
