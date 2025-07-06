package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hr.cxmodule.SecurityUserTreeSelect;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hr.module.system.service.SecurityUserQuery;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.StringUtil;

public class SecurityUserTreeSelectHandler
        extends TreeSelectHandler {
	private static final String ROOT_ORG_ID = "00000000-0000-0000-00000000000000000";
	
    public SecurityUserTreeSelectHandler() {
        super();
        this.serviceId = buildServiceId(SecurityUserTreeSelect.class);
        this.isMuilSelect = true;
        this.checkRelParentNode = false;
        this.mulSelectNodeTypes.add("Object");
    }

	public ViewRenderer prepareDisplay(DataParam param){
		this.setAttributes(param);
		return new LocalRenderer(getPage());
	}    
    
    protected TreeBuilder provideTreeBuilder(DataParam param) {
        return null;
    }
    
    
	@PageAction
	public ViewRenderer retrieveJson(DataParam param){
		String responseText = FAIL;
		try {
			JSONArray jsonArray = new JSONArray();
			String id = param.get("id",ROOT_ORG_ID);
			
	    	SecurityUserTreeSelect securityUserTreeSelect = this.getService();
	    	List<DataRow> records = securityUserTreeSelect.findChildGroupRecords(id);
	    	
	    	if (records != null && !records.isEmpty()){
				for (int i=0 ;i < records.size();i++){
					DataRow row = records.get(i);
					JSONObject jsonObject = new JSONObject();
					String groupId = row.stringValue("GRP_ID");
					String groupName = row.stringValue("GRP_NAME");
					jsonObject.put("id",groupId);
					jsonObject.put("text",groupName);
					jsonObject.put("state", "closed");					
					jsonArray.put(jsonObject);
				}	    		
	    	}

			records = securityUserTreeSelect.queryPickTreeRecords(new DataParam("groupId",id));
			if (records != null && !records.isEmpty()){
				List<String> existUserIds = getExistUserIds(param);
				for (int i=0 ;i < records.size();i++){
					DataRow row = records.get(i);
					String userId = row.stringValue("USER_ID");
					if (existUserIds.contains(userId)){
						continue;
					}
					String userName = row.stringValue("USER_NAME");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id",userId);
					jsonObject.put("text",userName);
					jsonArray.put(jsonObject);
				}					
			}
			responseText = jsonArray.toString();
		} catch (Exception e) {
			log.error(e);
		}
		return new AjaxRenderer(responseText);
	}
	
	private List<String> getExistUserIds(DataParam param){
		String resourceType = param.get("resourceType");
    	String resourceId = param.get("resourceId");
    	List<String> existUserIds = new ArrayList<String>();
    	if (StringUtil.isNotNullNotEmpty(resourceType) && StringUtil.isNotNullNotEmpty(resourceId)){
        	SecurityAuthorizationConfig securityAuthorizationConfig = lookupService(SecurityAuthorizationConfig.class);
        	List<DataRow> existsRecords = securityAuthorizationConfig.retrieveUserList(resourceType, resourceId);
        	for (int i=0;i < existsRecords.size();i++){
        		DataRow row = existsRecords.get(i);
        		String userId = row.stringValue("USER_ID");
        		if (existUserIds.contains(userId)){
        			continue;
        		}
        		existUserIds.add(userId);
        	}
    	}else{
    		String roleId = param.get("roleId");
    		if (StringUtil.isNotNullNotEmpty(roleId)){
    			SecurityUserQuery securityUserQuery = this.lookupService(SecurityUserQuery.class);
    			List<DataRow> existsRecords = securityUserQuery.findRecords(new DataParam("roleId",roleId));
            	for (int i=0;i < existsRecords.size();i++){
            		DataRow row = existsRecords.get(i);
            		String userId = row.stringValue("USER_ID");
            		if (existUserIds.contains(userId)){
            			continue;
            		}
            		existUserIds.add(userId);
            	}
    		}
    	}
    	return existUserIds;
	}
	
    @PageAction
    public ViewRenderer addUserRequest(DataParam param){
    	return this.prepareDisplay(param);
    }    
    
    protected SecurityUserTreeSelect getService() {
        return (SecurityUserTreeSelect) this.lookupService(this.getServiceId());
    }
}
