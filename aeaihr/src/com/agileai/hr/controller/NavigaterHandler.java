package com.agileai.hr.controller;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.frame.OnlineCounterService;
import com.agileai.hotweb.common.BeanFactory;
import com.agileai.hotweb.common.Constants;
import com.agileai.hotweb.common.Constants.FrameHandlers;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.StringUtil;

public class NavigaterHandler extends BaseHandler{
	public static final String  LOGIN_USER = "loginUser";
	
	public NavigaterHandler(){
		super();
	}
	
	public ViewRenderer prepareDisplay(DataParam param) {
		setAttribute(LOGIN_USER, getUser());
		String onlineAccount = this.dispatchServlet.getInitParameter(Constants.OnlineCounterServiceKey);
		if (!StringUtil.isNullOrEmpty(onlineAccount)){
			OnlineCounterService onlineCounterService = (OnlineCounterService)BeanFactory.instance().getBean(onlineAccount);
			int onlineCount = onlineCounterService.queryOnlineCount();
			setAttribute("onlineCount", onlineCount);
		}
		return new LocalRenderer(getPage());
	}
	
	public ViewRenderer doLogoutAction(DataParam param){
		String casServerLogoutUrl = this.dispatchServlet.getServletContext().getInitParameter("casServerLogoutUrl");
		if (!StringUtil.isNullOrEmpty(casServerLogoutUrl)){
			this.clearSession();
			String serverName = this.dispatchServlet.getServletContext().getInitParameter("serverName");
			String redirectUrl = casServerLogoutUrl + "?service="+ serverName+this.dispatchServlet.getServletContext().getContextPath();
			return new RedirectRenderer(redirectUrl);
		}else{
			return new RedirectRenderer(getHandlerURL(FrameHandlers.LoginHandlerId)+"&actionType=logout");			
		}
	}
}
