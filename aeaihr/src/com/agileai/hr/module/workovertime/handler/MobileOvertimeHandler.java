package com.agileai.hr.module.workovertime.handler;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.workovertime.service.HrWorkOvertimeManage;
import com.agileai.util.DateUtil;

public class MobileOvertimeHandler extends BaseHandler {
	
	@PageAction
	public ViewRenderer findOvertimeList(DataParam param) {
		String responseText = null;
		try {
			JSONObject jsonObject = new JSONObject();
			User user = (User) this.getUser();
			String userCode = user.getUserCode();
			param.put("userCode", userCode);
			
			List<DataRow> reList = getService().findOvertimeList(param);
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<reList.size();i++){
				DataRow dataRow = reList.get(i);
				JSONObject jsonObject1 = new JSONObject();
				jsonObject1.put("id", dataRow.get("WOT_ID"));
				jsonObject1.put("place", dataRow.get("WOT_PLACE"));
				jsonObject1.put("time", dataRow.get("WOT_TIME"));
				Date overtimeDate = (Date) dataRow.get("WOT_OVERTIME_DATE");
				String date = DateUtil.getDateByType(DateUtil.YYMMDD_SLANTING, overtimeDate);
				jsonObject1.put("overtimeDate", date);
				jsonObject1.put("state", dataRow.get("STATE"));
				jsonArray.put(jsonObject1);
			}
			jsonObject.put("datas", jsonArray);
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return new AjaxRenderer(responseText);
	}
	
    @PageAction
    public ViewRenderer createOvertimeInfo(DataParam param){
    	String responseText = FAIL;
    	try {
    		String inputString = this.getInputString();
        	JSONObject jsonObject = new JSONObject(inputString);
        	User user = (User) this.getUser();
        	String userId = user.getUserId();
        	
        	String place = jsonObject.get("place").toString();
        	
        	String wotDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String overtimeDateUTC = jsonObject.get("overtimeDate").toString().replace("Z", " UTC");
        	Date otd = format.parse(overtimeDateUTC);
        	String overtimeDate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, otd);
        	
        	String time = jsonObject.get("time").toString();
        	String participant = jsonObject.get("participant").toString();
        	String desc = jsonObject.get("desc").toString();
        	
        	DataParam createParam = new DataParam("USER_ID",userId,"WOT_DATE",wotDate,"WOT_PLACE",place,"WOT_OVERTIME_DATE",overtimeDate,"WOT_TIME",time,"WOT_PARTICIPANT",participant,"WOT_DESC",desc);
        	getService().createRecord(createParam);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer submitOvertimeInfo(DataParam param){
    	String responseText = FAIL;
    	try {
    		param.put("STATE","submitted");
    		getService().submitRecord(param);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer viewOvertimeInfo(DataParam param){
    	String responseText = null;
    	try {
    		DataRow dataRow = getService().getRecord(param);
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("id", dataRow.get("WOT_ID"));
    		jsonObject.put("place", dataRow.get("WOT_PLACE"));
    		jsonObject.put("overtimeDate", dataRow.get("WOT_OVERTIME_DATE"));
    		jsonObject.put("time", dataRow.get("WOT_TIME"));
    		jsonObject.put("participant", dataRow.get("WOT_PARTICIPANT"));
    		jsonObject.put("desc", dataRow.get("WOT_DESC"));
    		
    		responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer editOvertimeInfo(DataParam param){
    	String responseText = FAIL;
    	try {
    		String inputString = this.getInputString();
        	JSONObject jsonObject = new JSONObject(inputString);
        	User user = (User) this.getUser();
        	String userId = user.getUserId();
        	
        	String id = jsonObject.getString("id").toString();
        	String place = jsonObject.get("place").toString();
        	
        	String wotDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String overtimeDateUTC = jsonObject.get("overtimeDate").toString().replace("Z", " UTC");
        	Date otd = format.parse(overtimeDateUTC);
        	String overtimeDate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, otd);
        	
        	String time = jsonObject.get("time").toString();
        	String participant = jsonObject.get("participant").toString();
        	String desc = jsonObject.get("desc").toString();
        	
        	DataParam createParam = new DataParam("WOT_ID",id,"USER_ID",userId,"WOT_DATE",wotDate,"WOT_PLACE",place,"WOT_OVERTIME_DATE",overtimeDate,"WOT_TIME",time,"WOT_PARTICIPANT",participant,"WOT_DESC",desc,"STATE","drafe");
        	getService().updateRecord(createParam);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer deteleOvertimeInfo(DataParam param){
    	String responseText = FAIL;
    	try {
    		getService().deletRecord(param);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer initOvertimeInInfo(DataParam param){
    	String responseText = null;
    	try {
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("overtimeDate", DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date()));
    		
    		responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
	protected HrWorkOvertimeManage getService() {
		return (HrWorkOvertimeManage) this.lookupService(HrWorkOvertimeManage.class);
	}
}
