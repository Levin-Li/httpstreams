package com.thunisoft.sphy.sms.action;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionSupport;

public class GetMessageAction extends ActionSupport {

    /**   */
    private static final long serialVersionUID = 1L;
    
    
    private List<Map<String, String>> msgs = new ArrayList<Map<String,String>>();
    
    @Override
    public String execute() throws Exception {
        msgs.add(new HashMap<String, String>(){/**   */
            private static final long serialVersionUID = 1L;
        {
            put("id", "001");
            put("name", "张山");
            put("msg", "测试消息");
        }});
        
        
        return SUCCESS;
    }
    
    public List<Map<String, String>> getMsgs() {
        return msgs;
    }
}
