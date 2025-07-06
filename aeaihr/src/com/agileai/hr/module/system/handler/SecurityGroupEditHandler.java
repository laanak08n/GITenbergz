package com.agileai.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.TreeAndContentColumnEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hr.module.system.service.SecurityGroupManage;

public class SecurityGroupEditHandler
        extends TreeAndContentColumnEditHandler {
    public SecurityGroupEditHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupManage.class);
        this.columnIdField = "GRP_ID";
        this.columnParentIdField = "GRP_PID";
    }

    protected void processPageAttributes(DataParam param) {
        setAttribute("GRP_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("GRP_STATE",
                                                                               "1")));
    }

    protected SecurityGroupManage getService() {
        return (SecurityGroupManage) this.lookupService(this.getServiceId());
    }
}
