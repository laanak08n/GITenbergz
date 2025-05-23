package com.agileai.hr.controller.system;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hr.bizmoduler.system.SecurityGroupManage;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;

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

        return treeBuilder;
    }

    protected SecurityGroupManage getService() {
        return (SecurityGroupManage) this.lookupService(this.getServiceId());
    }
}
