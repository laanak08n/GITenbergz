package com.agileai.hr.module.workovertime.handler;

import java.util.Date;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.StandardEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.workovertime.service.HrWorkOvertimeManage;
import com.agileai.util.DateUtil;

public class HrWorkOvertimeManageEditHandler extends StandardEditHandler {
	public HrWorkOvertimeManageEditHandler() {
		super();
		this.listHandlerClass = HrWorkOvertimeManageListHandler.class;
		this.serviceId = buildServiceId(HrWorkOvertimeManage.class);
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		User user = (User) this.getUser();
		param.put("currentDate",
				DateUtil.getDateByType(DateUtil.YYMMDDHHMISS_HORIZONTAL, new Date()));
		param.put("USER_CODE", user.getUserId());
		if ("insert".equals(operaType)) {
			setAttribute("doEdit", true);
			setAttribute("doSignIn", false);
			setAttribute("doApprove", false);
			setAttribute("doSubmit", false);
		}
		if ("update".equals(operaType)) {
			setAttribute("doEdit", true);
			setAttribute("doSignIn", false);
			setAttribute("doApprove",false);
			setAttribute("doSubmit", true);
			if (isReqRecordOperaType(operaType)) {
				DataRow record = getService().getNowRecord(param);
				this.setAttributes(record);
			}
		}
		if ("approve".equals(operaType)) {
			setAttribute("isApprove", true);
			setAttribute("doSignIn", true);
			setAttribute("doEdit", false);
			setAttribute("doApprove",true);
			if (!isReqRecordOperaType(operaType)) {
				DataRow record = getService().getNowRecord(param);
				this.setAttributes(record);
				this.setAttribute(
						"WOT_APPROVER_NAME",
						this.getAttribute("WOT_APPROVER_NAME",
								user.getUserName()));
				this.setAttribute("WOT_APPROVER",
						this.getAttribute("WOT_APPROVER", user.getUserId()));
				if (this.getAttribute("WOT_APP_TIME") == null) {
					String date = DateUtil.getDateByType(
							DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
					this.setAttribute("WOT_APP_TIME", date);
				}
			}
		} else {
			setAttribute("isApprove", false);
		}
		if("revokeApproval".equals(operaType)){
			DataRow record = getService().getNowRecord(param);
			this.setAttributes(record);
			setAttribute("doRevokeApprove", true);
		}
		if("detail".equals(operaType)){
			if (isReqRecordOperaType(operaType)) {
				DataRow record = getService().getNowRecord(param);
				if(record!=null){
					if(record.get("STATE").equals("approved")){
						setAttribute("doSignIn", true);
					}
					if(record.get("STATE").equals("drafe")){
						if(user.getUserId().equals(record.get("USER_ID"))){
							setAttribute("doEdit", true);
							setAttribute("doSubmit", true);
						}
					}
					if(record.get("STATE").equals("submitted")){
						setAttribute("doApprove",true);
						setAttribute("doSignIn", true);
						DataRow records = getService().getNowRecord(param);
						this.setAttributes(records);
						this.setAttribute(
								"WOT_APPROVER_NAME",
								this.getAttribute("WOT_APPROVER_NAME",
										user.getUserName()));
						this.setAttribute("WOT_APPROVER",
								this.getAttribute("WOT_APPROVER", user.getUserId()));
						if (this.getAttribute("WOT_APP_TIME") == null) {
							String date = DateUtil.getDateByType(
									DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
							this.setAttribute("WOT_APP_TIME", date);
						}
					}
					if(record.get("STATE").equals("approved")){
						setAttribute("doSignIn", true);
						setAttribute("doRevokeApprove", true);
					}
				}
				this.setAttributes(record);
			}
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

		String date = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL,
				new Date());
		if (this.getAttribute("WOT_DATE") == null) {
			this.setAttribute("WOT_DATE", date);
		}		
		setAttribute("WOT_TIME", FormSelectFactory.create("WOT_TIME")
				.addSelectedValue(getOperaAttributeValue("WOT_TIME", "")));
		setAttribute("APP_RESULT",FormSelectFactory.create("APP_RESULT").addSelectedValue(
				getAttributeValue("APP_RESULT", "YES")));
		setAttribute("STATE",FormSelectFactory.create("STATE") .addSelectedValue(
                		getOperaAttributeValue("STATE","drafe")));

	}

	public ViewRenderer doSaveMasterRecordAction(DataParam param) {
		String operateType = param.get(OperaType.KEY);
		String responseText = "fail";
		if (OperaType.CREATE.equals(operateType)) {
			getService().createRecord(param);
			responseText = param.get("WOT_ID");
		} 
		else if (OperaType.UPDATE.equals(operateType)) {
			getService().updateRecord(param);
			responseText = param.get("WOT_ID");
		}else if(OperaType.DETAIL.equals(operateType)){
			getService().updateRecord(param);
			responseText = param.get("WOT_ID");
		}
		return new AjaxRenderer(responseText);
	}
	public ViewRenderer detail(DataParam param) {
		getService().getNowRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}
	@PageAction
	public ViewRenderer submit(DataParam param) {
		param.put("STATE","submitted");
		getService().updateRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}
	@PageAction
	public ViewRenderer approve(DataParam param) {
		param.put("STATE","approved");
		getService().approveRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}
	@PageAction
	public ViewRenderer revokeApproval(DataParam param) {
		param.put("STATE", "drafe");
		param.put("WOT_APPROVER","");
		param.put("WOT_APP_TIME","");
		param.put("APP_RESULT","");
		param.put("WOT_APP_OPINION","");
		getService().approveRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}
	@PageAction
	public ViewRenderer drafe(DataParam param) {
		param.put("STATE", "drafe");
		param.put("WOT_APPROVER","");
		param.put("WOT_APP_TIME","");
		param.put("APP_RESULT","");
		param.put("WOT_APP_OPINION","");
		getService().approveRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}
	protected HrWorkOvertimeManage getService() {
		return (HrWorkOvertimeManage) this.lookupService(this.getServiceId());
	}
}
