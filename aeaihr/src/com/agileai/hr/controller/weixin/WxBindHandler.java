package com.agileai.hr.controller.weixin;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.controller.core.SimpleHandler;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.attendance.HrAttendanceManage;
import com.agileai.hr.bizmoduler.system.SecurityAuthorizationConfig;
import com.agileai.util.CryptionUtil;
import com.agileai.util.MapUtil;

public class WxBindHandler extends SimpleHandler{
	public WxBindHandler(){
		super();
	}
	public ViewRenderer prepareDisplay(DataParam param) {
		return new LocalRenderer(getPage());
	}
	
	@PageAction
	public ViewRenderer bindWxUser(DataParam param){
		String responseText = FAIL;
		String userId = param.get("userId");
		String userPwd = param.get("userPwd");
		
		String valideCode = param.get("valideCode");
		String safecode = (String)this.request.getSession().getAttribute("safecode");
		if (!valideCode.equals(safecode)){
			responseText = "验证码不正确，请检查！";
		}else{
			SecurityAuthorizationConfig authorizationConfig = (SecurityAuthorizationConfig)this.lookupService("securityAuthorizationConfigService");
			DataRow userRow = authorizationConfig.retrieveUserRecord(userId);
			if (MapUtil.isNullOrEmpty(userRow)){
				responseText = "该用户不存在！";
			}else{
				String userPwdTemp = String.valueOf(userRow.get("USER_PWD"));
				String encryptedPassword = CryptionUtil.md5Hex(userPwd);
				if (userPwdTemp.equals(encryptedPassword)){
					HrAttendanceManage attendanceManage = this.lookupService(HrAttendanceManage.class);
					String wxOpenId = (String)this.getSessionAttribute("openId");
					attendanceManage.bindUserWxOpenId(userId, wxOpenId);
					responseText = SUCCESS;
				}
				else{
					responseText = "用户ID或者密码不正确！";
				}
			}			
		}
		return new AjaxRenderer(responseText);
	}
	
	@PageAction
	public ViewRenderer signIn(DataParam param){
		return new RedirectRenderer(this.getHandlerURL(WxSignInHandler.class));
	}
}