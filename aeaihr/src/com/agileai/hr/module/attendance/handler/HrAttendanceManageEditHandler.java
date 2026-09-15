package com.agileai.hr.module.attendance.handler;

import java.util.Date;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.StandardEditHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.cxmodule.HrAttendanceManage;
import com.agileai.util.DateUtil;

public class HrAttendanceManageEditHandler extends StandardEditHandler {
	public HrAttendanceManageEditHandler() {
		super();
		this.listHandlerClass = HrAttendanceManageListHandler.class;
		this.serviceId = buildServiceId(HrAttendanceManage.class);
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		User user = (User) this.getUser();
		param.put("currentDate",
				DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date()));
		param.put("currentUser", user.getUserId());

		if (isReqRecordOperaType(operaType)) {
			DataRow record = getService().getRecord(param);
			this.setAttributes(record);
		}
		this.setOperaType(operaType);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	protected void processPageAttributes(DataParam param) {
		User user = (User) this.getUser();
		this.setAttribute("USER_ID_NAME",
				this.getAttribute("USER_ID_NAME", user.getUserName()));
		this.setAttribute("USER_ID",
				this.getAttribute("USER_ID", user.getUserId()));

		if (this.getAttribute("ATD_IN_TIME") == null){
			this.setAttribute("ATD_IN_TIME", new Date());
			setAttribute("doSignIn", true);
		}else{
			this.setAttribute("ATD_OUT_TIME", new Date());
			setAttribute("doSignIn", false);
		}
		
		String adtDate = (String) this.getAttribute("ATD_DATE",DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date()));
		this.setAttribute("ATD_DATE", adtDate);
		
	}

	protected HrAttendanceManage getService() {

		return (HrAttendanceManage) this.lookupService(this.getServiceId());
	}
}
