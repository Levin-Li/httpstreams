package com.thunisoft.sphy.xfxs.action;

import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.opensymphony.xwork2.ActionSupport;
import com.thunisoft.sphy.ActionResult;
import com.thunisoft.sphy.hibernate.TXfxs;
import com.thunisoft.sphy.xfxs.AppointForm;
import com.thunisoft.sphy.xfxs.service.XfxsService;

/**
 * 填写预约申请.
 * 
 * @since V1.0 2014-5-15
 * @author chenxh
 */
public class MakeAnAppointmentAction extends ActionSupport {
    private XfxsService xfxsService;
    
    /**   */
    private static final long serialVersionUID = 1L;

    private AppointForm form = new AppointForm("您的姓名");

    /**
     * 信访线索
     */
    private TXfxs xfxs;

    /**
     * 规则说明： 1、可以是1开头的11位数字（手机号） 2、可以是“区号-电话号-分机号”或者是“(区号)电话号-分机号”格式 3、区号是0开头的3～4位数字，可以没有区号
     * 4、电话号是5～8位数字，不能以0开头 5、分机号是1～8位数字，可以没有分机号
     * 
     * 合法数据示例： 13812341234 010-12345678 (0432)1234567-1234
     */
    private static Pattern phonePattern = Pattern
        .compile("((\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$)");

    private static final Logger logger = LoggerFactory.getLogger(MakeAnAppointmentAction.class);

    @Override
    public String execute() throws Exception {
        _validate();

        Map<String, List<String>> errors = getFieldErrors();
        if (!errors.isEmpty()) {
            logger.warn("validate rst: {}", errors);
        } else {
            xfxs = xfxsService.insert(form, null);
        }

        return SUCCESS;
    }

    
    private void _validate() {
        
        if (StringUtils.isEmpty(form.phone)) {
            addFieldError("password", "phone.required");
        } else if (!phonePattern.matcher(form.phone).matches()) {
            addFieldError("password", "phone.illegal");
        }

    }

    public Map<String, Object> getResult() {
        

        ActionResult rst = new ActionResult(null != xfxs);
        
        // 信访线索
        if (null != xfxs) {
            rst.putXfxs(xfxs);
        } else {
            addFieldError("xfxs", "saved.fail");

            Map<String, List<String>> fieldErrors = getFieldErrors();
            rst.put("errors", fieldErrors);
        }
        
        return rst;
    }
    
    public void setName(String name) {
        this.form.name = name;
    }

    public void setCid(String cid) {
        this.form.cid = cid;
    }

    public void setPhone(String phone) {
        this.form.phone = phone;
    }

    public void setAcceptPhoneMessage(String accept) {
        this.form.acceptPhoneMessage = "true".equals(accept);
    }
    

    public void setAppointmentDate(String appointmentDate) {
        this.form.appointmentDate = appointmentDate;
    }

    public void setDescript(String descript) {
        this.form.descript = descript;
    }

    public void setTimeLength(String timeLength) {
        if (StringUtils.isNotEmpty(timeLength)) {
            this.form.timeLength = Integer.parseInt(timeLength);
        }
    }

    public void setLenderId(String lenderId) {
        this.form.lenderId = lenderId;
    }


    public void setXfxsService(XfxsService xfxsService) {
        this.xfxsService = xfxsService;
    }


}
