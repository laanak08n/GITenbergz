package com.agileai.hr.module.attendance.service;

import java.util.Date;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.hr.cxmodule.HrAttendanceManage;
import com.agileai.util.DateUtil;

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
	public List<DataRow> getAttendanceStatInfo() {
		String statementId = sqlNameSpace+"."+"getAttendanceStatInfoRecords";
		String startDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getBeginOfMonth(new Date()));
		String endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, new Date());
		DataParam param = new DataParam("startDate",startDate,"endDate",endDate);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}

	@Override
	public List<DataRow> getAttendContentData(String code, String date) {
		String statementId = sqlNameSpace+"."+"getAttendContentDataRecords";
		String startDate = null;
		String endDate = null;
		if(date.endsWith("12")){
			startDate =date+"-01";
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(startDate),DateUtil.YEAR,1));
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(endDate),DateUtil.MONTH,-11));
		}else{
			startDate =date+"-01";
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(startDate),DateUtil.MONTH,1));	
		}
		DataParam param = new DataParam("code",code,"startDate",startDate,"endDate",endDate);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}

	@Override
	public List<DataRow> getLeaveContentData(String code, String date) {
		String statementId = sqlNameSpace+"."+"getLeaveContentDataRecords";
		String startDate = null;
		String endDate = null;
		if(date.endsWith("12")){
			startDate =date+"-01";
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(startDate),DateUtil.YEAR,1));
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(endDate),DateUtil.MONTH,-11));
		}else{
			startDate =date+"-01";
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(startDate),DateUtil.MONTH,1));	
		}
		DataParam param = new DataParam("code",code,"startDate",startDate,"endDate",endDate);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}

	@Override
	public List<DataRow> getOverworkContentData(String code, String date) {
		String statementId = sqlNameSpace+"."+"getOverworkContentDataRecords";
		String startDate = null;
		String endDate = null;
		if(date.endsWith("12")){
			startDate =date+"-01";
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(startDate),DateUtil.YEAR,1));
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(endDate),DateUtil.MONTH,-11));
		}else{
			startDate =date+"-01";
			endDate = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getDate(startDate),DateUtil.MONTH,1));	
		}
		DataParam param = new DataParam("code",code,"startDate",startDate,"endDate",endDate);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}
	
	@Override
	public void createLocationRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"createLocationRecord";
		param.put("LOCAT_ID", KeyGenerator.instance().genKey());
		this.daoHelper.insertRecord(statementId, param);
	}

	@Override
	public List<DataRow> findCurrentDaySigninInfos(String adtDate) {
		DataParam param = new DataParam("adtDate",adtDate);
		String statementId = sqlNameSpace+"."+"findRecords";
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}

	@Override
	public List<DataRow> findUserInfos() {
		String statementId = sqlNameSpace+"."+"findUserInfos";
		List<DataRow> records = this.daoHelper.queryRecords(statementId, new DataParam());
		return records;
	}

	@Override
	public List<DataRow> findSignLocationInfos(String userId) {
		DataParam param = new DataParam("userId",userId);
		String statementId = sqlNameSpace+"."+"findSignLocationInfos";
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}

	@Override
	public List<DataRow> findCurrentDaySignOutInfos(String expression,
			String adtDate) {
		DataParam param = new DataParam("expression",expression,"adtDate",adtDate);
		String statementId = sqlNameSpace+"."+"findRecords";
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		return records;
	}
}
