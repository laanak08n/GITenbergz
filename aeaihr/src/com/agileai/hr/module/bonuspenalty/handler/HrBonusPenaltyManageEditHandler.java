package com.agileai.hr.module.bonuspenalty.handler;

import java.util.Date;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.StandardEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.cxmodule.HrBonusPenaltyManage;
import com.agileai.util.DateUtil;

public class HrBonusPenaltyManageEditHandler
        extends StandardEditHandler {
    public HrBonusPenaltyManageEditHandler() {
        super();
        this.listHandlerClass = HrBonusPenaltyManageListHandler.class;
        this.serviceId = buildServiceId(HrBonusPenaltyManage.class);
    }
    
	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		if (isReqRecordOperaType(operaType)){
			DataRow record = getService().getRecord(param);
			this.setAttributes(record);	
		}
		
		if(OperaType.CREATE.equals(operaType)){
			String date = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
			this.setAttribute("BP_DATE", date);
		}
		this.setOperaType(operaType);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

    protected void processPageAttributes(DataParam param) {
        setAttribute("BP_TYPE",
                     FormSelectFactory.create("BP_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("BP_TYPE",
                                                                               "PUNISHMENT")));
    }

    protected HrBonusPenaltyManage getService() {
        return (HrBonusPenaltyManage) this.lookupService(this.getServiceId());
    }
}
