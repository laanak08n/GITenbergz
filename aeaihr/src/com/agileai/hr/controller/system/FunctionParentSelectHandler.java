package com.agileai.hr.controller.system;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hr.bizmoduler.system.FunctionTreeManage;
import com.agileai.hotweb.controller.core.TreeSelectHandler;
import com.agileai.hotweb.domain.TreeBuilder;

public class FunctionParentSelectHandler
        extends TreeSelectHandler {
    public FunctionParentSelectHandler() {
        super();
        this.serviceId = buildServiceId(FunctionTreeManage.class);
        this.isMuilSelect = false;
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
        List<DataRow> records = getService().queryPickTreeRecords(param);
        TreeBuilder treeBuilder = new TreeBuilder(records, "FUNC_ID",
                                                  "FUNC_NAME", "FUNC_PID");

        String excludeId = param.get("FUNC_ID");
        treeBuilder.getExcludeIds().add(excludeId);

        return treeBuilder;
    }

    protected FunctionTreeManage getService() {
        return (FunctionTreeManage) this.lookupService(this.getServiceId());
    }
}
