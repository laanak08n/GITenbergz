package com.agileai.hr.module.salary.handler;

import java.util.Date;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.SimpleHandler;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.common.DateHelper;
import com.agileai.hr.cxmodule.HrAttendanceManage;
import com.agileai.hr.cxmodule.HrSalaryManage;
import com.agileai.util.DateUtil;
import com.agileai.util.MapUtil;
public class AddAttendanceHandler extends SimpleHandler{
	public AddAttendanceHandler(){
		super();
	}
	public ViewRenderer prepareDisplay(DataParam param) {
		this.setAttributes(param);
		HrSalaryManage hrSalaryManage = this.lookupService(HrSalaryManage.class);
		String userId = param.get("salUser");
		setAttribute("salUser", userId);
		DataRow row = hrSalaryManage.getInductionAndCreateTime(userId);
		Date inductionDate = (Date) row.get("EMP_INDUCTION_TIME");
		Date createDate = (Date) row.get("EMP_CREATE_TIME");
		long dateDiff = DateUtil.getDateDiff(inductionDate,createDate, 1);
		String gatherDate = param.get("salYear")+"-"+param.get("salMonth")+"-01";		
		DateHelper dateHelper = new DateHelper();
		boolean isCalSalary = dateHelper.dateDuringMonth(inductionDate, gatherDate);
		if(isCalSalary){
			setAttribute("EMP_INDUCTION_TIME", inductionDate);
			setAttribute("dateDiff", dateDiff);
		}
		this.processPageAttributes(param);
		return new LocalRenderer(getPage());
	}
	
	protected void processPageAttributes(DataParam param){
	}
	
	public ViewRenderer doSaveAction(DataParam param){
		String responseText = FAIL;
		int addDay = Integer.parseInt(param.get("addDay"));
		for(int i=0; i<addDay; i++){
			KeyGenerator keyGenerator = new KeyGenerator();
			String atdId = keyGenerator.genKey();
			String atdDate = param.get("attendanceDate"+i);
			String userId = param.get("salUser");
			String atdPlace = param.get("atdPlace");
			
			DataParam queryAtdParam = new DataParam("currentUser",userId, "currentDate",atdDate);
			DataRow atdRow = getService().getRecord(queryAtdParam);
			
			DataRow empRow = getSalaryService().getInductionAndCreateTime(userId);
			Date inductionDate = (Date) empRow.get("EMP_INDUCTION_TIME");
			Date atdDateType = DateUtil.getDate(atdDate);
			if(!MapUtil.isNullOrEmpty(atdRow) || inductionDate.compareTo(atdDateType)>0 || atdDateType.compareTo(new Date())>0){
				return new AjaxRenderer(responseText);
			}
			getService().addAttendance(atdId, userId, atdDate, atdPlace);
			responseText = SUCCESS;
		}
		return new AjaxRenderer(responseText);
	}
	
	protected HrAttendanceManage getService() {
		return (HrAttendanceManage) this.lookupService(HrAttendanceManage.class);
	}
	
	protected HrSalaryManage getSalaryService(){
		return this.lookupService(HrSalaryManage.class);
	}
}
