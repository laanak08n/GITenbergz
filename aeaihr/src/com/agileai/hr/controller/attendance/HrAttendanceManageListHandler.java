package com.agileai.hr.controller.attendance;

import java.io.ByteArrayInputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.StandardListHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.NullRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.attendance.HrAttendanceManage;
import com.agileai.hr.common.ExportFileHelper;
import com.agileai.util.DateUtil;

public class HrAttendanceManageListHandler extends StandardListHandler {
	public HrAttendanceManageListHandler() {
		super();
		this.editHandlerClazz = HrAttendanceManageEditHandler.class;
		this.serviceId = buildServiceId(HrAttendanceManage.class);
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		mergeParam(param);
		initParameters(param);

		String paramteDate = param.get("adtDate");
		setAttribute("canSignIn", true);
		setAttribute("canSignOut", true);
		String currentDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL);
		if (paramteDate.equals(currentDate)) {
			User user = (User) this.getUser();
			DataParam queryParam = new DataParam();
			queryParam.put("currentDate", DateUtil.getDateByType(
					DateUtil.YYMMDD_HORIZONTAL, new Date()));
			queryParam.put("currentUser", user.getUserId());
			DataRow record = getService().getRecord(queryParam);
			if (record != null) {
				if (record.get("ATD_IN_TIME") != null) {
					setAttribute("canSignIn", false);
				}
				if (record.get("ATD_OUT_TIME") != null) {
					setAttribute("canSignOut", false);
				}
			} else {
				setAttribute("canSignOut", false);
			}
		} else {
			setAttribute("canSignIn", false);
			setAttribute("canSignOut", false);
		}

		this.setAttributes(param);
		List<DataRow> rsList = getService().findRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	protected void processPageAttributes(DataParam param) {
	}

	protected void initParameters(DataParam param) {
		initParamItem(param, "adtDate",
				DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL));
	}

	@PageAction
	public ViewRenderer beforeDay(DataParam param) {
		String atdDate = param.get("adtDate");
		Date selectDate = DateUtil.getDate(atdDate);
		Date beforeDate = DateUtil.getDateAdd(selectDate, DateUtil.DAY, -1);
		String targetDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
				beforeDate);
		param.put("adtDate", targetDate);
		return prepareDisplay(param);
	}

	@PageAction
	public ViewRenderer nextDay(DataParam param) {
		String atdDate = param.get("adtDate");
		Date selectDate = DateUtil.getDate(atdDate);
		Date beforeDate = DateUtil.getDateAdd(selectDate, DateUtil.DAY, +1);
		String targetDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
				beforeDate);
		param.put("adtDate", targetDate);
		return prepareDisplay(param);
	}
	
	@SuppressWarnings({ "rawtypes", "unchecked" })
	public ViewRenderer doExportAction(DataParam param){
		String fileType = param.get("fileType");
		String exportFileName = "考勤表";
		try {
			Date adtDate = DateUtil.getDate(param.get("adtDate"));
			param.put("sdate", DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
					DateUtil.getDateAdd(DateUtil.getBeginOfMonth(adtDate),DateUtil.MONTH,0)));
			param.put("edate", DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
					DateUtil.getDateAdd(DateUtil.getBeginOfMonth(adtDate),DateUtil.MONTH,+1)));
			List<DataRow> records = getService().attendanceStatisticsRecords(param);
			HashMap model = new HashMap();
			model.put("title",exportFileName);
			String date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,adtDate).substring(0, 7);
			model.put("title1",date+"月考勤情况");
			String content ="";
			for(int i=0;i<records.size();i++){
				DataRow dataRow = records.get(i);
				content = content + (String) dataRow.get("USER_NAME")+":签到"+dataRow.get("IN_NUM").toString()+"次,签退"+dataRow.get("OUT_NUM").toString()+"次."+"<br/>";
			}
			model.put("content",content);
			
			ExportFileHelper exportFileHelper = new ExportFileHelper(request, response);
			String templateDir = exportFileHelper.geTemplateDirPath();
			if ("word".equals(fileType)){
				String templateFile = "AttendanceDoc.ftl";
				
				ByteArrayInputStream bais = exportFileHelper.buildHtml4Doc(templateDir,templateFile, model);
				exportFileHelper.exportWord(bais,exportFileName+".doc");				
			}else{
				String templateFile = "AttendancePdf.ftl";
				
				String inputFileContent = exportFileHelper.buildHtml4Pdf(templateDir,templateFile, model);
				exportFileHelper.exportPdf(inputFileContent, exportFileName+".pdf");
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return new NullRenderer();
	}

	public ViewRenderer doQueryAction(DataParam param) {
		return prepareDisplay(param);
	}

	protected HrAttendanceManage getService() {
		return (HrAttendanceManage) this.lookupService(this.getServiceId());
	}
}
