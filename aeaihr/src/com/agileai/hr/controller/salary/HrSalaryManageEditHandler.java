package com.agileai.hr.controller.salary;

import com.agileai.domain.*;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.StandardEditHandler;
import com.agileai.hotweb.domain.*;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.*;
import com.agileai.hr.bizmoduler.salary.HrSalaryManage;
import com.agileai.hr.common.PrivilegeHelper;

public class HrSalaryManageEditHandler extends StandardEditHandler {
	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		if ("approve".equals(operaType)) {
			setAttribute("isComeFromApprove", true);
			if (!isReqRecordOperaType(operaType)) {
				DataRow record = getService().getRecord(param);
				this.setAttributes(record);
			}
		} else {
			setAttribute("isComeFromApprove", false);
		}
		if (isReqRecordOperaType(operaType)) {
			DataRow record = getService().getRecord(param);
			this.setAttributes(record);
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
		if (!privilegeHelper.isSalMaster()) {
			setAttribute("hasRight", true);
		} else {
			setAttribute("hasRight", false);
		}
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

	protected HrSalaryManage getService() {
		return (HrSalaryManage) this.lookupService(this.getServiceId());
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
}
