package com.agileai.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.QueryModelListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hr.module.system.service.SecurityUserQuery;
import com.agileai.hotweb.renders.ViewRenderer;

public class SecurityUserQueryListHandler
        extends QueryModelListHandler {
    public SecurityUserQueryListHandler() {
        super();
        this.serviceId = buildServiceId(SecurityUserQuery.class);
    }

    protected void processPageAttributes(DataParam param) {
        initMappingItem("USER_STATE",
                        FormSelectFactory.create("SYS_VALID_TYPE").getContent());
    }
    @PageAction
    public ViewRenderer addUserTreeRelation(DataParam param){
    	String roleId = param.get("roleId");
    	String userIds = param.get("userIds");
    	getService().addUserTreeRelation(roleId, userIds.split(","));
    	return this.prepareDisplay(param);
    }
    @PageAction
    public ViewRenderer delUserTreeRelation(DataParam param){
    	String roleId = param.get("ROLE_ID");
    	String userId = param.get("USER_ID");
    	getService().delUserTreeRelation(roleId, userId);
    	return this.prepareDisplay(param);
    } 
    protected void initParameters(DataParam param) {
        initParamItem(param, "roleId", "");
    }

    protected SecurityUserQuery getService() {
        return (SecurityUserQuery) this.lookupService(this.getServiceId());
    }
}
