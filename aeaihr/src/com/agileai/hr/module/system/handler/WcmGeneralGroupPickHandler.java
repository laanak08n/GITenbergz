package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hr.module.system.service.WcmGeneralGroup8ContentManage;

public class WcmGeneralGroupPickHandler
        extends TreeSelectHandler {
    public WcmGeneralGroupPickHandler() {
        super();
        this.serviceId = buildServiceId(WcmGeneralGroup8ContentManage.class);
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

    protected WcmGeneralGroup8ContentManage getService() {
        return (WcmGeneralGroup8ContentManage) this.lookupService(this.getServiceId());
    }
}
