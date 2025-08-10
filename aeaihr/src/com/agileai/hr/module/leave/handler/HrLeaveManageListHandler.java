package com.agileai.hr.module.leave.handler;

import java.util.Date;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.StandardListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.hr.module.leave.service.HrLeaveManage;
import com.agileai.util.DateUtil;

public class HrLeaveManageListHandler
        extends StandardListHandler {
    public HrLeaveManageListHandler() {
        super();
        this.editHandlerClazz = HrLeaveManageEditHandler.class;
        this.serviceId = buildServiceId(HrLeaveManage.class);
    }

    protected void processPageAttributes(DataParam param) {
        initMappingItem("LEA_TYPE",
                        FormSelectFactory.create("LEA_TYPE").getContent());
        initMappingItem("APP_RESULT",
                        FormSelectFactory.create("APP_RESULT").getContent());
        initMappingItem("STATE",
                FormSelectFactory.create("STATE").getContent());
		setAttribute("STATE", FormSelectFactory.create("STATE")
				.addSelectedValue(param.get("STATE")));
    }
    
    public ViewRenderer prepareDisplay(DataParam param){
    	User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if (privilegeHelper.isApprove()) {
			param.put("currentUserId", "");
			setAttribute("hasRight", true);
			setAttribute("userId", user.getUserId());
		} else {
			param.put("currentUserId", user.getUserId());
			setAttribute("hasRight", false);
		}
    	
		mergeParam(param);
		initParameters(param);
		
		setAttribute("canSignIn", true);
		DataParam queryParam = new DataParam();
		queryParam.put("leaDate",
				DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date()));
		queryParam.put("currentUser", user.getUserId());
		this.setAttributes(param);
		List<DataRow> rsList = getService().findRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}
    public ViewRenderer doInsertRequestAction(DataParam param){
		String operaType = param.get(OperaType.KEY);
		User user = (User) this.getUser();
		if ("insert".equals(operaType)) {
			DataParam queryParam = new DataParam();
			queryParam.put("currentDate", DateUtil.getDateByType(
					DateUtil.YYMMDD_HORIZONTAL, new Date()));
			queryParam.put("USER_ID",user.getUserId());
			DataRow record = getService().getNowRecord(param);
			param.putAll(record);
		}
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz)+"&"+OperaType.KEY+"="+OperaType.CREATE);
	}
    public ViewRenderer UpdateRequest(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		if ("update".equals(operaType)) {
			DataParam queryParam = new DataParam();
			queryParam.put("currentDate", DateUtil.getDateByType(
					DateUtil.YYMMDD_HORIZONTAL, new Date()));
			queryParam.put("LEA_ID", param.get("LEA_ID"));
			DataRow record = getService().getNowRecord(param);
			param.putAll(record);
			if (record != null) {
				if (record.get("LEA_TYPE") != null) {
					setAttribute("canSignIn", true);
				}
				if (record.get("LEA_APP_OPINION") != null) {
					setAttribute("canApprove", false);
				}
			} else {
			}

		}
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=" + OperaType.UPDATE);
	}
    public ViewRenderer doApproveRequestAction(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=approve&comeFrome=approve");
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

    protected HrLeaveManage getService() {
        return (HrLeaveManage) this.lookupService(this.getServiceId());
    }
}
