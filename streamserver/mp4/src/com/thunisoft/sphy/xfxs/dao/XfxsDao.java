package com.thunisoft.sphy.xfxs.dao;

import java.util.List;

import com.thunisoft.sphy.BaseDaoSupport;
import com.thunisoft.sphy.hibernate.TXfxs;

public class XfxsDao extends BaseDaoSupport {

    /**
     * 
     * @param queryCode
     * @return
     * @since V1.0 2014-5-16
     * @author chenxh
     */
    public TXfxs findTXfxsByCxm(String queryCode) {
        String hql = "from TXfxs where CCxm = ? ";
        List<TXfxs> rows = executeFind(hql, queryCode);
        
        return rows.isEmpty() ? null : rows.get(0);
    }

}
