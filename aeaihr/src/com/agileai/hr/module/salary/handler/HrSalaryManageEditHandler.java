package com.agileai.hr.module.salary.handler;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
import com.agileai.hr.cxmodule.HrSalaryManage;
import com.agileai.util.DateUtil;

public class HrSalaryManageEditHandler extends StandardEditHandler {
	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		DataRow record = getService().getRecord(param);
		String salDateStr = "";
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
		
		if(param.get("salDate")==null){
			salDateStr = param.get("SAL_YEAR")+"-"+param.get("SAL_MONTH")+"-01";
		}else{
			salDateStr = param.get("salDate")+"-01";
		}
		User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if (privilegeHelper.isSalMaster() ) {
			if(record.get("SAL_STATE").equals("0")){
				setAttribute("doEdit&Save", "true");
			}
			setAttribute("hasRight", true);
		}

		Date salDate = DateUtil.getDate(salDateStr);
		Date inductionDate = (Date)record.get("EMP_INDUCTION_TIME");
		Date regularDate = (Date)record.get("EMP_REGULAR_TIME");
		BigDecimal salProbation = (BigDecimal) record.get("SAL_PROBATION");
		BigDecimal salRegular = (BigDecimal) record.get("SAL_BASIC");
		BigDecimal salTotal = (BigDecimal) record.get("SAL_TOTAL");
		BigDecimal validDays = (BigDecimal) record.get("SAL_VALID_DAYS");
		BigDecimal salProbationDayMoney = new BigDecimal("0.0000");
		BigDecimal salRegularDayMoney = new BigDecimal("0.0000");
		BigDecimal salPerformance = (BigDecimal) record.get("SAL_PERFORMANCE");
		BigDecimal salSubsidy = (BigDecimal)record.get("SAL_SUBSIDY");
		if(DateUtil.getDateDiff(regularDate, salDate, DateUtil.DAY) >= 0){
			salRegularDayMoney = salTotal.divide(validDays,4,RoundingMode.HALF_UP);
		}else if(DateUtil.getDateDiff(regularDate, salDate, DateUtil.MONTH) == 0){
			salProbationDayMoney = salProbation.divide(validDays, 4, RoundingMode.HALF_UP);
			salRegularDayMoney = (salRegular.add(salPerformance).add(salSubsidy)).divide(validDays, 4, RoundingMode.HALF_UP);
		}else{
			salProbationDayMoney = salProbation.divide(validDays, 4, RoundingMode.HALF_UP);
		}		
		this.setAttribute("salProbationDayMoney", salProbationDayMoney);
		this.setAttribute("salRegularDayMoney", salRegularDayMoney);
		boolean isAddAttendance = false;
		if(DateUtil.getDateDiff(salDate, inductionDate, DateUtil.MONTH) == 0 ){
			isAddAttendance = true;
		}	
		setAttribute("isAddAttendance", isAddAttendance);
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
		String year = param.get("SAL_YEAR");
		String month = param.get("SAL_MONTH");
		String userId = param.get("SAL_USER");
		getService().recalculation(year,month,userId);
		return prepareDisplay(param);
	}
	

	protected HrSalaryManage getService() {
		return (HrSalaryManage) this.lookupService(this.getServiceId());
	}
}
