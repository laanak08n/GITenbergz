package com.agileai.hr.service.salary;

import java.math.BigDecimal;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.ws.BaseRestService;
import com.agileai.hr.cxmodule.HrSalaryManage;


public class SalaryImpl extends BaseRestService implements Salary {

	@Override
	public String findSalaryList() {
		String responseText = "";
		try {
			User user = (User) this.getUser();
			String userId = user.getUserId();
			
			List<DataRow> rsList = getService().findSalaryList(new DataParam("userId", userId));
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<rsList.size();i++){
				DataRow dataRow = rsList.get(i);
				JSONObject jsonObject = new JSONObject();
				jsonObject.put("salId", dataRow.get("SAL_ID"));
				jsonObject.put("salYear", dataRow.get("SAL_YEAR"));
				jsonObject.put("salMonth", dataRow.get("SAL_MONTH"));
				jsonObject.put("salBasic", dataRow.get("SAL_BASIC"));
				jsonObject.put("salPerformance", dataRow.get("SAL_PERFORMANCE"));
				jsonObject.put("salSubsidy", dataRow.get("SAL_SUBSIDY"));
				jsonObject.put("salBonus", dataRow.get("SAL_BONUS"));
				jsonObject.put("salInsure", dataRow.get("SAL_INSURE"));
				jsonObject.put("salRemarks", dataRow.get("SAL_REMARKS"));
				jsonArray.put(jsonObject);
			}
			responseText = jsonArray.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return responseText;
	}

	@Override
	public String getSalaryRecord(String id) {
		String responseText = "";
		try {
			DataRow dataRow = getService().getRecord(new DataParam("SAL_ID",id));
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("salBasic", dataRow.get("SAL_BASIC"));
			jsonObject.put("salPerformance", dataRow.get("SAL_PERFORMANCE"));
			jsonObject.put("salSubsidy", dataRow.get("SAL_SUBSIDY"));
			jsonObject.put("salBonus", dataRow.get("SAL_BONUS"));
			jsonObject.put("salInsure", dataRow.get("SAL_INSURE"));
			jsonObject.put("salActual", dataRow.get("SAL_ACTUAL"));
			jsonObject.put("salTotal", dataRow.get("SAL_TOTAL"));
			jsonObject.put("salHousingFund", dataRow.get("SAL_HOUSING_FUND"));
			jsonObject.put("salShould", dataRow.get("SAL_SHOULD"));
			jsonObject.put("salRemarks ", dataRow.get("SAL_REMARKS"));
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return responseText;
	}

	@Override
	public String getSalaryDistributionMapInfo(String id) {
		String responseText = "";
		try {
			DataRow dataRow = getService().getRecord(new DataParam("SAL_ID",id));
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(0, dataRow.get("SAL_BASIC"));
			jsonArray.put(1, dataRow.get("SAL_PERFORMANCE"));
			jsonArray.put(2, dataRow.get("SAL_SUBSIDY"));
			int salBonus = ((BigDecimal) dataRow.get("SAL_BONUS")).intValue();
			if(salBonus < 0){
				salBonus = 0;
			}
			jsonArray.put(3, salBonus);
			responseText = jsonArray.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return responseText;
	}
	
	
	protected HrSalaryManage getService() {
		return (HrSalaryManage) this.lookupService(HrSalaryManage.class);
	}

	@Override
	public String getLastSalayInfo() {
		String responseText = "";
		try {
			User user = (User) getUser();
			DataRow dataRow = getService().getLastSalayInfo(new DataParam("userId", user.getUserId()));
			JSONArray jsonArray = new JSONArray();
			jsonArray.put(0, dataRow.get("SAL_BASIC"));
			jsonArray.put(1, dataRow.get("SAL_PERFORMANCE"));
			jsonArray.put(2, dataRow.get("SAL_SUBSIDY"));
			jsonArray.put(3, dataRow.get("SAL_BONUS"));
			jsonArray.put(4, dataRow.get("SAL_INSURE"));
			jsonArray.put(5, dataRow.get("SAL_TOTAL"));
			jsonArray.put(6, dataRow.get("SAL_TAX"));
			JSONArray jsonArray1 = new JSONArray();
			jsonArray1.put(jsonArray);
			JSONObject jsonObject = new JSONObject();
			jsonObject.put("data", jsonArray1);
			jsonObject.put("month", dataRow.get("SAL_YEAR_MONTH"));
			responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(),e);
		}
		return responseText;
	}

}
