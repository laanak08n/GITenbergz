package com.agileai.hr.service.bonuspenalty;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.ws.BaseRestService;
import com.agileai.hr.cxmodule.HrBonusPenaltyManage;

public class BonusPenaltyImpl extends BaseRestService implements BonusPenalty {

	@Override
	public String findAllPunRecord() {
		String responseText = "";
		try {
			List<DataRow> rsList = getService().findRecords(new DataParam());
			JSONArray jsonArray = new JSONArray();
			for(int i=0;i<rsList.size();i++){
				JSONObject jsonObject = new JSONObject();
				DataRow dataRow = rsList.get(i);
				jsonObject.put("bp_Id", dataRow.get("BP_ID"));
				jsonObject.put("user_Name", dataRow.get("USER_NAME"));
				jsonObject.put("bp_Date", dataRow.get("BP_DATE"));
				String bpType = FormSelectFactory.create("BP_TYPE").getText((String) dataRow.get("BP_TYPE"));
				jsonObject.put("bp_Type", bpType);
				jsonObject.put("bp_Monry", dataRow.get("BP_MONEY"));
				jsonArray.put(jsonObject);
			} 
			responseText = jsonArray.toString();
		}catch (JSONException e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return responseText;
	}

	@Override
	public String addPunInfo(String info) {
		String responseText = "fail";
		try {
			JSONObject jsonObject = new JSONObject(info);
			DataParam param = new DataParam();
			//BP_ID,USER_ID,BP_DATE,BP_TYPE,BP_MONEY,BP_DESC
			param.put("BP_ID", KeyGenerator.instance().genKey());
			param.put("USER_ID", jsonObject.getString("userName"));
			param.put("BP_DATE", jsonObject.getString("bpDate").substring(0, 10));
			param.put("BP_TYPE", jsonObject.getString("bpType"));
			param.put("BP_MONEY", jsonObject.getString("bpMonry"));
			param.put("BP_DESC", jsonObject.getString("bpDesc"));
			getService().createRecord(param);
			responseText = "success";
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return responseText;
	}
	
    protected HrBonusPenaltyManage getService() {
        return (HrBonusPenaltyManage) this.lookupService(HrBonusPenaltyManage.class);
    }
}
