package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.core.TreeSelectService;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityGroupQuery;
import com.agileai.util.ListUtil;
import com.agileai.util.StringUtil;

public class SecurityGRTreeSelectHandler
        extends TreeSelectHandler {
    public SecurityGRTreeSelectHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupQuery.class);
        this.isMuilSelect = true;
        this.checkRelParentNode = false;
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
    	SecurityGroupQuery getService = lookupService(SecurityGroupQuery.class);
        List<DataRow> records = getService.queryPickTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(records, "ROLE_ID",
                                                  "ROLE_NAME", "ROLE_PID");
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
    			String id = row.getString("ROLE_ID");
    			String name = row.getString("ROLE_NAME");
    			String pid = row.getString("ROLE_PID");
    			String type = row.getString("ROLE_TYPE");
    			String js="";
    			if(!StringUtil.isNotNullNotEmpty(pid)){
    				js="d.add('"+id+"',-1,'"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\");";
    			}
    			else{
	    			switch (type)
	    			{
	    			case "menu":
	    			  js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\",null,null,\"images/dtree/folder.gif\",\"images/dtree/folderopen.gif\");";
	    			  break;
	    			case "part":
	    			  js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\",null,null,\"images/dtree/user.png\",\"images/dtree/user.png\");";
	    			  break;
	    			case " ":
	    			  js="d.add('"+id+"','"+pid+"','"+name+"',\"javascript:setSelectTempValue('"+id+"','"+name+"')\",null,null,\"images/dtree/user.png\",\"images/dtree/user.png\");";
	    			  break;
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
 
    @PageAction
    public ViewRenderer doSaveAction(DataParam param){
    	String rspText = SUCCESS;
    	try {
    		SecurityGroupQuery getService = lookupService(SecurityGroupQuery.class);
        	String RG_ID=KeyGenerator.instance().genKey();
        	String ROLE_ID= param.get("ROLE_ID");
        	String GRP_ID=param.get("GRP_ID");
        	DataParam dataParam = new DataParam();
        	dataParam.put("ROLE_ID",ROLE_ID);
        	dataParam.put("GRP_ID",GRP_ID);
        	dataParam.put("RG_ID",RG_ID);
        	List<DataRow> ismenu = getService.queryTreeorgTypeRecords(ROLE_ID);
        	String posType=ismenu.get(0).getString("ROLE_TYPE");
        	List<DataRow> childRecord =  getService.queryChildRecords(ROLE_ID);
        	if(posType.equals("menu")){
   			 rspText = "isMenu";
   		    }
        	else if(ListUtil.isNullOrEmpty(childRecord)){
    			 rspText = "posChild";
    		}
    		else{	
    			getService.addGroupTreeRelation(dataParam);
   
    		}
			
		} catch (Exception e) {
				log.error(e.getLocalizedMessage(), e);
		}
		return new AjaxRenderer(rspText);
    }
 
    protected TreeSelectService getService() {
        return (TreeSelectService) this.lookupService(this.getServiceId());
    }
}
