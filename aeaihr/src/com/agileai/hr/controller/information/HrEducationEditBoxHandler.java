package com.agileai.hr.controller.information;

import com.agileai.domain.*;
import com.agileai.hotweb.controller.core.MasterSubEditPboxHandler;
import com.agileai.hr.bizmoduler.information.HrEmployeeManage;
import com.agileai.util.*;

public class HrEducationEditBoxHandler
        extends MasterSubEditPboxHandler {
    public HrEducationEditBoxHandler() {
        super();
        this.serviceId = buildServiceId(HrEmployeeManage.class);
        this.subTableId = "HrEducation";
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
