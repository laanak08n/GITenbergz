package com.agileai.hr.module.system.handler;

import java.util.Date;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.QueryModelListHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;

public class SysLogQueryListHandler extends QueryModelListHandler{
	public SysLogQueryListHandler(){
		super();
		this.detailHandlerClazz = SysLogQueryDetailHandler.class;
		this.serviceId = "systemLogQuery";
	}
	
	public ViewRenderer prepareDisplay(DataParam param){
		User user = (User) getUser();
		String userCode = user.getUserCode();
		if(!"admin".equals(userCode)){
			param.put("userId", userCode);
		}
		mergeParam(param);
		initParameters(param);
		this.setAttributes(param);
		List<DataRow> rsList = getService().findRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}
	
	@PageAction
	public ViewRenderer refresh(DataParam param){
		return doQueryAction(param);
	}
	
	protected void initParameters(DataParam param) {
		Date yestoday = DateUtil.getDateAdd(new Date(),DateUtil.DAY, -1);
		initParamItem(param,"OPER_STIME", DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,yestoday));
		Date tomorrow = DateUtil.getDateAdd(new Date(),DateUtil.DAY, 1);
		initParamItem(param,"OPER_ETIME", DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL,tomorrow));
	}
}
