package com.agileai.hr.cxmodule;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;

public interface HrAttendanceManage
        extends StandardService {
	public DataRow retrieveUserInfo(DataParam param);
	public void bindUserWxOpenId(String userCode,String wxOpenId);
	public List<DataRow> attendanceStatisticsRecords (DataParam param);
	public List<DataRow> getAttendanceStatInfo(String date);
	public List<DataRow> getAttendContentData(String code, String date);
	public List<DataRow> getLeaveContentData(String code, String date);
	public List<DataRow> getOverworkContentData(String code, String date);
	public void createLocationRecord(DataParam param);
	public List<DataRow> findCurrentDaySigninInfos (String adtDate);
	public List<DataRow> findUserInfos();
	public List<DataRow> findSignLocationInfos(String userId);
	public List<DataRow> findCurrentDaySignOutInfos(String expression,String adtDate);
	DataRow findActiveUserId(String userCode);
}
