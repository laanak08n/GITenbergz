package com.agileai.hr.module.salary.handler;

import java.util.Date;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.StandardListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.hr.module.salary.service.HrSalaryManage;
import com.agileai.util.DateUtil;

public class HrSalaryManageListHandler extends StandardListHandler {
	public HrSalaryManageListHandler() {
		super();
		this.editHandlerClazz = HrSalaryManageEditHandler.class;
		this.serviceId = buildServiceId(HrSalaryManage.class);
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if (!privilegeHelper.isSalMaster()) {
			param.put("currentUserCode", user.getUserId());
			setAttribute("hasRight", true);
		} else {
			param.put("currentUserCode", "");
			setAttribute("hasRight", false);
		}
		mergeParam(param);
		initParameters(param);
		setAttribute("canGather", true);
		String salDate = param.get("salDate");
		String paramDate = salDate.substring(0, 7);
		Date date = DateUtil.getBeginOfMonth(new Date());
		Date beforeDate = DateUtil.getDateAdd(date, DateUtil.MONTH, -1);
		String currentDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
				beforeDate);
		String currenDate = currentDate.substring(0, 7);
		if (paramDate.equals(currenDate)) {
			setAttribute("canGather", true);
		} else {
			setAttribute("canGather", false);
		}

		String year = salDate.substring(0, 4);
		String month = salDate.substring(5, 7);
		DataRow validDays = getService().retrieveValidDays(year, month);
		if(validDays == null){
			setAttribute("validDays", false);
		}else{
			setAttribute("validDays", true);
		}
		param.put("salYear", year, "salMonth", month);
		
		this.setAttributes(param);
		List<DataRow> rsList = getService().findRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}
	protected void processPageAttributes(DataParam param) {
		setAttribute("salState", FormSelectFactory.create("SAL_STATE")
				.addSelectedValue(param.get("salState")));
		initMappingItem("SAL_STATE", FormSelectFactory.create("SAL_STATE")
				.getContent());
	}

	protected void initParameters(DataParam param) {
		Date date = DateUtil.getBeginOfMonth(new Date());
		Date beforeDate = DateUtil.getDateAdd(date, DateUtil.MONTH, -1);
		String salDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(
						DateUtil.getBeginOfMonth(beforeDate), DateUtil.DAY, 9));
		String yearMonth = salDate.substring(0, 7);
		initParamItem(param, "salDate", yearMonth);
		initParamItem(param, "salState", "");
	}

	protected HrSalaryManage getService() {
		return (HrSalaryManage) this.lookupService(this.getServiceId());
	}

	@PageAction
	public ViewRenderer gather(DataParam param) {
		String salDate = param.get("salDate");  
		String year = salDate.substring(0, 4);
		String month = salDate.substring(5, 7);
		getService().gatherData(year, month);
		return this.prepareDisplay(param);
	}

	@PageAction
	public ViewRenderer beforeMonth(DataParam param) {
		String salDate = param.get("salDate");
		Date selectDate = DateUtil.getDate(salDate+"-01");
		Date beforeDate = DateUtil.getDateAdd(selectDate, DateUtil.MONTH, -1);
		String targetDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
				beforeDate);
		String yearMonth = targetDate.substring(0, 7);
		param.put("salDate", yearMonth);
		return prepareDisplay(param);
	}
	
	@PageAction
	public ViewRenderer currentMonth(DataParam param) {
		String date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
		String yearMonth = date.substring(0, 7);
		param.put("salDate", yearMonth);
		return prepareDisplay(param);
	}

	@PageAction
	public ViewRenderer nextMonth(DataParam param) {
		String salDate = param.get("salDate");
		Date selectDate = DateUtil.getDate(salDate+"-01");
		Date beforeDate = DateUtil.getDateAdd(selectDate, DateUtil.MONTH, +1);
		String targetDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
				beforeDate);
		String yearMonth = targetDate.substring(0, 7);
		param.put("salDate", yearMonth);
		return prepareDisplay(param);
	}

	@PageAction
	public ViewRenderer doApproveRequestAction(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=approve&comeFrome=approve");
	}
	
	@PageAction
	public ViewRenderer revokeApproval(DataParam param) {
		String salId = param.get("SAL_ID");
		getService().revokeApprovalRecords(salId);
		return prepareDisplay(param);
	}

	
}
