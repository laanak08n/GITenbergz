package com.agileai.hr.module.system.handler;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.TreeAndContentManageEditHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hr.module.system.service.WcmGeneralGroup8ContentManage;

public class WcmGeneralResourceEditHandler
        extends TreeAndContentManageEditHandler {
    public WcmGeneralResourceEditHandler() {
        super();
        this.serviceId = buildServiceId(WcmGeneralGroup8ContentManage.class);
        this.tabId = "WcmGeneralResource";
        this.columnIdField = "GRP_ID";
        this.contentIdField = "RES_ID";
    }

    protected void processPageAttributes(DataParam param) {
    	FormSelect isShareable = FormSelectFactory.create("BOOL_DEFINE");
    	String RES_SHAREABLE = this.getAttributeValue("RES_SHAREABLE","Y");
    	isShareable.addSelectedValue(RES_SHAREABLE);
    	this.setAttribute("RES_SHAREABLE",isShareable);
    }

    protected WcmGeneralGroup8ContentManage getService() {
        return (WcmGeneralGroup8ContentManage) this.lookupService(this.getServiceId());
    }
}
