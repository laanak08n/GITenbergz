package com.agileai.hr.module.system.handler;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityUserManage;
import com.agileai.util.CryptionUtil;

public class SecurityUserEditHandler
        extends TreeAndContentManageEditHandler {
    public SecurityUserEditHandler() {
        super();
        this.serviceId = buildServiceId(SecurityUserManage.class);
        this.tabId = "SecurityUser";
        this.columnIdField = "GRP_ID";
        this.contentIdField = "USER_ID";
    }

    protected void processPageAttributes(DataParam param) {
        setAttribute("USER_SEX",
                     FormSelectFactory.create("USER_SEX")
                                      .addSelectedValue(getOperaAttributeValue("USER_SEX",
                                                                               "M")));
        setAttribute("USER_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("USER_STATE",
                                                                               "1")));
    }

    protected SecurityUserManage getService() {
        return (SecurityUserManage) this.lookupService(this.getServiceId());
    }
    
    @Override
	public ViewRenderer doSaveAction(DataParam param){
		String rspText = SUCCESS;
		
		TreeAndContentManage service = this.getService();
		String operateType = param.get(OperaType.KEY);
		String USER_ID=KeyGenerator.instance().genKey();
		String GU_ID=KeyGenerator.instance().genKey();
		String USER_NAME=param.get("USER_NAME");
		String USER_CODE=param.get("USER_CODE");
		
		if (OperaType.CREATE.equals(operateType)){
			String colIdField = service.getTabIdAndColFieldMapping().get(this.tabId);
			String USER_PWD = param.get("USER_PWD");
			param.put("USER_PWD",CryptionUtil.md5Hex(USER_PWD));
			param.put("USER_ID",USER_ID);
			param.put("USER_NAME",USER_NAME);
			param.put("USER_CODE",USER_CODE);
			param.put("GU_ID",GU_ID);
			String columnId = param.get(this.columnIdField);
			param.put(colIdField,columnId);
			getService().createtContentRecord(tabId,param);	
		}
		else if (OperaType.UPDATE.equals(operateType)){
			getService().updatetContentRecord(tabId,param);	
		}
		
		return new AjaxRenderer(rspText);
	}
}
