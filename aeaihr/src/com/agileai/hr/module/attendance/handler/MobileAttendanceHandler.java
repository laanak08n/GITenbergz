package com.agileai.hr.module.attendance.handler;

import java.util.Date;

import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
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
	public ViewRenderer fingAroundBuilding(DataParam param){
		String responseText = null;
		try {
			String location = param.get("location");
//			String lat = param.get("lat");
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
    
	protected HrAttendanceManage getService() {
		return (HrAttendanceManage) this.lookupService(HrAttendanceManage.class);
	}
}
