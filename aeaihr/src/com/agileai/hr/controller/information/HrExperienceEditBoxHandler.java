package com.agileai.hr.controller.information;

import com.agileai.domain.*;
import com.agileai.hotweb.controller.core.MasterSubEditPboxHandler;
import com.agileai.hr.bizmoduler.information.HrEmployeeManage;
import com.agileai.util.*;

public class HrExperienceEditBoxHandler
        extends MasterSubEditPboxHandler {
    public HrExperienceEditBoxHandler() {
        super();
        this.serviceId = buildServiceId(HrEmployeeManage.class);
        this.subTableId = "HrExperience";
    }

    protected void processPageAttributes(DataParam param) {
        if (!StringUtil.isNullOrEmpty(param.get("EMP_ID"))) {
            this.setAttribute("EMP_ID", param.get("EMP_ID"));
        }
    }

    protected HrEmployeeManage getService() {
        return (HrEmployeeManage) this.lookupService(this.getServiceId());
    }
}
