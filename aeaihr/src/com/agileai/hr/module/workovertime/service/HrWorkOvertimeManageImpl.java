package com.agileai.hr.module.workovertime.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;

public class HrWorkOvertimeManageImpl extends StandardServiceImpl implements
		HrWorkOvertimeManage {
	public HrWorkOvertimeManageImpl() {
		super();
	}

	public void updateRecord(DataParam param) {
		String statementId = sqlNameSpace + "." + "updateRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);

	}

	public DataRow getNowRecord(DataParam param) {
		String statementId = sqlNameSpace + "." + "getNowRecord";
		DataRow result = this.daoHelper.getRecord(statementId, param);
		return result;
	}
	public void approveRecord(DataParam param) {
		String statementId = sqlNameSpace + "." + "approveRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);
	}

	@Override
	public List<DataRow> findOvertimeList(DataParam param) {
		String statementId = sqlNameSpace+"."+"findOvertimeList";
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}

	@Override
	public void submitRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"submitRecord";
		this.daoHelper.updateRecord(statementId, param);
	}
}
