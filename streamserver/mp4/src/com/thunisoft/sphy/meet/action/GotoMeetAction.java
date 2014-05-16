package com.thunisoft.sphy.meet.action;

import com.opensymphony.xwork2.ActionSupport;
import com.thunisoft.sphy.meet.service.MeetService;

public class GotoMeetAction extends ActionSupport {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

    private MeetService meetService;
    
    @Override
    public String execute() throws Exception {
        return "success";
    }

    public void setSmsService(MeetService meetService) {
        this.meetService = meetService;
    }
}
