package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;
import com.agileai.hotweb.controller.core.MasterSubEditMainHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.cxmodule.FunctionTreeManage;
import com.agileai.hr.module.system.service.HandlerManage;

public class HandlerManageEditHandler
        extends MasterSubEditMainHandler {
    public HandlerManageEditHandler() {
        super();
        this.listHandlerClass = HandlerManageListHandler.class;
        this.serviceId = buildServiceId(HandlerManage.class);
        this.baseTablePK = "HANLER_ID";
        this.defaultTabId = "SysOperation";
    }
    
	public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		if (isReqRecordOperaType(operaType)){
			DataRow record = getService().getMasterRecord(param);
			this.setAttributes(record);			
		}
		String currentSubTableId = param.get("currentSubTableId",defaultTabId);
		if (!currentSubTableId.equals(MasterSubService.BASE_TABLE_ID)){
			String subRecordsKey = currentSubTableId + "Records";
			if (!this.getAttributesContainer().containsKey(subRecordsKey)){
				List<DataRow> subRecords = getService().findSubRecords(currentSubTableId, param);
				this.setAttribute(currentSubTableId+"Records", subRecords);
			}
		}
		this.setAttribute("FUNC_ID", param.get("funcId"));
		this.setAttribute("currentSubTableId", currentSubTableId);
		this.setAttribute("currentSubTableIndex", getTabIndex(currentSubTableId));
		String operateType = param.get(OperaType.KEY);
		this.setOperaType(operateType);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

    protected void processPageAttributes(DataParam param) {
        setAttribute("HANLER_TYPE",
                     FormSelectFactory.create("HANDLER_TYPE")
                                      .addSelectedValue(getOperaAttributeValue("HANLER_TYPE",
                                                                               "MAIN")));
    }
    public ViewRenderer doSaveMasterRecordAction(DataParam param){
    	this.lookupService(FunctionTreeManage.class).clearFuncTreeCache();
    	return super.doSaveMasterRecordAction(param);
    }
    public ViewRenderer doMoveUpAction(DataParam param){
    	this.lookupService(FunctionTreeManage.class).clearFuncTreeCache();
    	return super.doMoveUpAction(param);
    }
    public ViewRenderer doMoveDownAction(DataParam param){
    	this.lookupService(FunctionTreeManage.class).clearFuncTreeCache();
    	return super.doMoveDownAction(param);
    }
    public ViewRenderer doSaveEntryRecordsAction(DataParam param){
    	this.lookupService(FunctionTreeManage.class).clearFuncTreeCache();
    	return super.doSaveEntryRecordsAction(param);
    }
    public ViewRenderer doDeleteEntryRecordAction(DataParam param){
    	this.lookupService(FunctionTreeManage.class).clearFuncTreeCache();
    	return super.doDeleteEntryRecordAction(param);
    }
    protected String[] getEntryEditFields(String currentSubTableId) {
        List<String> temp = new ArrayList<String>();

        if ("SysOperation".equals(currentSubTableId)) {
            temp.add("OPER_ID");
            temp.add("HANLER_ID");
            temp.add("OPER_CODE");
            temp.add("OPER_ACTIONTPYE");
            temp.add("OPER_NAME");
            temp.add("OPER_SORT");
        }

        return temp.toArray(new String[] {  });
    }

    protected String getEntryEditTablePK(String currentSubTableId) {
        HashMap<String, String> primaryKeys = new HashMap<String, String>();
        primaryKeys.put("SysOperation", "OPER_ID");

        return primaryKeys.get(currentSubTableId);
    }

    protected String getEntryEditForeignKey(String currentSubTableId) {
        HashMap<String, String> foreignKeys = new HashMap<String, String>();
        foreignKeys.put("SysOperation", "HANLER_ID");

        return foreignKeys.get(currentSubTableId);
    }

    protected HandlerManage getService() {
        return (HandlerManage) this.lookupService(this.getServiceId());
    }
}
