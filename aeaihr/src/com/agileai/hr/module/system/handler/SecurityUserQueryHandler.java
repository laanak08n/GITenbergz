package com.agileai.hr.module.system.handler;

import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.PickFillModelHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityUserGRManage;
import com.agileai.util.StringUtil;


public class SecurityUserQueryHandler
        extends PickFillModelHandler {
    public SecurityUserQueryHandler() {
        super();
        this.serviceId = buildServiceId(SecurityUserGRManage.class);
    }

    protected void processPageAttributes(DataParam param) {
    	 initMappingItem("USER_STATE",
                 FormSelectFactory.create("SYS_VALID_TYPE")
                                  .getContent());
    	
    }

    protected void initParameters(DataParam param) {
    }
    
    public ViewRenderer prepareDisplay(DataParam param){
    	SecurityUserGRManage securityUserGRManage=(SecurityUserGRManage) this.lookupService(this.getServiceId());
		mergeParam(param);
		initParameters(param);
		this.setAttributes(param);
		List<DataRow> rsList = securityUserGRManage.queryPickFillRecords(param);
		this.setRsList(rsList);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}	
    
    public ViewRenderer doSaveUserAction(DataParam param){
    	SecurityUserGRManage securityUserGRManage=(SecurityUserGRManage) this.lookupService(this.getServiceId());
    	String rspText = SUCCESS;
    	String userIds = param.get("USER_ID");
    	String rgId = param.getString("RG_ID");
        if(StringUtil.isNullOrEmpty(userIds)){
 		   userIds = param.get("targetValue");
        }
    	String[] userIdArray = userIds.split(",");
    	for(int i=0;i<userIdArray.length;i++){
    		String userId = userIdArray[i];
    		String urgId = KeyGenerator.instance().genKey();
    		securityUserGRManage.createURGMContentRecord(urgId,userId,rgId);
    	}	
    	
    	return new AjaxRenderer(rspText);
    	
    }
  
}
