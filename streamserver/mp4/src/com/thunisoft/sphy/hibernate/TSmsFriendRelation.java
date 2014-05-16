package com.thunisoft.sphy.hibernate;

import java.util.Date;


/**
 * TSmsFriendRelation entity. @author MyEclipse Persistence Tools
 */

public class TSmsFriendRelation implements java.io.Serializable {


    // Fields

    private String CId;
    private String CWorkgroupId;
    private String CSyrId;
    private String CSyrName;
    private Date DCjsj;
    private String CFriendId;
    private String CFriendMc;
    private Integer NIsZx;
    private Integer NIsYx;


    // Constructors

    /** default constructor */
    public TSmsFriendRelation() {}


    /** full constructor */
    public TSmsFriendRelation(String CWorkgroupId, String CSyrId, String CSyrName, Date DCjsj,
            String CFriendId, String CFriendMc, Integer NIsZx, Integer NIsYx) {
        this.CWorkgroupId = CWorkgroupId;
        this.CSyrId = CSyrId;
        this.CSyrName = CSyrName;
        this.DCjsj = DCjsj;
        this.CFriendId = CFriendId;
        this.CFriendMc = CFriendMc;
        this.NIsZx = NIsZx;
        this.NIsYx = NIsYx;
    }


    // Property accessors

    public String getCId() {
        return this.CId;
    }

    public void setCId(String CId) {
        this.CId = CId;
    }

    public String getCWorkgroupId() {
        return this.CWorkgroupId;
    }

    public void setCWorkgroupId(String CWorkgroupId) {
        this.CWorkgroupId = CWorkgroupId;
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

    public String getCFriendId() {
        return this.CFriendId;
    }

    public void setCFriendId(String CFriendId) {
        this.CFriendId = CFriendId;
    }

    public String getCFriendMc() {
        return this.CFriendMc;
    }

    public void setCFriendMc(String CFriendMc) {
        this.CFriendMc = CFriendMc;
    }

    public Integer getNIsZx() {
        return this.NIsZx;
    }

    public void setNIsZx(Integer NIsZx) {
        this.NIsZx = NIsZx;
    }

    public Integer getNIsYx() {
        return this.NIsYx;
    }

    public void setNIsYx(Integer NIsYx) {
        this.NIsYx = NIsYx;
    }



}
