package com.agileai.hr.service.overtime;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.ws.BaseRestService;
import com.agileai.hr.cxmodule.HrWorkOvertimeManage;
import com.agileai.util.DateUtil;


public class OvertimeImpl extends BaseRestService implements Overtime {
	
	@Override
	public String findOvertimeList() {
		String responseText = "";
		try {
			JSONObject jsonObject = new JSONObject();
			User user = (User) this.getUser();
			String userCode = user.getUserCode();
			
			List<DataRow> reList = getService().findOvertimeList(new DataParam("userCode", userCode));
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<reList.size();i++){
				DataRow dataRow = reList.get(i);
				JSONObject jsonObject1 = new JSONObject();
				jsonObject1.put("wotId", dataRow.get("WOT_ID"));
				jsonObject1.put("wotPlace", dataRow.get("WOT_PLACE"));
				jsonObject1.put("wotTime", dataRow.get("WOT_TIME"));
				Date overtimeDate = (Date) dataRow.get("WOT_OVERTIME_DATE");
				String date = DateUtil.getDateByType(DateUtil.YYMMDD_SLANTING, overtimeDate);
				jsonObject1.put("wotOverTimeDate", date);
				jsonObject1.put("wotOverTimeState", dataRow.get("STATE"));
				jsonArray.put(jsonObject1);
			}
			jsonObject.put("datas", jsonArray);
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return responseText;
	}
	
	@Override
	public String addRecord(String info) {
    	String responseText = "";
    	try {
    		JSONObject jsonObject = new JSONObject(info);
        	User user = (User) this.getUser();
        	String userId = user.getUserId();
        	
        	String place = jsonObject.get("wotPlace").toString();
        	
        	String wotDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String overtimeDateUTC = jsonObject.get("wotOverTimeDate").toString().replace("Z", " UTC");
        	Date otd = format.parse(overtimeDateUTC);
        	String overtimeDate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, otd);
        	
        	String time = jsonObject.get("wotTime").toString();
        	String participant = jsonObject.get("wotParticipant").toString();
        	String desc = jsonObject.get("wotDesc").toString();
        	
        	DataParam createParam = new DataParam("USER_ID",userId,"WOT_DATE",wotDate,"WOT_PLACE",place,"WOT_OVERTIME_DATE",overtimeDate,"WOT_TIME",time,"WOT_PARTICIPANT",participant,"WOT_DESC",desc);
        	getService().createRecord(createParam);
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String getOvertimeRecord(String id) {
    	String responseText = "";
    	try {
    		DataRow dataRow = getService().getRecord(new DataParam("WOT_ID",id));
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("wotId", dataRow.get("WOT_ID"));
    		jsonObject.put("wotPlace", dataRow.get("WOT_PLACE"));
    		jsonObject.put("wotOverTimeDate", dataRow.get("WOT_OVERTIME_DATE"));
    		jsonObject.put("wotTime", dataRow.get("WOT_TIME"));
    		jsonObject.put("wotParticipant", dataRow.get("WOT_PARTICIPANT"));
    		jsonObject.put("wotDesc", dataRow.get("WOT_DESC"));
    		
    		responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String updateOvertimeInfo(String info) {
    	String responseText = "";
    	try {
    		JSONObject jsonObject = new JSONObject(info);
        	User user = (User) this.getUser();
        	String userId = user.getUserId();
        	
        	String id = jsonObject.getString("wotId").toString();
        	String place = jsonObject.get("wotPlace").toString();
        	
        	String wotDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String overtimeDateUTC = jsonObject.get("wotOverTimeDate").toString().replace("Z", " UTC");
        	Date otd = format.parse(overtimeDateUTC);
        	String overtimeDate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, otd);
        	
        	String time = jsonObject.get("wotTime").toString();
        	String participant = jsonObject.get("wotParticipant").toString();
        	String desc = jsonObject.get("wotDesc").toString();
        	
        	DataParam createParam = new DataParam("WOT_ID",id,"USER_ID",userId,"WOT_DATE",wotDate,"WOT_PLACE",place,"WOT_OVERTIME_DATE",overtimeDate,"WOT_TIME",time,"WOT_PARTICIPANT",participant,"WOT_DESC",desc,"STATE","drafe");
        	getService().updateRecord(createParam);
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String submitOvertimeInfo(String id) {
    	String responseText = "";
    	try {
    		getService().submitRecord(new DataParam("WOT_ID",id,"STATE","submitted"));
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }

	@Override
	public String deleteOvertimeRecord(String id) {
    	String responseText = "";
    	try {
    		getService().deletRecord(new DataParam("WOT_ID",id));
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
    protected HrWorkOvertimeManage getService() {
        return (HrWorkOvertimeManage) this.lookupService(HrWorkOvertimeManage.class);
    }
}
