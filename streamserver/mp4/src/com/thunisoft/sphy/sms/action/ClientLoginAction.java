package com.thunisoft.sphy.sms.action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.opensymphony.xwork2.ActionContext;
import com.opensymphony.xwork2.ActionSupport;
import com.thunisoft.sphy.hibernate.TSmsFriendRelation;
import com.thunisoft.sphy.sms.service.SMSService;
import com.thunisoft.sphy.sms.session.TopicSessionService;

/**
 * 用户登录
 * 
 * <p>
 * 用户登录后，服务器告知客户端用户列表，以及用户的登录状态
 * 
 * @since V1.0 2014-5-15
 * @author chenxh
 */
public class ClientLoginAction extends ActionSupport {

    /**   */
    private static final long serialVersionUID = 1L;

    private TopicSessionService manager;

    private SMSService smsService;

    private String userId;

    private String userName;

    private List<Map> friends = Collections.emptyList();

    @Override
    public String execute() throws Exception {
        // 把自己注册进去
        // smsService.login(userId, userName);

        // 获取用户列表
        // List<TSmsFriendRelation> relation = smsService.findFriends(userId);
        friends = new ArrayList<Map>();
        for (int i = 0; i < 2; i++) {
            Map<String, Object> row = new HashMap<String, Object>();

            row.put("id", "r");
            row.put("name", "ddd");
            row.put("isYx", 1);
            row.put("isZx", 1);

            friends.add(row);
        }

        return "success";
    }

    public List<Map> getFriends() {
        return friends;
    }

    public void setSmsService(SMSService smsService) {
        this.smsService = smsService;
    }
}
