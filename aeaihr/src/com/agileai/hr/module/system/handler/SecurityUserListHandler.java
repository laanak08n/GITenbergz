package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataMap;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityFactTreeSelect;
import com.agileai.hr.module.system.service.SecurityUserManage;
import com.agileai.util.StringUtil;

public class SecurityUserListHandler
	extends TreeAndContentManageListHandler {
	public final String tabId="SecurityUser";
    public SecurityUserListHandler(){
    	super();
    	this.serviceId = buildServiceId(SecurityUserManage.class);
    	this.rootColumnId = "00000000-0000-0000-00000000000000000";
    	this.defaultTabId = "_base_";
    	this.columnIdField = "GRP_ID";
    	this.columnNameField = "GRP_NAME";
    	this.columnParentIdField = "GRP_PID";
    	this.columnSortField = "GRP_SORT";
	}

    protected void processPageAttributes(DataParam param) {	
    	this.setAttribute("rootColumnId", this.rootColumnId);
    }
		
    protected void initParameters(DataParam param) {

	}
            
    public ViewRenderer prepareDisplay(DataParam param){
    	initParameters(param);
    	this.setAttributes(param);
    	String columnId = param.get("columnId",this.rootColumnId);
    	this.setAttribute("columnId", columnId);
    	this.setAttribute("isRootColumnId",String.valueOf(this.rootColumnId.equals(columnId)));
    	String tabId = param.get(TreeAndContentManage.TAB_ID,this.defaultTabId);
    	param.put("columnId",columnId);
    	this.setAttribute(TreeAndContentManage.TAB_ID, tabId);
    	this.setAttribute(TreeAndContentManage.TAB_INDEX, getTabIndex(tabId));
    	processPageAttributes(param);
    	return new LocalRenderer(getPage());
    }
    
    @PageAction
	public ViewRenderer retrieveTreeJson(DataParam param) {
		String responseText = FAIL;
		try {
			JSONArray jsonArray = new JSONArray();
			String id = param.get("id");
			List<DataRow> records = new ArrayList<DataRow>();
			SecurityFactTreeSelect securityFactTreeSelect = this.lookupService(SecurityFactTreeSelect.class);
			if (StringUtil.isNullOrEmpty(id)) {
				records = securityFactTreeSelect.findRootGroupRecords(rootColumnId);
			} else {
				records = securityFactTreeSelect.findChildGroupRecords(id);
			}
			if (records != null && !records.isEmpty()) {
				for (int i = 0; i < records.size(); i++) {
					DataRow row = records.get(i);
					JSONObject jsonObject = new JSONObject();
					String groupId = row.stringValue("GRP_ID");
					String groupName = row.stringValue("GRP_NAME");
					jsonObject.put("id", groupId);
					jsonObject.put("text", groupName);
					jsonObject.put("state", "closed");
					jsonArray.put(jsonObject);
				}
			}
			if (StringUtil.isNotNullNotEmpty(id)) {
				String treeState = param.get("treeState");
				if (StringUtil.isNotNullNotEmpty(treeState) && "true".equals(treeState)) {
					records = securityFactTreeSelect.queryPickTreeRecords(new DataParam("orgId", id));
					if (records != null && !records.isEmpty()) {
						List<String> existUserIds = getExistPosIds(param);
						for (int i = 0; i < records.size(); i++) {
							DataRow row = records.get(i);
							String roleId = row.stringValue("ROLE_ID");
							String rgId = row.stringValue("RG_ID");
							if (existUserIds.contains(roleId)) {
								continue;
							}
							String roleName = row.stringValue("ROLE_NAME");
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("id", roleId);
							jsonObject.put("text", roleName);
							jsonObject.put("rgId", rgId);
							jsonArray.put(jsonObject);
						}
					}
				}
			}
			responseText = jsonArray.toString();
		} catch (Exception e) {
			log.error(e);
		}
		return new AjaxRenderer(responseText);
	}
	
	private List<String> getExistPosIds(DataParam param) {
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		List<String> existRoleIds = new ArrayList<String>();
		if (StringUtil.isNotNullNotEmpty(resourceType) && StringUtil.isNotNullNotEmpty(resourceId)) {
			SecurityFactTreeSelect authorConfig = lookupService(SecurityFactTreeSelect.class);
			List<DataRow> existsRecords = authorConfig.retrieveFactList(resourceType, resourceId);
			for (int i = 0; i < existsRecords.size(); i++) {
				DataRow row = existsRecords.get(i);
				String roleId = row.stringValue("ROLE_ID");
				if (existRoleIds.contains(roleId)) {
					continue;
				}
				existRoleIds.add(roleId);
			}
		}
		return existRoleIds;
	}
    
    @PageAction
	public ViewRenderer retrieveTableJson(DataParam param) {
    	String responseText = "";
    	try {
			TreeModel filterTreeModel = null;
			String columnId = param.get("columnId", this.rootColumnId);
			TreeBuilder treeBuilder = provideTreeBuilder(param);
			TreeModel treeModel = treeBuilder.buildTreeModel();
			TreeModel childModel = treeModel.getFullTreeMap().get(columnId);
			if (childModel != null) {
				filterTreeModel = childModel;
			} else {
				filterTreeModel = treeModel;
			}
			param.put("columnId",columnId);
			DataMap userSex = FormSelectFactory.create("USER_SEX").getContent();
			DataMap sysValidType = FormSelectFactory.create("SYS_VALID_TYPE").getContent();
			List<DataRow> rsList = getService().findContentRecords(filterTreeModel, this.tabId, param);

			int page = param.getInt("page");
    		int rows = param.getInt("rows");
    		int index = page*rows-rows;
    		int records = rsList.size();
    		int total = (int)Math.ceil((double)records/rows);
    		int indexRecords = page*rows;
    		if(page == total){
    			indexRecords = records;
    		}
    		
    		JSONObject jsonObject = new JSONObject();
    		JSONArray jsonArray = new JSONArray();
    		if(records > 0){
    			for (int i = index; i < indexRecords; i++) {
    				JSONObject jsonObj = new JSONObject();
    				DataRow row = rsList.get(i);
    				jsonObj.put("ID", i+1);
    				jsonObj.put("USER_ID", row.get("USER_ID"));
    				jsonObj.put("USER_CODE", row.get("USER_CODE"));
    				jsonObj.put("USER_NAME", row.get("USER_NAME"));
    				jsonObj.put("USER_SEX", userSex.get(row.get("USER_SEX")));
    				jsonObj.put("USER_STATE", sysValidType.get(row.get("USER_STATE")));
    				jsonObj.put("GRP_ID", row.get("GRP_ID"));
    				jsonObj.put("GRP_NAME", row.get("GRP_NAME"));
    				jsonArray.put(jsonObj);
    			}
    		}
			jsonObject.put("rows", jsonArray);
        	jsonObject.put("records", records);
        	jsonObject.put("page", page);
        	jsonObject.put("total", total);
        	responseText = jsonObject.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }

    @PageAction
    public ViewRenderer doDeleteAction(DataParam param){
		StringBuilder responseText = new StringBuilder("fail");
		SecurityUserManage service = this.getService();
		String tabId = param.get(TreeAndContentManage.TAB_ID);
		String columnId = param.get("curColumnId");
		Map<String,String> tabIdAndColFieldMapping = service.getTabIdAndColFieldMapping();
		String colField = tabIdAndColFieldMapping.get(tabId);
		param.put(colField,columnId);
		
		param.put("GRP_ID", columnId);
		param.put("ROOTCOLUMNID", this.rootColumnId);
		List<DataRow> userAuth = service.findUserAuthRecords(param);
		List<DataRow> userRole = service.findUserRoleGroupRecords(param);
		if(userAuth.size()>0) {
			responseText.append(",auth");
		}
		if(userRole.size()>0){
			responseText.append(",role");
		}
		if("fail".equals(responseText.toString())){
			service.deleteSecurityUserRelation(param);
			service.deleteSecurityUserRecord(param);
		}
		return new AjaxRenderer(responseText.toString());
	}
    
	public String uniqueRelation(DataParam param, String responseText){
		SecurityUserManage service = this.getService();
		List<DataRow> userGroupRecords = service.queryUserRelationRecords(param);
		if(userGroupRecords.size() == 1){
			responseText = "unique";
		}
		return responseText;
	}
	
	@Override
	public ViewRenderer doRemoveContentAction(DataParam param){
		String responseText = "false";
		SecurityUserManage service = this.getService();
		String columnId = param.get("curColumnId");
		param.put("grpId",columnId);
		
		List<DataRow> userRecords = service.queryUserGroupRelationRecords(param);
		if(userRecords.size() > 0){
			responseText = "true";
		}else{
			responseText = this.uniqueRelation(param, responseText);
			if("false".equals(responseText)){
				service.deleteSecurityUserRelation(param);
			}	
		}
		return new AjaxRenderer(responseText);
	}
	
	protected TreeBuilder provideTreeBuilder(DataParam param) {
		 SecurityUserManage service = this.getService();
		 List<DataRow> menuRecords = new ArrayList<DataRow>();
		 String treeState=param.get("treeState");
		 if(treeState==null||!treeState.equals("true")){
			 menuRecords = service.findTreeRecords(new DataParam());
		 }
		 else{
			 menuRecords = service.findPOSTreeRecords(new DataParam());
		 }
		 TreeBuilder treeBuilder = new TreeBuilder(menuRecords,
                                                          this.columnIdField,
                                                          this.columnNameField,
                                                          this.columnParentIdField);
		 this.setAttribute("menuRecords", menuRecords);
		 return treeBuilder;
	 }
	 
	 @Override
	 public ViewRenderer doCopyContentAction(DataParam param){
    	String rspText = FAIL;
    	try {
    		String GU_ID=KeyGenerator.instance().genKey();
    		String tabId = this.tabId;
    		String GRP_ID = param.get("targetParentId");
    		TreeAndContentManage service = this.getService();
    		DataParam newParam = new DataParam();
    		param.put("GU_ID",GU_ID);
    		param.put("GRP_ID",GRP_ID);
    		newParam.append(param);
    		Map<String,String> tabIdAndColFieldMapping = service.getTabIdAndColFieldMapping();
    		String colField = tabIdAndColFieldMapping.get(tabId);
    		newParam.put(colField,GRP_ID);
    		SecurityUserManage UserManage = (SecurityUserManage)this.lookupService(SecurityUserManage.class);
    		DataRow copyRow=new DataRow();
    		copyRow=UserManage.getCopyContentRecord(param);
			if(copyRow==null){
    			service.insertTreeContentRelation(tabId, newParam);
    			rspText = SUCCESS;	
    		}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(rspText);
	}
	    
    @Override
    public ViewRenderer doMoveContentAction(DataParam param){
		String rspText = FAIL;
		try {
			String tabId = this.tabId;
			String columnId = param.get("curColumnId");
			String GRP_ID = param.get("targetParentId");
			TreeAndContentManage service = this.getService();
			DataParam newParam = new DataParam();
			newParam.append(param);
			Map<String,String> tabIdAndColFieldMapping = service.getTabIdAndColFieldMapping();
			String colField = tabIdAndColFieldMapping.get(tabId);
			newParam.put(colField,columnId);
			param.put("GRP_ID",GRP_ID);
			newParam.put("NEW_"+colField,GRP_ID);
			SecurityUserManage UserManage = (SecurityUserManage)this.lookupService(SecurityUserManage.class);
			DataRow moveRow=new DataRow();
			moveRow=UserManage.getMoveContentRecord(param);
			if(moveRow==null){
				service.updateTreeContentRelation(tabId, newParam);
				rspText = SUCCESS;
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return new AjaxRenderer(rspText);
	}	
	    
	protected List<String> getTabList() {
    	List<String> result = new ArrayList<String>();
    	result.add(SecurityUserManage.BASE_TAB_ID);
    	result.add("Employee");
    	return result;
		}

	protected SecurityUserManage getService() {
		return (SecurityUserManage) this.lookupService(this.getServiceId());
	}
}
