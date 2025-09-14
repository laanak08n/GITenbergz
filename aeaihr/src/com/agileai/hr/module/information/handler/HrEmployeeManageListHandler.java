package com.agileai.hr.module.information.handler;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.MasterSubListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.DispatchRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.hr.module.information.service.HrEmployeeManage;

public class HrEmployeeManageListHandler
        extends MasterSubListHandler {
    public HrEmployeeManageListHandler() {
        super();
        this.editHandlerClazz = HrEmployeeManageEditHandler.class;
        this.serviceId = buildServiceId(HrEmployeeManage.class);
    }
    public ViewRenderer prepareDisplay(DataParam param) {
		User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		if (!privilegeHelper.isHRMASTER()) {
			param.put("currentUserCode", user.getUserCode());
		} else {
			param.put("currentUserCode", "");
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
        setAttribute("empSex",
                     FormSelectFactory.create("USER_SEX")
                                      .addSelectedValue(param.get("empSex")));
        initMappingItem("EMP_SEX",
                        FormSelectFactory.create("USER_SEX").getContent());
        initMappingItem("EMP_EDUCATION",
                        FormSelectFactory.create("EMP_EDUCATION").getContent());
        initMappingItem("EMP_STATE",
                FormSelectFactory.create("EMP_STATE").getContent());
		setAttribute("EMP_STATE", FormSelectFactory.create("EMP_STATE")
				.addSelectedValue(param.get("EMP_STATE")));
    }
    
	
    public ViewRenderer doApproveRequestAction(DataParam param) {
		storeParam(param);
		return new DispatchRenderer(getHandlerURL(this.editHandlerClazz) + "&"
				+ OperaType.KEY + "=approve&comeFrome=approve");
	}
    protected void initParameters(DataParam param) {
    	initParamItem(param, "EMP_STATE", "");
        initParamItem(param, "empSex", "");
        initParamItem(param, "empName", "");
    }

    protected HrEmployeeManage getService() {
        return (HrEmployeeManage) this.lookupService(this.getServiceId());
    }
    @PageAction
	public ViewRenderer revokeApproval(DataParam param) {
		String empId = param.get("EMP_ID");
		getService().revokeApprovalRecords(empId);
		return prepareDisplay(param);
	}
}
