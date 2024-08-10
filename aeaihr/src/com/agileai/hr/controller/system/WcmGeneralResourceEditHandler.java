package com.agileai.hr.controller.system;

import com.agileai.domain.DataParam;
import com.agileai.hr.bizmoduler.system.WcmGeneralGroup8ContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageEditHandler;
import com.agileai.hotweb.domain.FormSelect;
import com.agileai.hotweb.domain.FormSelectFactory;

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
