package com.agileai.hr.module.leave.handler;

import java.util.Date;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.StandardEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.hr.module.leave.service.HrLeaveManage;
import com.agileai.util.DateUtil;

public class HrLeaveManageEditHandler
        extends StandardEditHandler {
    public HrLeaveManageEditHandler() {
        super();
        this.listHandlerClass = HrLeaveManageListHandler.class;
        this.serviceId = buildServiceId(HrLeaveManage.class);
    }
    
    public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		User user = (User) this.getUser();
		param.put("currentDate",
				DateUtil.getDateByType(DateUtil.YYMMDDHHMISS_HORIZONTAL, new Date()));
		param.put("USER_ID", user.getUserId());
		if ("insert".equals(operaType)) {
			setAttribute("doInsertEdit", true);
			setAttribute("doSignIn", false);
			setAttribute("doApprove", false);
			setAttribute("doSubmit", false);
		}
		if ("update".equals(operaType)) {
			setAttribute("doInsertEdit", true);
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
			setAttribute("doInsertEdit", false);
			setAttribute("doApprove",true);
			if (!isReqRecordOperaType(operaType)) {
				DataRow record = getService().getNowRecord(param);
				this.setAttributes(record);
				this.setAttribute(
						"LEA_APPOVER_NAME",
						this.getAttribute("LEA_APPOVER_NAME",
								user.getUserName()));
				this.setAttribute("LEA_APPOVER",
						this.getAttribute("LEA_APPOVER", user.getUserId()));
				if (this.getAttribute("LEA_APP_TIME") == null) {
					String date = DateUtil.getDateByType(
							DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
					this.setAttribute("LEA_APP_TIME", date);
				}
			}
		} else {
			setAttribute("isApprove", false);
		}
		if("detail".equals(operaType)){
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
			setAttribute("doInsertEdit", true);
			setAttribute("doApprove",true);
			if (isReqRecordOperaType(operaType)) {
				DataRow record = getService().getNowRecord(param);
				if(record!=null){
					if(!privilegeHelper.isApprove()&&record.get("STATE").equals("drafe")){
						setAttribute("doInsertEdit", true);
						setAttribute("doApprove",false);
						setAttribute("doSignIn", false);
						setAttribute("doSubmit", true);
					}else if(!privilegeHelper.isApprove()&&record.get("STATE").equals("submitted")){
						setAttribute("doInsertEdit", false);
						setAttribute("doApprove",false);
						setAttribute("doSignIn", false);
					}else if(!privilegeHelper.isApprove()&&record.get("STATE").equals("approved")){
						setAttribute("doInsertEdit", false);
						setAttribute("doApprove",false);
						setAttribute("doSignIn", true);
					}else if(privilegeHelper.isApprove()&&record.get("STATE").equals("drafe")){
						setAttribute("doInsertEdit", false);
						setAttribute("doApprove",false);
						setAttribute("doSignIn", false);
						setAttribute("doSubmit", true);
						if(user.getUserId().equals(record.get("USER_ID"))){
							setAttribute("doInsertEdit", true);
						}
					}
					else if(privilegeHelper.isApprove()&&record.get("STATE").equals("submitted")){
						setAttribute("doInsertEdit", false);
						setAttribute("doApprove",true);
						setAttribute("doSignIn", true);
						DataRow records = getService().getNowRecord(param);
						this.setAttributes(records);
						this.setAttribute(
								"LEA_APPOVER_NAME",
								this.getAttribute("LEA_APPOVER_NAME",
										user.getUserName()));
						this.setAttribute("LEA_APPOVER",
								this.getAttribute("LEA_APPOVER", user.getUserId()));
						if (this.getAttribute("LEA_APP_TIME") == null) {
							String date = DateUtil.getDateByType(
									DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
							this.setAttribute("LEA_APP_TIME", date);
						}
					}else if(privilegeHelper.isApprove()&&record.get("STATE").equals("approved")){
						setAttribute("doInsertEdit", false);
						setAttribute("doApprove",false);
						setAttribute("doSignIn", true);
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
		if (this.getAttribute("LEA_DATE") == null) {
			this.setAttribute("LEA_DATE", date);
		}		

        setAttribute("LEA_TYPE",
                     FormSelectFactory.create("LEA_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("LEA_TYPE",
                                                                               "")));
        setAttribute("APP_RESULT",
                     FormSelectFactory.create("APP_RESULT")
                                      .addSelectedValue(getAttributeValue("APP_RESULT",
                                                                               "YES")));
        setAttribute("STATE",
                FormSelectFactory.create("STATE")
                                 .addSelectedValue(getOperaAttributeValue("STATE",
                                                                          "drafe")));
    }
    public ViewRenderer doSaveAction(DataParam param){
		String operateType = param.get(OperaType.KEY);
		if (OperaType.CREATE.equals(operateType)){
			getService().createRecord(param);	
		}
		else if (OperaType.UPDATE.equals(operateType)){
			getService().updateRecord(param);	
		}else if (OperaType.DETAIL.equals(operateType)){
			getService().updateRecord(param);	
		}
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
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
	public ViewRenderer drafe(DataParam param) {
		param.put("STATE","drafe");
		getService().updateRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}

    protected HrLeaveManage getService() {
        return (HrLeaveManage) this.lookupService(this.getServiceId());
    }
}
