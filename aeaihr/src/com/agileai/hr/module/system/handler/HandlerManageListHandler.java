package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.MasterSubListHandler;
import com.agileai.hr.cxmodule.FunctionTreeManage;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hr.module.system.service.HandlerManage;
import com.agileai.hotweb.renders.ViewRenderer;

public class HandlerManageListHandler
        extends MasterSubListHandler {
    public HandlerManageListHandler() {
        super();
        this.editHandlerClazz = HandlerManageEditHandler.class;
        this.serviceId = buildServiceId(HandlerManage.class);
    }
    
	private SecurityAuthorizationConfig getSecurityAuthorizationConfig(){
		return this.lookupService(SecurityAuthorizationConfig.class);
	}
    
    @PageAction
	public ViewRenderer synchronousSecurity(DataParam param){
    	String funcId = param.get("funcId");
    	List<String> handlerIdList = getService().retrieveHandlerIdList(funcId);
    	
    	List<String> userIdList = getService().retrieveUserIdList(funcId);
    	List<String> roleIdList = getService().retrieveRoleIdList(funcId);
    	List<String> groupIdList = getService().retrieveGroupIdList(funcId);
		
		List<String> resourceTypes = new ArrayList<String>();
		List<String> resourceIds = new ArrayList<String>();
		for(int i=0;i<handlerIdList.size(); i++){	
			String resourceId = handlerIdList.get(i);
			String handlerId = resourceId;
			
			resourceTypes.add(Resource.Type.Handler);
			resourceIds.add(handlerId);
		}
		if (!userIdList.isEmpty()){
			getSecurityAuthorizationConfig().addUserAuthRelation(resourceTypes, resourceIds,userIdList);			
		}
		if (!roleIdList.isEmpty()){
			getSecurityAuthorizationConfig().addRoleAuthRelation(resourceTypes, resourceIds,roleIdList);			
		}
		if (!groupIdList.isEmpty()){
			getSecurityAuthorizationConfig().addGroupAuthRelation(resourceTypes, resourceIds,groupIdList);			
		}
		return prepareDisplay(param);	
	}

    protected void processPageAttributes(DataParam param) {
        initMappingItem("HANLER_TYPE",
                        FormSelectFactory.create("HANDLER_TYPE").getContent());
    }
    public ViewRenderer doDeleteAction(DataParam param){
    	this.lookupService(FunctionTreeManage.class).clearFuncTreeCache();
    	return super.doDeleteAction(param);
    }
    protected void initParameters(DataParam param) {
        initParamItem(param, "funcId", "");
    }

    protected HandlerManage getService() {
        return (HandlerManage) this.lookupService(this.getServiceId());
    }
}
