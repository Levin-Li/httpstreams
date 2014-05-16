package com.thunisoft.sphy.sms.service;

import java.text.Collator;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import org.apache.commons.collections.CollectionUtils;

import com.thunisoft.sphy.hibernate.TSmsFriendRelation;
import com.thunisoft.sphy.sms.IMessage;
import com.thunisoft.sphy.sms.IMessageProducer;
import com.thunisoft.sphy.sms.ITopicSession;
import com.thunisoft.sphy.sms.dao.SMSDao;
import com.thunisoft.sphy.sms.session.TopicSessionService;


public class SMSService {
    private SMSDao smsDao;

    private TopicSessionService manager;

    /**
     * 登录 sms 系统，
     * 
     * 把自己的状态设置为 在线
     * 
     * @param userId
     * @param userName
     * @since V1.0 2014-5-15
     * @author chenxh
     */
    public void login(String userId, String userName) {
        ITopicSession session = manager.getTopicSession(asTopicName(userId));

        session.setProperty("name", asTopicName(userName));
    }

    /**
     * 用户的朋友们
     * 
     * 朋友按在线状态、姓名拼音排序
     * 
     * @param asUserTopicName(usrId) 用户ID
     * @return
     * @since V1.0 2014-5-15
     * @author chenxh
     */
    public List<TSmsFriendRelation> findFriends(String usrId) {
        List<TSmsFriendRelation> friends = smsDao.findFriends(asTopicName(usrId));

        if (CollectionUtils.isNotEmpty(friends)) {
            final Collator collator = Collator.getInstance(Locale.CHINA);

            Collections.sort(friends, new Comparator<TSmsFriendRelation>() {
                public int compare(TSmsFriendRelation o1, TSmsFriendRelation o2) {
                    // 在线是1， 不在线是2
                    // 在线的排在前面
                    int rst = (booleanValue(o1.getNIsZx()) - booleanValue(o2.getNIsZx()));

                    // 根据姓名拼音排序
                    if (0 == rst) {
                        rst = collator.compare(o1.getCFriendMc(), o2.getCFriendMc());
                    }

                    return rst;
                }
            });
        }

        return friends;
    }


    /**
     * @param userIdFrom
     * @param userIdTo
     * @param message
     * @since V1.0 2014-5-15
     * @author chenxh
     */
    public void sendMessage(String userIdFrom, String userIdTo, IMessage message) {
        ITopicSession session = manager.getTopicSession(asTopicName(userIdTo));

        IMessageProducer producer = session.createProducer(userIdFrom);
        producer.sendMessage(message);
    }

    /**
     * 广播一个消息
     * 
     * 事件只发   "把我当朋友的人" 和   “被我当做朋友的人”
     * @param userIdFrom 发出消息的人
     * @param message
     * @since V1.0 2014-5-15
     * @author chenxh
     */
    public void publishMessage(String userIdFrom, IMessage message) {

        HashSet<String> cachedFriends = new HashSet<String>();

        // 发送给“把我当朋友的人”
        List<TSmsFriendRelation> friends = smsDao.reversedFindFriends(userIdFrom);
        for (TSmsFriendRelation r : friends) {
            if (cachedFriends.contains(r.getCSyrId())) {
                continue;
            } else {
                sendMessage(userIdFrom, r.getCSyrId(), message);
                cachedFriends.add(r.getCSyrId());
            }
        }

        // 发送给 "被我当作朋友的人"
        friends = smsDao.findFriends(userIdFrom);
        for (TSmsFriendRelation r : friends) {
            if (cachedFriends.contains(r.getCFriendId())) {
                continue;
            } else {
                sendMessage(userIdFrom, r.getCFriendId(), message);
                cachedFriends.add(r.getCFriendId());
            }
        }
    }

    public void logout(String userId) {
        ITopicSession session = manager.remove(asTopicName(userId));

        if (null != session) {
            session.close();
        }
    }

    private String asTopicName(String userId) {
        return userId;
    }

    private int booleanValue(Integer iValue) {
        return Integer.valueOf(1).equals(iValue) ? 1 : 2;
    }


}
