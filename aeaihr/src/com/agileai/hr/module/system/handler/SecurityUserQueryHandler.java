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
    	String USER_IDS=param.get("USER_ID");
    	String RG_ID=param.getString("RG_ID");
    	String[]USER=USER_IDS.split(",");
    	for(int i=0;i<USER.length;i++){
    		String USER_ID=USER[i];
    		String URG_ID=KeyGenerator.instance().genKey();
    		DataParam NParam=new DataParam();
    		NParam.put("URG_ID",URG_ID);
    		NParam.put("RG_ID",RG_ID);
    		NParam.put("USER_ID",USER_ID);
    		securityUserGRManage.createtURGMContentRecord(NParam);
    	}	
    	
    	return new AjaxRenderer(rspText);
    	
    }
  
}
