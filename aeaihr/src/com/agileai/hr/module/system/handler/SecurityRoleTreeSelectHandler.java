package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hr.cxmodule.SecurityRoleTreeSelect;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.renders.ViewRenderer;

public class SecurityRoleTreeSelectHandler
        extends TreeSelectHandler {
    public SecurityRoleTreeSelectHandler() {
        super();
        this.serviceId = buildServiceId(SecurityRoleTreeSelect.class);
        this.isMuilSelect = true;
        this.checkRelParentNode = false;
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
        List<DataRow> records = getService().queryPickTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(records, "ROLE_ID",
                                                  "ROLE_NAME", "ROLE_PID");

        return treeBuilder;
    }
    
    @PageAction
    public ViewRenderer addRoleRequest(DataParam param){
    	String resourceType = param.get("resourceType");
    	String resourceId = param.get("resourceId");
    	SecurityAuthorizationConfig groupQuery = lookupService(SecurityAuthorizationConfig.class);
    	List<DataRow> records = groupQuery.retrieveRoleList(resourceType, resourceId);
    	for (int i=0;i < records.size();i++){
    		DataRow row = records.get(i);
    		String groupId = row.stringValue("ROLE_ID");
    		this.invisiableCheckBoxIdList.add(groupId);
    	}
    	return this.prepareDisplay(param);
    }
    
    protected SecurityRoleTreeSelect getService() {
        return (SecurityRoleTreeSelect) this.lookupService(this.getServiceId());
    }
}
