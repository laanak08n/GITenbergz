package com.agileai.hr.module.leave.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.hr.module.leave.service.HrLeaveManage;

public class HrLeaveManageImpl
        extends StandardServiceImpl
        implements HrLeaveManage {
    public HrLeaveManageImpl() {
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
}
