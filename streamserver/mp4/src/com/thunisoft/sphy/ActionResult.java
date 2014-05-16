package com.thunisoft.sphy;

import java.util.HashMap;
import java.util.Map;

import com.thunisoft.sphy.hibernate.TXfxs;

public class ActionResult extends HashMap<String, Object> {
    private static final String KEY_XFXS = "xfxs";
    private static final String KEY_SUCCSSS = "success";
    private static final String KEY_MSG = "msg";
    
    
    /**   */
    private static final long serialVersionUID = 1L;


    public ActionResult(boolean success) {
        put(KEY_SUCCSSS, success);
    }

    public boolean isSuccess() {
        Object v = get(KEY_SUCCSSS);
        return v != null && (Boolean) v;
    }

    /**
     * 新增属性
     * xfxs: {
     *    xsId:..,
     *    appointCode: ...,
     *    state:..
     * }
     * @param xfxs
     * @since V1.0 2014-5-16
     * @author chenxh
     * @return 
     */
    public Map<String, Object> putXfxs(TXfxs xfxs) {
        Map<String, Object> map = make(KEY_XFXS);
        if (null != xfxs) {
            map.put("xsId", xfxs.getCId());
            map.put("appointCode", xfxs.getCCxm());
            map.put("state", xfxs.getNZt());
        }
        
        return map;
    }

    public Map<String, Object> getXfxs() {
        return (Map<String, Object>) get(KEY_XFXS);
    }

    public void setMsg(String msg) {
        put(KEY_MSG, msg);
    }
    
    public String getMsg() {
        return (String) get(KEY_MSG);
    }

    /**
     * 新建一个对象属性
     * 
     * @param property
     * @return
     * @since V1.0 2014-5-16
     * @author chenxh
     */
    public Map<String, Object> make(String property) {
        HashMap<String, Object> v = new HashMap<String, Object>();
        put(property, v);

        return v;
    }
}
