package com.agileai.hr.module.attendance.service;

import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.hr.cxmodule.HrAttendanceManage;

public class HrAttendanceManageImpl
        extends StandardServiceImpl
        implements HrAttendanceManage {
    public HrAttendanceManageImpl() {
        super();
    }

	@Override
	public DataRow retrieveUserInfo(DataParam param) {
		String statementId = "SecurityUserQuery.retrieveUserInfo";
		DataRow row = this.daoHelper.getRecord(statementId, param);
		return row;
	}

	@Override
	public void bindUserWxOpenId(String userCode, String wxOpenId) {
		DataParam param = new DataParam();
		String statementId = "SecurityUserQuery.updateUserInfo";
		param.put("openId",wxOpenId,"userCode",userCode);
		this.daoHelper.updateRecord(statementId, param);
	}

	@Override
	public List<DataRow> attendanceStatisticsRecords(DataParam param) {
		String statementId = sqlNameSpace+"."+"attendanceStatisticsRecords";
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}

	@Override
	public void createLocationRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"createLocationRecord";
		param.put("LOCAT_ID", KeyGenerator.instance().genKey());
		this.daoHelper.insertRecord(statementId, param);
	}
}
