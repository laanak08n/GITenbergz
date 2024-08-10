package com.agileai.hr.controller;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hr.bizmoduler.system.HotwebAuthHelper;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.StringUtil;

public class MenuTreeHandler extends BaseHandler{
	public static final String FUNC_ID_TAG = "funcId";
	
	public MenuTreeHandler(){
		super();
	}
	
	public ViewRenderer prepareDisplay(DataParam param){
		User user = (User)this.getUser();
		HotwebAuthHelper menuHelper = new HotwebAuthHelper(user);
		String menuTreeSyntax = menuHelper.getTreeSyntax();
		this.setAttribute("menuTreeSyntax", menuTreeSyntax);
		return new LocalRenderer(getPage());
	}

	@PageAction
	public ViewRenderer showFunction(DataParam param){
		String funcId = param.get(FUNC_ID_TAG);
		User user = (User)getUser();
		HotwebAuthHelper userHelper = new HotwebAuthHelper(user);
		userHelper.setCurrentFuncId(funcId);
		FuncMenu function = userHelper.getFunction(funcId);
		
		String funcURL = function.getFuncUrl();
		String handlerURL = funcURL;				
		
		this.getSessionAttributes().remove(PARAM_TRACE);
		StringBuffer responseText = new StringBuffer();
		StringBuffer script = new StringBuffer();
		script.append("parent.mainFrame.location.href='").append(handlerURL).append("';");
		String currentVisitPath = this.buildCurrentPath(function);
		script.append("parent.topright.document.getElementById('currentPath').innerHTML=\"").append(currentVisitPath).append("\";");
		responseText.append(script);
		return new AjaxRenderer(responseText.toString());
	}
	
	private String buildCurrentPath(FuncMenu function){
		String result = null;
		StringBuffer path = new StringBuffer();
		
		String funcURL = function.getFuncUrl();
		String funcName = function.getFuncName();
		path.append("<a href=javascript:void(parent.mainFrame.location.href='").append(funcURL).append("')>").append(funcName).append("</a>");
		
		String funcParentId = function.getFuncPid();
		if (!StringUtil.isNullOrEmpty(funcParentId)){
			User user = (User)getUser();
			HotwebAuthHelper userHelper = new HotwebAuthHelper(user);
			FuncMenu parent = userHelper.getFunction(funcParentId);
			if (parent != null){
				this.uploopMenuItem(path,parent);
			}			
		}
		result = path.toString();
		return result;
	}
	private void uploopMenuItem(StringBuffer path,FuncMenu function ){
		StringBuffer temp = new StringBuffer();
		if (function.isFolder()){
			if (StringUtil.isNullOrEmpty(function.getFuncPid())){
				String funcURL = "index?MainWin";
				temp.append("<a href=javascript:showPage('").append(funcURL).append("')>").append(function.getFuncName()).append("</a>&nbsp;--> &nbsp;");	
			}else{
				temp.append(function.getFuncName()).append("&nbsp;-->&nbsp;");				
			}
		}else{
			temp.append("<a href=javascript:showPage('").append(function.getFuncUrl()).append("')>").append(function.getFuncName()).append("</a> &nbsp; --> &nbsp;");			
		}
		path.insert(0, temp.toString());
		String funcParentId = function.getFuncPid();
		if (!StringUtil.isNullOrEmpty(funcParentId)){
			User user = (User)getUser();
			HotwebAuthHelper userHelper = new HotwebAuthHelper(user);
			FuncMenu parent = userHelper.getFunction(funcParentId);
			this.uploopMenuItem(path,parent);
		}
	}
}
