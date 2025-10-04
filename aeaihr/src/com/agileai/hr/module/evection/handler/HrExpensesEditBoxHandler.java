package com.agileai.hr.module.evection.handler;

import java.math.BigDecimal;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.MasterSubEditPboxHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.evection.service.HrEvectionManage;
import com.agileai.util.StringUtil;

public class HrExpensesEditBoxHandler extends MasterSubEditPboxHandler {
	public HrExpensesEditBoxHandler() {
		super();
		this.serviceId = buildServiceId(HrEvectionManage.class);
		this.subTableId = "HrExpenses";
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		if ("update".equals(operaType)){
			DataRow record = getService().getSubRecord(subTableId, param); 
			this.setAttributes(record);	
			DataParam eveParam = new DataParam("EVE_ID", record.get("EVE_ID"));
			DataRow eveRecord = getService().getMasterRecord(eveParam);
			if (eveRecord.get("STATE").equals("drafe")) {
				setAttribute("doDetail", true);
			}else{
				setAttribute("doDetail", false);
			}
			User user = (User) this.getUser();
			if(eveRecord.get("EVE_APPLY_USER").equals(user.getUserId())){
				setAttribute("noUpdate", true);
			}else{
				setAttribute("noUpdate", false);
			}
		}
		if("insert".equals(operaType)){
			setAttribute("doDetail", true);
			setAttribute("noUpdate", true);
		}
			this.setOperaType(operaType);
			processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	public ViewRenderer doSaveAction(DataParam param) {
		String responseText = FAIL;
		String operateType = param.get(OperaType.KEY);
		if (OperaType.CREATE.equals(operateType)) {
			getService().createSubRecord(subTableId, param);
			responseText = SUCCESS;
		} else if (OperaType.UPDATE.equals(operateType)) {
			getService().updateSubRecord(subTableId, param);
			responseText = SUCCESS;
		}
		String masterRecordId = param.get("EVE_ID");
		this.getService().computeTotalMoney(masterRecordId);
		return new AjaxRenderer(responseText);
	}

	protected void processPageAttributes(DataParam param) {
		if (!StringUtil.isNullOrEmpty(param.get("EVE_ID"))) {
			this.setAttribute("EVE_ID", param.get("EVE_ID"));
		}

		setAttribute(
				"EXPE_TRANSPORTATION_WAY",
				FormSelectFactory.create("EXPE_TRANSPORTATION_WAY")
						.addSelectedValue(
								getOperaAttributeValue(
										"EXPE_TRANSPORTATION_WAY", "")));
		BigDecimal empMoney = new BigDecimal("0.00");
		if (this.getAttribute("EXPE_HOTEL") == null) {
			this.setAttribute("EXPE_HOTEL", empMoney);
		}
		if (this.getAttribute("EXPE_OTHER") == null) {
			this.setAttribute("EXPE_OTHER", empMoney);
		}
	}

	protected HrEvectionManage getService() {
		return (HrEvectionManage) this.lookupService(this.getServiceId());
	}
}
