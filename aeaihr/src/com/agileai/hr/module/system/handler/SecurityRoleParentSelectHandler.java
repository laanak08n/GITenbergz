package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityRoleTreeManage;
import com.agileai.util.StringUtil;

public class SecurityRoleParentSelectHandler
        extends TreeSelectHandler {
    public SecurityRoleParentSelectHandler() {
        super();
        this.serviceId = buildServiceId(SecurityRoleTreeManage.class);
        this.isMuilSelect = false;
    }
    
    @Override
    public ViewRenderer prepareDisplay(DataParam param){
		this.setAttributes(param);
		TreeBuilder treeBuilder = provideTreeBuilder(param);
		TreeModel topTreeModel = treeBuilder.buildTreeModel();
		String pickTreeSyntax = null;
		pickTreeSyntax = getMuliPickTreeSyntax(topTreeModel,new StringBuffer());
		this.setAttribute("pickTreeSyntax", pickTreeSyntax);
		return new LocalRenderer(getPage());
	}

    protected TreeBuilder provideTreeBuilder(DataParam param) {
        List<DataRow> records = getService().queryPickTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(records, "ROLE_ID",
                                                  "ROLE_NAME", "ROLE_PID");

        String excludeId = param.get("ROLE_ID");
        treeBuilder.getExcludeIds().add(excludeId);
        this.setAttribute("menuRecords", records);
        return treeBuilder;
    }
    
    protected void processPageAttributes(DataParam param) {
        setAttribute("ROLE_TYPE",
                 FormSelectFactory.create("INFO_TYPE")
                                  .addSelectedValue(getAttributeValue("POS_TYPE","part")));
       
     }
    @SuppressWarnings("unchecked")
	@Override
    protected String getMuliPickTreeSyntax(TreeModel topTreeModel,StringBuffer treeSyntax){
    	String result = null;
    	try {
    		treeSyntax.append("d = new dTree('d');");
			List<DataRow> menuRecords = (List<DataRow>) this.getAttribute("menuRecords");
    		for(DataRow row :menuRecords){
    			String id = row.getString("ROLE_ID");
    			String name = row.getString("ROLE_NAME");
    			String pid = row.getString("ROLE_PID");
    			String type = row.getString("ROLE_TYPE");
    			String js="";
    			if(id.equals(this.getAttribute("ROLE_ID"))){
    				js="";
    			}
    			else{
    			if(!StringUtil.isNotNullNotEmpty(pid)){
    				js="d.add('"+id+"',-1,'"+name+"',\"javascript:setSelectTempValue('"+id+"')\");";
    			}
    			else{
	    			switch (type)
	    			{
	    			case "menu":
	    				js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"')\",null,null,\"images/dtree/folder.gif\",\"images/dtree/folderopen.gif\");";
	    			  break;
	    			case "part":
	    				js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"')\",null,null,\"images/dtree/user.png\",\"images/dtree/user.png\");";
	    			  break;
	    			}
    			}
    			}
    			treeSyntax.append(js);
    			treeSyntax.append("\r\n");
    		}
    		 treeSyntax.append("document.write(d);");
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
