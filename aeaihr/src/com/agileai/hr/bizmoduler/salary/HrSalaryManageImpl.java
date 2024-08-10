package com.agileai.hr.bizmoduler.salary;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;

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
				dataParam.put("SAL_LEAVE", leaveDaysRow.get("LEAVE_DAYS"));
			}
			DataRow overTimeDaysRow = overTimeDaysMap.get(userCode);
			if(overTimeDaysRow == null){
				dataParam.put("SAL_OVERTIME", new BigDecimal("0.0"));
			}else{
				dataParam.put("SAL_OVERTIME", overTimeDaysRow.get("WOT_DAYS"));
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

}
