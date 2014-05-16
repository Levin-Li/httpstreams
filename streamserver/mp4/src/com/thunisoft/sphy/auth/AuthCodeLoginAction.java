package com.thunisoft.sphy.auth;

import java.util.Date;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.google.code.kaptcha.Constants;
import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.thunisoft.sphy.ActionResult;
import com.thunisoft.sphy.SessionConstants;

/**
 * 验证码登陆
 * 
 * @since V1.0  2014-5-16
 * @author chenxh
 */
public class AuthCodeLoginAction extends ActionSupport {
    /**   */
    private static final long serialVersionUID = 1L;
    
    /** 验证码 */
    private String code;
    
    private ActionResult result;
    
    @Override
    public String execute() throws Exception {
        Map<String, Object> session = ActionContext.getContext().getSession();
        String key = (String) session.get(Constants.KAPTCHA_SESSION_KEY);
        Date date = (Date) session.get(Constants.KAPTCHA_SESSION_DATE);
        
        if (!StringUtils.equals(key, code)) {
            result = new ActionResult(false);
        } else if (null == date) {
            result = new ActionResult(false);
        } else if (System.currentTimeMillis() - date.getTime() > 5 * 60 * 60 * 1000) {
            result = new ActionResult(false);
        } else {
            result = new ActionResult(true);
            
            session.put(SessionConstants.KEY_AUTHCODE, key);
        }

        return SUCCESS;
    }
    
    public void setCode(String code) {
        this.code = code;
    }
    
    public ActionResult getResult() {
        return result;
    }
}
