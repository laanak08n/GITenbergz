package com.agileai.hr.module.bonuspenalty.handler;

import java.util.Date;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.StandardListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hr.module.bonuspenalty.service.HrBonusPenaltyManage;
import com.agileai.util.DateUtil;

public class HrBonusPenaltyManageListHandler
        extends StandardListHandler {
    public HrBonusPenaltyManageListHandler() {
        super();
        this.editHandlerClazz = HrBonusPenaltyManageEditHandler.class;
        this.serviceId = buildServiceId(HrBonusPenaltyManage.class);
    }

    protected void processPageAttributes(DataParam param) {
        setAttribute("bpType",
                     FormSelectFactory.create("BP_TYPE")
                                      .addSelectedValue(param.get("bpType")));
        initMappingItem("BP_TYPE",
                        FormSelectFactory.create("BP_TYPE").getContent());
    }

    protected void initParameters(DataParam param) {
        initParamItem(param, "bpType", "");
        initParamItem(param, "bpDate", "");
		initParamItem(
				param,
				"sdate",
				DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
						DateUtil.getBeginOfMonth(new Date())));
		initParamItem(
				param,
				"edate",
				DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
						DateUtil.getDateAdd(new Date(), DateUtil.DAY, 1)));
    }

    protected HrBonusPenaltyManage getService() {
        return (HrBonusPenaltyManage) this.lookupService(this.getServiceId());
    }
}
