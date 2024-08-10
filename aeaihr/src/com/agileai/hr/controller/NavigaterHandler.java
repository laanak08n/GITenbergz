package com.agileai.hr.controller;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.frame.OnlineCounterService;
import com.agileai.hr.bizmoduler.system.HotwebAuthHelper;
import com.agileai.hotweb.common.BeanFactory;
import com.agileai.hotweb.common.Constants;
import com.agileai.hotweb.common.Constants.FrameHandlers;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.ListUtil;
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
			String serverName = this.dispatchServlet.getServletContext().getInitParameter("serverName");
			String redirectUrl = casServerLogoutUrl + "?url="+ serverName+this.dispatchServlet.getServletContext().getContextPath();
			return new RedirectRenderer(redirectUrl);
		}else{
			return new RedirectRenderer(getHandlerURL(FrameHandlers.LoginHandlerId)+"&actionType=logout");			
		}
	}
	
	@SuppressWarnings("unchecked")
	public ViewRenderer doControlPageAction(DataParam param){
		String rspText = "";
		User user = (User)this.getUser();
		HotwebAuthHelper menuHelper = new HotwebAuthHelper(user);
		String funcId = menuHelper.getCurrentFuncId();
		String handlerId = param.get("handlerId");
		String buttonIds = param.get("buttonIds");
		String controlType = param.get("controlType");
		
		if (!StringUtil.isNullOrEmpty(funcId) && !StringUtil.isNullOrEmpty(handlerId)
				&& !StringUtil.isNullOrEmpty(buttonIds) && !StringUtil.isNullOrEmpty(controlType)){
			
			String[] buttonIdArray = buttonIds.split(",");
			List<String> buttonIdList = ListUtil.arrayToList(buttonIdArray); 
			FuncMenu function = menuHelper.getFunction(funcId);
			if (function != null){
				List<FuncHandler> funcHandlers = function.getHandlers();
				FuncHandler funcHandler = this.getHandler(funcHandlers, handlerId);
				if (funcHandler != null){
					List<Operation> operationList = funcHandler.getOperations();
					if (!operationList.isEmpty()){
						StringBuffer script = new StringBuffer();
						for (int i=0;i < operationList.size();i++){
							Operation operation = operationList.get(i);
							String operCode = operation.getOperCode();
							String operationId = operation.getOperId();
							if (buttonIdList.contains(operCode)){
								if (!user.isAdmin() && !user.containResouce(Resource.Type.Operation, operationId)){
									script.append("pms.controlPageAction('").append(operCode).append("','").append(controlType).append("');");
									script.append("\r\n");						
								}
							}
						}
						rspText = script.toString();
					}	
				}
			}
		}
		return new AjaxRenderer(rspText);
	}
	
	private FuncHandler getHandler(List<FuncHandler> funcHandlers,String handlerCode){
		FuncHandler result = null;
		for (int i=0;i < funcHandlers.size();i++){
			FuncHandler funcHandler = (FuncHandler)funcHandlers.get(i);
			if (handlerCode.equals(funcHandler.getHandlerCode())){
				result = funcHandler;
				break;
			}
		}
		return result;
	}
}
