package com.agileai.hr.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.SystemLogService;
import com.agileai.hr.bizmoduler.system.HotwebAuthHelper;
import com.agileai.hr.bizmoduler.system.SecurityAuthorizationConfig;
import com.agileai.hotweb.common.Constants;
import com.agileai.hotweb.common.Constants.FrameHandlers;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.core.Profile;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;
import com.agileai.util.MapUtil;

public class LoginHandler  extends BaseHandler{
	public LoginHandler(){
		super();
	}
	
	public ViewRenderer prepareDisplay(DataParam param) {
		if (getProfile() != null){
			return new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.HomepageHandlerId));
		}else{
			return new LocalRenderer(getPage());			
		}
	}

	protected void writeSystemLog(String content, String actionType) {
		String ipAddress = request.getRemoteAddr();
		User user = (User) this.getUser();
		String userId = user.getUserCode();
		String userName = user.getUserName();
		SystemLogService logService = (SystemLogService) this.lookupService("sysLogService");
		logService.insertLogRecord(ipAddress, userId, userName, content,actionType);
	}	
	
	public ViewRenderer doLoginAction(DataParam param){
		ViewRenderer result = null;
		String userId = param.get("userId");
		String userPwd = param.get("userPwd");
		
		SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)this.lookupService("securityAuthorizationConfigService");
		DataRow userRow = authorizationConfig.retrieveUserRecord(userId);
		if (MapUtil.isNullOrEmpty(userRow)){
			this.setErrorMsg("该用户不存在!");
			result = prepareDisplay(param);
		}else{
			String valideCode = param.get("valideCode");
			String safecode = (String)this.request.getSession().getAttribute("safecode");
			if (!valideCode.equals(safecode)){
				this.setErrorMsg("验证码不正确，请检查!");
				result = prepareDisplay(param);
			}
			else{
				String userPwdTemp = String.valueOf(userRow.get("USER_PWD"));
				String encryptedPassword = CryptionUtil.md5Hex(userPwd);
				if (userPwdTemp.equals(encryptedPassword)){
					User user = new User();
					String fromIpAddress = request.getLocalAddr();
					Profile profile = new Profile(userId,fromIpAddress,user);
					request.getSession().setAttribute(Profile.PROFILE_KEY, profile);
					HotwebAuthHelper hotwebAuthHelper = new HotwebAuthHelper(user);
					hotwebAuthHelper.initAuthedUser(userRow,user);
					writeSystemLog("系统登陆",getActionType());
					result = new RedirectRenderer(getHandlerURL(Constants.FrameHandlers.HomepageHandlerId));
				}
				else{
					this.setErrorMsg("用户ID或者密码不正确!");
					result = prepareDisplay(param);
				}
			}
		}
		return result;
	}
	protected Profile getProfile(HttpServletRequest request){
		Profile profile = null;
		HttpSession session = request.getSession(false);
		if (session != null){
			profile = (Profile)session.getAttribute(Profile.PROFILE_KEY);	
		}
		return profile;
	}
	public ViewRenderer doLogoutAction(DataParam param){
		writeSystemLog("退出系统",getActionType());
		this.clearSession();
		return new RedirectRenderer(getHandlerURL(FrameHandlers.LoginHandlerId));
	}	
}
