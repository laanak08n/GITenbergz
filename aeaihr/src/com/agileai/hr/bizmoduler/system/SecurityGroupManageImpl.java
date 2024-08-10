package com.agileai.hr.bizmoduler.system;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManageImpl;

public class SecurityGroupManageImpl
        extends TreeAndContentManageImpl
        implements SecurityGroupManage {
    public SecurityGroupManageImpl() {
        super();
        this.columnIdField = "GRP_ID";
        this.columnParentIdField = "GRP_PID";
        this.columnSortField = "GRP_SORT";
    }
    public void deletContentRecord(String tabId,DataParam param){
    	super.deletContentRecord(tabId, param);
    	if ("SecurityUser".equals(tabId)) {
        	String statementId = "SecurityAuthorizationConfig.delUserAuthRelation";
        	String userId = param.get("USER_ID");
        	DataParam deleteParam = new DataParam("USER_ID",userId);
        	this.daoHelper.deleteRecords(statementId, deleteParam);
    	}
    }
    
	public void deleteTreeRecord(String currentId) {
		super.deleteTreeRecord(currentId);
		String statementId = "SecurityAuthorizationConfig.delGroupAuthRelation";
    	DataParam deleteParam = new DataParam("GRP_ID",currentId);
    	this.daoHelper.deleteRecords(statementId, deleteParam);
	}
}
