package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityGroupManage;
import com.agileai.util.StringUtil;

public class SecurityGroupPickHandler
        extends TreeSelectHandler {
    public SecurityGroupPickHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupManage.class);
        this.isMuilSelect = false;
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
        List<DataRow> records = getService().queryPickTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(records, "GRP_ID",
                                                  "GRP_NAME", "GRP_PID");

        String excludeId = param.get("GRP_ID");
        treeBuilder.getExcludeIds().add(excludeId);
        this.setAttribute("menuRecords", records);
        return treeBuilder;
    }
    
    @Override
    public ViewRenderer prepareDisplay(DataParam param){
		this.setAttributes(param);
		TreeBuilder treeBuilder = provideTreeBuilder(param);
		TreeModel topTreeModel = treeBuilder.buildTreeModel();
		
		String pickTreeSyntax = null;
		pickTreeSyntax = getMuliPickTreeSyntax(topTreeModel, new StringBuffer());
		
		this.setAttribute("pickTreeSyntax", pickTreeSyntax);
		return new LocalRenderer(getPage());
	}
    
    @SuppressWarnings("unchecked")
	@Override
    protected String getMuliPickTreeSyntax(TreeModel treeModel,StringBuffer treeSyntax){
    	String result = null;
    	try {
    		treeSyntax.append("d = new dTree('d');");
			List<DataRow> menuRecords = (List<DataRow>) this.getAttribute("menuRecords");
    		for(DataRow row :menuRecords){
    			String id = row.getString("GRP_ID");
    			String name = row.getString("GRP_NAME");
    			String pid = row.getString("GRP_PID");
    			String type = row.getString("GRP_TYPE");
    			String js="";
    			if(id.equals(this.getAttribute("GRP_ID"))){
    				js="";
    			}
    			else{
    			if(!StringUtil.isNotNullNotEmpty(pid)){
    				js="d.add('"+id+"',-1,'"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\");";
    			}
    			else{
	    			switch (type)
	    			{
	    			case "company":
	    			  js = "d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\",null,null,\"images/dtree/folder.gif\",\"images/dtree/folderopen.gif\");";
	    			  break;
	    			case "department":
	    			  js = "d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\",null,null,\"images/dtree/page.gif\",\"images/dtree/page.gif\");";
	    			  break;
	    			case " ":
	    			  js = "d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\",null,null,\"images/dtree/question.gif\",\"images/dtree/question.gif\");";
	    			  break;
	    			}
    			}
    			}
    			treeSyntax.append(js);
    			treeSyntax.append("\r\n");
    		}
    		treeSyntax.append("document.write(d);");
    		result = treeSyntax.toString();           
    		result = treeSyntax.toString();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return result;
    }
    
    protected SecurityGroupManage getService() {
        return (SecurityGroupManage) this.lookupService(this.getServiceId());
    }
}
