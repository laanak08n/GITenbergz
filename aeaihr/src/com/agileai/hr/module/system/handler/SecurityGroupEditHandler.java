package com.agileai.hr.module.system.handler;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.hotweb.controller.core.TreeAndContentColumnEditHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityGroupManage;

public class SecurityGroupEditHandler
        extends TreeAndContentColumnEditHandler {
    public SecurityGroupEditHandler() {
        super();
        this.serviceId = buildServiceId(SecurityGroupManage.class);
        this.columnIdField = "GRP_ID";
        this.columnParentIdField = "GRP_PID";
    }

    protected void processPageAttributes(DataParam param) {
        setAttribute("GRP_STATE",
                     FormSelectFactory.create("SYS_VALID_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("GRP_STATE",
                                                                               "1")));
        setAttribute("GRP_RANK",        
                FormSelectFactory.create("ORGNAZITION_RANK")
                                 .addSelectedValue(getOperaAttributeValue("GRP_RANK",
                                                                          "1")));
        setAttribute("GRP_TYPE",
                FormSelectFactory.create("ORGNAZITION_TYPE")
                                 .addSelectedValue(getOperaAttributeValue("GRP_TYPE",
                                                                "department")));
    }
    
    
    @Override
	public ViewRenderer doSaveAction(DataParam param){
		String operateType = param.get(OperaType.KEY);
		String GRP_ID=KeyGenerator.instance().genKey();
		if (OperaType.CREATE.equals(operateType)){
			param.put("GRP_ID",GRP_ID);
			getService().createTreeRecord(param);	
		}
		else if (OperaType.UPDATE.equals(operateType)){
			getService().updateTreeRecord(param);	
		}
		return new AjaxRenderer(SUCCESS);
	}
	
 

    protected SecurityGroupManage getService() {
        return (SecurityGroupManage) this.lookupService(this.getServiceId());
    }
}
