package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityFactTreeSelect;
import com.agileai.util.StringUtil;

public class SecurityFactTreeSelectHandler
        extends TreeSelectHandler {
private static final String ROOT_ROLE_ID = "00000000-0000-0000-00000000000000000";
	
    public SecurityFactTreeSelectHandler() {
        super();
        this.serviceId = buildServiceId(SecurityFactTreeSelect.class);
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
			String id = param.get("id",ROOT_ROLE_ID);
			
			SecurityFactTreeSelect securityFactTreeSelect = this.getService();
	    	List<DataRow> records = securityFactTreeSelect.findChildGroupRecords(id);
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
			records = securityFactTreeSelect.queryPickTreeRecords(new DataParam("orgId",id));
			if (records != null && !records.isEmpty()){
				List<String> existRgIds = getExistPosIds(param);
				for (int i=0 ;i < records.size();i++){
					DataRow row = records.get(i);
					String roleId = row.stringValue("ROLE_ID");
					String rgId = row.stringValue("RG_ID");
					if (existRgIds.contains(rgId)){
						continue;
					}
					String roleName = row.stringValue("ROLE_NAME");
					JSONObject jsonObject = new JSONObject();
					jsonObject.put("id",roleId);
					jsonObject.put("text",roleName);
					jsonObject.put("rgId", rgId);
					jsonArray.put(jsonObject);
				}					
			}
			responseText = jsonArray.toString();
		} catch (Exception e) {
			log.error(e);
		}
		return new AjaxRenderer(responseText);
	}
	
	private List<String> getExistPosIds(DataParam param){
		String resourceType = param.get("resourceType");
    	String resourceId = param.get("resourceId");
    	List<String> existRgIds = new ArrayList<String>();
    	if (StringUtil.isNotNullNotEmpty(resourceType) && StringUtil.isNotNullNotEmpty(resourceId)){
    		SecurityFactTreeSelect authorConfig = lookupService(SecurityFactTreeSelect.class);
        	List<DataRow> existsRecords = authorConfig.retrieveFactList(resourceType, resourceId);
        	for (int i=0;i < existsRecords.size();i++){
        		DataRow row = existsRecords.get(i);
        		String rgId = row.stringValue("RG_ID");
        		if (existRgIds.contains(rgId)){
        			continue;
        		}
        		existRgIds.add(rgId);
        	}
    	}
    	return existRgIds;
	}
	
    @PageAction
    public ViewRenderer addFactRequest(DataParam param){
    	return this.prepareDisplay(param);
    }    
    
    protected SecurityFactTreeSelect getService() {
        return (SecurityFactTreeSelect) this.lookupService(this.getServiceId());
    }
}
