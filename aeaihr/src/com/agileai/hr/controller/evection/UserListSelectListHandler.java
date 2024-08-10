package com.agileai.hr.controller.evection;

import com.agileai.domain.*;
import com.agileai.hotweb.controller.core.PickFillModelHandler;
import com.agileai.hr.bizmoduler.evection.UserListSelect;

public class UserListSelectListHandler
        extends PickFillModelHandler {
    public UserListSelectListHandler() {
        super();
        this.serviceId = buildServiceId(UserListSelect.class);
    }

    protected void processPageAttributes(DataParam param) {
    }

    protected void initParameters(DataParam param) {
        initParamItem(param, "userName", "");
    }

    protected UserListSelect getService() {
        return (UserListSelect) this.lookupService(this.getServiceId());
    }
}
