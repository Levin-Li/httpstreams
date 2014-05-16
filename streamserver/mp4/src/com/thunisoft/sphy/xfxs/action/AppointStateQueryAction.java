package com.thunisoft.sphy.xfxs.action;

import java.util.HashMap;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;
import com.thunisoft.sphy.ActionResult;
import com.thunisoft.sphy.hibernate.TXfxs;
import com.thunisoft.sphy.xfxs.AppointState;
import com.thunisoft.sphy.xfxs.service.XfxsService;

/**
 * 查询预约状态
 * 
 * @since V1.0 2014-5-16
 * @author chenxh
 */
public class AppointStateQueryAction extends ActionSupport {
    /**   */
    private static final long serialVersionUID = 1L;

    private XfxsService xfxsService;

    // input
    private String appointCode;

    // ouput
    
    /** 错误消息 */
    private String error;

    /** 当前状态 */
    private AppointState state;

    @Override
    public String execute() throws Exception {
        TXfxs xfxs = xfxsService.findXfxsByAppointCode(appointCode);

        if (null == xfxs) {
            error = getText("Xfxs.AppointCode.NotFound");
        } else {
            setArrangeDetail(xfxs);
        }

        return SUCCESS;
    }

    /**
     * 线索安排
     * 
     * @param xfxs
     * @return
     * @since V1.0 2014-5-16
     * @author chenxh
     */
    private String setArrangeDetail(TXfxs xfxs) {
        int xfxsState = xfxs.getNZt();
        state = AppointState.getByCode(xfxsState);

        if (state == AppointState.ARRANGED) {
            
        } 
        
        return null;
    }

    public Map<String, Object> getResult() {
        ActionResult rst = new ActionResult(null == error);
        
        
        if (rst.isSuccess()) {
            rst.put("state", state.getCode());
            rst.put("descript", getText("Xfxs.State.Descript." + state.getCode()));
        } else {
            rst.put("error", error);
        }
        

        return rst;
    }
    public void setAppointCode(String appointCode) {
        this.appointCode = appointCode;
    }

    public void setXfxsService(XfxsService xfxsService) {
        this.xfxsService = xfxsService;
    }
}
