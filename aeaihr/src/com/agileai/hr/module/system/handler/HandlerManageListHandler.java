package com.agileai.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.MasterSubListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.cxmodule.FunctionTreeManage;
import com.agileai.hr.module.system.service.HandlerManage;

public class HandlerManageListHandler
        extends MasterSubListHandler {
    public HandlerManageListHandler() {
        super();
        this.editHandlerClazz = HandlerManageEditHandler.class;
        this.serviceId = buildServiceId(HandlerManage.class);
    }

    protected void processPageAttributes(DataParam param) {
        initMappingItem("HANLER_TYPE",
                        FormSelectFactory.create("HANDLER_TYPE").getContent());
    }
    public ViewRenderer doDeleteAction(DataParam param){
    	FunctionTreeManage functionTreeManage = this.lookupService(FunctionTreeManage.class);
    	String appName = request.getContextPath().substring(1);
    	functionTreeManage.setAppName(appName);
    	functionTreeManage.clearFuncTreeCache();
    	return super.doDeleteAction(param);
    }
    protected void initParameters(DataParam param) {
        initParamItem(param, "funcId", "");
    }

    protected HandlerManage getService() {
        return (HandlerManage) this.lookupService(this.getServiceId());
    }
}
