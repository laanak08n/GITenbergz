package com.agileai.hr.controller.system;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.SimpleHandler;
import com.agileai.hotweb.domain.core.Profile;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;

public class ModifyPasswordHandler extends SimpleHandler{
	public ModifyPasswordHandler(){
		super();
	}
	public ViewRenderer prepareDisplay(DataParam param) {
		Profile profile = this.getProfile();
		User user = (User)profile.getUser();
		String userCode = user.getUserCode();
		this.setAttribute("userCode",userCode);
		return new LocalRenderer(getPage());
	}
	
	@PageAction
	public ViewRenderer confirmOldPwd(DataParam param){
		String responseText = FAIL;
		String userCode = param.get("userCode");
		SecurityAuthorizationConfig authorizationConfig = this.lookupService(SecurityAuthorizationConfig.class);
		DataRow row = authorizationConfig.retriveUserInfoRecord(userCode);
		String userOldPwdInDb = row.stringValue("USER_PWD");
		String oldUserPwd = param.get("oldUserPwd");
		String encryptPwd = CryptionUtil.md5Hex(oldUserPwd);
		if (encryptPwd.equals(userOldPwdInDb)){
			responseText = SUCCESS;
		}
		return new AjaxRenderer(responseText);
	}
	
	@PageAction
	public ViewRenderer modifyPassword(DataParam param){
		String responseText = FAIL;
		String userCode = param.get("userCode");
		SecurityAuthorizationConfig authorizationConfig = this.lookupService(SecurityAuthorizationConfig.class);
		String newUserPwd = param.get("newUserPwd");
		String encryptPwd = CryptionUtil.md5Hex(newUserPwd);
		authorizationConfig.modifyUserPassword(userCode, encryptPwd);
		responseText = SUCCESS;
		return new AjaxRenderer(responseText);
	}
}
