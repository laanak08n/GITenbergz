package com.agileai.hr.module.system.handler;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataMap;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;
import com.agileai.hotweb.controller.core.TreeAndContentManageListHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.module.system.service.SecurityUserGRManage;


public class SecurityUserGRManageListHandler 
			extends TreeAndContentManageListHandler{
    public SecurityUserGRManageListHandler() {
        super();
        this.serviceId = buildServiceId(SecurityUserGRManage.class);
        this.rootColumnId = "00000000-0000-0000-00000000000000000";
        this.defaultTabId = "Position";
        this.columnIdField = "GRP_ID";
        this.columnNameField = "GRP_NAME";
        this.columnParentIdField = "GRP_PID";
        this.columnSortField = "GRP_SORT";
    }

    protected void processPageAttributes(DataParam param) {
    	
    }

    protected void initParameters(DataParam param) {

    }
    
    public ViewRenderer prepareDisplay(DataParam param){
		initParameters(param);
		processPageAttributes(param);
		this.setAttributes(param);
		String columnId = param.get("columnId",this.rootColumnId);
		this.setAttribute("columnId", columnId);
		this.setAttribute("isRootColumnId",String.valueOf(this.rootColumnId.equals(columnId)));
		param.put("RG_ID",columnId);
		String tabId = param.get(TreeAndContentManage.TAB_ID,this.defaultTabId);
		
		this.setAttribute(TreeAndContentManage.TAB_ID, tabId);
		this.setAttribute(TreeAndContentManage.TAB_INDEX, getTabIndex(tabId));
		return new LocalRenderer(getPage());
	}
    
    @PageAction
	public ViewRenderer tableJson(DataParam param) {
    	String responseText = "";
    	try {
			String columnId = param.get("columnId", this.rootColumnId);
			param.put("columnId",columnId);
			JSONObject jsonObject = new JSONObject();
			List<DataRow> arrayList = new ArrayList<DataRow>();
			DataMap userSex = FormSelectFactory.create("USER_SEX").getContent();
			DataMap sysValidType = FormSelectFactory.create("SYS_VALID_TYPE").getContent();
			SecurityUserGRManage service = this.getService();
			List<DataRow> rsList=service.doQueryEmpAction(param);
			for (int i = 0; i < rsList.size(); i++) {
				DataRow rowNew = new DataRow();
				DataRow row = rsList.get(i);
				rowNew.put("ID", i+1);
				rowNew.put("USER_ID", row.get("USER_ID"));
				rowNew.put("USER_CODE", row.get("USER_CODE"));
				rowNew.put("USER_NAME", row.get("USER_NAME"));
				rowNew.put("USER_SEX", userSex.get(row.get("USER_SEX")));
				rowNew.put("USER_STATE", sysValidType.get(row.get("USER_STATE")));
				rowNew.put("GRP_ID", row.get("GRP_ID"));
				rowNew.put("USER_PHONE", row.get("USER_PHONE"));
				arrayList.add(rowNew);
			}
			jsonObject.put("arrayList", arrayList);
			JSONArray jsonArray = (JSONArray) jsonObject.get("arrayList");
			responseText = jsonArray.toString();
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
    	return new AjaxRenderer(responseText);
    }
    
    public ViewRenderer doQueryEmpAction(DataParam param){

    	 return new LocalRenderer(getPage());
	}
    
    protected TreeBuilder provideTreeBuilder(DataParam param) {
    	SecurityUserGRManage service = this.getService();
        List<DataRow> menuRecords = service.findTreeRecords(new DataParam());
        TreeBuilder treeBuilder = new TreeBuilder(menuRecords,
                                                  this.columnIdField,
                                                  this.columnNameField,
                                                  this.columnParentIdField);

        return treeBuilder;
    }
    
    public ViewRenderer doDeletePosempAction(DataParam param){
    	SecurityUserGRManage service = this.getService();
    	DataRow row=service.queryURGRelation(param);
    	String URG_ID=row.getString("URG_ID");
    	service.deletTureContentRecord(URG_ID);
		return prepareDisplay(param);
	}

    protected List<String> getTabList() {
        List<String> result = new ArrayList<String>();
        result.add(SecurityUserGRManage.BASE_TAB_ID);
        result.add("Position");
        return result;
    }

    protected SecurityUserGRManage getService() {
        return (SecurityUserGRManage) this.lookupService(this.getServiceId());
    }
}
