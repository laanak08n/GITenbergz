package com.agileai.hr.module.bonuspenalty.handler;

import java.util.Date;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.StandardListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.hr.cxmodule.HrBonusPenaltyManage;
import com.agileai.util.DateUtil;

public class HrBonusPenaltyManageListHandler
        extends StandardListHandler {
    public HrBonusPenaltyManageListHandler() {
        super();
        this.editHandlerClazz = HrBonusPenaltyManageEditHandler.class;
        this.serviceId = buildServiceId(HrBonusPenaltyManage.class);
    }
    
	public ViewRenderer prepareDisplay(DataParam param){
		User user = (User) getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if(!privilegeHelper.isSalMaster()){
			param.put("userId", user.getUserId());
		}
		mergeParam(param);
		initParameters(param);
		this.setAttributes(param);
		List<DataRow> rsList = getService().findRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
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
						DateUtil.getDateAdd(
								DateUtil.getBeginOfMonth(new Date()), DateUtil.MONTH, -1)));
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
