package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityGroupManage;
import com.agileai.util.ListUtil;
import com.agileai.util.StringUtil;

public class SecurityGroupListHandler
        extends TreeAndContentManageListHandler {
    public SecurityGroupListHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupManage.class);
        this.rootColumnId = "00000000-0000-0000-00000000000000000";
        this.defaultTabId = SecurityGroupManage.BASE_TAB_ID;
        this.columnIdField = "GRP_ID";
        this.columnNameField = "GRP_NAME";
        this.columnParentIdField = "GRP_PID";
        this.columnSortField = "GRP_SORT";
    }  
    
    @Override
    public ViewRenderer prepareDisplay(DataParam param){
		initParameters(param);
		this.setAttributes(param);
		String columnId = param.get("columnId",this.rootColumnId);
		this.setAttribute("columnId", columnId);
		this.setAttribute("isRootColumnId",String.valueOf(this.rootColumnId.equals(columnId)));
		TreeBuilder treeBuilder = provideTreeBuilder(param);
		TreeModel treeModel = treeBuilder.buildTreeModel();
		TreeModel filterTreeModel = null;
		TreeModel childModel = treeModel.getFullTreeMap().get(columnId);
		if (childModel != null){
			filterTreeModel = childModel;
		}else{
			filterTreeModel = treeModel;
		}
		
		String menuTreeSyntax = this.getORGTreeSyntax(treeModel,new StringBuffer());
		this.setAttribute("menuTreeSyntax", menuTreeSyntax);
		String tabId = param.get(TreeAndContentManage.TAB_ID,this.defaultTabId);
		
		if (!TreeAndContentManage.BASE_TAB_ID.equals(tabId)){
			param.put("columnId",columnId);
			List<DataRow> rsList = getService().findContentRecords(filterTreeModel,tabId,param);
			this.setRsList(rsList);			
		}else{
			DataParam queryParam = new DataParam(columnIdField,columnId);
			DataRow row = getService().queryTreeRecord(queryParam);
			this.setAttributes(row);
		}
		
		this.setAttribute(TreeAndContentManage.TAB_ID, tabId);
		this.setAttribute(TreeAndContentManage.TAB_INDEX, getTabIndex(tabId));
		
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}
     
    protected void processPageAttributes(DataParam param) {
        setAttribute("GRP_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("GRP_STATE",
                                                                               "1")));
        setAttribute("GRP_RANK",
                FormSelectFactory.create("ORGNAZITION_RANK")
                                 .addSelectedValue(getOperaAttributeValue("GRP_RANK",
                                                                          "1")));
        setAttribute("GRP_TYPE",
                FormSelectFactory.create("ORGNAZITION_TYPE")
                                 .addSelectedValue(getOperaAttributeValue("GRP_TYPE",
                                                                          "")));
        initMappingItem("ROLE_STATE",
                FormSelectFactory.create("SYS_VALID_TYPE").getContent());
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
    	SecurityGroupManage service = this.getService();
        List<DataRow> menuRecords = new ArrayList<DataRow>();
        String treeState=param.get("treeState");
        if(treeState==null||!treeState.equals("true")){
        	menuRecords = service.findTreeRecords(new DataParam());
		}
		else{
			menuRecords = service.queryTreeTypeRecords(new DataParam());
		}
        TreeBuilder treeBuilder = new TreeBuilder(menuRecords,
                                                  this.columnIdField,
                                                  this.columnNameField,
                                                  this.columnParentIdField);
       
        this.setAttribute("menuRecords", menuRecords);
        return treeBuilder;
    }
    
    @Override
    public ViewRenderer doMoveTreeAction(DataParam param){
		String rspText = SUCCESS;
		String columnId = param.get("columnId");
		String targetParentId = param.get("targetParentId");
		TreeAndContentManage service = this.getService();
		String grpType = param.get("GRP_TYPE");
		if(StringUtil.isNullOrEmpty(targetParentId)){
			rspText = buildRelationResults(columnId,grpType,null);
		}else{
			int newMaxSortId = service.retrieveNewMaxSort(targetParentId);
			DataParam queryParam = new DataParam(columnIdField,columnId);
			DataRow row = service.queryTreeRecord(queryParam);
			row.put(this.columnSortField,newMaxSortId);
			row.put(this.columnParentIdField,targetParentId);
    		service.updateTreeRecord(row.toDataParam(true));
		}
		return new AjaxRenderer(rspText);
	}
    
    
    protected List<String> getTabList() {
        List<String> result = new ArrayList<String>();
        result.add(TreeAndContentManage.BASE_TAB_ID);
        result.add("Position");
        return result;
    }
    
    @Override
    public ViewRenderer doSaveTreeBaseRecordAction(DataParam param){
    	String rspText = SUCCESS;
    	try {
        	getService().updateTreeRecord(param); 	
		} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(rspText);
    }  
     
    
    
    @Override
    public ViewRenderer doDeleteTreeNodeAction(DataParam param){
    	String rspText = SUCCESS;
    	try {
    		String columnId = param.get("columnId");
    		TreeAndContentManage service = this.getService();
    		rspText = buildRelationResults(columnId,null,null);
    		if(!SUCCESS.equals(rspText)){
    			return new AjaxRenderer(rspText);
    		}else{
    			service.deleteTreeRecord(columnId);
    		}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
		return new AjaxRenderer(rspText);
	}

    @Override
    public ViewRenderer doDeleteAction(DataParam param){
    	String rspText = SUCCESS;
    	try {
    		String grpId = param.get("columnId");
    		String roleId = param.get("ROLE_ID");
    		param.put("groupId",grpId);
    		rspText = buildRelationResults(grpId,null,roleId);
    		if(!SUCCESS.equals(rspText)){
    			return new AjaxRenderer(rspText);
    		}else{
    			getService().delGroupTreeRelation(param);
    		}
    	} catch (Exception e) {
    		log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(rspText);
	}
    
    protected boolean isRootColumnId(DataParam param) {
        boolean result = true;
        String nodeId = param.get(this.columnIdField,
                                  this.rootColumnId);
        DataParam queryParam = new DataParam(this.columnIdField, nodeId);
        DataRow row = this.getService().queryTreeRecord(queryParam);
        if (row == null) {
            result = false;
        } else {
            String parentId = row.stringValue("GRP_PID");
            result = StringUtil.isNullOrEmpty(parentId);
        }
        return result;
    } 
    
    @SuppressWarnings("unchecked")
	public String getORGTreeSyntax(TreeModel treeModel,StringBuffer treeSyntax){
    	String result = null;
    	try {
    		treeSyntax.append("<script type='text/javascript'>");
    		treeSyntax.append("d = new dTree('d');");
			List<DataRow> menuRecords = (List<DataRow>) this.getAttribute("menuRecords");
    		for(DataRow row :menuRecords){
    			String id = row.getString("GRP_ID");
    			String name = row.getString("GRP_NAME");
    			String pid = row.getString("GRP_PID");
    			String type = row.getString("GRP_TYPE");
    			String js="";
    			if(!StringUtil.isNotNullNotEmpty(pid)){
    				js="d.add('"+id+"',-1,'"+name+"',\"javascript:refreshContent('"+id+"')\");";
    			}
    			else{
	    			switch (type)
	    			{
	    			case "company":
	    			  js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:refreshContent('"+id+"')\",null,null,\"images/dtree/folder.gif\",\"images/dtree/folderopen.gif\");";
	    			  break;
	    			case "department":
	    				js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:refreshContent('"+id+"')\",null,null,\"images/dtree/page.gif\",\"images/dtree/page.gif\");";
	    			  break;
	    			case " ":
	    				js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:refreshContent('"+id+"')\",null,null,\"images/dtree/question.gif\",\"images/dtree/question.gif\");";
	    			  break;
	    			}
    			}
    			treeSyntax.append(js);
    			treeSyntax.append("\r\n");
    		}
    		 treeSyntax.append("$('#treeArea').html(d.toString());");
             treeSyntax.append("\r\n");
             String currentColumnId = this.getAttributeValue("columnId");
             if (!StringUtil.isNullOrEmpty(currentColumnId)){
             	treeSyntax.append("d.s(d.getIndex('").append(currentColumnId).append("'));");            	
             }
             treeSyntax.append("\r\n");            	
             treeSyntax.append("</script>");
    		result = treeSyntax.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
    }

    protected void initParameters(DataParam param) {
        
    }

    protected SecurityGroupManage getService() {
        return (SecurityGroupManage) this.lookupService(this.getServiceId());
    }
    
    private String buildRelationResults(String grpId,String grpType,String roleId){
    	String rspText = SUCCESS;
    	SecurityGroupManage service = this.getService();
    	DataParam queryParam = new DataParam();
    	queryParam.put("columnId",grpId);
    	queryParam.put("grpId",grpId);
    	List<DataRow> childRecords = service.queryChildTreeRecords(grpId);
		List<DataRow> positionRelationRecords = service.findContentRecords(null,"position", queryParam);
		List<DataRow> userRelationRecords = service.findContentRecords(null,"userRelation",queryParam);
		List<DataRow> userRoleRelationRecords = null;
		List<DataRow> roleAuthRecords = null;
		boolean isDeleteGroup = false;
		boolean isDeleteRoleGroupRelation = false;
		if(!StringUtil.isNullOrEmpty(roleId)){
			isDeleteGroup = true;
			isDeleteRoleGroupRelation = true;
			queryParam.put("roleId",roleId);
			userRoleRelationRecords = service.findContentRecords(null,"userRoleRelation",queryParam);
			roleAuthRecords = service.findRoleGroupAuthRecords(grpId,roleId);
		}
		rspText = buildResult(grpType, rspText, childRecords,
				positionRelationRecords, userRelationRecords,
				userRoleRelationRecords, roleAuthRecords, isDeleteGroup,
				isDeleteRoleGroupRelation);
		return rspText;
	}

	private String buildResult(String grpType, String rspText,
			List<DataRow> childRecords, List<DataRow> positionRelationRecords,
			List<DataRow> userRelationRecords,
			List<DataRow> userRoleRelationRecords,
			List<DataRow> roleAuthRecords, boolean isDeleteGroup,
			boolean isDeleteRoleGroupRelation) {
		if (!isDeleteRoleGroupRelation&&!ListUtil.isNullOrEmpty(positionRelationRecords)&&!ListUtil.isNullOrEmpty(userRelationRecords)){
			rspText = "hasContent";
		}else if(!isDeleteRoleGroupRelation&&!ListUtil.isNullOrEmpty(positionRelationRecords)){
			rspText = "roleContent";
		}else if(!isDeleteRoleGroupRelation&&!ListUtil.isNullOrEmpty(userRelationRecords)){
			rspText = "empContent";
		}else if (!isDeleteRoleGroupRelation&&!ListUtil.isNullOrEmpty(childRecords)){
			rspText = "hasChild";	
		}else if(isDeleteRoleGroupRelation&&!ListUtil.isNullOrEmpty(roleAuthRecords)&&!ListUtil.isNullOrEmpty(userRoleRelationRecords)){
			rspText = "hasEmpContent8Auth";
		}else if(isDeleteRoleGroupRelation&&!ListUtil.isNullOrEmpty(roleAuthRecords)){
			rspText = "hasAuth";
		}else if(isDeleteRoleGroupRelation&&!ListUtil.isNullOrEmpty(userRoleRelationRecords)){
			rspText = "empContent";
		}else if(!isDeleteGroup&&!isDeleteRoleGroupRelation&&!StringUtil.isNullOrEmpty(grpType)&&"company".equals(grpType)){
			rspText = "isCompany";	
		}
		return rspText;
	}
}
