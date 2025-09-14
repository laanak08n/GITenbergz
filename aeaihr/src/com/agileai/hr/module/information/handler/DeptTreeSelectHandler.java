package com.agileai.hr.module.information.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hr.module.information.service.DeptTreeSelect;

public class DeptTreeSelectHandler
        extends TreeSelectHandler {
    public DeptTreeSelectHandler() {
        super();
        this.serviceId = buildServiceId(DeptTreeSelect.class);
        this.isMuilSelect = false;
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
        List<DataRow> records = getService().queryPickTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(records, "GRP_ID",
                                                  "GRP_NAME", "GRP_PID");
        String rootId = param.get("Root");
        treeBuilder.setRootId(rootId);

        return treeBuilder;
    }

    protected DeptTreeSelect getService() {
        return (DeptTreeSelect) this.lookupService(this.getServiceId());
    }
}
