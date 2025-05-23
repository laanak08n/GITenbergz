package com.agileai.hr.controller.system;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hr.bizmoduler.system.SecurityGroupQuery;
import com.agileai.hotweb.controller.core.QueryModelListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.ViewRenderer;

public class SecurityGroupQueryListHandler
        extends QueryModelListHandler {
    public SecurityGroupQueryListHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupQuery.class);
    }

    protected void processPageAttributes(DataParam param) {
        initMappingItem("GRP_STATE",
                        FormSelectFactory.create("SYS_VALID_TYPE").getContent());
    }
    @PageAction
    public ViewRenderer addGroupTreeRelation(DataParam param){
    	String roleId = param.get("roleId");
    	String groupIds = param.get("groupIds");
    	getService().addGroupTreeRelation(roleId, groupIds.split(","));
    	return this.prepareDisplay(param);
    }
    @PageAction
    public ViewRenderer delGroupTreeRelation(DataParam param){
    	String roleId = param.get("ROLE_ID");
    	String groupId = param.get("GRP_ID");
    	getService().delGroupTreeRelation(roleId, groupId);
    	return this.prepareDisplay(param);
    }    
    protected void initParameters(DataParam param) {
        initParamItem(param, "roleId", "");
    }

    protected SecurityGroupQuery getService() {
        return (SecurityGroupQuery) this.lookupService(this.getServiceId());
    }
}
