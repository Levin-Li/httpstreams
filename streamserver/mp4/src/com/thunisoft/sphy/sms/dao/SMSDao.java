package com.thunisoft.sphy.sms.dao;

import java.util.List;


import com.thunisoft.sphy.BaseDaoSupport;
import com.thunisoft.sphy.hibernate.TSmsFriendRelation;

public class SMSDao extends BaseDaoSupport {
    /**
     * 获取朋友关系
     * 
     * @param syrId 朋友关系的所有者的ID
     * @return
     * @since V1.0 2014-5-15
     * @author chenxh
     */
    public List<TSmsFriendRelation> findFriends(String syrId) {
        String hql = "from TSmsFriendRelation where CSyrId=? and NIsYx=1";

        return executeFind(hql, syrId);
    }
    
    /**
     * 
     * @param friendId 朋友关系的朋友角色的ID
     * @return
     * @since V1.0 2014-5-15
     * @author chenxh
     */
    public List<TSmsFriendRelation> reversedFindFriends(String friendId) {
        String hql = "from TSmsFriendRelation where CFriendId=? and NIsYx=1";

        return executeFind(hql, friendId);
    }
}
