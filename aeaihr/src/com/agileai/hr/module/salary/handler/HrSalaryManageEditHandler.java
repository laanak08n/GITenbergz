package com.agileai.hr.module.salary.handler;

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
import com.agileai.hr.module.salary.service.HrSalaryManage;

public class HrSalaryManageEditHandler extends StandardEditHandler {
	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		DataRow record = getService().getRecord(param);
		
		if ("approve".equals(operaType)) {
			setAttribute("isComeFromApprove", true);

		} else {
			setAttribute("isComeFromApprove", false);
		}
		if (operaType.equals("detail")) {
			setAttribute("isComeFromDetail", false);
		} else {
			setAttribute("isComeFromDetail", true);
		}
		if (operaType.equals("update")) {
			setAttribute("isComeFromUpdate", true);
		} else {
			setAttribute("isComeFromUpdate", false);
		}
		User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if (privilegeHelper.isSalMaster() ) {
			if(record.get("SAL_STATE").equals("0")){
				setAttribute("doEdit&Save", "true");
			}
			setAttribute("hasRight", true);
		}
		this.setAttributes(record);
		this.setOperaType(operaType);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	public HrSalaryManageEditHandler() {
		super();
		this.listHandlerClass = HrSalaryManageListHandler.class;
		this.serviceId = buildServiceId(HrSalaryManage.class);
	}

	protected void processPageAttributes(DataParam param) {
		setAttribute("SAL_STATE", FormSelectFactory.create("SAL_STATE")
				.addSelectedValue(getOperaAttributeValue("SAL_STATE", "")));
	}

	@PageAction
	public ViewRenderer doSaveAction(DataParam param) {
		String operateType = param.get(OperaType.KEY);
		if (OperaType.CREATE.equals(operateType)) {
			getService().createRecord(param);
		} else if (OperaType.UPDATE.equals(operateType)) {
			getService().updateRecord(param);
		}
		String masterRecordId = param.get("SAL_ID");
		this.getService().computeTotalMoney(masterRecordId);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}

	@PageAction
	public ViewRenderer approve(DataParam param) {
		param.put("SAL_STATE", "1");
		getService().approveRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}

	@PageAction
	public ViewRenderer revokeApproval(DataParam param) {
		param.put("SAL_STATE", "0");
		getService().approveRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}
	
	@PageAction
	public ViewRenderer annualLeaveRecalculation(DataParam param){
		String yaer = param.get("SAL_YEAR");
		String month = param.get("SAL_MONTH");
		String userId = param.get("SAL_USER");
		getService().recalculation(yaer,month,userId);
		return prepareDisplay(param);
	}
	

	protected HrSalaryManage getService() {
		return (HrSalaryManage) this.lookupService(this.getServiceId());
	}
}
