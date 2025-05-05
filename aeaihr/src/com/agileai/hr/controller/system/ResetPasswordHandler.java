package com.agileai.hr.controller.system;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.frame.SecurityAuthorizationConfig;
import com.agileai.hotweb.controller.core.SimpleHandler;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.system.SecurityGroupManage;
import com.agileai.util.CryptionUtil;

public class ResetPasswordHandler extends SimpleHandler{
	public ResetPasswordHandler(){
		super();
	}
	public ViewRenderer prepareDisplay(DataParam param) {
		String userId = param.get("USER_ID");
		DataParam queryParam = new DataParam("USER_ID",userId);
		SecurityGroupManage groupManage = this.lookupService(SecurityGroupManage.class);
		DataRow row = groupManage.getContentRecord("SecurityUser", queryParam);
		String userCode = row.stringValue("USER_CODE");
		this.setAttribute("userCode",userCode);
		return new LocalRenderer(getPage());
	}
	
	@PageAction
	public ViewRenderer resetPassword(DataParam param){
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
