package com.agileai.hr.module.location.handler;

import java.util.Date;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.StandardListHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.location.service.HrLocationManage;
import com.agileai.util.DateUtil;

public class HrLocationManageListHandler
        extends StandardListHandler {
    public HrLocationManageListHandler() {
        super();
        this.serviceId = buildServiceId(HrLocationManage.class);
    }

	public ViewRenderer prepareDisplay(DataParam param){
//		User user = (User) getUser();
//		if("".equals(param.get("userName"))){
//			param.put("userName",user.getUserName());
//		}
		
		mergeParam(param);
		initParameters(param);
		this.setAttributes(param);
		List<DataRow> rsList = getService().findRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}
	
    protected void processPageAttributes(DataParam param) {
    	
    }

    protected void initParameters(DataParam param) {
    	User user = (User) getUser();
    	initParamItem(param, "userName", user.getUserName());
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

    protected HrLocationManage getService() {
        return (HrLocationManage) this.lookupService(this.getServiceId());
    }
}
