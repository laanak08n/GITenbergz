package com.agileai.hr.controller.system;

import java.util.Date;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.QueryModelListHandler;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.DateUtil;

public class SysLogQueryListHandler extends QueryModelListHandler{
	public SysLogQueryListHandler(){
		super();
		this.detailHandlerClazz = SysLogQueryDetailHandler.class;
		this.serviceId = "systemLogQuery";
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
