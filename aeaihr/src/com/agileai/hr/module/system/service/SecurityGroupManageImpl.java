package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
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
	
	@Override
	public void deleteTreeRecords(String currentId) {
		String statementId = sqlNameSpace+"."+"deleteTreeRecords";
		this.daoHelper.deleteRecords(statementId, currentId);
		
	}

	@Override
	public void createTreeRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"insertTreeRecord";
		processDataType(param, tableName);
		String parentId = param.get(columnParentIdField);
		String newSort = String.valueOf(this.retrieveNewMaxSort(parentId));
		param.put(columnSortField,newSort);
		this.daoHelper.insertRecord(statementId, param);
	}

	@Override
	public List<DataRow> queryTreeorgNameRecords(String currentId) {
		String statementId = this.sqlNameSpace+"."+"queryTreeorgNameRecords";
		return this.daoHelper.queryRecords(statementId, currentId);
	}

	@Override
	public List<DataRow> queryTreeTypeRecords(DataParam param) {
		String statementId = this.sqlNameSpace+"."+"queryTreeorgNameRecords";
		return this.daoHelper.queryRecords(statementId, param);
	}
	@Override
	public void delGroupTreeRelation(DataParam param) {
		String statementId = sqlNameSpace+"."+"delGroupTreeRelation";
		this.daoHelper.deleteRecords(statementId, param);
		
	}
	
	@Override
	public List<DataRow> findRoleGroupAuthRecords(String grpId,String roleId){
		DataParam param = new DataParam();
		param.put("grpId",grpId);
		param.put("roleId",roleId);
		String statementId = sqlNameSpace+"."+"findRoleGroupAuthRecords";
		return this.daoHelper.queryRecords(statementId, param);
	}
}
