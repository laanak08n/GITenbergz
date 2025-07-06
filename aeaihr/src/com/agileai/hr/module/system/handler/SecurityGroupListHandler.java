package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hr.module.system.service.SecurityGroupManage;

public class SecurityGroupListHandler
        extends TreeAndContentManageListHandler {
    public SecurityGroupListHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupManage.class);
        this.rootColumnId = "00000000-0000-0000-00000000000000000";
        this.defaultTabId = SecurityGroupManage.BASE_TAB_ID;
        this.columnIdField = "GRP_ID";
        this.columnNameField = "GRP_NAME";
        this.columnParentIdField = "GRP_PID";
        this.columnSortField = "GRP_SORT";
    }

    protected void processPageAttributes(DataParam param) {
        String tabId = param.get(TreeAndContentManage.TAB_ID, this.defaultTabId);

        if ("SecurityUser".equals(tabId)) {
            initMappingItem("USER_SEX",
                            FormSelectFactory.create("USER_SEX").getContent());
            initMappingItem("USER_STATE",
                            FormSelectFactory.create("SYS_VALID_TYPE")
                                             .getContent());
        }

        setAttribute("GRP_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("GRP_STATE",
                                                                               "1")));
    }

    protected void initParameters(DataParam param) {
        String tabId = param.get(TreeAndContentManage.TAB_ID, this.defaultTabId);

        if ("SecurityUser".equals(tabId)) {
            initParamItem(param, "columnId", "");
            initParamItem(param, "userCode", "");
            initParamItem(param, "userName", "");
        }
    }

    protected TreeBuilder provideTreeBuilder(DataParam param) {
        SecurityGroupManage service = this.getService();
        List<DataRow> menuRecords = service.findTreeRecords(new DataParam());
        TreeBuilder treeBuilder = new TreeBuilder(menuRecords,
                                                  this.columnIdField,
                                                  this.columnNameField,
                                                  this.columnParentIdField);

        return treeBuilder;
    }

    protected List<String> getTabList() {
        List<String> result = new ArrayList<String>();
        result.add(SecurityGroupManage.BASE_TAB_ID);
        result.add("SecurityUser");

        return result;
    }

    protected SecurityGroupManage getService() {
        return (SecurityGroupManage) this.lookupService(this.getServiceId());
    }
}
