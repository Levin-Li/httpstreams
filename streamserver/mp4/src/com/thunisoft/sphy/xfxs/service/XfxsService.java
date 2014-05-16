package com.thunisoft.sphy.xfxs.service;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.thunisoft.sphy.hibernate.TXfxs;
import com.thunisoft.sphy.xfxs.AppointForm;
import com.thunisoft.sphy.xfxs.AppointState;
import com.thunisoft.sphy.xfxs.dao.XfxsDao;

/**
 * 信访线索 Service
 * 
 * @since V1.0 2014-5-16
 * @author chenxh
 */
public class XfxsService {
    private XfxsDao xfxsDao;

    /**
     * 生成预约码使用的字符。
     * 
     * 在网页应用中， 'o'和'0', 1 和 I, V 和 U 都是比较难去人的， 所以去掉了
     */
    private static char[] RANDOM_CHARS = new char[] {
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'J', 'K', 'L', 'M', 'N', 'P', 'Q', 'R', 'S',
            'T', 'W', 'X', 'Y', 'Z', '2', '3', '4', '5', '6', '7', '8', '9'};

    /**
     * 把预约资料变成信访线索
     * 
     * @param appoint 预约申请资料
     * @param usrId 创建人Id
     * @return
     * @since V1.0 2014-5-16
     * @author chenxh
     */
    public TXfxs insert(AppointForm appoint, String usrId) {
        try {
            return saveIt(appoint, usrId);
        } catch (RuntimeException e) {
            return null;
        }
    }

    private TXfxs saveIt(AppointForm appoint, String usrId) {
        Timestamp now = xfxsDao.now();

        TXfxs xfxs = new TXfxs();

        xfxs.setCCjrId(usrId);
        xfxs.setDCjsj(now);

        // 信访人
        xfxs.setCXrfMc(appoint.name);
        xfxs.setCXrfSfz(appoint.cid);
        xfxs.setCXrfDhhm(appoint.phone);

        // 接访日期
        if (StringUtils.isNotEmpty(appoint.appointmentDate)) {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");

            try {
                xfxs.setDYysj(new Timestamp(format.parse(appoint.appointmentDate).getTime()));
            } catch (ParseException e) {
                throw new IllegalArgumentException("解析[" + appoint.appointmentDate + "]失败！");
            }
        }

        // 上访原因
        xfxs.setCJs(appoint.descript);

        // 预计接访时长
        if (appoint.timeLength > 0) {
            xfxs.setNYysc(appoint.timeLength);
        }

        // 接访人
        // TODO 接访人

        xfxs.setNZt(AppointState.INIT.getCode());

        // 插入信访材料
        saveWithQueryCode(xfxs);


        if (appoint.acceptPhoneMessage && StringUtils.isNotEmpty(appoint.phone)) {
            sendPhoneMessage(xfxs.getCId(), appoint.phone, null);
        }

        return xfxs;
    }

    private void saveWithQueryCode(TXfxs xfxs) {
        String queryCode = newAppointCode();
        xfxs.setCCxm(queryCode);

        xfxsDao.save(xfxs);
    }

    private Logger logger = LoggerFactory.getLogger(getClass());

    public void sendPhoneMessage(String xfxsId, String phone, String content) {
        logger.warn("短信没有发出去!");
    }

    /**
     * 重新生成查询码
     * 
     * @return
     * @since V1.0 2014-5-16
     * @author chenxh
     */
    private String newAppointCode() {
        String appointCode = null;

        int tryRemaining = 128;
        do {
            appointCode = RandomStringUtils.random(6, RANDOM_CHARS);

            // 避免死循环
            tryRemaining--;
        } while (null != xfxsDao.findTXfxsByCxm(appointCode) && tryRemaining > 0);


        if (null == appointCode) {
            throw new IllegalAccessError("生成预约码失败, 系统已经经历了!");
        }

        return appointCode;
    }

    public TXfxs findXfxsByAppointCode(String appointCode) {
        return xfxsDao.findTXfxsByCxm(appointCode);
    }
    
    public TXfxs getXfxs(String id) {
        return xfxsDao.get(TXfxs.class, id);
    }

    public void setXfxsDao(XfxsDao xfxsDao) {
        this.xfxsDao = xfxsDao;
    }
}
