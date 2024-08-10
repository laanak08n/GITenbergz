package com.agileai.hr.bizmoduler.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.common.BeanFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.domain.core.Group;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.core.Role;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;

public class HotwebAuthHelper {
	private static final String CurrentFuncList = "funcsList";
	private static final String CurrentFuncIdKey = "currentFuncId";

	private User user = null;
	
	public HotwebAuthHelper(User user){
		this.user = user;
	}
	
	public String getCurrentFuncId() {
		String currentFuncId = (String)user.getTempProperties().get(CurrentFuncIdKey);		
		return currentFuncId;
	}

	public void setCurrentFuncId(String currentFuncId) {
		user.getTempProperties().put(CurrentFuncIdKey, currentFuncId);
	}
	
	public FuncMenu getFunction(String functionId){
		BeanFactory beanFactory = BeanFactory.instance();
		FunctionTreeManage functionTreeManage = (FunctionTreeManage)beanFactory.getBean("functionTreeManageService");
		return functionTreeManage.getFunction(functionId);
	}
	public String getTreeSyntax(){
    	String result = null;
    	StringBuffer treeSyntax = new StringBuffer();
    	try {
    		List<FuncMenu> funcRecords = this.getAuthedFuncList();
    		treeSyntax.append("<script type='text/javascript'>");
    		treeSyntax.append("d = new dTree('d');");
    		TreeBuilder treeBuilder = new TreeBuilder(funcRecords,"funcId","funcName","funcPid");
    		treeBuilder.setTypeKey("funcType");
    		treeBuilder.setPojoList(true);
            TreeModel treeModel = treeBuilder.buildTreeModel();
            String rootFuncId = treeModel.getId();
            String rootFuncName = treeModel.getName();
            treeSyntax.append("d.add('"+rootFuncId+"','-1','"+rootFuncName+"',null);");
            treeSyntax.append("\r\n");
            buildTreeSyntax(treeSyntax,treeModel);
            treeSyntax.append("\r\n");
            treeSyntax.append("document.write(d);");
            treeSyntax.append("\r\n");
    		treeSyntax.append("</script>");
    		result = treeSyntax.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
    }
	
	private void buildTreeSyntax(StringBuffer treeSyntax,TreeModel treeModel){
		List<TreeModel> children = treeModel.getChildren();
		String parentId = treeModel.getId();
        for (int i=0;i < children.size();i++){
        	TreeModel child = children.get(i);
            String curFuncId = child.getId();
            String curFuncName = child.getName();
            String curNodeType = child.getType();
            if ("funcmenu".equals(curNodeType)){
            	treeSyntax.append("d.add('"+curFuncId+"','"+parentId+"','"+curFuncName+"',\"javascript:void(0)\",null,null,d.icon.folder,d.icon.folder.folderOpen,null);");
            	treeSyntax.append("\r\n");
            }else{
            	treeSyntax.append("d.add('"+curFuncId+"','"+parentId+"','"+curFuncName+"',\"javascript:showFunction(\\\'"+curFuncId+"\\\')\");");
            	treeSyntax.append("\r\n");
            }
            this.buildTreeSyntax(treeSyntax,child);
        }
    }
	
	@SuppressWarnings("unchecked")
	private List<FuncMenu> getAuthedFuncList(){
		List<FuncMenu> result = null;
		BeanFactory beanFactory = BeanFactory.instance();
		FunctionTreeManage functionTreeManage = (FunctionTreeManage)beanFactory.getBean("functionTreeManageService");
		if (user.isAdmin()){
			result = functionTreeManage.getFuncMenuList();
		}else{
			if (!user.getTempProperties().containsKey(CurrentFuncList)){
				List<FuncMenu> funcs = new ArrayList<FuncMenu>();
				List<FuncMenu> funcList = functionTreeManage.getFuncMenuList();
				for (int i=0;i < funcList.size();i++){
					FuncMenu function = funcList.get(i);
					String functionId = function.getFuncId();
					if (user.containResouce(Resource.Type.Menu, functionId)){
						funcs.add(function);						
					}
				}
				user.getTempProperties().put(CurrentFuncList, funcs);
			}
			result = (List<FuncMenu>)user.getTempProperties().get(CurrentFuncList);			
		}
		return result;
	}
	
	public void initAuthedUser(DataRow userRow,User user){
		BeanFactory beanFactory = BeanFactory.instance();
		SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)beanFactory.getBean("securityAuthorizationConfigService");
		String userId = userRow.stringValue("USER_ID");
		String userCode = userRow.stringValue("USER_CODE");
		String userName = userRow.stringValue("USER_NAME");
		user.setUserId(userId);
		user.setUserCode(userCode);
		user.setUserName(userName);
		
		List<DataRow> roleRecords = authorizationConfig.retrieveRoleRecords(userId);
		for (int i=0;i < roleRecords.size();i++){
			DataRow row = roleRecords.get(i);
			String roleId = row.stringValue("ROLE_ID");
			String roleCode = row.stringValue("ROLE_CODE");
			String roleName = row.stringValue("ROLE_NAME");
			
			Role role = new Role();
			role.setRoleId(roleId);
			role.setRoleCode(roleCode);
			role.setRoleName(roleName);
			user.getRoleList().add(role);
		}
		
		List<DataRow> groupRecords = authorizationConfig.retrieveGroupRecords(userId);
		for (int i=0;i < groupRecords.size();i++){
			DataRow row = groupRecords.get(i);
			String groupId = row.stringValue("GRP_ID");
			String groupCode = row.stringValue("GRP_CODE");
			String groupName = row.stringValue("GRP_NAME");
			Group group = new Group();
			group.setGroupId(groupId);
			group.setGroupCode(groupCode);
			group.setGroupName(groupName);
			user.getGroupList().add(group);
		}
		
		FunctionTreeManage functionTreeManage = (FunctionTreeManage)beanFactory.getBean("functionTreeManageService");
		List<String> menuItemIdList = authorizationConfig.retrieveMenuIdList(userId);
		List<String> handlerIdList = authorizationConfig.retrieveHandlerIdList(userId);
		List<String> operationIdList = authorizationConfig.retrieveOperationIdList(userId);
		
		if (menuItemIdList != null){
			if (menuItemIdList.contains(FuncMenu.RootId)){
				String functionId = FuncMenu.RootId;
				FuncMenu funcMenu = functionTreeManage.getFunction(functionId);
				HashMap<String,Resource> menuResources = getResourceRef(user, Resource.Type.Menu);
				
				Resource resource = new Resource();
				resource.setResouceType(Resource.Type.Menu);
				resource.setResouceId(funcMenu.getFuncId());
				resource.setResouceName(funcMenu.getFuncName());
				menuResources.put(funcMenu.getFuncId(), resource);
				
				List<FuncMenu> children = funcMenu.getChildren();
				this.processFuncMenu(user,children,menuItemIdList,handlerIdList,operationIdList);
			}
		}
	}
	
	private void processFuncMenu(User user,List<FuncMenu> funcMenuList
			,List<String> funcMenuIdList,List<String> handlerIdList,List<String> operationIdList){
		for (int i=0;i < funcMenuList.size();i++){
			FuncMenu funcMenu = funcMenuList.get(i);
			String curFuncId = funcMenu.getFuncId();
			
			if (funcMenuIdList.contains(curFuncId)){
				HashMap<String,Resource> menuResources = getResourceRef(user, Resource.Type.Menu);
				Resource menuResource = new Resource();
				menuResource.setResouceType(Resource.Type.Menu);
				String menuId = funcMenu.getFuncId();
				menuResource.setResouceId(menuId);
				menuResource.setResouceName(funcMenu.getFuncName());
				menuResources.put(menuId, menuResource);
			}else{
				loopFuncMenuList(funcMenu,funcMenuList, funcMenuIdList, user);
			}
			
			if (user.containResouce(Resource.Type.Menu, curFuncId)){
				if (funcMenu.isFolder()){
					List<FuncMenu> curFuncMenuList = funcMenu.getChildren();
					if (curFuncMenuList.size() > 0){
						this.processFuncMenu(user,curFuncMenuList,funcMenuIdList,handlerIdList,operationIdList);						
					}
				}else{
					List<FuncHandler> funcHandlers = funcMenu.getHandlers();
					if (funcHandlers.size() == 1){
						FuncHandler handler = funcHandlers.get(0);
						
						HashMap<String,Resource> handlerResources = getResourceRef(user, Resource.Type.Handler);
						Resource resource = new Resource();
						resource.setResouceType(Resource.Type.Handler);
						resource.setResouceId(handler.getHandlerId());
						resource.setResouceName(handler.getHandlerCode());
						handlerResources.put(handler.getHandlerId(), resource);
						
						if (handler.getOperations().size() > 0){
							this.processOperation(user, handler.getOperations(),operationIdList);									
						}
					}else{
						if (funcHandlers.size() > 1){
							for (int j=0; j < funcHandlers.size();j++){
								FuncHandler handler = funcHandlers.get(j);
								String handlerId = handler.getHandlerId();
								if (handlerIdList.contains(handlerId)){
									HashMap<String,Resource> handlerResources = getResourceRef(user, Resource.Type.Handler);
									Resource resource = new Resource();
									resource.setResouceType(Resource.Type.Handler);
									resource.setResouceId(handler.getHandlerId());
									resource.setResouceName(handler.getHandlerCode());
									handlerResources.put(handler.getHandlerId(), resource);	
								}else{
									this.loopFuncHandlerList(handler, funcHandlers, handlerIdList, user);
								}
								
								if (user.containResouce(Resource.Type.Handler, handlerId) && handler.getOperations().size() > 0){
									this.processOperation(user, handler.getOperations(),operationIdList);									
								}
							}
						}
					}
				}
			}
		}
	}
	private void processOperation(User user,List<Operation> operationList,List<String> operationIdList){
		for (int i=0;i < operationList.size();i++){
			Operation operation = operationList.get(i);
			String operationId = operation.getOperId();
			if (operationIdList.contains(operationId)){
				HashMap<String,Resource> operResources = getResourceRef(user, Resource.Type.Operation);
				Resource resource = new Resource();
				resource.setResouceType(Resource.Type.Operation);
				resource.setResouceId(operation.getOperId());
				resource.setResouceName(operation.getOperName());
				operResources.put(operation.getOperId(), resource);				
			}else{
				this.loopOperationList(operation, operationList, operationIdList, user);
			}
		}
	}
	private void loopOperationList(Operation operation,List<Operation> operationList,List<String> operationIdList,User user){
		boolean isMatched = false;
		for (int i=0;i < operationList.size();i++){
			Operation menuItem = operationList.get(i);
			String operationId = menuItem.getOperId();
			if (operationIdList.contains(operationId)){
				isMatched = true;
				break;
			}
		}
		if (!isMatched){
			HashMap<String,Resource> menuResources = getResourceRef(user, Resource.Type.Operation);
			Resource menuResource = new Resource();
			menuResource.setResouceType(Resource.Type.Operation);
			String operationId = operation.getOperId();
			menuResource.setResouceId(operationId);
			menuResource.setResouceName(operation.getOperName());
			menuResources.put(operationId, menuResource);
		}
	}

	private void loopFuncMenuList(FuncMenu curMenuItem,List<FuncMenu> funcMenuList,List<String> funcMenuIdList,User user){
		boolean isMatched = false;
		for (int i=0;i < funcMenuList.size();i++){
			FuncMenu menuItem = funcMenuList.get(i);
			String menuItemId = menuItem.getFuncId();
			if (funcMenuIdList.contains(menuItemId)){
				isMatched = true;
				break;
			}
		}
		if (!isMatched){
			HashMap<String,Resource> menuResources = getResourceRef(user, Resource.Type.Menu);
			Resource menuResource = new Resource();
			menuResource.setResouceType(Resource.Type.Menu);
			String menuId = curMenuItem.getFuncId();
			menuResource.setResouceId(menuId);
			menuResource.setResouceName(curMenuItem.getFuncName());
			menuResources.put(menuId, menuResource);
		}
	}
	
	private void loopFuncHandlerList(FuncHandler funcHandler,List<FuncHandler> funcHandlerList,List<String> handlerIdList,User user){
		boolean isMatched = false;
		for (int i=0;i < funcHandlerList.size();i++){
			FuncHandler curHandler = funcHandlerList.get(i);
			String funcHandlerId = curHandler.getHandlerId();
			if (handlerIdList.contains(funcHandlerId)){
				isMatched = true;
				break;
			}
		}
		if (!isMatched){
			HashMap<String,Resource> menuResources = getResourceRef(user, Resource.Type.Handler);
			Resource menuResource = new Resource();
			menuResource.setResouceType(Resource.Type.Handler);
			String funcHandlerId = funcHandler.getHandlerId();
			menuResource.setResouceId(funcHandlerId);
			menuResource.setResouceName(funcHandler.getHandlerCode());
			menuResources.put(funcHandlerId, menuResource);
		}
	}
	
	
	private HashMap<String,Resource> getResourceRef(User user,String resourceType){
		HashMap<String,Resource> result = null;
		if (!user.getResoucesRefs().containsKey(resourceType)){
			HashMap<String,Resource> temp = new HashMap<String,Resource>();
			user.getResoucesRefs().put(resourceType, temp);
		}
		result = user.getResoucesRefs().get(resourceType);
		return result;
	}
}
