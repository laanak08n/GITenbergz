package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.TreeManageHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.cxmodule.FunctionTreeManage;
import com.agileai.util.StringUtil;

public class FunctionTreeManageHandler
        extends TreeManageHandler {
    public FunctionTreeManageHandler() {
        super();
        this.serviceId = buildServiceId(FunctionTreeManage.class);
        this.nodeIdField = "FUNC_ID";
        this.nodePIdField = "FUNC_PID";
        this.moveUpErrorMsg = "该节点是第一个节点，不能上移！";
        this.moveDownErrorMsg = "该节点是最后一个节点，不能下移！";
        this.deleteErrorMsg = "该节点还有子节点，不能删除！";
    }
    protected TreeBuilder provideTreeBuilder(DataParam param) {
        FunctionTreeManage service = this.getService();
        List<DataRow> menuRecords = service.findTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(menuRecords, "FUNC_ID","FUNC_NAME", "FUNC_PID");
        treeBuilder.setTypeKey("FUNC_TYPE");

        return treeBuilder;
    }
    @PageAction
    public ViewRenderer focusTab(DataParam param){
		String currentTabId = param.get("currentTabId");
		this.getSessionAttributes().put(FunctionTreeManageHandler.class.getName()+"currentTabId",currentTabId);
    	return new AjaxRenderer(SUCCESS);
    }
    
	protected void buildTreeSyntax(StringBuffer treeSyntax,TreeModel treeModel){
		List<TreeModel> children = treeModel.getChildren();
		String parentId = treeModel.getId();
        for (int i=0;i < children.size();i++){
        	TreeModel child = children.get(i);
            String curNodeId = child.getId();
            String curNodeName = child.getName();
            String curNodeType = child.getType();
            
            if ("funcmenu".equals(curNodeType)){
            	treeSyntax.append("d.add('"+curNodeId+"','"+parentId+"','"+curNodeName+"',\"javascript:doRefresh('"+curNodeId+"')\",null,null,d.icon.folder,d.icon.folder.folderOpen,null);");
            	treeSyntax.append("\r\n");
            }else{
            	treeSyntax.append("d.add('"+curNodeId+"','"+parentId+"','"+curNodeName+"',\"javascript:doRefresh('"+curNodeId+"')\");");
            	treeSyntax.append("\r\n");
            }
            this.buildTreeSyntax(treeSyntax,child);
        }
    }
    
    protected void processPageAttributes(DataParam param) {
        setAttribute("FUNC_TYPE",
                     FormSelectFactory.create("FUNCTION_TYPE")
                                      .addSelectedValue(getAttributeValue("FUNC_TYPE")).addHasBlankValue(false));
        setAttribute("FUNC_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getAttributeValue("FUNC_STATE")));
        setAttribute("CHILD_FUNC_TYPE",
                     FormSelectFactory.create("FUNCTION_TYPE")
                                      .addSelectedValue(getAttributeValue("CHILD_FUNC_TYPE",
                                                                          "funcnode")).addHasBlankValue(false));
        setAttribute("CHILD_FUNC_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getAttributeValue("CHILD_FUNC_STATE",
                                                                          "1")));
        
        
		String currentTabId = (String)this.getSessionAttributes().get(FunctionTreeManageHandler.class.getName()+"currentTabId");
		if (StringUtil.isNullOrEmpty(currentTabId)){
			currentTabId = "base";
		}
		this.setAttribute("currentTabId",currentTabId);
    }

    protected String provideDefaultNodeId(DataParam param) {
        return "00000000-0000-0000-00000000000000000";
    }

    protected boolean isRootNode(DataParam param) {
        boolean result = true;
        String nodeId = param.get(this.nodeIdField,
                                  this.provideDefaultNodeId(param));
        DataParam queryParam = new DataParam(this.nodeIdField, nodeId);
        DataRow row = this.getService().queryCurrentRecord(queryParam);

        if (row == null) {
            result = false;
        } else {
            String parentId = row.stringValue("FUNC_PID");
            result = StringUtil.isNullOrEmpty(parentId);
        }
        return result;
    }
    
	@PageAction
	public ViewRenderer existSecurityRelation(DataParam param){
		String resourceId = param.get("menuId");
		String resourceType = Resource.Type.Menu;
		boolean isExistSecurityRelation = isExistSecurityRelation(resourceId, resourceType);
		if (isExistSecurityRelation){
			return new AjaxRenderer("Y");
		}
		return new AjaxRenderer("N");		
	}
	
	private boolean isExistSecurityRelation(String resourceId,String resourceType){
		boolean result = false;
		SecurityAuthorizationConfig authorizationConfig = this.lookupService(SecurityAuthorizationConfig.class);
		List<DataRow> roleList = authorizationConfig.retrieveRoleList(resourceType, resourceId);
		if (roleList != null && roleList.size() > 0){
			result = true;
		}
		List<DataRow> userList = authorizationConfig.retrieveUserList(resourceType, resourceId);
		if (userList != null && userList.size() > 0){
			result = true;
		}
		List<DataRow> groupList = authorizationConfig.retrieveGroupList(resourceType, resourceId);
		if (groupList != null && groupList.size() > 0){
			result = true;
		}
		return result;
	}    
    
    public ViewRenderer doSaveAction(DataParam param){
    	getService().clearFuncTreeCache();
    	return super.doSaveAction(param);
    }
    public ViewRenderer doInsertChildAction(DataParam param){
    	getService().clearFuncTreeCache();
    	return super.doInsertChildAction(param);
    }
    public ViewRenderer doCopyCurrentAction(DataParam param){
    	getService().clearFuncTreeCache();
    	return super.doCopyCurrentAction(param);
    }
    public ViewRenderer doChangeParentAction(DataParam param){
    	getService().clearFuncTreeCache();
    	return super.doChangeParentAction(param);
    }
	public ViewRenderer doMoveDownAction(DataParam param){
		getService().clearFuncTreeCache();
		return super.doMoveDownAction(param);
	}
    public ViewRenderer doMoveUpAction(DataParam param){
    	getService().clearFuncTreeCache();
    	return super.doMoveUpAction(param);
    }
    public ViewRenderer doDeleteAction(DataParam param){
    	getService().clearFuncTreeCache();
    	return super.doDeleteAction(param);
    }
    protected FunctionTreeManage getService() {
    	FunctionTreeManage functionTreeManage = (FunctionTreeManage) this.lookupService(this.getServiceId());
    	String appName = request.getContextPath().substring(1);
    	functionTreeManage.setAppName(appName);
    	return functionTreeManage;
    }
}
