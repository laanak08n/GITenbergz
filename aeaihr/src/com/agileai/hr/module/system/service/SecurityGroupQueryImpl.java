package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.QueryModelServiceImpl;

public class SecurityGroupQueryImpl
        extends QueryModelServiceImpl
        implements SecurityGroupQuery {
	protected String queryChildRecords = "queryChildRecords";
	protected String queryPickTreeRecords = "queryPickTreeRecords";
	protected String queryTreeorgNameRecords = "queryTreeorgNameRecords";
	protected String queryTreeorgTypeRecords = "queryTreeorgTypeRecords";	
    public SecurityGroupQueryImpl() {
        super();
    }

	@Override
	public void addGroupTreeRelation(DataParam param) {
		String statementId = this.sqlNameSpace+".addGroupTreeRelation";
		this.daoHelper.insertRecord(statementId, param);
	}

	@Override
	public void delGroupTreeRelation(String roleId, String groupId) {
		String statementId = this.sqlNameSpace+".delGroupTreeRelation";
		DataParam param = new DataParam("roleId",roleId,"groupId",groupId);
		this.daoHelper.deleteRecords(statementId, param);
	}
	@Override
	public List<DataRow> queryTreeorgNameRecords(String groupId) {
		String statementId = sqlNameSpace+"."+queryTreeorgNameRecords;
		List<DataRow> result = this.daoHelper.queryRecords(statementId, groupId);
		return result;
	}

	@Override
	public List<DataRow> queryTreeorgTypeRecords(String roleId) {
		String statementId = sqlNameSpace+"."+queryTreeorgTypeRecords;
		List<DataRow> result = this.daoHelper.queryRecords(statementId, roleId);
		return result;
	}

	@Override
	public List<DataRow> queryChildRecords(String rolId) {
		String statementId = sqlNameSpace+"."+queryChildRecords;
		List<DataRow> result = this.daoHelper.queryRecords(statementId, rolId);
		return result;
	}

	@Override
	public List<DataRow> queryPickTreeRecords(DataParam param) {
		String statementId = sqlNameSpace+"."+queryPickTreeRecords;
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}
}
