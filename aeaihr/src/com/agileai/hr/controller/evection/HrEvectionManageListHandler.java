package com.agileai.hr.controller.evection;

import java.util.Date;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.MasterSubListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.evection.HrEvectionManage;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.util.DateUtil;

public class HrEvectionManageListHandler extends MasterSubListHandler {
	public HrEvectionManageListHandler() {
		super();
		this.editHandlerClazz = HrEvectionManageEditHandler.class;
		this.serviceId = buildServiceId(HrEvectionManage.class);
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if (privilegeHelper.isApprove()) {
			param.put("approveUserCode", "");
			setAttribute("isApprove", true);
			setAttribute("EVE_APPLY_USER", user.getUserId());
		} else {
			param.put("approveUserCode", user.getUserId());
			setAttribute("isApprove", false);
		}
		mergeParam(param);
		initParameters(param);
		this.setAttributes(param);
		List<DataRow> rsList = getService().findMasterRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	protected void processPageAttributes(DataParam param) {
		initMappingItem("APP_RESULT",
				FormSelectFactory.create("APP_RESULT").getContent());
		initMappingItem("STATE",
                FormSelectFactory.create("STATE").getContent());
		setAttribute("STATE", FormSelectFactory.create("STATE")
				.addSelectedValue(param.get("STATE")));
		
		
	}

	public ViewRenderer doApproveRequestAction(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=approve&comeFrome=approve");
	}
	
	public ViewRenderer doRevokeApproveRequestAction(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=revokeApproval&comeFrome=revokeApproval");
	}
	
	public ViewRenderer doPaymentRequestAction(DataParam param){
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=payment&comeFrome=payment");
	}
	
	public ViewRenderer doViewDetailAction(DataParam param){
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz)+"&"+OperaType.KEY+"="+OperaType.DETAIL+"&comeFrome=view");
	}
	
	protected void initParameters(DataParam param) {
		initParamItem(param, "STATE", "");
		initParamItem(
				param,
				"sdate",
				DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
						DateUtil.getDateAdd(DateUtil.getBeginOfMonth(new Date()),DateUtil.MONTH,-1)));
		initParamItem(
				param,
				"edate",
				DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,
						DateUtil.getDateAdd(new Date(), DateUtil.DAY, 1)));
		initParamItem(param, "user_Name", "");
	}

	protected HrEvectionManage getService() {
		return (HrEvectionManage) this.lookupService(this.getServiceId());
	}
}
