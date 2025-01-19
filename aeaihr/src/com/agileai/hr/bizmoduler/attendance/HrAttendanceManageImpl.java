package com.agileai.hr.bizmoduler.attendance;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;

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
}
