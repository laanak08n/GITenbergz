package com.agileai.hr.service.common;

import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.ws.BaseRestService;
import com.agileai.hr.cxmodule.UserListSelect;


public class FormSelectUtilImpl extends BaseRestService implements FormSelectUtil {
	
	@Override
	public String findCodeList(String codeType) {
		String responseText = "";
		responseText = FormSelectFactory.create(codeType).toString();
		return responseText;
	}
	
	@Override
	public String findUserList() {
		String responseText = "";
			try {
				UserListSelect service = this.lookupService(UserListSelect.class);
				List<DataRow> rsList = service.queryPickFillRecords(new DataParam());
				JSONArray jsonArray = new JSONArray();
				for(int i=0;i<rsList.size();i++){
					JSONObject jsonObject = new JSONObject();
					DataRow dataRow = rsList.get(i);
					jsonObject.put("value", dataRow.get("USER_ID"));
					jsonObject.put("key", dataRow.get("USER_NAME"));
					
					jsonArray.put(jsonObject);
				}
				responseText = jsonArray.toString();
			} catch (JSONException e) {
				log.error(e.getLocalizedMessage(), e);
			}
		return responseText;
	}
}
