package com.thunisoft.sphy.xfxs;

/**
 * 预约状态
 *
 * @since V1.0  2014-5-16
 * @author chenxh
 */
public enum AppointState {
    INIT(1, "初始化"),
    ARRANGED(2, "已安排"),
    EXCUTING(3, "正在接待"),
    REFUSED(4, "拒绝"),
    FINISH(5, "结束"),
    ;

    
    private int code;
    private String descript;
    private AppointState(int code, String descript) {
        this.code = code;
        this.descript = descript;
    }

    public String getDescript() {
        return descript;
    }
    
    public static AppointState getByCode(int code) {
        AppointState[] v = AppointState.values();
        for (int i = 0; i < v.length; i++) {
            if (v[i].code == code) {
                return v[i];
            }
        }
        
        return null;
    }
    
    public int getCode() {
        return code;
    }
}
