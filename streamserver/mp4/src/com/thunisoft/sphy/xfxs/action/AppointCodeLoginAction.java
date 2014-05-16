package com.thunisoft.sphy.xfxs.action;

import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.thunisoft.sphy.ActionResult;
import com.thunisoft.sphy.SessionConstants;
import com.thunisoft.sphy.hibernate.TXfxs;
import com.thunisoft.sphy.xfxs.service.XfxsService;

/**
 * 预约码登录
 * 
 * @since V1.0 2014-5-16
 * @author chenxh
 */
public class AppointCodeLoginAction extends ActionSupport {

    

    /**   */
    private static final long serialVersionUID = 1L;

    private XfxsService xfxsService;
    private String code;

    private TXfxs xfxs;

    @Override
    public String execute() throws Exception {
        xfxs = xfxsService.findXfxsByAppointCode(code);

        if (null != xfxs) {
            ActionContext.getContext().getSession().put(SessionConstants.KEY_XFXS, xfxs);
        }

        return SUCCESS;
    }

    public Map<String, Object> getResult() {
        ActionResult rst = new ActionResult(null != xfxs);
        rst.putXfxs(xfxs);

        return rst;
    }


    public void setCode(String code) {
        this.code = code;
    }

    public void setXfxsService(XfxsService xfxsService) {
        this.xfxsService = xfxsService;
    }
}
