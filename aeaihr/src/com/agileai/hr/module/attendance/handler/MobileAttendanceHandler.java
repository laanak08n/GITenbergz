package com.agileai.hr.module.attendance.handler;

import java.util.Date;

import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.common.HttpClientHelper;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.cxmodule.HrAttendanceManage;
import com.agileai.util.DateUtil;

public class MobileAttendanceHandler extends BaseHandler {
	
	@PageAction
	public ViewRenderer findAroundBuilding(DataParam param){
		String responseText = null;
		try {
			String location = param.get("location");
			String url = "http://api.map.baidu.com/place/v2/search?ak=j6x1iGtGaxQcxaEDgbHRKDUp&output=json&query=%E6%88%BF%E5%AD%90&page_size=10&page_num=0&scope=1&location="+location+"&radius=1000";
			System.out.println("url is " + url);

			HttpClientHelper httpClientHelper = new HttpClientHelper();
			String jsonStr = httpClientHelper.retrieveGetReponseText(url);
	        JSONObject jsonObject = new JSONObject(jsonStr);
	        responseText = jsonObject.toString();
		}catch(Exception ex){
			log.error(ex.getLocalizedMessage(), ex);
		}
		return new AjaxRenderer(responseText);
	}
	
    @PageAction
    public ViewRenderer signIn(DataParam param){
    	String responseText = FAIL;
    	try {
    		String inputString = this.getInputString();
        	JSONObject jsonObject = new JSONObject(inputString);
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		String atdInTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		String atdInPlace = jsonObject.getString("address");
    		
    		DataParam createparam = new DataParam("USER_ID", userId,"ATD_DATE",atdDate,"ATD_IN_TIME",atdInTime,"ATD_IN_PLACE",atdInPlace);
    		
    		getService().createRecord(createparam);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer getSignInInfo(DataParam param){
    	String responseText = null;
    	try {
    		JSONObject jsonObject = new JSONObject();
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		param.put("currentUser", userId);
    		param.put("currentDate", atdDate);
    		DataRow dataRow = getService().getRecord(param);
    		if(dataRow == null){
    			jsonObject.put("isSignIn", "N");
    			jsonObject.put("atdInTime", DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date()));
    		}else{
    			jsonObject.put("isSignIn", "Y");
    			Date atdInTime = (Date) dataRow.get("ATD_IN_TIME");
    			jsonObject.put("atdInTime", DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, atdInTime));
    			jsonObject.put("address", dataRow.get("ATD_IN_PLACE"));
    		}
    		
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer signOut(DataParam param){
    	String responseText = FAIL;
    	try {
    		String inputString = this.getInputString();
        	JSONObject jsonObject = new JSONObject(inputString);
        	
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		param.put("currentUser", userId);
    		param.put("currentDate", atdDate);
    		DataRow dataRow = getService().getRecord(param);
    		String atdId = (String) dataRow.get("ATD_ID");
    				
    		String atdOutTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		String atdOutPlace = jsonObject.getString("address");
    		
    		DataParam updateparam = new DataParam("ATD_ID", atdId,"ATD_OUT_TIME",atdOutTime,"ATD_OUT_PLACE",atdOutPlace);
    		
    		getService().updateRecord(updateparam);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer getSignOutInfo(DataParam param){
    	String responseText = null;
    	try {
    		JSONObject jsonObject = new JSONObject();
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		param.put("currentUser", userId);
    		param.put("currentDate", atdDate);
    		DataRow dataRow = getService().getRecord(param);
    		if(dataRow == null){
    			jsonObject.put("isSignInOpera", "N");
    		}else if(dataRow.get("ATD_OUT_TIME") != null){
    			jsonObject.put("isSignInOpera", "Y");
    			jsonObject.put("isSignOut", "Y");
    			Date atdOutTime = (Date) dataRow.get("ATD_OUT_TIME");
    			jsonObject.put("atdOutTime", DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, atdOutTime));
    			jsonObject.put("address", dataRow.get("ATD_OUT_PLACE"));
    		}else{
    			jsonObject.put("isSignInOpera", "Y");
    			jsonObject.put("isSignOut", "N");
    			jsonObject.put("atdOutTime",DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date()));
    		}
    		
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer location(DataParam param){
    	String responseText = FAIL;
    	try {
    		String inputString = this.getInputString();
        	JSONObject jsonObject = new JSONObject(inputString);
    		User user = (User) getUser();
    		
    		String userId = user.getUserId();
    		String locatTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		String locatPlace = jsonObject.getString("address");
    		
    		DataParam createparam = new DataParam("USER_ID",userId,"LOCAT_TIME",locatTime,"LOCAT_PLACE",locatPlace);
    		
    		getService().createLocationRecord(createparam);
        	responseText = SUCCESS;
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    @PageAction
    public ViewRenderer getLocationInfo(DataParam param){
    	String responseText = null;
    	try {
    		JSONObject jsonObject = new JSONObject();
    		String locatTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		jsonObject.put("locatTime", locatTime);
    		
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
	protected HrAttendanceManage getService() {
		return (HrAttendanceManage) this.lookupService(HrAttendanceManage.class);
	}
}
