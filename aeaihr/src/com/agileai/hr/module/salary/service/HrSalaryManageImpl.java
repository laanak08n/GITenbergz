package com.agileai.hr.module.salary.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.util.DateUtil;

public class HrSalaryManageImpl extends StandardServiceImpl implements
		HrSalaryManage {
	public HrSalaryManageImpl() {
		super();
	}

	@Override
	public DataRow retrieveValidDays(String year, String month) {
		DataParam param = new DataParam("year", year, "month", month);
		String statementId = sqlNameSpace + "." + "retrieveValidDays";
		DataRow row = this.daoHelper.getRecord(statementId, param);
		return row;
	}

	public void createValidDayRecord(DataParam param) {
		String statementId = sqlNameSpace + "." + "insertValidDaysRecord";
		processDataType(param, tableName);
		processPrimaryKeys(param);
		this.daoHelper.insertRecord(statementId, param);
	}

	public void updateValidDayRecord(DataParam param) {
		String statementId = sqlNameSpace + "." + "updateValidDaysRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);
	}

	@Override
	public void gatherData(String year, String month) {
		String statementId = sqlNameSpace + "." + "findMasterDaysRecords";
		List<DataRow> basicRecords = this.daoHelper.queryRecords(statementId,
				new DataParam("year",year,"month",month));

		KeyGenerator keyGenerator = new KeyGenerator();

		DataRow validDaysRow = this.retrieveValidDays(year, month);

		String yearMonth = year + "-" + month;
		Date date = DateUtil.getDate(yearMonth + "-01");
		String  currentDate = yearMonth + "-31";
		String beginYear = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getBeginOfYear(date));
		
		statementId = sqlNameSpace + "." + "totalLeaveOfYearRecords";
		HashMap<String, DataRow> totalLeaveOfYearMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("beginYear",beginYear,"currentDate", currentDate));
		
		statementId = sqlNameSpace + "." + "totalOverTimeOfYearRecords";
		HashMap<String, DataRow> totalOverTimeOfYearMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("beginYear",beginYear,"currentDate", currentDate));
		
		statementId = sqlNameSpace + "." + "workDayRecords";
		HashMap<String, DataRow> workDaysMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth", yearMonth));

		statementId = sqlNameSpace + "." + "leaveDayRecords";
		HashMap<String, DataRow> leaveDaysMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth", yearMonth));

		statementId = sqlNameSpace + "." + "overTimeDayRecords";
		HashMap<String, DataRow> overTimeDaysMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth", yearMonth));

		statementId = sqlNameSpace + "." + "existsDataRecords";
		HashMap<String, DataRow> existsDataMap = this.daoHelper.queryRecords(
				"SAL_USER", statementId, new DataParam("year",year,"month",month));
		
		List<DataParam> insertParamList = new ArrayList<DataParam>();
		List<DataParam> updateParamList = new ArrayList<DataParam>();
		for (int i = 0; i < basicRecords.size(); i++) {
			DataRow row = basicRecords.get(i);
			DataParam dataParam = new DataParam();
			dataParam.put("SAL_ID", keyGenerator.genKey());

			String userCode = row.getString("USER_ID");
			
			DataRow workDaysRow = workDaysMap.get(userCode);
			if(workDaysRow == null){
				dataParam.put("SAL_WORK_DAYS",new BigDecimal("0.0"));
			}else{
				dataParam.put("SAL_WORK_DAYS", workDaysRow.get("WORK_DAYS"));
			}
			DataRow leaveDaysRow = leaveDaysMap.get(userCode);
			if(leaveDaysRow == null){
				dataParam.put("SAL_LEAVE", new BigDecimal("0.0"));
			}else{
				double leaveDouble = (Double) leaveDaysRow.get("LEAVE_DAYS");
				dataParam.put("SAL_LEAVE", new BigDecimal(leaveDouble));
			}
			DataRow overTimeDaysRow = overTimeDaysMap.get(userCode);
			if(overTimeDaysRow == null){
				dataParam.put("SAL_OVERTIME", new BigDecimal("0.0"));
			}else{
				double overTimeDaysDouble = (Double) overTimeDaysRow.get("WOT_DAYS");
				dataParam.put("SAL_OVERTIME", new BigDecimal(overTimeDaysDouble));
			}
			dataParam.put("SAL_VALID_DAYS", validDaysRow.get("VALID_DAYS"));
			dataParam.put("SAL_USER", userCode);
			dataParam.put("SAL_YEAR", year);
			dataParam.put("SAL_MONTH", month);
			dataParam.put("SAL_BASIC", row.get("EMP_BASIC"));
			dataParam.put("SAL_PERFORMANCE", row.get("EMP_PERFORMANCE"));
			dataParam.put("SAL_SUBSIDY", row.get("EMP_SUBSIDY"));
			dataParam.put("SAL_INSURE", row.get("EMP_INSURE"));
			dataParam.put("SAL_TAX", row.get("EMP_TAX"));
			dataParam.put("SAL_STATE", "0");
			BigDecimal sal_basic = (BigDecimal) row.get("EMP_BASIC");
			BigDecimal sal_performance = (BigDecimal) row
					.get("EMP_PERFORMANCE");
			BigDecimal sal_subsidy = (BigDecimal) row.get("EMP_SUBSIDY");
			dataParam.put("SAL_BONUS", "0.00");
			
			BigDecimal sal_total = sal_basic.add(sal_performance).add(
					sal_subsidy);
			dataParam.put("SAL_TOTAL", sal_total);
			
			Date lastDate = DateUtil.getDateAdd(date, DateUtil.MONTH, -1);
			String lastYearMonth = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, lastDate);
			String lastYear = lastYearMonth.substring(0,4);
			String lastMonth = lastYearMonth.substring(5,7);
			
			BigDecimal offsetVacation = new BigDecimal(row.getInt("EMP_ANNUAL_LEAVE_DAYS"));
			
			DataRow totalLeaveOfYearRow = totalLeaveOfYearMap.get(userCode);
			BigDecimal totalLeaveDecimal = new BigDecimal("0.0");
			if(totalLeaveOfYearRow != null){
				double totalLeaveDouble = (Double) totalLeaveOfYearRow.get("LEAVE_DAYS");
				totalLeaveDecimal = new BigDecimal(totalLeaveDouble);
			}
			
			DataRow totalOverTimeOfYearRow = totalOverTimeOfYearMap.get(userCode);
			BigDecimal totalOverTimeDecimal = new BigDecimal("0.0");
			if(totalOverTimeOfYearRow != null){
				double totalOverTimeDouble = (Double) totalOverTimeOfYearRow.get("WOT_DAYS");
				totalOverTimeDecimal = new BigDecimal(totalOverTimeDouble);
			}
			
			statementId = sqlNameSpace + "." + "getLastOffsetVacationInfo";
			DataRow lastOffsetVacationRow = this.daoHelper.getRecord(statementId, new DataParam("userCode",userCode,"lastYear",lastYear,"lastMonth",lastMonth));
			
			Number leaveDaysNum = (Number) dataParam.getObject("SAL_LEAVE");
			BigDecimal leaveDaysDecimal = (BigDecimal)leaveDaysNum;
			Number overTimeDaysNum = (Number) dataParam.getObject("SAL_OVERTIME");
			BigDecimal overTimeDaysDecimal = (BigDecimal)overTimeDaysNum;
			if(lastOffsetVacationRow == null){
				dataParam.put("SAL_OFFSET_VACATION", offsetVacation.subtract(totalLeaveDecimal).add(totalOverTimeDecimal)); 
			}else{
				BigDecimal lastOffsetVacationDecimal = (BigDecimal) lastOffsetVacationRow.get("SAL_OFFSET_VACATION");
				if(lastOffsetVacationDecimal.compareTo(new BigDecimal("0"))==1 || lastOffsetVacationDecimal.compareTo(new BigDecimal("0"))==0){
					dataParam.put("SAL_OFFSET_VACATION", lastOffsetVacationDecimal.subtract(leaveDaysDecimal).add(overTimeDaysDecimal));
				}else if(lastOffsetVacationDecimal.compareTo(new BigDecimal("0")) == -1){
					dataParam.put("SAL_OFFSET_VACATION", overTimeDaysDecimal.subtract(leaveDaysDecimal));
				}
			}
			
			if (existsDataMap.containsKey(userCode)){
				updateParamList.add(dataParam);
			}else{
				insertParamList.add(dataParam);
			}
		}
		if (insertParamList.size() > 0){
			statementId = this.sqlNameSpace + ".insertRecord";
			this.daoHelper.batchInsert(statementId, insertParamList);
		}
		
		if (updateParamList.size() > 0){
			statementId = this.sqlNameSpace + "."+"validupdateRecord";
			this.daoHelper.batchUpdate(statementId, updateParamList);
		}
		
		Iterator<String> iter = existsDataMap.keySet().iterator();
		while (iter.hasNext()) {
			String key = iter.next();
			DataRow dataRow = existsDataMap.get(key);
			String empParticipateSalary = (String) dataRow.get("EMP_PARTICIPATE_SALARY");
			if("N".equals(empParticipateSalary)){
				DataParam param = new DataParam();
				param.put("SAL_ID", dataRow.get("SAL_ID"));
				statementId = this.sqlNameSpace + "."+"deleteRecord";
				this.daoHelper.deleteRecords(statementId, param);
			}
		}
	}

	@Override
	public void computeTotalMoney(String masterRecordId) {
		String statementId = sqlNameSpace + "." + "getRecord";
		DataParam param = new DataParam("SAL_ID", masterRecordId);
		DataRow masterRecord = this.daoHelper.getRecord(statementId, param);

		BigDecimal sal_bonus = (BigDecimal) masterRecord.get("SAL_BONUS");
		BigDecimal sal_basic = (BigDecimal) masterRecord.get("SAL_BASIC");
		BigDecimal sal_performance = (BigDecimal) masterRecord.get("SAL_PERFORMANCE");
		BigDecimal sal_subsidy = (BigDecimal) masterRecord.get("SAL_SUBSIDY");
		BigDecimal sal_total = sal_bonus.add(sal_basic).add(sal_performance).add(sal_subsidy);

		statementId = sqlNameSpace + "." + "updateBonusRecord";
		DataParam updateParam = new DataParam();
		updateParam.put("SAL_ID", masterRecordId, "SAL_TOTAL", sal_total);
		this.daoHelper.updateRecord(statementId, updateParam);
	}

	@Override
	public void approveRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"approveRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);
	}

	@Override
	public void revokeApprovalRecords(String salId) {
		DataParam param = new DataParam("SAL_ID", salId,"SAL_STATE","0");
		String statementId = sqlNameSpace+"."+"revokeApprovalRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);
		
	}
	
	@Override
	public List<DataRow> findSalaryList(DataParam param) {
		String statementId = sqlNameSpace+"."+"findSalaryList";
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}

	@Override
	public DataRow getLastSalayInfo(DataParam param) {
		String statementId = sqlNameSpace+"."+"getLastSalayInfo";
		DataRow dataRow = this.daoHelper.getRecord(statementId, param);
		return dataRow;
	}

}
