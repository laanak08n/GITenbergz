package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeManageHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityGroupQuery;
import com.agileai.hr.module.system.service.SecurityRoleTreeManage;
import com.agileai.util.ListUtil;
import com.agileai.util.StringUtil;

public class SecurityRoleTreeManageHandler
        extends TreeManageHandler {
	private static String deleteRelErrorMsg = "该节点有关联群组，不能删除！";
	private static String deleteAuthErrorMsg = "该节点有关联资源，不能删除！";
    public SecurityRoleTreeManageHandler() {
        super();
        this.serviceId = buildServiceId(SecurityRoleTreeManage.class);
        this.nodeIdField = "ROLE_ID";
        this.nodePIdField = "ROLE_PID";
        this.moveUpErrorMsg = "该节点是第一个节点，不能上移！";
        this.moveDownErrorMsg = "该节点是最后一个节点，不能下移！";
        this.deleteErrorMsg = "该节点还有子节点，不能删除！";
        
    }
     
    
    
    
    protected TreeBuilder provideTreeBuilder(DataParam param) {
        SecurityRoleTreeManage service = this.getService();
        List<DataRow> menuRecords = service.findTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(menuRecords, "ROLE_ID",
                                                  "ROLE_NAME", "ROLE_PID");
        this.setAttribute("menuRecords", menuRecords);
        return treeBuilder;
    }
    
    protected void processPageAttributes(DataParam param) {
        setAttribute("ROLE_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getAttributeValue("ROLE_STATE")));
        setAttribute("CHILD_ROLE_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getAttributeValue("CHILD_ROLE_STATE",
                                                                          "1")));
        setAttribute("CHILD_ROLE_TYPE",
                FormSelectFactory.create("INFO_TYPE")
                                 .addSelectedValue(getAttributeValue("CHILD_ROLE_TYPE",
                                                                     "part")));
        setAttribute("ROLE_TYPE",
                FormSelectFactory.create("INFO_TYPE")
                                 .addSelectedValue(getAttributeValue("ROLE_TYPE","part")));
        setAttribute("ROLE_TYPE_TEXT",
                FormSelectFactory.create("INFO_TYPE")
                                 .addSelectedValue(getAttributeValue("ROLE_TYPE_TEXT")));
        this.setAttribute("currentTabId", param.get("currentTabId"));
        setAttribute("currentTabId",getAttributeValue("currentTabId", "0"));
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
            String parentId = row.stringValue("ROLE_PID");
            result = StringUtil.isNullOrEmpty(parentId);
        }

        return result;
    }
    
    @Override
    public ViewRenderer doInsertChildAction(DataParam param){
    	String roleType=param.getString("CHILD_POLE_TYPE");
    	param.put("CHILD_ROLE_TYPE",roleType);
		getService().insertChildRecord(param);
		return prepareDisplay(param);
	}
    @Override
    public ViewRenderer doDeleteAction(DataParam param){
		String nodeId = param.get(this.nodeIdField);
		SecurityGroupQuery securityGroupQuery=this.lookupService(SecurityGroupQuery.class);
		List<DataRow> childRecords = getService().queryChildRecords(nodeId);
		if (ListUtil.isNullOrEmpty(childRecords)){
			DataParam tempParam = new DataParam();
			tempParam.put("roleId",param.get("ROLE_ID"));
			List<DataRow> tempList=securityGroupQuery.findRecords(tempParam);
			List<DataRow> roleList =getService().queryAuthRecords(param);
			if(ListUtil.isNullOrEmpty(tempList)){
				if(ListUtil.isNullOrEmpty(roleList)){
					getService().deleteCurrentRecord(nodeId);
					param.remove(this.nodeIdField);
					param.put(this.nodeIdField,param.get(this.nodePIdField));
				}else{
					this.setErrorMsg(deleteAuthErrorMsg);
				}
			}else{
				this.setErrorMsg(deleteRelErrorMsg);
			}
		}else{
			this.setErrorMsg(deleteErrorMsg);
		}
		return prepareDisplay(param);
	}
    
    @SuppressWarnings("unchecked")
    protected String getTreeSyntax(DataParam param,TreeModel treeModel,StringBuffer treeSyntax){
    	String result = null;
    	try {
    		treeSyntax.append("<script type='text/javascript'>");
    		treeSyntax.append("d = new dTree('d');");
			List<DataRow> menuRecords = (List<DataRow>) this.getAttribute("menuRecords");
    		for(DataRow row :menuRecords){
    			String id = row.getString("ROLE_ID");
    			String name = row.getString("ROLE_NAME");
    			String pid = row.getString("ROLE_PID");
    			String type = row.getString("ROLE_TYPE");
    			String js="";
    			if(!StringUtil.isNotNullNotEmpty(pid)){
    				js="d.add('"+id+"',-1,'"+name+"',\"javascript:doRefresh('"+id+"')\");";
    			}
    			else{
	    			switch (type)
	    			{
	    			case "menu":
	    				js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:doRefresh('"+id+"')\",null,null,\"images/dtree/folder.gif\",\"images/dtree/folderopen.gif\");";
	    			  break;
	    			case "part":
	    				js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:doRefresh('"+id+"')\",null,null,\"images/dtree/user.png\",\"images/dtree/user.png\");";
	    			  break;
	    			}
    			}
    			treeSyntax.append(js);
    			treeSyntax.append("\r\n");
    		}
    		 treeSyntax.append("$('#treeArea').html(d.toString());");
             treeSyntax.append("\r\n");
             String currentnodeId = this.getAttributeValue("nodeId");
             if (!StringUtil.isNullOrEmpty(currentnodeId)){
             	treeSyntax.append("d.s(d.getIndex('").append(currentnodeId).append("'));");            	
             }
             treeSyntax.append("\r\n");            	
             treeSyntax.append("</script>");
    		result = treeSyntax.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
    }
    protected SecurityRoleTreeManage getService() {
        return (SecurityRoleTreeManage) this.lookupService(this.getServiceId());
    }
}
