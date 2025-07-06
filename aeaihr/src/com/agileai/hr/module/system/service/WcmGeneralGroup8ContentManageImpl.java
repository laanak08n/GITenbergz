package com.agileai.hr.module.system.service;

import com.agileai.hotweb.bizmoduler.core.TreeAndContentManageImpl;

public class WcmGeneralGroup8ContentManageImpl
        extends TreeAndContentManageImpl
        implements WcmGeneralGroup8ContentManage {
    public WcmGeneralGroup8ContentManageImpl() {
        super();
        this.columnIdField = "GRP_ID";
        this.columnParentIdField = "GRP_PID";
        this.columnSortField = "GRP_ORDERNO";
    }
}
