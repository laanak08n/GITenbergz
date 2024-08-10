package com.agileai.hr.controller.workovertime;

import java.util.Date;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.StandardListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.workovertime.HrWorkOvertimeManage;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.util.DateUtil;

public class HrWorkOvertimeManageListHandler extends StandardListHandler {
	public HrWorkOvertimeManageListHandler() {
		super();
		this.editHandlerClazz = HrWorkOvertimeManageEditHandler.class;
		this.serviceId = buildServiceId(HrWorkOvertimeManage.class);
	}

	protected void processPageAttributes(DataParam param) {
		initMappingItem("WOT_TIME", FormSelectFactory.create("WOT_TIME")
				.getContent());
		initMappingItem("APP_RESULT",
				FormSelectFactory.create("APP_RESULT").getContent());
		initMappingItem("STATE",
                FormSelectFactory.create("STATE").getContent());
		setAttribute("STATE", FormSelectFactory.create("STATE")
				.addSelectedValue(param.get("STATE")));

	}

	public ViewRenderer prepareDisplay(DataParam param) {
		User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if (privilegeHelper.isApprove()) {
			param.put("currentUserId","");
			setAttribute("hasRight", true);
			setAttribute("userId", user.getUserId());
		} else {
			param.put("currentUserId", user.getUserId());
			setAttribute("hasRight", false);
		}

		mergeParam(param);
		initParameters(param);

		setAttribute("canSignIn", true);
		this.setAttributes(param);
		List<DataRow> rsList = getService().findRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	public ViewRenderer doInsertRequestAction(DataParam param){
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz)+"&"+OperaType.KEY+"="+OperaType.CREATE);
	}

	public ViewRenderer UpdateRequest(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=" + OperaType.UPDATE);
	}
	public ViewRenderer doDetailRequestAction(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=detail&comeFrome=detail");
	}
	public ViewRenderer doApproveRequestAction(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=approve&comeFrome=approve");
	}
	public ViewRenderer doDeleteAction(DataParam param){
		storeParam(param);
		getService().deletRecord(param);	
		return new RedirectRenderer(getHandlerURL(getClass()));
	}

	protected void initParameters(DataParam param) {
		initParamItem(param, "STATE", "");
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
		initParamItem(param, "user_Name", "");
	}

	protected HrWorkOvertimeManage getService() {
		return (HrWorkOvertimeManage) this.lookupService(this.getServiceId());
	}
}
