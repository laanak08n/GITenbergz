package com.agileai.hr.service.leave;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.ws.BaseRestService;
import com.agileai.hr.cxmodule.HrLeaveManage;
import com.agileai.util.DateUtil;


public class LeaveImpl extends BaseRestService implements Leave {
	
	@Override
	public String findLeaveList() {
		String responseText = null;
		try {
			User user = (User) this.getUser();
			String userCode = user.getUserCode();
			
			List<DataRow> reList = getService().findLeaveList(new DataParam("userCode", userCode));
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<reList.size();i++){
				DataRow dataRow = reList.get(i);
				JSONObject jsonObject1 = new JSONObject();
				jsonObject1.put("leaId", dataRow.get("LEA_ID"));
				jsonObject1.put("leaType", dataRow.get("LEA_TYPE"));
				jsonObject1.put("leaDays", dataRow.get("LEA_DAYS"));
				Date leaDate = (Date) dataRow.get("LEA_DATE");
				String date = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_SLANTING, leaDate);
				jsonObject1.put("leaDate", date.substring(5));
				jsonObject1.put("leaState", dataRow.get("STATE"));
				jsonArray.put(jsonObject1);
			}
			responseText = jsonArray.toString();
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
        	
        	String type = jsonObject.get("leaType").toString();
        	
        	String leaDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String sdateUTC = jsonObject.get("leaSdate").toString().replace("Z", " UTC");
        	Date sd = format.parse(sdateUTC);
        	String sdate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, sd);
        	
        	String edateUTC = jsonObject.get("leaEdate").toString().replace("Z", " UTC");
        	Date ed = format.parse(edateUTC);
        	String edate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, ed);
        	
        	String days = jsonObject.get("leaDays").toString();
        	String cause = jsonObject.get("leaCause").toString();
        	DataParam createParam = new DataParam("USER_ID",userId,"LEA_TYPE",type,"LEA_DATE",leaDate,"LEA_SDATE",sdate,"LEA_EDATE",edate,"LEA_DAYS",days,"LEA_CAUSE",cause);
        	getService().createRecord(createParam);
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String getLeaveRecord(String id) {
    	String responseText = null;
    	try {
    		DataRow dataRow = getService().getRecord(new DataParam("LEA_ID",id));
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("leaId", dataRow.get("LEA_ID"));
    		jsonObject.put("leaType", dataRow.get("LEA_TYPE"));
    		jsonObject.put("leaSdate", dataRow.get("LEA_SDATE"));
    		jsonObject.put("leaEdate", dataRow.get("LEA_EDATE"));
    		jsonObject.put("leaDays", dataRow.get("LEA_DAYS"));
    		jsonObject.put("leaCause", dataRow.get("LEA_CAUSE"));
    		
    		responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String updateLeaveInfo(String info) {
    	String responseText = "";
    	try {
    		JSONObject jsonObject = new JSONObject(info);
        	User user = (User) this.getUser();
        	String userId = user.getUserId();
        	
        	String id = jsonObject.get("leaId").toString();
        	String type = jsonObject.get("leaType").toString();
        	
        	String leaDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String sdateUTC = jsonObject.get("leaSdate").toString().replace("Z", " UTC");
        	Date sd = format.parse(sdateUTC);
        	String sdate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, sd);
        	
        	String edateUTC = jsonObject.get("leaEdate").toString().replace("Z", " UTC");
        	Date ed = format.parse(edateUTC);
        	String edate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, ed);
        	
        	String days = jsonObject.get("leaDays").toString();
        	String cause = jsonObject.get("leaCause").toString();
        	DataParam updateParam = new DataParam("LEA_ID",id,"USER_ID",userId,"LEA_TYPE",type,"LEA_DATE",leaDate,"LEA_SDATE",sdate,"LEA_EDATE",edate,"LEA_DAYS",days,"LEA_CAUSE",cause,"STATE","drafe");
        	getService().updateRecord(updateParam);
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String submitLeaveInfo(String id) {
    	String responseText = "";
    	try {
    		getService().submitRecord(new DataParam("LEA_ID",id,"STATE","submitted"));
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
	}

	@Override
	public String deleteLeaveRecord(String id) {
    	String responseText = "";
    	try {
    		getService().deletRecord(new DataParam("LEA_ID",id));
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
    protected HrLeaveManage getService() {
        return (HrLeaveManage) this.lookupService(HrLeaveManage.class);
    }
}
