package com.thunisoft.sphy.taglibs;

import javax.servlet.jsp.JspException;

import org.apache.taglibs.standard.tag.common.core.OutSupport;

public class CorpNameOutTag extends OutSupport {

    /**   */
    private static final long serialVersionUID = 1L;

    @Override
    public int doStartTag() throws JspException {
        escapeXml = true;
        value = "浙江省瑞安市人民检察院";
        
        return super.doStartTag();
    }


}
