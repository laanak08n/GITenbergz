package com.agileai.hr.module.leave.handler;

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
import com.agileai.hr.cxmodule.HrLeaveManage;
import com.agileai.util.DateUtil;

public class MobileLeaveHandler extends BaseHandler {
	
	@PageAction
	public ViewRenderer findLeaveList(DataParam param) {
		String responseText = null;
		try {
			JSONObject jsonObject = new JSONObject();
			User user = (User) this.getUser();
			String userCode = user.getUserCode();
			param.put("userCode", userCode);
			
			List<DataRow> reList = getService().findLeaveList(param);
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<reList.size();i++){
				DataRow dataRow = reList.get(i);
				JSONObject jsonObject1 = new JSONObject();
				jsonObject1.put("id", dataRow.get("LEA_ID"));
				jsonObject1.put("type", dataRow.get("LEA_TYPE"));
				jsonObject1.put("days", dataRow.get("LEA_DAYS"));
				Date leaDate = (Date) dataRow.get("LEA_DATE");
				String date = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_SLANTING, leaDate);
				jsonObject1.put("date", date.substring(5));
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
    public ViewRenderer createLeaveInfo(DataParam param){
    	String responseText = FAIL;
    	try {
    		String inputString = this.getInputString();
        	JSONObject jsonObject = new JSONObject(inputString);
        	User user = (User) this.getUser();
        	String userId = user.getUserId();
        	
        	String type = jsonObject.get("type").toString();
        	
        	String leaDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String sdateUTC = jsonObject.get("sdate").toString().replace("Z", " UTC");
        	Date sd = format.parse(sdateUTC);
        	String sdate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, sd);
        	
        	String edateUTC = jsonObject.get("edate").toString().replace("Z", " UTC");
        	Date ed = format.parse(edateUTC);
        	String edate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, ed);
        	
        	String days = jsonObject.get("days").toString();
        	String cause = jsonObject.get("cause").toString();
        	DataParam createParam = new DataParam("USER_ID",userId,"LEA_TYPE",type,"LEA_DATE",leaDate,"LEA_SDATE",sdate,"LEA_EDATE",edate,"LEA_DAYS",days,"LEA_CAUSE",cause);
        	getService().createRecord(createParam);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer editLeaveInfo(DataParam param){
    	String responseText = FAIL;
    	try {
    		String inputString = this.getInputString();
        	JSONObject jsonObject = new JSONObject(inputString);
        	User user = (User) this.getUser();
        	String userId = user.getUserId();
        	
        	String id = jsonObject.get("id").toString();
        	String type = jsonObject.get("type").toString();
        	
        	String leaDate = DateUtil.format(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
        	
        	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS Z");
        	String sdateUTC = jsonObject.get("sdate").toString().replace("Z", " UTC");
        	Date sd = format.parse(sdateUTC);
        	String sdate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, sd);
        	
        	String edateUTC = jsonObject.get("edate").toString().replace("Z", " UTC");
        	Date ed = format.parse(edateUTC);
        	String edate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, ed);
        	
        	String days = jsonObject.get("days").toString();
        	String cause = jsonObject.get("cause").toString();
        	DataParam updateParam = new DataParam("LEA_ID",id,"USER_ID",userId,"LEA_TYPE",type,"LEA_DATE",leaDate,"LEA_SDATE",sdate,"LEA_EDATE",edate,"LEA_DAYS",days,"LEA_CAUSE",cause,"STATE","drafe");
        	getService().updateRecord(updateParam);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer submitLeaveInfo(DataParam param){
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
    public ViewRenderer viewLeaveInfo(DataParam param){
    	String responseText = null;
    	try {
    		DataRow dataRow = getService().getRecord(param);
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("id", dataRow.get("LEA_ID"));
    		jsonObject.put("type", dataRow.get("LEA_TYPE"));
    		jsonObject.put("sdate", dataRow.get("LEA_SDATE"));
    		jsonObject.put("edate", dataRow.get("LEA_EDATE"));
    		jsonObject.put("days", dataRow.get("LEA_DAYS"));
    		jsonObject.put("cause", dataRow.get("LEA_CAUSE"));
    		
    		responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    
    @PageAction
    public ViewRenderer deteleLeaveInfo(DataParam param){
    	String responseText = FAIL;
    	try {
    		getService().deletRecord(param);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }

    protected HrLeaveManage getService() {
        return (HrLeaveManage) this.lookupService(HrLeaveManage.class);
    }
    
    @PageAction
    public ViewRenderer initLeaveInInfo(DataParam param){
    	String responseText = null;
    	try {
    		JSONObject jsonObject = new JSONObject();
    		jsonObject.put("sdate", DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date()));
    		jsonObject.put("edate", DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(new Date(), DateUtil.DAY, 1)));
    		
    		responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
}
