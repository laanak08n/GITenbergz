package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManageImpl;

public class SecurityUserGRManageImpl 
		extends TreeAndContentManageImpl
		implements SecurityUserGRManage {
	public SecurityUserGRManageImpl() {
		super();
		this.columnIdField = "GRP_ID";
		this.columnParentIdField = "GRP_ID";
		this.columnSortField = "GRP_ID";
	}

	@Override
	public List<DataRow> doQueryEmpAction(DataParam param) {
		String statementId = sqlNameSpace+"."+"queryEmpRecords";
		processDataType(param, tableName);
		List<DataRow> row=this.daoHelper.queryRecords(statementId, param);
		return row;
	}

	@Override
	public List<DataRow> queryPickFillRecords(DataParam param) {
		String statementId = sqlNameSpace+"."+"queryPickFillRecords";
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;

	}

	@Override
	public void  createtURGMContentRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"insertURGMRecords";
		this.daoHelper.insertRecord(statementId, param);	
	}

	@Override
	public void deletPOSEMPContentRecord(String EOPR_ID) {
		String statementId = sqlNameSpace+"."+"deletePOSEMPRelation";
		DataParam Nparam=new DataParam();
		Nparam.put("EOPR_ID",EOPR_ID);
		this.daoHelper.deleteRecords(statementId, Nparam);
	}

	@Override
	public DataRow queryURGRelation(DataParam param) {
		String statementId = sqlNameSpace+"."+"queryURGRelation";
		DataRow result = this.daoHelper.getRecord(statementId, param);
		return result;
	}

	@Override
	public void deletTureContentRecord(String urgId) {
		String statementId = sqlNameSpace+"."+"deleteTrueRelation";
		DataParam Nparam=new DataParam();
		Nparam.put("URG_ID",urgId);
		this.daoHelper.deleteRecords(statementId, Nparam);
	}
	
	@Override
	public List<DataRow> findTreeRecords(DataParam param) {
		String statementId = this.sqlNameSpace+"."+"queryTreeRecords";
		return this.daoHelper.queryRecords(statementId, param);
	}

}
