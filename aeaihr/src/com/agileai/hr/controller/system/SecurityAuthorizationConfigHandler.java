package com.agileai.hr.controller.system;

import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hr.bizmoduler.system.FunctionTreeManage;
import com.agileai.hr.bizmoduler.system.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.ListUtil;
import com.agileai.util.StringUtil;

public class SecurityAuthorizationConfigHandler extends BaseHandler{

	public SecurityAuthorizationConfigHandler(){
		super();
	}
	
	private FunctionTreeManage getFunctionTreeManage(){
		return this.lookupService(FunctionTreeManage.class);
	}
	
	public ViewRenderer prepareDisplay(DataParam param){
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		
		FormSelect roleSelect = getRoleSelect(resourceType, resourceId);
		this.setAttribute("roleList", roleSelect);
		
		FormSelect groupSelect = getGroupSelect(resourceType, resourceId);
		this.setAttribute("groupList", groupSelect);
		
		FormSelect userSelect = getUserSelect(resourceType, resourceId);
		this.setAttribute("userList", userSelect);
		
		this.setAttribute("resourceType", resourceType);
		this.setAttribute("resourceId", resourceId);
		return new LocalRenderer(getPage());
	}
	
	private FormSelect getRoleSelect(String resourceType,String resourceId){
		List<DataRow> roleList = this.getService().retrieveRoleList(resourceType, resourceId);
		FormSelect roleSelect = new FormSelect();
		roleSelect.setKeyColumnName("ROLE_ID");
		roleSelect.setValueColumnName("ROLE_NAME");
		roleSelect.addHasBlankValue(false);
		roleSelect.putValues(roleList);
		return roleSelect;
	}
	
	private FormSelect getUserSelect(String resourceType,String resourceId){
		List<DataRow> userList = this.getService().retrieveUserList(resourceType, resourceId);
		FormSelect userSelect = new FormSelect();
		userSelect.setKeyColumnName("USER_ID");
		userSelect.setValueColumnName("USER_NAME");
		userSelect.addHasBlankValue(false);
		userSelect.putValues(userList);
		return userSelect;
	}
	
	private FormSelect getGroupSelect(String resourceType,String resourceId){
		List<DataRow> groupList = this.getService().retrieveGroupList(resourceType, resourceId);
		FormSelect groupSelect = new FormSelect();
		groupSelect.setKeyColumnName("GRP_ID");
		groupSelect.setValueColumnName("GRP_NAME");
		groupSelect.addHasBlankValue(false);
		groupSelect.putValues(groupList);
		return groupSelect;
	}
	
	@PageAction
	public ViewRenderer addUserAuthRelation(DataParam param){
		String responseText = "";
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		String userIds = param.get("userIds");
		List<String> userIdList = new ArrayList<String>();
		ListUtil.addArrayToList(userIdList, userIds.split(","));
		if (Resource.Type.Operation.equals(resourceType)){
			String operationId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddOperationAuth(operationId, resourceTypes, resourceIds);
			getService().addUserAuthRelation(resourceTypes, resourceIds,userIdList);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().addUserAuthRelation(resourceTypes, resourceIds,userIdList);
		}
		else if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddMenuAuth(menuItemId, resourceTypes, resourceIds);
			getService().addUserAuthRelation(resourceTypes, resourceIds, userIdList);
		}
		FormSelect userSelect = getUserSelect(resourceType, resourceId);
		responseText = userSelect.getScriptSyntax("userList");
		return new AjaxRenderer(responseText);
	}

	private void packResoucesOnAddOperationAuth(String operationId,List<String> resourceTypes,List<String> resourceIds){
		Operation operation = getFunctionTreeManage().getOperation(operationId);
		resourceTypes.add(Resource.Type.Operation);
		resourceIds.add(operationId);
		
		String handlerId = operation.getHandlerId();
		resourceTypes.add(Resource.Type.Handler);
		resourceIds.add(handlerId);
		
		FuncHandler funcHandler = getFunctionTreeManage().getFuncHandler(handlerId);
		String functionId = funcHandler.getFunctionId();
		
		FuncMenu funcMenu = getFunctionTreeManage().getFunction(functionId);
		resourceTypes.add(Resource.Type.Menu);
		resourceIds.add(functionId);
		
		this.buildMenuParent(funcMenu, resourceTypes, resourceIds);
	}

	private void packResoucesOnAddHandlerAuth(String handlerId,List<String> resourceTypes,List<String> resourceIds){
		resourceTypes.add(Resource.Type.Handler);
		resourceIds.add(handlerId);
		
		FuncHandler funcHandler = getFunctionTreeManage().getFuncHandler(handlerId);
		String functionId = funcHandler.getFunctionId();
		
		FuncMenu funcMenu = getFunctionTreeManage().getFunction(functionId);
		resourceTypes.add(Resource.Type.Menu);
		resourceIds.add(functionId);
		
		this.buildMenuParent(funcMenu, resourceTypes, resourceIds);
	}
	
	private void packResoucesOnAddMenuAuth(String menuItemId,List<String> resourceTypes,List<String> resourceIds){
		FuncMenu funcMenu = getFunctionTreeManage().getFunction(menuItemId);
		resourceTypes.add(Resource.Type.Menu);
		resourceIds.add(menuItemId);
		this.buildMenuParent(funcMenu, resourceTypes, resourceIds);
	}
	
	private void buildMenuParent(FuncMenu funcMenu,List<String> resourceTypes,List<String> resourceIds){
		if (!StringUtil.isNullOrEmpty(funcMenu.getFuncPid())){
			String parrentId = funcMenu.getFuncPid();
			FuncMenu parentFuncMenu = getFunctionTreeManage().getFunction(parrentId);
			resourceTypes.add(Resource.Type.Menu);
			resourceIds.add(parentFuncMenu.getFuncId());
			this.buildMenuParent(parentFuncMenu, resourceTypes, resourceIds);
		}
	}
	
	@PageAction
	public ViewRenderer addRoleAuthRelation(DataParam param){
		String responseText = "";
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		String roleIds = param.get("roleIds");
		List<String> roleIdList = new ArrayList<String>();
		ListUtil.addArrayToList(roleIdList, roleIds.split(","));
		
		if (Resource.Type.Operation.equals(resourceType)){
			String operationId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddOperationAuth(operationId, resourceTypes, resourceIds);
			getService().addRoleAuthRelation(resourceTypes, resourceIds,roleIdList);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().addRoleAuthRelation(resourceTypes, resourceIds,roleIdList);
		}
		else if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddMenuAuth(menuItemId, resourceTypes, resourceIds);
			getService().addRoleAuthRelation(resourceTypes, resourceIds, roleIdList);
		}
		FormSelect roleSelect = getRoleSelect(resourceType, resourceId);
		responseText = roleSelect.getScriptSyntax("roleList");
		return new AjaxRenderer(responseText);
	}
	
	@PageAction
	public ViewRenderer addGroupAuthRelation(DataParam param){
		String responseText = "";
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		String groupIds = param.get("groupIds");
		List<String> groupIdList = new ArrayList<String>();
		ListUtil.addArrayToList(groupIdList, groupIds.split(","));
		
		if (Resource.Type.Operation.equals(resourceType)){
			String operationId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddOperationAuth(operationId, resourceTypes, resourceIds);
			getService().addGroupAuthRelation(resourceTypes, resourceIds,groupIdList);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().addGroupAuthRelation(resourceTypes, resourceIds,groupIdList);
		}
		else if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnAddMenuAuth(menuItemId, resourceTypes, resourceIds);
			getService().addGroupAuthRelation(resourceTypes, resourceIds, groupIdList);
		}
		
		FormSelect groupSelect = getGroupSelect(resourceType, resourceId);
		responseText = groupSelect.getScriptSyntax("groupList");
		return new AjaxRenderer(responseText);
	}
	
	
	@PageAction
	public ViewRenderer delUserAuthRelation(DataParam param){
		String userId = param.get("userList");
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		
		if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
			
			getService().delUserAuthRelation(resourceTypes, resourceIds, userId);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().delUserAuthRelation(resourceTypes, resourceIds, userId);
		}
		else if (Resource.Type.Operation.equals(resourceType)){
			getService().delUserAuthRelation(resourceType, resourceId, userId);
		}
		return this.prepareDisplay(param);
	}
	
	private void packResoucesOnDelHandlerAuth(String handlerId,List<String> resourceTypes,List<String> resourceIds){
		resourceTypes.add(Resource.Type.Handler);
		resourceIds.add(handlerId);
		
		FuncHandler funcHandler = getFunctionTreeManage().getFuncHandler(handlerId);
		List<Operation> operations = funcHandler.getOperations();
		for (int j=0; j < operations.size();j++){
			Operation operation = operations.get(j);
			String operationId = operation.getOperId();
			resourceTypes.add(Resource.Type.Operation);
			resourceIds.add(operationId);
		}
	}
	
	
	private void packResoucesOnDelMenuAuth(String menuItemId,List<String> resourceTypes,List<String> resourceIds){
		resourceTypes.add(Resource.Type.Menu);
		resourceIds.add(menuItemId);
		
		FuncMenu menuItem = getFunctionTreeManage().getFunction(menuItemId);
		this.buildMenuChildren(menuItem, resourceTypes, resourceIds);
	}
	
	private void buildMenuChildren(FuncMenu menuItem,List<String> resourceTypes,List<String> resourceIds){
		if (menuItem.isFolder()){
			if (menuItem.getChildren().size() > 0){
				List<FuncMenu> menuItems = menuItem.getChildren();
				for (int i=0;i < menuItems.size();i++){
					FuncMenu childMenuItem = menuItems.get(i);
					String childMenuItemId = childMenuItem.getFuncId();
					resourceTypes.add(Resource.Type.Menu);
					resourceIds.add(childMenuItemId);
					
					this.buildMenuChildren(childMenuItem, resourceTypes, resourceIds);
				}
			}	
		}else{
			List<FuncHandler> funcHandlers = menuItem.getHandlers();
			for (int i=0;i < funcHandlers.size();i++){
				FuncHandler funcHandler = funcHandlers.get(i);
				String handlerId = funcHandler.getHandlerId();
				resourceTypes.add(Resource.Type.Handler);
				resourceIds.add(handlerId);
				
				List<Operation> operations = funcHandler.getOperations();
				for (int j=0; j < operations.size();j++){
					Operation operation = operations.get(j);
					String operationId = operation.getOperId();
					resourceTypes.add(Resource.Type.Operation);
					resourceIds.add(operationId);
				}
			}
		}
	}
	
	@PageAction
	public ViewRenderer delRoleAuthRelation(DataParam param){
		String roleId = param.get("roleList");
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		
		if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
			getService().delRoleAuthRelation(resourceTypes, resourceIds, roleId);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().delRoleAuthRelation(resourceTypes, resourceIds, roleId);
		}
		else if (Resource.Type.Operation.equals(resourceType)){
			getService().delRoleAuthRelation(resourceType, resourceId, roleId);
		}
		return this.prepareDisplay(param);
	}
	
	@PageAction
	public ViewRenderer delGroupAuthRelation(DataParam param){
		String groupId = param.get("groupList");
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		
		if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
			getService().delGroupAuthRelation(resourceTypes, resourceIds, groupId);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().delGroupAuthRelation(resourceTypes, resourceIds, groupId);
		}
		else if (Resource.Type.Operation.equals(resourceType)){
			getService().delGroupAuthRelation(resourceType, resourceId, groupId);
		}
		return this.prepareDisplay(param);
	}	
	
	
	@PageAction
	public ViewRenderer delUserAuthRelations(DataParam param){
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		
		if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
			getService().delUserAuthRelations(resourceTypes, resourceIds);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().delUserAuthRelations(resourceTypes, resourceIds);
		}
		else if (Resource.Type.Operation.equals(resourceType)){
			getService().delUserAuthRelations(resourceType, resourceId);
		}
		return this.prepareDisplay(param);
	}
	
	@PageAction
	public ViewRenderer delRoleAuthRelations(DataParam param){
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		
		if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
			
			getService().delRoleAuthRelations(resourceTypes, resourceIds);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().delRoleAuthRelations(resourceTypes, resourceIds);
		}
		else if (Resource.Type.Operation.equals(resourceType)){
			getService().delRoleAuthRelations(resourceType, resourceId);
		}
		return this.prepareDisplay(param);
	}
	
	@PageAction
	public ViewRenderer delGroupAuthRelations(DataParam param){
		String resourceType = param.get("resourceType");
		String resourceId = param.get("resourceId");
		
		if (Resource.Type.Menu.equals(resourceType)){
			String menuItemId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelMenuAuth(menuItemId, resourceTypes, resourceIds);
			
			getService().delGroupAuthRelations(resourceTypes, resourceIds);
		}
		else if (Resource.Type.Handler.equals(resourceType)){
			String handlerId = resourceId;
			List<String> resourceTypes = new ArrayList<String>();
			List<String> resourceIds = new ArrayList<String>();
			this.packResoucesOnDelHandlerAuth(handlerId, resourceTypes, resourceIds);
			getService().delGroupAuthRelations(resourceTypes, resourceIds);
		}
		else if (Resource.Type.Operation.equals(resourceType)){
			getService().delGroupAuthRelations(resourceType, resourceId);
		}
		return this.prepareDisplay(param);
	}	
	
    protected SecurityAuthorizationConfig getService() {
        return (SecurityAuthorizationConfig) this.lookupService(SecurityAuthorizationConfig.class);
    }
}
