package com.agileai.hr.controller.weixin;

import java.util.Date;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.SimpleHandler;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.attendance.HrAttendanceManage;
import com.agileai.util.DateUtil;
import com.agileai.util.MapUtil;
import com.agileai.util.StringUtil;
import com.agileai.weixin.core.MessageEventHandler;
import com.agileai.weixin.tool.LocationHelper;
import com.agileai.weixin.tool.SecurityAuthHelper;

public class WxSignInHandler extends SimpleHandler{
	
	public WxSignInHandler(){
		super();
	}
	
	public ViewRenderer prepareDisplay(DataParam param) {
		try {
			String openId = (String)this.getSessionAttribute("openId");
			if (openId == null){
				String code = request.getParameter("code");
				openId = SecurityAuthHelper.getOpenId(code);
		        this.getSessionAttributes().put("openId", openId);
			}
			System.out.println("openId is " + openId);
	        HrAttendanceManage attendanceManage = this.lookupService(HrAttendanceManage.class);
	        DataParam queryParam = new DataParam("openId",openId);
	        DataRow userRow = attendanceManage.retrieveUserInfo(queryParam);
	        if (MapUtil.isNullOrEmpty(userRow)){
	        	this.setErrorMsg("没有跟微信绑定，点击这里<a href='javascript:bindWxUser()'>配置用户绑定</a>");
	        }else{
	        	String userId = userRow.getString("USER_ID");
	        	String currentDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
	        	queryParam = new DataParam();
	        	queryParam.put("currentDate",currentDate);
	        	queryParam.put("currentUser", userId);
	        	DataRow attendanceRow = attendanceManage.getRecord(queryParam);
	        	if (MapUtil.isNullOrEmpty(attendanceRow)){
	        		DataRow locaionRow = MessageEventHandler.getLocation(openId);
	        		if (MapUtil.isNullOrEmpty(locaionRow)){
	        			this.setErrorMsg("获取不到地理位置信息，请确认允许本公众号获取您的位置信息！");		
	        		}else{
						String latitude = String.valueOf(locaionRow.get("Latitude"));
						String longitude = String.valueOf(locaionRow.get("Longitude"));
//						String precision = locaionRow.getString("Precision");
						String address = LocationHelper.getAddress(latitude, longitude);
						
						if (StringUtil.isNullOrEmpty(address)){
							this.setErrorMsg("获取地理位置失败，请尝试重新签到！");
						}else{
			        		DataParam dataParam = new DataParam();
			        		String atdId = KeyGenerator.instance().genKey();
			        		dataParam.put("ATD_ID",atdId);
			        		dataParam.put("ATD_DATE",currentDate);
			        		dataParam.put("USER_ID",userId);
			        		dataParam.put("ATD_IN_TIME",new Date());
			        		dataParam.put("ATD_IN_PLACE",address);
			        		attendanceManage.createRecord(dataParam);	
			        		
			        		this.setAttribute("resultMsg", "签到成功！热爱生活、开心工作！");
			        		this.setAttribute("date", currentDate);
			        		this.setAttribute("week", getWeek(currentDate));
			        		DataParam adtDateParam = new DataParam("adtDate",currentDate);
			        		List<DataRow> records = attendanceManage.findRecords(adtDateParam);
			        		this.setRsList(records);
						}
	        		}
	        	}else{
	        		this.setAttribute("resultMsg", "已签到过！该检查是否有强迫症了:)");
	        		this.setAttribute("date", currentDate);
	        		this.setAttribute("week", getWeek(currentDate));
	        		DataParam adtDateParam = new DataParam("adtDate",currentDate);
	        		List<DataRow> records = attendanceManage.findRecords(adtDateParam);
	        		this.setRsList(records);
	        	}
	        }
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return new LocalRenderer(getPage());
	}

	@PageAction
	public ViewRenderer showBeforeDay(DataParam param){
		HrAttendanceManage attendanceManage = this.lookupService(HrAttendanceManage.class);
		String date = param.get("date");
		Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), DateUtil.DAY, -1);
		date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, tempDate);
		this.setAttribute("date", date);
		this.setAttribute("week", getWeek(date));
		DataParam adtDateParam = new DataParam("adtDate",date);
		List<DataRow> records = attendanceManage.findRecords(adtDateParam);
		this.setRsList(records);
		
		return new LocalRenderer(getPage());
	}
	@PageAction
	public ViewRenderer showNextDay(DataParam param){
		HrAttendanceManage attendanceManage = this.lookupService(HrAttendanceManage.class);
		String date = param.get("date");
		Date tempDate = DateUtil.getDateAdd(DateUtil.getDate(date), DateUtil.DAY, 1);
		date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, tempDate);
		this.setAttribute("date", date);
		this.setAttribute("week", getWeek(date));
		DataParam adtDateParam = new DataParam("adtDate",date);
		List<DataRow> records = attendanceManage.findRecords(adtDateParam);
		this.setRsList(records);
		
		return new LocalRenderer(getPage());
	}
	@PageAction
	public ViewRenderer showToday(DataParam param){
		HrAttendanceManage attendanceManage = this.lookupService(HrAttendanceManage.class);
		String date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
		this.setAttribute("date", date);
		this.setAttribute("week", getWeek(date));
		DataParam adtDateParam = new DataParam("adtDate",date);
		List<DataRow> records = attendanceManage.findRecords(adtDateParam);
		this.setRsList(records);
		
		return new LocalRenderer(getPage());
	}
	
	private String getWeek(String date){
		Date dateTemp = DateUtil.getDateTime(date);
		String week = DateUtil.getWeekText(dateTemp);
		return week;
	}
}