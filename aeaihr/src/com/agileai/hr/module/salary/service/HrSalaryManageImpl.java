package com.agileai.hr.module.salary.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
			BigDecimal sal_insure = (BigDecimal) row.get("EMP_INSURE");
			if(sal_insure == null){
				sal_insure = new BigDecimal("0.0");
			}
			dataParam.put("SAL_INSURE", sal_insure);
			dataParam.put("EMP_HOUSING_FUND", row.get("EMP_HOUSING_FUND"));
			BigDecimal sal_tax = (BigDecimal) row
					.get("EMP_TAX");
			if(sal_tax == null){
				sal_tax = new BigDecimal("0.0");
			}
			dataParam.put("SAL_TAX", sal_tax);
			
			BigDecimal sal_housing_fund = (BigDecimal) row
					.get("EMP_HOUSING_FUND");
			if(sal_housing_fund == null){
				sal_housing_fund = new BigDecimal("0.0");
			}
			
			dataParam.put("SAL_HOUSING_FUND", sal_housing_fund);
			
			dataParam.put("SAL_STATE", "0");
			BigDecimal sal_basic = (BigDecimal) row.get("EMP_BASIC");
			if(sal_basic == null){
				sal_basic = new BigDecimal("0.0");
			}
			BigDecimal sal_performance = (BigDecimal) row.get("EMP_PERFORMANCE");
			if(sal_performance == null){
				sal_performance = new BigDecimal("0.0");
			}
			BigDecimal sal_subsidy = (BigDecimal) row.get("EMP_SUBSIDY");
			if(sal_subsidy == null){
				sal_subsidy = new BigDecimal("0.0");
			}
			
			BigDecimal sal_total = sal_basic.add(sal_performance).add(
					sal_subsidy);
			dataParam.put("SAL_TOTAL", sal_total);
			
			statementId = sqlNameSpace + "." + "getPunishmentRecord";
			DataRow punishmentRow = this.daoHelper.getRecord(statementId, new DataParam("USER_ID",userCode,"yearMonth",yearMonth));
			BigDecimal punishmentMoneyDecimal = new BigDecimal("0.0");
			if(!punishmentRow.isEmpty()){
				punishmentMoneyDecimal = (BigDecimal) punishmentRow.get("MONEY");
			}
			
			statementId = sqlNameSpace + "." + "getRewardtRecord";
			DataRow rewardtRow = this.daoHelper.getRecord(statementId, new DataParam("USER_ID",userCode,"yearMonth",yearMonth));
			BigDecimal rewardMoneyDecimal = new BigDecimal("0.0");
			if(!rewardtRow.isEmpty()){
				rewardMoneyDecimal = (BigDecimal) rewardtRow.get("MONEY");
			}
			
			Date lastDate = DateUtil.getDateAdd(date, DateUtil.MONTH, -1);
			String lastYearMonth = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, lastDate);
			String lastYear = lastYearMonth.substring(0,4);
			String lastMonth = lastYearMonth.substring(5,7);
			
			BigDecimal offsetVacation = new BigDecimal(row.getInt("EMP_ANNUAL_LEAVE_DAYS",0));
			
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
			BigDecimal lastOffsetVacationDecimal = new BigDecimal("0");
			
			Number leaveDaysNum = (Number) dataParam.getObject("SAL_LEAVE");
			BigDecimal leaveDaysDecimal = (BigDecimal)leaveDaysNum;
			Number overTimeDaysNum = (Number) dataParam.getObject("SAL_OVERTIME");
			BigDecimal overTimeDaysDecimal = (BigDecimal)overTimeDaysNum;
			Date regularTime = (Date) row.get("EMP_REGULAR_TIME");
			String regularTimeStr = DateUtil.getDateByType(DateUtil.YYMMDD_HORIZONTAL, regularTime);
			if(lastOffsetVacationRow == null || lastOffsetVacationRow.size() == 0 || lastMonth.equals("12")){
				dataParam.put("SAL_OFFSET_VACATION", offsetVacation.subtract(totalLeaveDecimal).add(totalOverTimeDecimal)); 
			}else if(yearMonth.equals(regularTimeStr.substring(0, 7))){
				dataParam.put("SAL_OFFSET_VACATION", offsetVacation.subtract(leaveDaysDecimal).add(overTimeDaysDecimal));
			}else{
				lastOffsetVacationDecimal = (BigDecimal) lastOffsetVacationRow.get("SAL_OFFSET_VACATION");
				if(lastOffsetVacationDecimal.compareTo(new BigDecimal("0"))==1 || lastOffsetVacationDecimal.compareTo(new BigDecimal("0"))==0){
					dataParam.put("SAL_OFFSET_VACATION", lastOffsetVacationDecimal.subtract(leaveDaysDecimal).add(overTimeDaysDecimal));
				}else if(lastOffsetVacationDecimal.compareTo(new BigDecimal("0")) == -1){
					dataParam.put("SAL_OFFSET_VACATION", overTimeDaysDecimal.subtract(leaveDaysDecimal));
				}
			}
			
			BigDecimal sal_should = sal_total.subtract(sal_insure).subtract(sal_tax).subtract(sal_housing_fund);
			dataParam.put("SAL_SHOULD", sal_should);
			
			BigDecimal sal_offset_vacation =  (BigDecimal) dataParam.getObject("SAL_OFFSET_VACATION");
			
			BigDecimal valid_days = (BigDecimal) validDaysRow.get("VALID_DAYS");
			BigDecimal sal_day_money = sal_total.divide(valid_days,2, RoundingMode.HALF_UP);
			
			BigDecimal sal_day_money_total = new BigDecimal("0.0");
			if(sal_offset_vacation.compareTo(new BigDecimal("0")) == -1){
				sal_day_money_total = sal_offset_vacation.multiply(sal_day_money);
				DataParam bonusPenaltyParam = new DataParam();
				bonusPenaltyParam.put("BP_ID", keyGenerator.genKey());
				bonusPenaltyParam.put("USER_ID", userCode);
				bonusPenaltyParam.put("BP_DATE", date);
				bonusPenaltyParam.put("BP_TYPE", "OVERRUN");
				bonusPenaltyParam.put("BP_MONEY", sal_day_money_total.abs());
				
				statementId = sqlNameSpace + "." + "getBonusPenaltyRecord";
				DataRow bonusPenaltyRow = this.daoHelper.getRecord(statementId, bonusPenaltyParam);
				if(bonusPenaltyRow != null && !bonusPenaltyRow.isEmpty()){
					statementId = sqlNameSpace + "." + "updateBonusPenaltyRecord";
					this.daoHelper.updateRecord(statementId, bonusPenaltyParam);
				}else{
					statementId = sqlNameSpace + "." + "insertBonusPenaltyRecord";
					this.daoHelper.insertRecord(statementId, bonusPenaltyParam);
				}
			}

			BigDecimal sal_fulltime_award = new BigDecimal("0.0");
			if(leaveDaysRow == null){
				statementId = sqlNameSpace + "." + "getFulltimeAwardRecord";
				DataRow dataRow = this.daoHelper.getRecord(statementId, new DataParam());
				sal_fulltime_award = new BigDecimal(dataRow.getString("TYPE_NAME"));
				DataParam fulltimeAwardParam = new DataParam();
				fulltimeAwardParam.put("BP_ID", keyGenerator.genKey());
				fulltimeAwardParam.put("USER_ID", userCode);
				fulltimeAwardParam.put("BP_DATE", date);
				fulltimeAwardParam.put("BP_TYPE", "FULLTIME");
				fulltimeAwardParam.put("BP_MONEY", sal_fulltime_award);
				statementId = sqlNameSpace + "." + "getBonusPenaltyRecord";
				DataRow bonusPenaltyRow = this.daoHelper.getRecord(statementId, fulltimeAwardParam);
				if(bonusPenaltyRow != null && !bonusPenaltyRow.isEmpty()){
					statementId = sqlNameSpace + "." + "updateBonusPenaltyRecord";
					this.daoHelper.updateRecord(statementId, fulltimeAwardParam);
				}else{
					statementId = sqlNameSpace + "." + "insertBonusPenaltyRecord";
					this.daoHelper.insertRecord(statementId, fulltimeAwardParam);
				}
				
			}else{
				DataParam fulltimeAwardParam = new DataParam();
				fulltimeAwardParam.put("USER_ID", userCode);
				fulltimeAwardParam.put("BP_DATE", date);
				fulltimeAwardParam.put("BP_TYPE", "FULLTIME");
				statementId = sqlNameSpace + "." + "getBonusPenaltyRecord";
				DataRow bonusPenaltyRow = this.daoHelper.getRecord(statementId, fulltimeAwardParam);
				if(bonusPenaltyRow != null && !bonusPenaltyRow.isEmpty()){
					String bpId = (String) bonusPenaltyRow.get("BP_ID");
					statementId = sqlNameSpace + "." + "deleteBonusPenaltRecord";
					this.daoHelper.deleteRecords(statementId, new DataParam("BP_ID",bpId));
				}
			}
			
			BigDecimal sal_bonus = rewardMoneyDecimal.subtract(punishmentMoneyDecimal).add(sal_day_money_total).add(sal_fulltime_award);
			dataParam.put("SAL_BONUS", sal_bonus);
			
			BigDecimal sal_actual  = sal_should.add(sal_bonus);
			dataParam.put("SAL_ACTUAL", sal_actual);
			
			statementId = sqlNameSpace + "." + "getSalYearLeaveInfo";
			DataRow salYearLeaveRow = this.daoHelper.getRecord(statementId, new DataParam("userCode",userCode,"beginTime",year+"-01-01 00:00:00","endTime",year+"-12-31 23:59:59"));
			if(salYearLeaveRow == null || salYearLeaveRow.size() == 0){
				dataParam.put("SAL_YEAR_LEAVE", new BigDecimal("0.0"));
			}else{
				dataParam.put("SAL_YEAR_LEAVE", salYearLeaveRow.get("SAL_YEAR_LEAVE"));
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

		BigDecimal sal_basic = (BigDecimal) masterRecord.get("SAL_BASIC");
		BigDecimal sal_performance = (BigDecimal) masterRecord.get("SAL_PERFORMANCE");
		BigDecimal sal_subsidy = (BigDecimal) masterRecord.get("SAL_SUBSIDY");
		BigDecimal sal_total = sal_basic.add(sal_performance).add(sal_subsidy);

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

	@Override
	public void recalculation(String year,String month,String userId) {
		int monthInt = Integer.parseInt(month);
		Date beginOfYearDate = DateUtil.getBeginOfYear(new Date());
		
		String statementId = sqlNameSpace+"."+"getUserAnnualLeaveDays";
		DataRow userAnnualLeaveDaysRow = this.daoHelper.getRecord(statementId, new DataParam("userId",userId));
		String annualLeaveDays = (String) userAnnualLeaveDaysRow.get("EMP_ANNUAL_LEAVE_DAYS");
		BigDecimal annualLeaveDaysDecimal = new BigDecimal(annualLeaveDays);
		
		for(int i=0;i<monthInt;i++){
			Date date = DateUtil.getDateAdd(beginOfYearDate, DateUtil.MONTH, i);
			String dateStr = DateUtil.getMonthText(date);
			statementId = sqlNameSpace+"."+"getMonthlyInfo";
			DataRow monthlyInfoRow = this.daoHelper.getRecord(statementId, new DataParam("year",year,"month",dateStr,"userId",userId));
			if(monthlyInfoRow != null){
				
				statementId = sqlNameSpace+"."+"leaveDayRecords";
				DataRow leaveDaysRow = this.daoHelper.getRecord(statementId, new DataParam("yearMonth",year+"-"+dateStr,"userCode",userId));
				double leaveDaysDouble = 0.0;
				if(leaveDaysRow != null){
					leaveDaysDouble = (Double) leaveDaysRow.get("LEAVE_DAYS");
				}
				BigDecimal salLeaveDecimal = new BigDecimal(leaveDaysDouble);
				
				statementId = sqlNameSpace+"."+"overTimeDayRecords";
				DataRow overTimeDaysRow = this.daoHelper.getRecord(statementId, new DataParam("yearMonth",year+"-"+dateStr,"userCode",userId));
				double overTimeDaysDouble = 0.0;
				if(overTimeDaysRow != null){
					overTimeDaysDouble = (Double) overTimeDaysRow.get("WOT_DAYS");
				}
				BigDecimal overtimeDecimal = new BigDecimal(overTimeDaysDouble);
				
				
				annualLeaveDaysDecimal = annualLeaveDaysDecimal.subtract(salLeaveDecimal).add(overtimeDecimal);
				
				if(i == monthInt-1){
					statementId = sqlNameSpace+"."+"updateOffsetVacationrecord";
					this.daoHelper.updateRecord(statementId, new DataParam("SAL_ID",monthlyInfoRow.get("SAL_ID"),"SAL_OFFSET_VACATION",annualLeaveDaysDecimal,"SAL_OVERTIME",overtimeDecimal,"SAL_LEAVE",salLeaveDecimal));
				}
				
				if(annualLeaveDaysDecimal.compareTo(new BigDecimal("0.0")) == -1){
					annualLeaveDaysDecimal = new BigDecimal("0.0");
				}
			}
		}
	}
}
