package com.agileai.hr.controller.system;

import com.agileai.domain.DataParam;
import com.agileai.hr.bizmoduler.system.FunctionTreeManage;
import com.agileai.hr.bizmoduler.system.HandlerManage;
import com.agileai.hotweb.controller.core.MasterSubListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.ViewRenderer;

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
