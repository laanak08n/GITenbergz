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
import com.agileai.hr.cxmodule.HrSalaryManage;
import com.agileai.util.DateUtil;
import com.agileai.util.MapUtil;

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
	
	@Override
	public DataRow getInductionAndCreateTime(String userId){
		DataParam param = new DataParam("userId",userId);
		String statementId = sqlNameSpace + "." + "getUserAnnualLeaveDays";
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
		statementId = "HrLeave"+"."+"findRecords";
		HashMap<String, DataRow> totalLeaveTimeMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("edate",currentDate));
		statementId = "HrAttendance"+"."+"attendanceStatisticsRecords";
		
		HashMap<String, DataRow> currentMonthAttendMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("sdate",date,"edate",currentDate));
		
		List<DataParam> insertParamList = new ArrayList<DataParam>();
		List<DataParam> updateParamList = new ArrayList<DataParam>();
		for (int i = 0; i < basicRecords.size(); i++) {
			DataRow row = basicRecords.get(i);
			DataParam dataParam = new DataParam();
			dataParam.put("SAL_ID", keyGenerator.genKey());
			String userCode = row.getString("USER_ID");
			DataRow currentMonthAttendRow = currentMonthAttendMap.get(userCode);
			DataRow leaveDaysRow = totalLeaveTimeMap.get(userCode);
			dataParam = calculateTotalSalary(workDaysMap,leaveDaysMap,overTimeDaysMap,currentMonthAttendRow,userCode,dataParam,validDaysRow,year,month,row,date);
			Date regularTime = (Date)row.get("EMP_REGULAR_TIME");
			dataParam = calculateOverRunDays(totalLeaveOfYearMap,totalOverTimeOfYearMap,date,row,userCode,dataParam,regularTime,yearMonth);
			BigDecimal salTotal = (BigDecimal) dataParam.getObject("SAL_TOTAL");
			BigDecimal salInsure = (BigDecimal) dataParam.getObject("SAL_INSURE");
			BigDecimal salTax = (BigDecimal) dataParam.getObject("SAL_TAX");
			BigDecimal salHousingFund =(BigDecimal) dataParam.getObject("SAL_HOUSING_FUND");
			BigDecimal salShould = salTotal.subtract(salInsure).subtract(salTax).subtract(salHousingFund);
			if(regularTime.compareTo(DateUtil.getDateAdd(date, DateUtil.MONTH, 1))>=0){
				salShould = salTotal;
			}			
			dataParam.put("SAL_SHOULD", salShould);
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
			BigDecimal salFulltimeAward = calculateFullTimeAward(leaveDaysRow,currentMonthAttendRow,userCode,regularTime,date,dataParam);
			dataParam = calculateOverRunSalary(dataParam,validDaysRow,userCode,date,regularTime);
			BigDecimal salDayMoneyTotal = (BigDecimal)dataParam.getObject("salDayMoneyTotal");
			
			BigDecimal salBonus = rewardMoneyDecimal.subtract(punishmentMoneyDecimal).add(salDayMoneyTotal).add(salFulltimeAward);
			dataParam.put("SAL_BONUS", salBonus);
			BigDecimal salActual  = salShould.add(salBonus);
			dataParam.put("SAL_ACTUAL", salActual);
			statementId = sqlNameSpace + "." + "getSalYearLeaveInfo";
			DataRow salYearLeaveRow = this.daoHelper.getRecord(statementId, new DataParam("userCode",userCode,"beginTime",year+"-01-01 00:00:00","endTime",year+"-12-31 23:59:59"));
			if(MapUtil.isNullOrEmpty(salYearLeaveRow)){
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
	private DataParam calculateTotalSalary(HashMap<String,DataRow> workDaysMap,
			HashMap<String,DataRow> leaveDaysMap,HashMap<String,DataRow> overTimeDaysMap,DataRow currentMonthAttendRow,String userCode,DataParam dataParam,
			DataRow validDaysRow,String year,String month,DataRow row,Date date){
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
		BigDecimal salInsure = (BigDecimal) row.get("EMP_INSURE");
		if(salInsure == null){
			salInsure = new BigDecimal("0.0");
		}
		BigDecimal salTax = (BigDecimal) row.get("EMP_TAX");
		if(salTax == null){
			salTax = new BigDecimal("0.0");
		}
		BigDecimal salHousingFund = (BigDecimal) row.get("EMP_HOUSING_FUND");
		if(salHousingFund == null){
			salHousingFund = new BigDecimal("0.0");
		}
		BigDecimal salProbation = (BigDecimal) row.get("EMP_PROBATION");
		if(salProbation == null){
			salProbation = new BigDecimal("0.0");
		}
		BigDecimal salBasic = (BigDecimal) row.get("EMP_BASIC");		
		if(salBasic == null){
			salBasic = new BigDecimal("0.0");
		}
		BigDecimal salTotal = new BigDecimal("0.0");		
		Date inductionDate = (Date)row.get("EMP_INDUCTION_TIME");
		Date regularDate = (Date)row.get("EMP_REGULAR_TIME");	
		BigDecimal validDays = (BigDecimal) validDaysRow.get("VALID_DAYS");
		BigDecimal salProbationDayMoney = salProbation.divide(validDays,6, RoundingMode.HALF_UP);
		BigDecimal salRegularDayMoney = salBasic.divide(validDays,6, RoundingMode.HALF_UP);
		dataParam.put("salProbationDayMoney", salProbationDayMoney);
		dataParam.put("salRegularDayMoney", salRegularDayMoney);
		BigDecimal salPerformance = (BigDecimal) row.get("EMP_PERFORMANCE");		
		if(salPerformance == null){
			salPerformance = new BigDecimal("0.0");
		}		
		BigDecimal salSubsidy = (BigDecimal) row.get("EMP_SUBSIDY");
		if(salSubsidy == null){
			salSubsidy = new BigDecimal("0.0");
		}
		BigDecimal currentMonthAttendDays = new BigDecimal("0.0");
		if(!(currentMonthAttendRow == null || currentMonthAttendRow.isEmpty())){
			currentMonthAttendDays = BigDecimal.valueOf(currentMonthAttendRow.getInt("IN_NUM"));
		}
		BigDecimal beforeRegularLeaveDays = new BigDecimal("0.0");
		BigDecimal regularLeaveDays = new BigDecimal("0.0");
		if(DateUtil.getDateDiff(regularDate, date, DateUtil.DAY) >= 0){
			salTotal = salBasic.add(salPerformance).add(salSubsidy);
		}else if(DateUtil.getDateDiff(regularDate, date, DateUtil.MONTH) == 0){
			String statementId = sqlNameSpace + "." + "getSalYearLeaveInfo";
			DataRow beforeRegularLeaveRow = this.daoHelper.getRecord(
					statementId, new DataParam("userCode",userCode,"beginTime",DateUtil.getDateAdd(date, DateUtil.DAY, -1),"endTime",regularDate));
			if(!(beforeRegularLeaveRow == null || beforeRegularLeaveRow.isEmpty())){
				beforeRegularLeaveDays = BigDecimal.valueOf((double)beforeRegularLeaveRow.get("SAL_YEAR_LEAVE"));
			}
			DataRow regularLeaveRow = this.daoHelper.getRecord(
					statementId, new DataParam("userCode",userCode,"beginTime",DateUtil.getDateAdd(regularDate, DateUtil.DAY, -1),"endTime",DateUtil.getDateAdd(date, DateUtil.MONTH, 1)));	
			if(!(regularLeaveRow == null || regularLeaveRow.isEmpty())){
				regularLeaveDays = BigDecimal.valueOf((double)regularLeaveRow.get("SAL_YEAR_LEAVE"));
			}
			statementId = "HrAttendance"+"."+"attendanceStatisticsRecords";
			HashMap<String, DataRow> regularAtdMap = this.daoHelper.queryRecords(
					"USER_ID", statementId, new DataParam("sdate",regularDate,"edate",DateUtil.getEndOfMonth(date)));
			BigDecimal regularAtdDays = new BigDecimal("0.0");
			if(!(regularAtdMap == null || regularAtdMap.isEmpty())){
				DataRow regularAtdRow = regularAtdMap.get(userCode);
				if(regularAtdRow != null){
					regularAtdDays = BigDecimal.valueOf(regularAtdRow.getInt("IN_NUM"));
				}
			}
			BigDecimal beforeRegularAtdDays = currentMonthAttendDays.subtract(regularAtdDays);
			salTotal = salProbationDayMoney.multiply(beforeRegularAtdDays.add(beforeRegularLeaveDays)).add(salRegularDayMoney.multiply(regularAtdDays.add(regularLeaveDays)));
		}else{
			if(DateUtil.getDateDiff(inductionDate, date, DateUtil.DAY) >= 0){
				salBasic = salProbation;
				salTotal = salProbation;
			}else{
				salBasic = salProbation;
				BigDecimal ProbationLeaveDays = new BigDecimal("0.0");
				String statementId = sqlNameSpace + "." + "getSalYearLeaveInfo";
				DataRow ProbationLeaveRow = this.daoHelper.getRecord(
						statementId, new DataParam("userCode",userCode,"beginTime",DateUtil.getDateAdd(inductionDate, DateUtil.DAY, -1),"endTime",DateUtil.getDateAdd(date, DateUtil.MONTH, 1)));
				if(!(ProbationLeaveRow == null || ProbationLeaveRow.isEmpty())){
					ProbationLeaveDays = BigDecimal.valueOf((double)ProbationLeaveRow.get("SAL_YEAR_LEAVE"));
				}
				salTotal = salProbationDayMoney.multiply(currentMonthAttendDays.add(ProbationLeaveDays));
			}
		}
		dataParam.put("SAL_TOTAL", salTotal);
		dataParam.put("SAL_VALID_DAYS", validDaysRow.get("VALID_DAYS"));
		dataParam.put("SAL_USER", userCode);
		dataParam.put("SAL_YEAR", year);
		dataParam.put("SAL_MONTH", month);
		dataParam.put("SAL_PROBATION", row.get("EMP_PROBATION"));
		dataParam.put("SAL_BASIC", salBasic);
		dataParam.put("SAL_PERFORMANCE", row.get("EMP_PERFORMANCE"));
		dataParam.put("SAL_SUBSIDY", row.get("EMP_SUBSIDY"));
		dataParam.put("SAL_INSURE", salInsure);
		dataParam.put("EMP_HOUSING_FUND", row.get("EMP_HOUSING_FUND"));
		dataParam.put("SAL_TAX", salTax);
		dataParam.put("SAL_HOUSING_FUND", salHousingFund);
		dataParam.put("SAL_STATE", "0");
		dataParam.put("beforeRegularLeaveDays",beforeRegularLeaveDays);
		dataParam.put("regularLeaveDays",regularLeaveDays);
		dataParam.put("salProbationDayMoney",salProbationDayMoney);
		dataParam.put("salRegularDayMoney",salRegularDayMoney);
		return dataParam;
	}
	private BigDecimal calculateFullTimeAward(DataRow leaveDaysRow,DataRow currentMonthAttendRow,String userCode,Date regularTime,Date date,DataParam param){
		KeyGenerator keyGenerator = KeyGenerator.instance();
		String statementId = "";
		BigDecimal salFullTimeAward = new BigDecimal("0.0");
		BigDecimal validDay = (BigDecimal) param.getObject("SAL_VALID_DAYS");
		int inAttend = 0 ;
		int outAttend = 0;
		if(!MapUtil.isNullOrEmpty(currentMonthAttendRow)){
			inAttend = currentMonthAttendRow.getInt("IN_NUM",0);
			outAttend = currentMonthAttendRow.getInt("OUT_NUM",0);
		}
		BigDecimal inAttendDecimal = new BigDecimal(inAttend).add(BigDecimal.valueOf(3.0));
		BigDecimal outAttendDecimal = new BigDecimal(outAttend).add(BigDecimal.valueOf(3.0));
		boolean isLeave = false;
		boolean isFullAttend = true;
		if(!MapUtil.isNullOrEmpty(leaveDaysRow)){
			Date endDate = (Date) leaveDaysRow.get("LEA_EDATE");
			if(endDate.compareTo(DateUtil.getDateAdd(date, DateUtil.MONTH, 1))>=0){
				isLeave = true;
			}else if(endDate.compareTo(DateUtil.getDateAdd(date, DateUtil.MONTH, 1))<0&&endDate.compareTo(DateUtil.getDateAdd(date, DateUtil.MONTH, -2))>=0){
				isLeave = true;
			}
		}
		if(inAttendDecimal.compareTo(validDay)==-1||outAttendDecimal.compareTo(validDay)==-1){
			isFullAttend = false;
		}
		if(!isLeave&&isFullAttend&&regularTime.compareTo(DateUtil.getDateAdd(date, DateUtil.DAY, -1)) <= 0){
			statementId = sqlNameSpace + "." + "getFulltimeAwardRecord";
			DataRow dataRow = this.daoHelper.getRecord(statementId, new DataParam());
			salFullTimeAward = new BigDecimal(dataRow.getString("TYPE_NAME"));
			DataParam fulltimeAwardParam = new DataParam();
			fulltimeAwardParam.put("BP_ID", keyGenerator.genKey());
			fulltimeAwardParam.put("USER_ID", userCode);
			fulltimeAwardParam.put("BP_DATE", date);
			fulltimeAwardParam.put("BP_TYPE", "FULLTIME");
			fulltimeAwardParam.put("BP_MONEY", salFullTimeAward);
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
			if(!MapUtil.isNullOrEmpty(bonusPenaltyRow)){
				String bpId = (String) bonusPenaltyRow.get("BP_ID");
				statementId = sqlNameSpace + "." + "deleteBonusPenaltRecord";
				this.daoHelper.deleteRecords(statementId, new DataParam("BP_ID",bpId));
			}
		}
		return salFullTimeAward;
	}
	private DataParam calculateOverRunDays(HashMap<String,DataRow> totalLeaveOfYearMap,HashMap<String,DataRow> totalOverTimeOfYearMap,
			Date date,DataRow row,String userCode,DataParam dataParam,Date regularTime,String yearMonth){
		String january = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getBeginOfYear(date));
		String march = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getBeginOfYear(date), DateUtil.MONTH, 2));
		String beforeYearDec = DateUtil.format(DateUtil.YYMM_NONE, DateUtil.getDateAdd(DateUtil.getBeginOfYear(date), DateUtil.MONTH, -1));
		String nowSalDate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, date);
		Date lastDate = DateUtil.getDateAdd(date, DateUtil.MONTH, -1);
		String lastYearMonth = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, lastDate);
		String lastYear = lastYearMonth.substring(0,4);
		String lastMonth = lastYearMonth.substring(5,7);
		int empAnnualLeaveDay = row.getInt("EMP_ANNUAL_LEAVE_DAYS",0);
		BigDecimal nowAnnualLeaveDecimal = new BigDecimal(empAnnualLeaveDay);
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
		String statementId = sqlNameSpace + "." + "getLastOffsetVacationInfo";
		DataRow lastOffsetVacationRow = this.daoHelper.getRecord(statementId, new DataParam("userCode",userCode,"lastYear",lastYear,"lastMonth",lastMonth));
		DataRow beforeYearDecOffsetVacationRow = this.daoHelper.getRecord(statementId, new DataParam("userCode",userCode,"lastYear",lastYear,"lastMonth",beforeYearDec));
		BigDecimal beforeYearDecVacationDecimal = new BigDecimal("0");
		BigDecimal lastOffsetVacationDecimal = new BigDecimal("0");
		if(!MapUtil.isNullOrEmpty(lastOffsetVacationRow)){
			lastOffsetVacationDecimal = (BigDecimal) lastOffsetVacationRow.get("SAL_OFFSET_VACATION");
		}
		if(!MapUtil.isNullOrEmpty(beforeYearDecOffsetVacationRow)){
			beforeYearDecVacationDecimal =(BigDecimal) beforeYearDecOffsetVacationRow.get("SAL_OFFSET_VACATION");
		}
		Number leaveDaysNum = (Number) dataParam.getObject("SAL_LEAVE");
		BigDecimal leaveDaysDecimal = (BigDecimal)leaveDaysNum;
		Number overTimeDaysNum = (Number) dataParam.getObject("SAL_OVERTIME");
		BigDecimal overTimeDaysDecimal = (BigDecimal)overTimeDaysNum;
		if(beforeYearDec.equals(nowSalDate)){
			
		}else if(january.equals(nowSalDate)){
			dataParam.put("SAL_OFFSET_VACATION", lastOffsetVacationDecimal.add(nowAnnualLeaveDecimal).subtract(leaveDaysDecimal).add(overTimeDaysDecimal));
		}else if(march.equals(nowSalDate)){
			BigDecimal vacationRemainDecimal = beforeYearDecVacationDecimal.subtract(totalLeaveDecimal).add(leaveDaysDecimal);
			if(vacationRemainDecimal.compareTo(BigDecimal.ZERO)>=0){
				dataParam.put("SAL_OFFSET_VACATION",nowAnnualLeaveDecimal.add(totalOverTimeDecimal).subtract(totalLeaveDecimal));
			}else{
				dataParam.put("SAL_OFFSET_VACATION",beforeYearDecVacationDecimal.add(vacationRemainDecimal)
						.add(totalOverTimeDecimal).subtract(leaveDaysDecimal));
			}
		}else{
			if(lastOffsetVacationDecimal.compareTo(BigDecimal.ZERO)<0){
				lastOffsetVacationDecimal = new BigDecimal("0.0");
			}
			dataParam.put("SAL_OFFSET_VACATION", lastOffsetVacationDecimal.subtract(leaveDaysDecimal).add(overTimeDaysDecimal));
		}
		if(DateUtil.getDateDiff(regularTime, date, DateUtil.MONTH)==0){
			
		}
		
		dataParam.put("probationOverRunDays");
		return dataParam;
	}
	private DataParam calculateOverRunSalary(DataParam dataParam,DataRow validDaysRow,
			String userCode,Date date,Date regularTime){
		BigDecimal salOffsetVacation = (BigDecimal) dataParam.getObject("SAL_OFFSET_VACATION");
		BigDecimal salProbationDayMoney = (BigDecimal) dataParam.getObject("salProbationDayMoney");
		BigDecimal salRegularDayMoney = (BigDecimal) dataParam.getObject("salRegularDayMoney");
		BigDecimal probationOverRunDays = (BigDecimal)dataParam.getObject("probationOverRunDays");
		BigDecimal salDayMoneyTotal = new BigDecimal("0.0");
		BigDecimal salProbationDayMoneyTotal = new BigDecimal("0.0");
		if(regularTime.compareTo(date)==0){
			salProbationDayMoneyTotal = probationOverRunDays.multiply(salProbationDayMoney);
		}
		if(salOffsetVacation.compareTo(new BigDecimal("0")) == -1){
			salDayMoneyTotal = salOffsetVacation.multiply(salRegularDayMoney);
			DataParam bonusPenaltyParam = new DataParam();
			bonusPenaltyParam.put("BP_ID", KeyGenerator.instance().genKey());
			bonusPenaltyParam.put("USER_ID", userCode);
			bonusPenaltyParam.put("BP_DATE", date);
			bonusPenaltyParam.put("BP_TYPE", "OVERRUN");
			bonusPenaltyParam.put("BP_MONEY", salDayMoneyTotal.abs());
			String statementId = sqlNameSpace + "." + "getBonusPenaltyRecord";
			DataRow bonusPenaltyRow = this.daoHelper.getRecord(statementId, bonusPenaltyParam);
			if(!MapUtil.isNullOrEmpty(bonusPenaltyRow)){
				statementId = sqlNameSpace + "." + "updateBonusPenaltyRecord";
				this.daoHelper.updateRecord(statementId, bonusPenaltyParam);
			}else{
				statementId = sqlNameSpace + "." + "insertBonusPenaltyRecord";
				this.daoHelper.insertRecord(statementId, bonusPenaltyParam);
			}
		}
		dataParam.put("salDayMoneyTotal",salDayMoneyTotal);
		dataParam.put("unregularSalDayMoneyTotal",salProbationDayMoneyTotal);
		
		return dataParam;
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

	@Override
	public List<DataRow> findFiveMonthsSalayInfos(DataParam param) {
		String statementId = sqlNameSpace+"."+"findFiveMonthsSalayInfos";
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}
}
