package com.agileai.hr.controller.system;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hr.bizmoduler.system.SecurityGroupManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.CryptionUtil;

public class SecurityUserEditHandler
        extends TreeAndContentManageEditHandler {
    public SecurityUserEditHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupManage.class);
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

    protected SecurityGroupManage getService() {
        return (SecurityGroupManage) this.lookupService(this.getServiceId());
    }
    
	public ViewRenderer doSaveAction(DataParam param){
		String rspText = SUCCESS;
		TreeAndContentManage service = this.getService();
		String operateType = param.get(OperaType.KEY);
		
		if (OperaType.CREATE.equals(operateType)){
			String colIdField = service.getTabIdAndColFieldMapping().get(this.tabId);
			String USER_PWD = param.get("USER_PWD");
			param.put("USER_PWD",CryptionUtil.md5Hex(USER_PWD));
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
