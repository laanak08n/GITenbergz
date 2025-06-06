package com.agileai.hr.module.attendance.exteral;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.common.HttpClientHelper;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.ws.BaseRestService;
import com.agileai.hr.cxmodule.HrAttendanceManage;
import com.agileai.hr.module.attendance.handler.MobileAttendanceHandler;
import com.agileai.util.DateUtil;
import com.agileai.util.StringUtil;


public class AttendanceImpl extends BaseRestService implements Attendance {

	@Override
	public String findAroundBuilding(String location) {
		String responseText = null;
		try {
			String url = "https://restapi.amap.com/v3/place/around?location="+location+"&types=%E5%95%86%E5%8A%A1%E4%BD%8F%E5%AE%85%7C%E5%85%AC%E5%8F%B8%E4%BC%81%E4%B8%9A&offset=10&page=1&radius=1000&output=json&key=c65021cd6043c78e7dd473ac1f9233a1";
			HttpClientHelper httpClientHelper = new HttpClientHelper();
			String jsonStr = httpClientHelper.retrieveGetReponseText(url);
	        JSONObject jsonObject = new JSONObject(jsonStr);
	        responseText = jsonObject.toString();
		}catch(Exception ex){
			log.error(ex.getLocalizedMessage(), ex);
		}
		return responseText;
	}

	@Override
	public String signIn(String info) {
    	String responseText = "";
    	try {
        	JSONObject jsonObject = new JSONObject(info);
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		String atdInTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		String atdInPlace = jsonObject.getString("address");
    		String house = jsonObject.getString("name");
    		String lng = jsonObject.getString("lng");
    		String lat = jsonObject.getString("lat");
    		JSONObject jsonObject1 = new JSONObject();
    		jsonObject1.put("lng",lng);
    		jsonObject1.put("lat",lat);
    		String coordinate = jsonObject1.toString();
    		DataParam createparam = new DataParam("USER_ID", userId,"ATD_DATE",atdDate,"ATD_IN_TIME",atdInTime,
    				"ATD_IN_COORDINATE",coordinate,"ATD_IN_PLACE",atdInPlace,"ATD_IN_HOUSE",house);
    		
    		getService().createRecord(createparam);
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String getSignInInfo() {
    	String responseText = "";
    	try {
    		JSONObject jsonObject = new JSONObject();
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		DataRow dataRow = getService().getRecord(new DataParam("currentUser", userId,"currentDate", atdDate));
    		if(dataRow == null){
    			jsonObject.put("isSignIn", "N");
    			jsonObject.put("atdInTime", DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date()));
    		}else{
    			jsonObject.put("isSignIn", "Y");
    			Date atdInTime = (Date) dataRow.get("ATD_IN_TIME");
    			jsonObject.put("atdInTime", DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, atdInTime));
    			jsonObject.put("name", dataRow.get("ATD_IN_HOUSE"));
    			jsonObject.put("address", dataRow.get("ATD_IN_PLACE"));
    			JSONObject jsonObject1 = new JSONObject(dataRow.getString("ATD_IN_COORDINATE"));
    			jsonObject.put("placeInfo", jsonObject1);
    		}
    		
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String signOut(String info) {
    	String responseText = "";
    	try {
        	JSONObject jsonObject = new JSONObject(info);
        	
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		DataRow dataRow = getService().getRecord(new DataParam("currentUser", userId,"currentDate", atdDate));
    		String atdId = (String) dataRow.get("ATD_ID");
    				
    		String atdOutTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		String atdOutPlace = jsonObject.getString("address");
    		String house = jsonObject.getString("name");
    		String lng = jsonObject.getString("lng");
    		String lat = jsonObject.getString("lat");
    		JSONObject jsonObject1 = new JSONObject();
    		jsonObject1.put("lng",lng);
    		jsonObject1.put("lat",lat);
    		String coordinate = jsonObject1.toString();
    		
    		DataParam updateparam = new DataParam("ATD_ID", atdId,"ATD_OUT_TIME",atdOutTime,"ATD_OUT_PLACE",atdOutPlace,
    				"ATD_OUT_COORDINATE",coordinate,"ATD_OUT_HOUSE",house);
    		
    		getService().updateRecord(updateparam);
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String getSignOutInfo() {
    	String responseText = null;
    	try {
    		JSONObject jsonObject = new JSONObject();
    		User user = (User) getUser();
    		String userId = user.getUserId();
    		String atdDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
    		DataRow dataRow = getService().getRecord(new DataParam("currentUser", userId,"currentDate", atdDate));
    		if(dataRow == null){
    			jsonObject.put("isSignInOpera", "N");
    		}else if(dataRow.get("ATD_OUT_TIME") != null){
    			jsonObject.put("isSignInOpera", "Y");
    			jsonObject.put("isSignOut", "Y");
    			Date atdOutTime = (Date) dataRow.get("ATD_OUT_TIME");
    			jsonObject.put("atdOutTime", DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, atdOutTime));
    			jsonObject.put("address", dataRow.get("ATD_OUT_PLACE"));
    			jsonObject.put("name", dataRow.get("ATD_OUT_HOUSE"));
    			if (StringUtil.isNotNullNotEmpty(dataRow.getString("ATD_OUT_COORDINATE"))){
    				JSONObject jsonObject1 = new JSONObject(dataRow.getString("ATD_OUT_COORDINATE"));
        			jsonObject.put("placeInfo", jsonObject1);    				
    			}
    		}else{
    			jsonObject.put("isSignInOpera", "Y");
    			jsonObject.put("isSignOut", "N");
    			jsonObject.put("atdOutTime",DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date()));
    			if (StringUtil.isNotNullNotEmpty(dataRow.getString("ATD_OUT_COORDINATE"))){
    				JSONObject jsonObject1 = new JSONObject(dataRow.getString("ATD_OUT_COORDINATE"));
        			jsonObject.put("placeInfo", jsonObject1);    				
    			}
    		}
    		
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String location(String info) {
    	String responseText = "";
    	try {
        	JSONObject jsonObject = new JSONObject(info);
    		User user = (User) getUser();
    		
    		String userId = user.getUserId();
    		String locatTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		String locatPlace = jsonObject.getString("address");
    		String locatHouse = jsonObject.getString("name");
    		
    		DataParam createparam = new DataParam("USER_ID",userId,"LOCAT_TIME",locatTime,"LOCAT_PLACE",locatPlace,"LOCAT_HOUSE",locatHouse);
    		
    		getService().createLocationRecord(createparam);
        	responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String getLocationInfo() {
    	String responseText = "";
    	try {
    		JSONObject jsonObject = new JSONObject();
    		String locatTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
    		jsonObject.put("locatTime", locatTime);
    		
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String findCurrentDaySigninInfos() {
    	String responseText = "";
    	try {
    		String currentDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,new Date());
    		String weekText = DateUtil.getWeekText(new Date());
    		List<DataRow> records = getService().findCurrentDaySigninInfos(currentDate);
    		String signInDateShow = currentDate.substring(5, 10);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
					String inTime = row.stringValue("ATD_IN_TIME");
					if(!inTime.isEmpty()){
						jsonObject11.put("inTime", inTime.substring(11, 16));
					}else{
						jsonObject11.put("inTime", inTime);
					}
					jsonObject11.put("inPlace", row.stringValue("ATD_IN_PLACE"));
					jsonObject11.put("inHouse", row.stringValue("ATD_IN_HOUSE"));
					jsonArray.put(jsonObject11);
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("name", "无记录");
				jsonArray.put(jsonObject11);
			}
    		
    		jsonObject.put("signInfos", jsonArray);
    		jsonObject.put("signInDate", currentDate);
    		jsonObject.put("signInDateShow", signInDateShow);
    		jsonObject.put("weekText", weekText);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }

	@Override
	public String findLastDaySigninInfos(String date) {
    	String responseText = "";
    	try {
    		if("null".equals(date) || null == date){
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(new Date(), DateUtil.DAY, -1));
    		}else{
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(DateUtil.getDateTime(date), DateUtil.DAY, -1));
    		}
    		String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
    		List<DataRow> records = getService().findCurrentDaySigninInfos(date);
    		String signInDateShow = date.substring(5, 10);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
					String inTime = row.stringValue("ATD_IN_TIME");
					if(!inTime.isEmpty()){
						inTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMISS_HORIZONTAL, row.getTimestamp("ATD_IN_TIME"));
						inTime = inTime.substring(11, 16).replaceAll("/", "-");
						jsonObject11.put("inTime", inTime);
					}else{
						jsonObject11.put("inTime", inTime);
					}
					jsonObject11.put("inPlace", row.stringValue("ATD_IN_PLACE"));
					jsonObject11.put("inHouse", row.stringValue("ATD_IN_HOUSE"));
					jsonArray.put(jsonObject11);
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("name", "无记录");
				jsonArray.put(jsonObject11);
			}
    		
    		jsonObject.put("signInfos", jsonArray);
    		jsonObject.put("signInDate", date);
    		jsonObject.put("signInDateShow", signInDateShow);
    		jsonObject.put("weekText", weekText);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String findFollowDaySigninInfos(String date) {
    	String responseText = "";
    	try {
    		if("null".equals(date) || null == date){
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(new Date(), DateUtil.DAY, 1));
    		}else{
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(DateUtil.getDateTime(date), DateUtil.DAY, 1));
    		}
    		String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
    		List<DataRow> records = getService().findCurrentDaySigninInfos(date);
    		String signInDateShow = date.substring(5, 10);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
					String inTime = row.stringValue("ATD_IN_TIME");
					if(!inTime.isEmpty()){
						inTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMISS_HORIZONTAL, row.getTimestamp("ATD_IN_TIME"));
						inTime = inTime.substring(11, 16).replaceAll("/", "-");
						jsonObject11.put("inTime", inTime);
					}else{
						jsonObject11.put("inTime", inTime);
					}
					jsonObject11.put("inPlace", row.stringValue("ATD_IN_PLACE"));
					jsonObject11.put("inHouse", row.stringValue("ATD_IN_HOUSE"));
					jsonArray.put(jsonObject11);
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("name", "无记录");
				jsonArray.put(jsonObject11);
			}
    		
    		jsonObject.put("signInfos", jsonArray);
    		jsonObject.put("signInDate", date);
    		jsonObject.put("signInDateShow", signInDateShow);
    		jsonObject.put("weekText", weekText);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String findLastDaySignOutInfos(String date) {
    	String responseText = "";
    	try {
    		if("null".equals(date) || null == date){
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(new Date(), DateUtil.DAY, -1));
    		}else{
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(DateUtil.getDateTime(date), DateUtil.DAY, -1));
    		}
    		String signOutDateShow = date.substring(5, 10);
    		String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
    		String expression = "and ATD_OUT_TIME is not null";
    		List<DataRow> records = getService().findCurrentDaySignOutInfos(expression,date);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
					String outTime = row.stringValue("ATD_OUT_TIME");
					if(!outTime.isEmpty()){
						outTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMISS_HORIZONTAL, row.getTimestamp("ATD_OUT_TIME"));
						outTime = outTime.substring(11, 16).replaceAll("/", "-");
						jsonObject11.put("outTime", outTime);
					}else{
						jsonObject11.put("outTime", outTime);
					}
					jsonObject11.put("outPlace", row.stringValue("ATD_OUT_PLACE"));
					jsonObject11.put("outHouse", row.stringValue("ATD_OUT_HOUSE"));
					jsonArray.put(jsonObject11);
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("name", "无记录");
				jsonArray.put(jsonObject11);
			}
    		
    		jsonObject.put("signOutInfos", jsonArray);
    		jsonObject.put("signOutDate", date);
    		jsonObject.put("signOutDateShow", signOutDateShow);
    		jsonObject.put("outWeekText", weekText);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String findCurrentDaySignOutInfos() {
    	String responseText = "";
    	try {
    		String currentDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,new Date());
    		String weekText = DateUtil.getWeekText(new Date());
    		String expression = "and ATD_OUT_TIME is not null";
    		List<DataRow> records = getService().findCurrentDaySignOutInfos(expression,currentDate);
    		String signOutDateShow = currentDate.substring(5, 10);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
					String outTime = row.stringValue("ATD_OUT_TIME");
					if(!outTime.isEmpty()){
						jsonObject11.put("outTime", outTime.substring(11, 16));
					}else{
						jsonObject11.put("outTime", outTime);
					}
					jsonObject11.put("outPlace", row.stringValue("ATD_OUT_PLACE"));
					jsonObject11.put("outHouse", row.stringValue("ATD_OUT_HOUSE"));
					jsonArray.put(jsonObject11);
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("name", "无记录");
				jsonArray.put(jsonObject11);
			}
    		
    		jsonObject.put("signOutInfos", jsonArray);
    		jsonObject.put("signOutDate", currentDate);
    		jsonObject.put("signOutDateShow", signOutDateShow);
    		jsonObject.put("outWeekText", weekText);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String findFollowDaySignOutInfos(String date) {
    	String responseText = "";
    	try {
    		if("null".equals(date) || null == date){
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(new Date(), DateUtil.DAY, 1));
    		}else{
    			date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,DateUtil.getDateAdd(DateUtil.getDateTime(date), DateUtil.DAY, 1));
    		}
    		String weekText = DateUtil.getWeekText(DateUtil.getDateTime(date));
    		List<DataRow> records = getService().findCurrentDaySigninInfos(date);
    		String signOutDateShow = date.substring(5, 10);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					jsonObject11.put("name", row.stringValue("USER_ID_NAME"));
					String outTime = row.stringValue("ATD_OUT_TIME");
					if(!outTime.isEmpty()){
						outTime = DateUtil.getDateByType(DateUtil.YYMMDDHHMISS_HORIZONTAL, row.getTimestamp("ATD_OUT_TIME"));
						outTime = outTime.substring(11, 16).replaceAll("/", "-");
						jsonObject11.put("outTime", outTime);
					}else{
						jsonObject11.put("outTime", outTime);
					}
					jsonObject11.put("outPlace", row.stringValue("ATD_OUT_PLACE"));
					jsonObject11.put("outHouse", row.stringValue("ATD_OUT_HOUSE"));
					jsonArray.put(jsonObject11);
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("name", "无记录");
				jsonArray.put(jsonObject11);
			}
    		
    		jsonObject.put("signOutInfos", jsonArray);
    		jsonObject.put("signOutDate", date);
    		jsonObject.put("signOutDateShow", signOutDateShow);
    		jsonObject.put("outWeekText", weekText);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String findSignLocationInfos(String userId) {
    	String responseText = "";
    	try {
    		User user = (User)this.getUser();
    		if("null".equals(userId)){
    			userId = user.getUserId();
    		}
    		List<DataRow> records = getService().findSignLocationInfos(userId);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					String time = row.stringValue("LOCAT_TIME");
					String weekText = DateUtil.getWeekText(DateUtil.getDateTime(time.substring(0,10)));
					if(!time.isEmpty()){
						jsonObject11.put("time", time.substring(0, 16));
					}else{
						jsonObject11.put("time", time);
					}
					jsonObject11.put("place", row.stringValue("LOCAT_PLACE"));
					jsonObject11.put("weekText", weekText);
					jsonArray.put(jsonObject11);
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("time", "无记录");
				jsonArray.put(jsonObject11);
			}
    		
    		jsonObject.put("locationInfos", jsonArray);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }

	@Override
	public String findUserInfos(String curUserCodes) {
    	String responseText = null;
    	try {
    		String userCodes = "";
    		List<String> userList = new ArrayList<String>();
    		MobileAttendanceHandler.addArrayToList(userList, curUserCodes.split(","));
    		for(int i=0;i<userList.size();i++){
    			String userCode = userList.get(i);
    			if(i == userList.size()-1){
    				userCodes = userCodes +userCode;
    			}else{
    				userCodes = userCodes +userCode +",";
    			}
    		}
    		List<DataRow> records = getService().findUserInfos(userCodes);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		JSONArray jsonArray2 = new JSONArray();
    		if(records.size() != 0){
				for(int i=0;i<records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject11 = new JSONObject();
					jsonObject11.put("userId", row.stringValue("USER_ID"));
					jsonObject11.put("userName", row.stringValue("USER_NAME"));
					jsonObject11.put("userCode", row.stringValue("USER_CODE"));
					jsonArray.put(jsonObject11);
					jsonArray2.put(row.stringValue("USER_CODE"));
				}
			}else{
				JSONObject jsonObject11 = new JSONObject();
				jsonObject11.put("userName", "无记录");
				jsonArray.put(jsonObject11);
			}
    		jsonObject.put("userInfos", jsonArray);
    		jsonObject.put("userCodes", jsonArray2);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
    }
	
	@Override
	public String findActiveUserId(String userCode) {
		String responseText = "";
		try {
			DataRow userInfo = getService().findActiveUserId(userCode);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("userId", userInfo.stringValue("USER_ID"));
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return responseText;
	}
	
	protected HrAttendanceManage getService() {
		return (HrAttendanceManage) this.lookupService(HrAttendanceManage.class);
	}
	
	@Override
	public String getSigninState(String todyTime) {
		String responseText = "";
		User user = (User)this.getUser();
		String	userId = user.getUserId();
		DataParam param=new DataParam("userId",userId);
		param.put("todyTime", todyTime);
		DataRow row=this.getService().getSigninState(param);
		JSONObject jsonOTemp = new JSONObject();
		try {
			if(row!=null){
				jsonOTemp.put("isSign", true);
				String atdOutTime= row.stringValue("ATD_OUT_TIME");
				if(StringUtil.isNotNullNotEmpty(atdOutTime)){
					jsonOTemp.put("isSignOut", true);
				}else{
					jsonOTemp.put("isSignOut", false);
				}
			}else{
				jsonOTemp.put("isSign", false);
				jsonOTemp.put("isSignOut", true);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		}
		responseText=jsonOTemp.toString();
		return responseText;
	}

	@Override
	public String findLocationInfos(String currentMonth,String paramUserId) {
		String responseText = "";
    	try {
    		DataParam param=new DataParam("currentMonth",currentMonth);
    		if(!"undefined".equals(paramUserId)){
    			param.put("paramUserId", paramUserId);
    		}else{
    			param.put("isgroup", true);
    		}
    		List<DataRow> records = getService().findLocationInfos(param);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
			for(DataRow row:records){
				JSONObject jsonOTemp = new JSONObject();
				String userName = row.stringValue("USER_NAME");
				String theUserId = row.stringValue("USER_ID");
				String grpName = row.stringValue("GRP_NAME");
				String locatTime = row.stringValue("LOCAT_TIME");
				String locatHouse = row.stringValue("LOCAT_HOUSE");
				String locatPlace = row.stringValue("LOCAT_PLACE");
				
				jsonOTemp.put("theUserId", theUserId);
				jsonOTemp.put("userName", userName);
				jsonOTemp.put("grpName", grpName);
				jsonOTemp.put("locatTime", locatTime);
				jsonOTemp.put("locatHouse", locatHouse);
				jsonOTemp.put("locatPlace", locatPlace);
				jsonArray.put(jsonOTemp);
			}    		
    		jsonObject.put("locationInfos", jsonArray);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
	}

	@Override
	public String findSigninInfos(String currentDay) {
		String responseText = "";
		String userId="";
    	try {
    		User user = (User)this.getUser();
    		if("null".equals(userId)){
    			userId = user.getUserId();
    		}
    		DataParam param=new DataParam("currentDay",currentDay);
    		List<DataRow> records = getService().findSigninInfos(param);
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
			for(DataRow row:records){
				JSONObject jsonOTemp = new JSONObject();
				String userName = row.stringValue("USER_NAME");
				String grpName = row.stringValue("GRP_NAME");
				String atdInTime = row.stringValue("ATD_IN_TIME");
				String atdOutTime = row.stringValue("ATD_OUT_TIME");
				String atdInHouse = row.stringValue("ATD_IN_HOUSE");
				String atdInPlace = atdInHouse+row.stringValue("ATD_IN_PLACE");
				String atdOutHouse = row.stringValue("ATD_OUT_HOUSE");
				String atdOutPlace = atdOutHouse+row.stringValue("ATD_OUT_PLACE");
				
				jsonOTemp.put("userName", userName);
				jsonOTemp.put("grpName", grpName);
				jsonOTemp.put("atdInTime", atdInTime);
				jsonOTemp.put("atdOutTime", atdOutTime);
				jsonOTemp.put("atdInPlace", atdInPlace);
				jsonOTemp.put("atdOutPlace", atdOutPlace);
				jsonArray.put(jsonOTemp);
			}    	 
    		jsonObject.put("signinInfos", jsonArray);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return responseText;
	}
}
