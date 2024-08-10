package com.agileai.hr.controller.system;

import com.agileai.domain.DataParam;
import com.agileai.hr.bizmoduler.system.SecurityGroupManage;
import com.agileai.hotweb.controller.core.TreeAndContentColumnEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;

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
