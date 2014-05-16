package com.thunisoft.sphy.xfxs;

/**
 * 预约申请表单
 * 
 * @since V1.0  2014-5-16
 * @author chenxh
 */
public class AppointForm {
    /**
     * 姓名
     */
    public String name;
    /**
     * 身份证号
     */
    public String cid;
    /**
     * 电话
     */
    public String phone;
    
    public boolean acceptPhoneMessage;
    /**
     * 选中的接待人员
     */
    public String lenderId;
    /**
     * 约定的时间
     */
    public String appointmentDate;
    public String descript;
    /**
     * 预计的接访时长
     */
    public int timeLength;

    public AppointForm(String name) {
        this.name = name;
    }
}