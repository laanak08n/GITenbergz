package com.agileai.hr.module.salary.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardServiceImpl;
import com.agileai.hr.cxmodule.HrSalaryManage;
import com.agileai.util.DateUtil;
import com.agileai.util.ListUtil;
import com.agileai.util.MapUtil;

public class HrSalaryManageImpl extends StandardServiceImpl implements
		HrSalaryManage {
	public HrSalaryManageImpl() {
		super();
	}
	private HashMap<String,List<DataRow>> queryRecords(String indexFieldName,String statementId,DataParam param) throws DataAccessException {
		HashMap<String,List<DataRow>> result = new HashMap<String,List<DataRow>>();
		List<DataRow> recordList = this.daoHelper.queryRecords(statementId, param);
		HashMap<String, DataRow> recordMap = this.daoHelper.queryRecords(indexFieldName, statementId, param);
		if (recordList != null && recordList.size() > 0){
			int count = recordList.size();
			Iterator<String> it = recordMap.keySet().iterator();
			while (it.hasNext()) {
				String indexField = it.next();
				List<DataRow> list = new ArrayList<DataRow>();
				for (int i = 0; i < count; i++) {
					if (indexField.equals(String.valueOf(recordList.get(i).get(indexFieldName)))) {
						list.add(recordList.get(i));
					}
				}
				result.put(indexField, list);
			}
		}
		return result;
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
		String statementId = "";
		String yearMonth = year + "-" + month;
		Date date = DateUtil.getDate(yearMonth + "-01");
		String  currentDate = yearMonth + "-31";
		Date lastDateMonth = DateUtil.getDate(currentDate);
		String beginOfYearDate = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getBeginOfYear(date));
		String beforeMonthDateStr = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(date, DateUtil.MONTH, -1));
		String beforeYearDecemberStr = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, DateUtil.getDateAdd(DateUtil.getBeginOfYear(date), DateUtil.MONTH, -1));
		String previousMonth = beforeMonthDateStr.substring(5,7);
		String beforeMonth = beforeMonthDateStr.substring(0,7);
		String beforeYear = beforeYearDecemberStr.substring(0,4);
		String beforeYearDecember = beforeYearDecemberStr.substring(5,7);
		DataRow validDaysRow = this.retrieveValidDays(year, month);
		DataRow beforeYearDecemberValidDaysRow = this.retrieveValidDays(beforeYear, beforeYearDecember);
		statementId = sqlNameSpace + "." + "workDayRecords";
		HashMap<String,DataRow> workDayMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth",yearMonth));
		HashMap<String,DataRow> probationWorkDayMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("sdate",date));
		HashMap<String,DataRow> regularWorkDayMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("edate",currentDate));
		statementId = sqlNameSpace + "." + "overTimeDayRecords";
		HashMap<String,DataRow> totalOverTimeDaysMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth",yearMonth));
		statementId = sqlNameSpace + "." + "leaveDayRecords";
		HashMap<String,DataRow> totalLeaveDaysMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth",yearMonth));
		HashMap<String,DataRow> currentYearLeaveDaysMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("sdate",beginOfYearDate,"edate",currentDate));
		statementId = sqlNameSpace + "." + "currentMonthLeaveDayRecords";
		HashMap<String,List<DataRow>> currentMonthLeaveDayRecordsMap = queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth",yearMonth));
		HashMap<String,List<DataRow>> totalLeaveRecordsMap = queryRecords(
				"USER_ID",statementId, new DataParam("year",beforeYear));
		statementId = sqlNameSpace + "." + "findOverTimeRecords";
		HashMap<String,List<DataRow>> beforeMonthOverTimeRecordsMap = queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth",beforeMonth));
		HashMap<String,List<DataRow>> currentMonthOverTimeRecordsMap = queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth",yearMonth));
		HashMap<String,List<DataRow>> totalOverTimeRecordsMap = queryRecords(
				"USER_ID", statementId, new DataParam("year",beforeYear));
		statementId = sqlNameSpace + "." + "findOffsetVacationDayRecords";
		HashMap<String,DataRow> beforeYearDecemberOffsetVationDaysMap = this.daoHelper.queryRecords(
				"SAL_USER", statementId, new DataParam("year",beforeYear,"month",beforeYearDecember));
		HashMap<String,DataRow> beforeMonthOffsetVationDaysMap = this.daoHelper.queryRecords(
				"SAL_USER", statementId, new DataParam("year",year,"month",previousMonth));
		HashMap<String,List<DataRow>> beforeYearOffesetVationDaysMap = queryRecords(
				"SAL_USER", statementId, new DataParam("year",beforeYear));
		statementId = sqlNameSpace + "." + "findMasterDaysRecords";
		List<DataRow> basicRecords = this.daoHelper.queryRecords(statementId,
				new DataParam("year",year,"month",month));
		statementId = sqlNameSpace + "." + "findPunishmentRecords";
		HashMap<String,DataRow> punishmentMap = this.daoHelper.queryRecords(
				"USER_ID",statementId, new DataParam("yearMonth",yearMonth));
		statementId = sqlNameSpace + "." + "findRewardtRecords";
		HashMap<String,DataRow> rewardMap = this.daoHelper.queryRecords(
				"USER_ID",statementId, new DataParam("yearMonth",yearMonth));
		statementId = sqlNameSpace+ "." + "findRecords";
		HashMap<String,DataRow> salaryRecordMap = this.daoHelper.queryRecords(
				"SAL_USER", statementId, new DataParam("salYear",year,"salMonth",month));
		HashMap<String,DataRow> beforeYearDecemberSalaryRecords = this.daoHelper.queryRecords(
				"SAL_USER",statementId,new DataParam("salYear",beforeYear,"salMonth",beforeYearDecember));
		statementId = sqlNameSpace+ "." + "findBonusPenaltyRecords";
		HashMap<String,DataRow> overRunDayRecordMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("BP_DATE",date,"BP_TYPE","OVERRUN"));
		HashMap<String,DataRow> fullTimeRecordMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("BP_DATE",date,"BP_TYPE","FULLTIMEAWARD"));
		HashMap<String,DataRow> additionalVationRecordMap = this.daoHelper.queryRecords(
				"USER_ID", statementId, new DataParam("BP_DATE",date,"BP_TYPE","ADDITIONALVATION"));
		statementId = sqlNameSpace+ "." + "findAttendanceRecords";
		HashMap<String,DataRow> attendanceRecordMap = this.daoHelper.queryRecords(
				"USER_ID",statementId, new DataParam("sdate",date,"edate",currentDate));
		statementId = sqlNameSpace+ "." +"getFulltimeAwardRecord";
		DataRow fulltimeAward = this.daoHelper.getRecord(statementId, new DataParam());
		BigDecimal validDayDecimal = (BigDecimal) validDaysRow.get("VALID_DAYS");
		BigDecimal beforeYearDecemberValidDayDecimal = new BigDecimal("22.0");
		if(!MapUtil.isNullOrEmpty(beforeYearDecemberValidDaysRow)){
			beforeYearDecemberValidDayDecimal =(BigDecimal) beforeYearDecemberValidDaysRow.get("VALID_DAYS");
		}
		Double validDay = validDayDecimal.doubleValue();
		Double beforeYearDecemberValidDay = beforeYearDecemberValidDayDecimal.doubleValue();
		List<DataParam> insertAdditionalVationParamList = new ArrayList<DataParam>();
		List<DataParam> updateAdditionalVationParamList = new ArrayList<DataParam>();
		List<DataParam> updateOverRunParamList = new ArrayList<DataParam>();
		List<DataParam> insertOverRunParamList = new ArrayList<DataParam>();
		List<DataParam> insertFullTimeParamList = new ArrayList<DataParam>();
		List<DataParam> updateFullTimeParamList = new ArrayList<DataParam>();
		List<DataParam> updateParamList = new ArrayList<DataParam>();
		List<DataParam> insertParamList = new ArrayList<DataParam>();
		String fulltimeAwardStr = fulltimeAward.getString("TYPE_NAME");
		Double fulltimeAwardMoney = Double.valueOf(fulltimeAwardStr);
		for(int i = 0;i<basicRecords.size();i++){
			DataParam param = new DataParam();
			DataRow row = basicRecords.get(i);
			BigDecimal nonSal =  new BigDecimal("0.00");
			BigDecimal salBasic = new BigDecimal("0.00");
			String userId = row.getString("USER_ID");
			String salId = KeyGenerator.instance().genKey();
			Date regularTime = (Date) row.get("EMP_REGULAR_TIME");
			BigDecimal empBasic = (BigDecimal) row.get("EMP_BASIC");
			BigDecimal empProbation = (BigDecimal) row.get("EMP_PROBATION");
			BigDecimal salPerformance = (BigDecimal) row.get("EMP_PERFORMANCE");
			BigDecimal salInsure = (BigDecimal)row.get("EMP_INSURE");
			BigDecimal salSubsidy = (BigDecimal)row.get("EMP_SUBSIDY");
			BigDecimal salTax = (BigDecimal)row.get("EMP_TAX");
			BigDecimal salHousingFund = (BigDecimal)row.get("EMP_HOUSING_FUND");
			param.put("SAL_ID",salId);
			param.put("SAL_NAME",row.get("EMP_NAME"));
			param.put("SAL_YEAR",year);
			param.put("SAL_MONTH",month);
			param.put("SAL_BASIC",salBasic);
			param.put("SAL_PERFORMANCE",nonSal);
			param.put("SAL_INSURE",nonSal);
			param.put("SAL_SUBSIDY",nonSal);
			param.put("SAL_TAX",nonSal);
			param.put("SAL_HOUSING_FUND",nonSal);
			param.put("SAL_USER",userId);
			param.put("SAL_PROBATION",empProbation);
			param.put("SAL_STATE",0);
			param.put("SAL_VALID_DAYS",validDayDecimal);
			param.put("empProbation",empProbation);
			param.put("validDay",validDay);
			param.put("fulltimeAwardMoney",fulltimeAwardMoney);
			if(DateUtil.getDateDiff(regularTime, date, DateUtil.DAY)>=0&&empBasic!=null){
				param.put("SAL_BASIC",empBasic);
			}else if(empProbation!=null){
				param.put("SAL_BASIC",empProbation);
			}
			if(salPerformance!=null){
				param.put("SAL_PERFORMANCE",salPerformance);
			}
			if(salInsure!=null){
				param.put("SAL_INSURE",salInsure);
			}
			if(salSubsidy!=null){
				param.put("SAL_SUBSIDY",salSubsidy);
			}
			if(salTax!=null){
				param.put("SAL_TAX",salTax);
			}
			if(salHousingFund!=null){
				param.put("SAL_HOUSING_FUND",salHousingFund);
			}
			param = buildSalaryResults(param,row,userId,regularTime,date,lastDateMonth,beforeYearDecemberValidDay,workDayMap,totalOverTimeDaysMap,totalLeaveDaysMap,currentYearLeaveDaysMap,
					currentMonthLeaveDayRecordsMap,beforeMonthOverTimeRecordsMap,currentMonthOverTimeRecordsMap,
					beforeYearDecemberOffsetVationDaysMap,beforeMonthOffsetVationDaysMap,beforeYearDecemberSalaryRecords,punishmentMap,rewardMap,salaryRecordMap,overRunDayRecordMap,
					probationWorkDayMap,regularWorkDayMap,updateOverRunParamList,insertOverRunParamList,insertAdditionalVationParamList,insertFullTimeParamList,updateFullTimeParamList,fullTimeRecordMap,attendanceRecordMap,fulltimeAwardMoney,beforeYearOffesetVationDaysMap,totalOverTimeRecordsMap,insertFullTimeParamList,totalLeaveRecordsMap
					,additionalVationRecordMap,updateAdditionalVationParamList);
			if(!MapUtil.isNullOrEmpty(salaryRecordMap)&&salaryRecordMap.containsKey(userId)){
				DataRow salaryRecordRow = salaryRecordMap.get(userId);
				salId = (String) salaryRecordRow.get("SAL_ID");
				param.put("SAL_ID",salId);
				updateParamList.add(param);
			}else{
				insertParamList.add(param);
			}
		}
		statementId = sqlNameSpace+ "." + "insertBonusPenaltyRecord";
		if(insertOverRunParamList.size()>0){
			this.daoHelper.batchInsert(statementId, insertOverRunParamList);
		}
		if(insertAdditionalVationParamList.size()>0){
			this.daoHelper.batchInsert(statementId, insertAdditionalVationParamList);
		}
		if(insertFullTimeParamList.size()>0){
			this.daoHelper.batchInsert(statementId, insertFullTimeParamList);
		}
		statementId = sqlNameSpace+ "." + "updateBonusPenaltyRecord";
		if(updateOverRunParamList.size()>0){
			this.daoHelper.batchUpdate(statementId, updateOverRunParamList);
		}
		if(updateFullTimeParamList.size()>0){
			this.daoHelper.batchUpdate(statementId, updateFullTimeParamList);
		}
		statementId = sqlNameSpace+ "." +"insertRecord";
		if(insertParamList.size()>0){
			this.daoHelper.batchInsert(statementId, insertParamList);
		}
		statementId = sqlNameSpace+ "." +"updateRecord";
		if(updateParamList.size()>0){
			this.daoHelper.batchUpdate(statementId, updateParamList);
		}
	}
	
	private DataParam buildSalaryResults(DataParam dataParam,DataRow row,String userId,Date regularTime,Date date,Date lastDateMonth,Double beforeYearDecemberValidDay,HashMap<String,DataRow> workDayMap,
			HashMap<String,DataRow> totalOverTimeDaysMap,HashMap<String,DataRow> totalLeaveDaysMap,HashMap<String,DataRow> currentYearLeaveDaysMap,
			HashMap<String,List<DataRow>> currentMonthLeaveDayRecordsMap,
			HashMap<String,List<DataRow>> beforeMonthOverTimeRecordsMap,HashMap<String,List<DataRow>> currentMonthOverTimeRecordsMap,
			HashMap<String,DataRow> beforeYearDecemberOffsetVationDaysMap,HashMap<String,DataRow> beforeMonthOffsetVationDaysMap,
			HashMap<String,DataRow> beforeYearDecemberSalaryRecords,HashMap<String,DataRow> punishmentMap,
			HashMap<String,DataRow> rewardMap,HashMap<String,DataRow> salaryRecordMap,HashMap<String,DataRow> overRunDayRecordMap,
			HashMap<String,DataRow> probationWorkDayMap,HashMap<String,DataRow> regularWorkDayMap,List<DataParam> updateOverRunParamList,
			List<DataParam> insertOverRunParamList,List<DataParam> insertAdditionalVationParamList,List<DataParam> insertFullTimeParamList,
			List<DataParam> updateFullTimeParamList,HashMap<String,DataRow>fullTimeRecordMap,HashMap<String,DataRow> attendanceRecordMap,Double fulltimeAwardMoney,HashMap<String, List<DataRow>> beforeYearOffesetVationDaysMap,
			HashMap<String, List<DataRow>> totalOverTimeRecordsMap,List<DataParam> insertfullTimeParamList,HashMap<String,List<DataRow>> totalLeaveRecordsMap,HashMap<String, DataRow> additionalVationRecordMap,List<DataParam> updateAdditionalVationParamList){
		BigDecimal salWorkDays = new BigDecimal("0.0");
		BigDecimal salOvertime = new BigDecimal("0.0");
		BigDecimal salLeave = new BigDecimal("0.0");
		BigDecimal salYearLeave = new BigDecimal("0.0");
		BigDecimal salOffsetVacation = new BigDecimal("0.0");
		BigDecimal salBonus = new BigDecimal("0.0");
		BigDecimal salActual = new BigDecimal("0.00");
		BigDecimal salTotal = new BigDecimal("0.00");
		BigDecimal salShould = new BigDecimal("0.00");
		BigDecimal salProbationDayMoney = new BigDecimal("0.00");
		BigDecimal salRegularDayMoney = new BigDecimal("0.00");
		
		if(workDayMap.containsKey(userId)){
			DataRow workDayRow = workDayMap.get(userId);
			if(!MapUtil.isNullOrEmpty(workDayRow)){
				Long workDay = (Long) workDayRow.get("WORK_DAYS");
				salWorkDays = BigDecimal.valueOf(workDay);
			}
		}
		Date inductionDate = (Date) row.get("EMP_INDUCTION_TIME");
		dataParam = buildOverTimeRecords(dataParam,regularTime,date,lastDateMonth,userId,totalOverTimeDaysMap,currentMonthOverTimeRecordsMap,beforeMonthOverTimeRecordsMap);
		dataParam = calculateLeaveDays(dataParam,regularTime,userId,totalLeaveDaysMap,currentYearLeaveDaysMap,currentMonthLeaveDayRecordsMap);
		dataParam = calculateOffsetVacationDays(dataParam,row,regularTime,date,userId,beforeYearDecemberOffsetVationDaysMap,beforeMonthOffsetVationDaysMap);
		dataParam = calculateTotalSaray(dataParam,regularTime,inductionDate,date,userId,beforeYearDecemberValidDay,
				beforeYearDecemberSalaryRecords,overRunDayRecordMap,punishmentMap,rewardMap,insertAdditionalVationParamList,updateOverRunParamList,insertOverRunParamList,
				probationWorkDayMap,regularWorkDayMap,attendanceRecordMap,insertfullTimeParamList,updateFullTimeParamList,
				fullTimeRecordMap,salaryRecordMap,beforeYearOffesetVationDaysMap,totalLeaveRecordsMap,totalOverTimeRecordsMap,additionalVationRecordMap,updateAdditionalVationParamList);
		salTotal = (BigDecimal) dataParam.getObject("salTotal");
		salShould = (BigDecimal) dataParam.getObject("salShould");
		salProbationDayMoney = (BigDecimal) dataParam.getObject("salProbationDayMoney");
		salRegularDayMoney = (BigDecimal) dataParam.getObject("salRegularDayMoney");
		salOvertime = (BigDecimal) dataParam.getObject("thisMonthTotalOverDayDecimal");
		Double totalLeaveDay = (double) dataParam.getObject("totalLeaveDay");
		salLeave = BigDecimal.valueOf(totalLeaveDay);
		Double salYearLeaveDouble = (Double) dataParam.getObject("totalYearLeaveDay");
		salOffsetVacation= (BigDecimal)dataParam.getObject("salOffsetVacation") ;
		salYearLeave = BigDecimal.valueOf(salYearLeaveDouble);
		salBonus = (BigDecimal) dataParam.getObject("salBonus");
		salActual = (BigDecimal) dataParam.getObject("salActual");
		dataParam.put("SAL_WORK_DAYS",salWorkDays);
		dataParam.put("SAL_OVERTIME",salOvertime);
		dataParam.put("SAL_LEAVE",salLeave);
		dataParam.put("SAL_YEAR_LEAVE",salYearLeave);
		dataParam.put("SAL_OFFSET_VACATION",salOffsetVacation);
		dataParam.put("SAL_BONUS",salBonus);
		dataParam.put("SAL_TOTAL",salTotal);
		dataParam.put("SAL_SHOULD",salShould);
		dataParam.put("SAL_ACTUAL",salActual);
		dataParam.put("salProbationDayMoney",salProbationDayMoney);
		dataParam.put("salRegularDayMoney",salRegularDayMoney);
		return dataParam;
		
	}
	private DataParam buildOverTimeRecords(DataParam dataParam,Date regularTime,Date date,Date lastDateMonth,String userId,
			HashMap<String,DataRow> totalOverTimeDaysMap,HashMap<String,List<DataRow>> currentMonthOverTimeRecordsMap,HashMap<String,List<DataRow>> beforeMonthOverTimeRecordsMap){
		DataRow totalOverTimeRow = totalOverTimeDaysMap.get(userId);
		List<DataRow> currentMonthOverTimeRecords = currentMonthOverTimeRecordsMap.get(userId);
		List<DataRow> beforeMonthOverTimeRecords = null;
		if(!MapUtil.isNullOrEmpty(beforeMonthOverTimeRecordsMap)){
			beforeMonthOverTimeRecords = beforeMonthOverTimeRecordsMap.get(userId);
		}
		Double currentMonthWotTime = 0.0;
		Double beforeMonthWotTime = 0.0;
		DataRow currentMonthOverTimeRow = new DataRow();
		DataRow beforeMonthOverTimeRow = new DataRow();
		String currentMonthWotTimeStr = "";
		String beforeMonthOverTimeStr = "";
		BigDecimal thisMonthOverDayDecimal = new BigDecimal("0.0");
		BigDecimal beforeMonthOverDayDecimal = new BigDecimal("0.0");
		BigDecimal thisMonthTotalOverDayDecimal = new BigDecimal("0.0");
		Date beforeMonthWotDate = date;
		Date currentMonthWotDate = lastDateMonth;
		Long beforeMonthOverDay = 0L;
		Double totalProbationOverTime = 0.0;
		Double totalRetulartionOverTime = 0.0;
		if(!ListUtil.isNullOrEmpty(beforeMonthOverTimeRecords)){
			beforeMonthOverTimeRow = beforeMonthOverTimeRecords.get(beforeMonthOverTimeRecords.size()-1);
			beforeMonthOverTimeStr = beforeMonthOverTimeRow.getString("WOT_TIME");
			beforeMonthWotDate = (Date) beforeMonthOverTimeRow.get("WOT_DATE");
			beforeMonthWotTime = Double.parseDouble(beforeMonthOverTimeStr);
		}
		if(!ListUtil.isNullOrEmpty(currentMonthOverTimeRecords)){
			currentMonthOverTimeRow = currentMonthOverTimeRecords.get(currentMonthOverTimeRecords.size()-1);
			currentMonthWotTimeStr = currentMonthOverTimeRow.getString("WOT_TIME");
			currentMonthWotDate = (Date) currentMonthOverTimeRow.get("WOT_DATE");
			currentMonthWotTime = Double.parseDouble(currentMonthWotTimeStr);
		}else{
			currentMonthOverTimeRecords = new ArrayList<DataRow>();
		}
		if(!MapUtil.isNullOrEmpty(totalOverTimeRow)){
			Double totalOverTimeDouble = (Double) totalOverTimeRow.get("WOT_DAYS");
			if(totalOverTimeDouble!=null){
				thisMonthTotalOverDayDecimal = BigDecimal.valueOf(totalOverTimeDouble);
			}
		}
		if(beforeMonthWotTime>=2){
			Date beforeMonthOverTimeDate = DateUtil.getDateAdd(beforeMonthWotDate, DateUtil.DAY, beforeMonthWotTime.intValue());
			beforeMonthOverDay = DateUtil.getDateDiff(date,beforeMonthOverTimeDate, DateUtil.DAY);
			if(beforeMonthOverDay>0){
				beforeMonthOverDayDecimal = BigDecimal.valueOf(beforeMonthOverDay+1);
			}
		}else{
			beforeMonthOverDayDecimal = new BigDecimal("0.0");
		}
		dataParam = caluateOverTimeDays(dataParam, regularTime, date, lastDateMonth,
				currentMonthOverTimeRecords, currentMonthWotTime,
				thisMonthOverDayDecimal, beforeMonthOverDayDecimal,
				thisMonthTotalOverDayDecimal, currentMonthWotDate,totalProbationOverTime,totalRetulartionOverTime);
		return dataParam;
	}
	private DataParam caluateOverTimeDays(DataParam dataParam, Date regularTime,
			Date date, Date lastDateMonth,
			List<DataRow> currentMonthOverTimeRecords,
			Double currentMonthWotTime, BigDecimal thisMonthOverDayDecimal,
			BigDecimal beforeMonthOverDayDecimal,
			BigDecimal thisMonthTotalOverDayDecimal, Date currentMonthWotDate,Double totalProbationOverTime,Double totalRegulationOverTime) {
		DataRow currentMonthOverTimeRow;
		String currentMonthWotTimeStr;
		Long nextMonthOverDay;
		int thisMonthOverDay;
		if(DateUtil.getDateDiff(regularTime, date, DateUtil.MONTH)!=0){
			if(currentMonthWotTime>=2){
				thisMonthTotalOverDayDecimal = thisMonthTotalOverDayDecimal.subtract(BigDecimal.valueOf(currentMonthWotTime));
				int currentMonthWotTimeInt = currentMonthWotTime.intValue();
				Date currentMonthOverTimeDate = DateUtil.getDateAdd(currentMonthWotDate, DateUtil.DAY, currentMonthWotTimeInt);
				nextMonthOverDay = DateUtil.getDateDiff(lastDateMonth,currentMonthOverTimeDate, DateUtil.DAY);
				if(nextMonthOverDay>0){
					thisMonthOverDay = currentMonthWotTimeInt-nextMonthOverDay.intValue();
					thisMonthOverDayDecimal = BigDecimal.valueOf(thisMonthOverDay);
				}else{
					thisMonthTotalOverDayDecimal = thisMonthTotalOverDayDecimal.add(BigDecimal.valueOf(currentMonthWotTime));
				}
			}
			thisMonthTotalOverDayDecimal = thisMonthTotalOverDayDecimal.add(thisMonthOverDayDecimal).add(beforeMonthOverDayDecimal);
			if(DateUtil.getDateDiff(regularTime, date, DateUtil.MONTH)>0){
				totalRegulationOverTime = thisMonthTotalOverDayDecimal.doubleValue();
			}else{
				totalProbationOverTime = thisMonthTotalOverDayDecimal.doubleValue();
			}
		}else{
			for(int i=0;i<currentMonthOverTimeRecords.size();i++){
				Double probationOverTimeDay = 0.0;
				Double regularOverTimeDay = 0.0;
				currentMonthOverTimeRow = currentMonthOverTimeRecords.get(i);
				currentMonthWotTimeStr = currentMonthOverTimeRow.getString("WOT_TIME");
				currentMonthWotDate = (Date) currentMonthOverTimeRow.get("WOT_DATE");
				currentMonthWotTime = Double.parseDouble(currentMonthWotTimeStr);
				Date currentMonthOverTimeDate = DateUtil.getDateAdd(currentMonthWotDate, DateUtil.DAY, currentMonthWotTime.intValue());
				Long regularOverTimeDiff = DateUtil.getDateDiff(regularTime,currentMonthOverTimeDate, DateUtil.DAY);
				if(regularOverTimeDiff<0){
					probationOverTimeDay = currentMonthWotTime;
				}else if(currentMonthWotTime>=2&&regularOverTimeDiff>0&&DateUtil.getDateDiff(regularTime,currentMonthWotDate,DateUtil.DAY)<0&&DateUtil.getDateDiff(lastDateMonth,currentMonthOverTimeDate, DateUtil.DAY)<0){
					probationOverTimeDay = currentMonthWotTime-regularOverTimeDiff;
					regularOverTimeDay = regularOverTimeDiff.doubleValue();
				}else if(currentMonthWotTime>=2&&regularOverTimeDiff>0&&DateUtil.getDateDiff(regularTime,currentMonthWotDate,DateUtil.DAY)<0&&DateUtil.getDateDiff(lastDateMonth,currentMonthOverTimeDate, DateUtil.DAY)>0){
					probationOverTimeDay = currentMonthWotTime.doubleValue()-regularOverTimeDiff.doubleValue();
					nextMonthOverDay = DateUtil.getDateDiff(lastDateMonth,currentMonthOverTimeDate, DateUtil.DAY);
					regularOverTimeDiff = regularOverTimeDiff-nextMonthOverDay;
					regularOverTimeDay = regularOverTimeDiff.doubleValue();
				}else if(regularOverTimeDiff>0&&DateUtil.getDateDiff(regularTime, currentMonthWotDate, DateUtil.DAY)>0&&DateUtil.getDateDiff(lastDateMonth,currentMonthOverTimeDate, DateUtil.DAY)<0){
					regularOverTimeDay = currentMonthWotTime;
				}else if(regularOverTimeDiff>0&&DateUtil.getDateDiff(regularTime, currentMonthWotDate, DateUtil.DAY)>0&&DateUtil.getDateDiff(lastDateMonth,currentMonthOverTimeDate, DateUtil.DAY)>=0){
					int currentMonthWotTimeInt = currentMonthWotTime.intValue();
					nextMonthOverDay = DateUtil.getDateDiff(lastDateMonth,currentMonthOverTimeDate, DateUtil.DAY);
					thisMonthOverDay = currentMonthWotTimeInt-nextMonthOverDay.intValue();
					regularOverTimeDay = (double) thisMonthOverDay;
				}
				totalProbationOverTime += probationOverTimeDay;
				totalRegulationOverTime += regularOverTimeDay;
			}
			totalProbationOverTime += beforeMonthOverDayDecimal.doubleValue();
			thisMonthTotalOverDayDecimal = BigDecimal.valueOf(totalProbationOverTime+totalRegulationOverTime);
		}
		dataParam.put("thisMonthTotalOverDayDecimal",thisMonthTotalOverDayDecimal);
		dataParam.put("totalRegulationOverTime",totalRegulationOverTime);
		dataParam.put("totalProbationOverTime",totalProbationOverTime);
		return dataParam;
	}
	private DataParam calculateLeaveDays(DataParam dataParam,Date regularTime,String userId,HashMap<String,DataRow> totalLeaveDaysMap,
			HashMap<String,DataRow> currentYearLeaveDaysMap,HashMap<String,List<DataRow>> currentMonthLeaveDayRecordsMap){
		double probationLeaveDay = 0;
		double regulationLeaveDay = 0.0;
		Double currentYearLeaveDay = 0.0;
		double totalLeaveDay = 0.0;
		DataRow currentYearLeaveDayRow = null;
		if(!MapUtil.isNullOrEmpty(currentYearLeaveDaysMap)){
			currentYearLeaveDayRow = currentYearLeaveDaysMap.get(userId);
		}
		List<DataRow> currentMonthLeaveDayList = currentMonthLeaveDayRecordsMap.get(userId);
		if(ListUtil.isNullOrEmpty(currentMonthLeaveDayList)){
			currentMonthLeaveDayList = new ArrayList<DataRow>();
		}
		if(MapUtil.isNullOrEmpty(currentYearLeaveDayRow)){
			currentYearLeaveDayRow = new DataRow();
		}else{
			currentYearLeaveDay = (Double) currentYearLeaveDayRow.get("LEAVE_DAYS");
		}
		double totalYearLeaveDay = currentYearLeaveDay;
		for(int i =0;i<currentMonthLeaveDayList.size();i++){
			DataRow currentMonthLeaveDayRow = currentMonthLeaveDayList.get(i);
			String leaveDayStr = currentMonthLeaveDayRow.getString("LEA_DAYS");
			Date beginDate = (Date) currentMonthLeaveDayRow.get("LEA_SDATE");
			Date endDate = (Date) currentMonthLeaveDayRow.get("LEA_EDATE");
			Double leaveDay = Double.parseDouble(leaveDayStr);
			Double leaveDayFloorDouble = Math.floor(leaveDay);
			long regularDateDiff = DateUtil.getDateDiff(regularTime, beginDate, DateUtil.MONTH);
			if(regularDateDiff>0){
				regulationLeaveDay += leaveDay;
			}else{
				if(leaveDayFloorDouble>=2&&regularDateDiff==0&&DateUtil.getDateDiff(beginDate, regularTime, DateUtil.DAY)>=0&&DateUtil.getDateDiff(regularTime, endDate, DateUtil.DAY)>0){
					Date beginDateWeekDay = DateUtil.getBeginOfWeek(beginDate);
					Long weekDateDiff = DateUtil.getDateDiff(beginDateWeekDay,regularTime, DateUtil.DAY);
					Long dateDiff = DateUtil.getDateDiff(beginDate,regularTime, DateUtil.DAY);
					int weeks = weekDateDiff.intValue()/7;
					probationLeaveDay = probationLeaveDay+dateDiff.intValue()-(weeks*2);
					regulationLeaveDay = regulationLeaveDay+leaveDay-dateDiff.intValue()+(weeks*2);
				}else if(regularDateDiff==0&&DateUtil.getDateDiff(beginDate, regularTime, DateUtil.DAY)<0){
					regulationLeaveDay += leaveDay;
				}else if(regularDateDiff==0&&DateUtil.getDateDiff(regularTime, endDate, DateUtil.DAY)<0){
					probationLeaveDay += leaveDay;
				}else if(regularDateDiff<0){
					probationLeaveDay += leaveDay;
				}
			}
		}
		totalLeaveDay = regulationLeaveDay+probationLeaveDay;
		dataParam.put("regulationLeaveDay",regulationLeaveDay);
		dataParam.put("probationLeaveDay",probationLeaveDay);
		dataParam.put("totalYearLeaveDay",totalYearLeaveDay);
		dataParam.put("totalLeaveDay",totalLeaveDay);
		return dataParam;
	}
	private DataParam calculateOffsetVacationDays(DataParam dataParam,DataRow row,Date regularTime,Date date,String userId,HashMap<String,DataRow> beforeYearDecemberOffsetVationDaysMap,
			HashMap<String,DataRow> beforeMonthOffsetVationDaysMap){
		double regulationLeaveDay = (double) dataParam.getObject("regulationLeaveDay");
		double probationLeaveDay = (double) dataParam.getObject("probationLeaveDay");
		double totalRegulationOverTime = (double) dataParam.getObject("totalRegulationOverTime");
		double totalProbationOverTime = (double)dataParam.getObject("totalProbationOverTime");
		double totalLeaveDay = (double) dataParam.getObject("totalLeaveDay");
		BigDecimal thisMonthTotalOverDayDecimal = (BigDecimal) dataParam.getObject("thisMonthTotalOverDayDecimal");
		Double totalOffsetVacationDay = 0.0;
		Double probationOffsetVacationDay = 0.0;
		Double regulartionOffsetVacationDay = 0.0;
		BigDecimal beforeMonthOffsetVacationDay = new BigDecimal("0.0");
		BigDecimal beforeYearDecemberOffsetVationDay = new BigDecimal("0.0");
		DataRow beforeMonthOffSetRow = beforeMonthOffsetVationDaysMap.get(userId);
		DataRow beforeYearDecemberOffsetVationDayRow = beforeYearDecemberOffsetVationDaysMap.get(userId);
		int annualLeaveDay = row.getInt("EMP_ANNUAL_LEAVE_DAYS",0);
		Date jan = DateUtil.getBeginOfYear(date);
 		if(!MapUtil.isNullOrEmpty(beforeMonthOffsetVationDaysMap)){
			beforeMonthOffsetVacationDay = (BigDecimal) beforeMonthOffSetRow.get("SAL_OFFSET_VACATION");
			if(beforeMonthOffsetVacationDay.compareTo(BigDecimal.ZERO)<0){
				beforeMonthOffsetVacationDay  = new BigDecimal("0.0");
			}
		}
		if(!MapUtil.isNullOrEmpty(beforeYearDecemberOffsetVationDayRow)){
			beforeYearDecemberOffsetVationDay = (BigDecimal) beforeYearDecemberOffsetVationDayRow.get("SAL_OFFSET_VACATION");
		}
		if(DateUtil.getDateDiff(regularTime, date, DateUtil.MONTH)>0){
			if(DateUtil.getDateDiff(jan, date, DateUtil.DAY)==0){
				regulartionOffsetVacationDay = totalRegulationOverTime-regulationLeaveDay+annualLeaveDay;
			}else{
				regulartionOffsetVacationDay = beforeMonthOffsetVacationDay.doubleValue()+totalRegulationOverTime-regulationLeaveDay;
			}
			totalOffsetVacationDay = regulartionOffsetVacationDay;
		}else if(DateUtil.getDateDiff(regularTime, date, DateUtil.MONTH)==0){
			probationOffsetVacationDay = totalProbationOverTime-probationLeaveDay;
			regulartionOffsetVacationDay = totalRegulationOverTime-regulationLeaveDay+annualLeaveDay;
			if(probationOffsetVacationDay<0){
				probationOffsetVacationDay = probationOffsetVacationDay+beforeMonthOffsetVacationDay.doubleValue();
				if(probationOffsetVacationDay<0){
					totalOffsetVacationDay = regulartionOffsetVacationDay;
				}else{
					totalOffsetVacationDay = thisMonthTotalOverDayDecimal.doubleValue()-totalLeaveDay+beforeMonthOffsetVacationDay.doubleValue()+annualLeaveDay;;
				}
			}else{
				totalOffsetVacationDay = thisMonthTotalOverDayDecimal.doubleValue()-totalLeaveDay+beforeMonthOffsetVacationDay.doubleValue()+annualLeaveDay;;
			}	
		}else{
			probationOffsetVacationDay = totalProbationOverTime-probationLeaveDay+beforeMonthOffsetVacationDay.doubleValue();
			totalOffsetVacationDay = probationOffsetVacationDay;
		}
		dataParam.put("probationOffsetVacationDay",probationOffsetVacationDay);
		dataParam.put("regulartionOffsetVacationDay",regulartionOffsetVacationDay);
		dataParam.put("beforeYearDecemberOffsetVationDay",beforeYearDecemberOffsetVationDay);
		dataParam.put("salOffsetVacation",BigDecimal.valueOf(totalOffsetVacationDay));
		return dataParam;
	}
	private DataParam calculateTotalSaray(DataParam dataParam,Date regularTime,Date inductionDate,Date date,String userId,Double beforeYearDecemberValidDay,
			HashMap<String,DataRow> beforeYearDecemberSalaryRecords,HashMap<String,DataRow> overRunDayRecordMap,
			HashMap<String,DataRow> punishmentMap,HashMap<String,DataRow> rewardMap,
			List<DataParam> insertAdditionalVationParamList,List<DataParam> updateOverRunParamList,List<DataParam> insertOverRunParamList,
			HashMap<String,DataRow> probationWorkDayMap,HashMap<String,DataRow> regularWorkDayMap,HashMap<String,DataRow> attendanceRecordMap,List<DataParam> insertfullTimeParamList,List<DataParam> updateFullTimeParamList,
			HashMap<String,DataRow> fullTimeRecordMap,HashMap<String,DataRow> salaryRecordMap,HashMap<String, List<DataRow>> beforeYearOffesetVationDaysMap,HashMap<String,List<DataRow>> totalLeaveRecordsMap,
			HashMap<String, List<DataRow>> totalOverTimeRecordsMap,HashMap<String, DataRow> additionalVationRecordMap,List<DataParam> updateAdditionalVationParamList){
		Date feb = DateUtil.getDateAdd(DateUtil.getBeginOfYear(date), DateUtil.MONTH, 1);
		BigDecimal salBasic = (BigDecimal) dataParam.getObject("SAL_BASIC");
		BigDecimal salInsure = (BigDecimal) dataParam.getObject("SAL_INSURE");
		BigDecimal salPerformance = (BigDecimal) dataParam.getObject("SAL_PERFORMANCE");
		BigDecimal salSubsidy = (BigDecimal) dataParam.getObject("SAL_SUBSIDY"); 
		BigDecimal salTax = (BigDecimal) dataParam.getObject("SAL_TAX");
		BigDecimal salHousingFund = (BigDecimal) dataParam.getObject("SAL_HOUSING_FUND");
		BigDecimal empProbation = (BigDecimal) dataParam.getObject("empProbation");
		Double validDay = (Double) dataParam.getObject("validDay");
		BigDecimal beforeYearDecemberOffsetVationDay = (BigDecimal) dataParam.getObject("beforeYearDecemberOffsetVationDay");
		BigDecimal validDecimal = BigDecimal.valueOf(validDay);
		BigDecimal salTotal  = salBasic.add(salPerformance).add(salSubsidy);
		BigDecimal salShould = salTotal.subtract(salInsure).subtract(salTax).subtract(salHousingFund);
		BigDecimal salProbationDayMoney = empProbation.divide(validDecimal,4,RoundingMode.HALF_UP);
		BigDecimal salRegularDayMoney = salTotal.divide(validDecimal,4,RoundingMode.HALF_UP);
		BigDecimal salAuthal = new BigDecimal("0.0");
		BigDecimal beforeYearDecemberRewordSalary = new BigDecimal("0.000");
		Double probationOffsetVacationDay = (Double) dataParam.getObject("probationOffsetVacationDay");
		Double regulartionOffsetVacationDay = (Double) dataParam.getObject("regulartionOffsetVacationDay");
		DataParam insertAdditionalVationParam = new DataParam();
		DataParam overRunParam = new DataParam();
		BigDecimal probationOverRunSalary = salProbationDayMoney.multiply(BigDecimal.valueOf(probationOffsetVacationDay));   
		BigDecimal regulartionOverRunSalary = salRegularDayMoney.multiply(BigDecimal.valueOf(regulartionOffsetVacationDay));
		dataParam = calculateShouldSalray(dataParam,regularTime,inductionDate,date,userId,beforeYearDecemberValidDay,beforeYearDecemberSalaryRecords,overRunDayRecordMap,
insertAdditionalVationParamList,updateOverRunParamList,insertOverRunParamList,feb,beforeYearDecemberOffsetVationDay,beforeYearDecemberRewordSalary,salAuthal,
insertAdditionalVationParam,overRunParam,probationOverRunSalary,regulartionOverRunSalary,salProbationDayMoney,salRegularDayMoney,probationWorkDayMap,
regularWorkDayMap,punishmentMap,rewardMap,attendanceRecordMap,insertfullTimeParamList,updateFullTimeParamList,fullTimeRecordMap,salaryRecordMap,salShould,totalLeaveRecordsMap,beforeYearOffesetVationDaysMap,
totalOverTimeRecordsMap,additionalVationRecordMap,updateAdditionalVationParamList); 
		dataParam.put("salTotal",salTotal);
		dataParam.put("salShould",salShould);
		dataParam.put("salProbationDayMoney",salProbationDayMoney);
		dataParam.put("salRegularDayMoney",salRegularDayMoney);
		dataParam.put("probationOverRunSalary",probationOverRunSalary);
		dataParam.put("regulartionOverRunSalary",regulartionOverRunSalary);
		return dataParam;
	}
	private DataParam calculateShouldSalray(DataParam dataParam, Date regularTime,Date inductionDate,Date date,
			String userId, double beforeYearDecemberValidDay,
			HashMap<String, DataRow> beforeYearDecemberSalaryRecords,
			HashMap<String, DataRow> overRunDayRecordMap,
			List<DataParam> insertAdditionalVationParamList,
			List<DataParam> updateOverRunParamList,
			List<DataParam> insertOverRunParamList, Date feb,
			BigDecimal beforeYearDecemberOffsetVationDay,
			BigDecimal beforeYearDecemberRewordSalary, BigDecimal salAuthal,
			DataParam insertAdditionalVationParam, DataParam overRunParam,
			BigDecimal probationOverRunSalary,
			BigDecimal regulartionOverRunSalary,
			BigDecimal salProbationDayMoney,
			BigDecimal salRegularDayMoney,
			HashMap<String,DataRow> probationWorkDayMap,
			HashMap<String,DataRow> regularWorkDayMap,
			HashMap<String,DataRow> punishmentMap,
			HashMap<String,DataRow> rewardMap,
			HashMap<String,DataRow> attendanceRecordMap,List<DataParam> insertfullTimeParamList,List<DataParam> updateFullTimeParamList
			,HashMap<String,DataRow> fullTimeRecordMap,HashMap<String,DataRow> salaryRecordMap,BigDecimal salShould,HashMap<String,List<DataRow>> totalLeaveRecordsMap,HashMap<String, List<DataRow>> beforeYearOffesetVationDaysMap,
			HashMap<String, List<DataRow>> totalOverTimeRecordsMap,HashMap<String, DataRow> additionalVationRecordMap,List<DataParam> updateAdditionalVationParamList) {
		Double probationDays = 0.0;
		Double regularDays = 0.0;
		BigDecimal punishmentSalary = new BigDecimal("0.00");
		BigDecimal rewardSalary = new BigDecimal("0.00");
		double regulationLeaveDay = (double) dataParam.getObject("regulationLeaveDay");
		double probationLeaveDay = (double) dataParam.getObject("probationLeaveDay");
		Double validDay = (Double) dataParam.getObject("validDay");
		Double fulltimeAwardMoney = (Double) dataParam.getObject("fulltimeAwardMoney");
		BigDecimal fullTimeAward = BigDecimal.valueOf(fulltimeAwardMoney);
		int inDays = 0;
		int outDays = 0;
		BigDecimal probationSalary = new BigDecimal("0.00");
		BigDecimal regulartionSalary = new BigDecimal("0.00");
		BigDecimal beforeYearDecemberDayMoney =  new BigDecimal("0.0000");
		DataParam fullTimeParam = new DataParam();
		if(!MapUtil.isNullOrEmpty(probationWorkDayMap)){
			DataRow probationWorkDayRow = probationWorkDayMap.get(userId);
			if(!MapUtil.isNullOrEmpty(probationWorkDayRow)){
				Long workDay = (Long)probationWorkDayRow.get("WORK_DAYS");
				probationDays = workDay.doubleValue();
				probationDays += probationLeaveDay;
			}
		}
		if(!MapUtil.isNullOrEmpty(regularWorkDayMap)){
			DataRow regularWorkDayRow = regularWorkDayMap.get(userId);
			if(!MapUtil.isNullOrEmpty(regularWorkDayRow)){
				Long workDay = (Long)regularWorkDayRow.get("WORK_DAYS");
				regularDays = workDay.doubleValue();
				regularDays += regulationLeaveDay;
			}
		}
		if(!MapUtil.isNullOrEmpty(punishmentMap)){
			DataRow punishmentRow = punishmentMap.get(userId);
			if(!MapUtil.isNullOrEmpty(punishmentRow)){
				punishmentSalary = (BigDecimal) punishmentRow.get("BP_MONEY");
			}
		}
		if(!MapUtil.isNullOrEmpty(rewardMap)){
			DataRow rewardRow = rewardMap.get(userId);
			if(!MapUtil.isNullOrEmpty(rewardRow)){
				rewardSalary = (BigDecimal) rewardRow.get("BP_MONEY");
			}
		}
		if(!MapUtil.isNullOrEmpty(attendanceRecordMap)){
			DataRow attendanceRecordRow = attendanceRecordMap.get(userId);
			if(!MapUtil.isNullOrEmpty(attendanceRecordRow)){
				Long nums = (Long) attendanceRecordRow.get("IN_NUM");
				inDays = nums.intValue();
				nums = (Long) attendanceRecordRow.get("OUT_NUM");
				outDays = nums.intValue();
			}
		}
		boolean isFeb = (DateUtil.getDateDiff(date, feb, DateUtil.MONTH)==0);
		boolean isRegular = (DateUtil.getDateDiff(date, regularTime, DateUtil.MONTH)<0);
		boolean isFullTime = (inDays+3>=validDay&&outDays+3>=validDay);
		boolean isLeave = (regulationLeaveDay>0||probationLeaveDay>0);
		if(!MapUtil.isNullOrEmpty(beforeYearDecemberSalaryRecords)){
			DataRow beforeYearDecemberSalaryRow = beforeYearDecemberSalaryRecords.get(userId);
			if(!MapUtil.isNullOrEmpty(beforeYearDecemberSalaryRow)){
				BigDecimal empTotal = (BigDecimal) beforeYearDecemberSalaryRow.get("SAL_TOTAL");
				beforeYearDecemberDayMoney = empTotal.divide(BigDecimal.valueOf(beforeYearDecemberValidDay),4,RoundingMode.HALF_UP);
				beforeYearDecemberRewordSalary = beforeYearDecemberDayMoney.multiply(beforeYearDecemberOffsetVationDay);
			}
		}
		boolean isAutoAdditionalVation = (beforeYearDecemberRewordSalary.compareTo(BigDecimal.ZERO)>0);
		boolean isProbationOverRun = (probationOverRunSalary.compareTo(BigDecimal.ZERO)<0);
		boolean isRegularOverRun = (regulartionOverRunSalary.compareTo(BigDecimal.ZERO)<0);
		if(DateUtil.getDateDiff(date, regularTime, DateUtil.MONTH)==0&&DateUtil.getDateDiff(date, regularTime, DateUtil.DAY)>0){
			probationSalary = salProbationDayMoney.multiply(BigDecimal.valueOf(probationDays));
			regulartionSalary = salRegularDayMoney.multiply(BigDecimal.valueOf(regularDays));
			salAuthal = salAuthal.add(probationSalary).add(regulartionSalary);
		}else if(DateUtil.getDateDiff(date, inductionDate, DateUtil.MONTH)==0){
			probationSalary = salProbationDayMoney.multiply(BigDecimal.valueOf(probationDays));
			salAuthal = salAuthal.add(probationSalary);
		}else{
			salAuthal = salAuthal.add(salShould);
		}
		salAuthal = salAuthal.add(rewardSalary).subtract(punishmentSalary);
		dataParam.put("beforeYearDecemberRewordSalary",beforeYearDecemberRewordSalary);
		dataParam.put("beforeYearDecemberOffsetVationDay",beforeYearDecemberOffsetVationDay);
		dataParam.put("beforeYearDecemberDayMoney",beforeYearDecemberDayMoney);
		dataParam = buildRewordList(dataParam,regularTime,date,userId,overRunDayRecordMap,additionalVationRecordMap,insertAdditionalVationParamList,updateAdditionalVationParamList,updateOverRunParamList,
				insertOverRunParamList,beforeYearDecemberRewordSalary,salAuthal,insertAdditionalVationParam,overRunParam,
probationOverRunSalary,regulartionOverRunSalary,insertfullTimeParamList,fullTimeAward,fullTimeParam,isFeb,isRegular,isFullTime,isLeave,isAutoAdditionalVation,isProbationOverRun,
isRegularOverRun,updateFullTimeParamList,fullTimeRecordMap,fulltimeAwardMoney,totalLeaveRecordsMap,beforeYearOffesetVationDaysMap,totalOverTimeRecordsMap,salProbationDayMoney);
		salAuthal = (BigDecimal)dataParam.getObject("salActual");
		dataParam.put("salActual",salAuthal);
		dataParam.put("beforeYearDecemberRewordSalary",beforeYearDecemberRewordSalary);
		return dataParam;
	}
	private DataParam buildRewordList(DataParam dataParam,Date regularTime, Date date, String userId,
			HashMap<String, DataRow> overRunDayRecordMap,
			HashMap<String, DataRow> additionalVationRecordMap,
			List<DataParam> insertAdditionalVationParamList,
			List<DataParam> updateAdditionalVationParamList,
			List<DataParam> updateOverRunParamList,
			List<DataParam> insertOverRunParamList,
			BigDecimal beforeYearDecemberRewordSalary, BigDecimal salAuthal,
			DataParam additionalVationParam, DataParam overRunParam,
			BigDecimal probationOverRunSalary,
			BigDecimal regulartionOverRunSalary,
			List<DataParam> insertfullTimeParamList, BigDecimal fullTimeAward,
			DataParam fullTimeParam, boolean isFeb, boolean isRegular,
			boolean isFullTime, boolean isLeave,
			boolean isAutoAdditionalVation, boolean isProbationOverRun,
			boolean isRegularOverRun,List<DataParam> updateFullTimeParamList
			,HashMap<String,DataRow> fullTimeRecordMap,Double fulltimeAwardMoney,HashMap<String,List<DataRow>> totalLeaveRecordsMap,HashMap<String, List<DataRow>> beforeYearOffesetVationDaysMap,
			HashMap<String, List<DataRow>> totalOverTimeRecordsMap,BigDecimal salProbationDayMoney) {
		BigDecimal salBonus = new BigDecimal("0.00");
		dataParam = calculateBeforeYearProbationDays(dataParam,regularTime,userId,totalLeaveRecordsMap,beforeYearOffesetVationDaysMap,totalOverTimeRecordsMap); 
		beforeYearDecemberRewordSalary = (BigDecimal) dataParam.getObject("beforeYearDecemberRewordSalary");
		BigDecimal probationAdditionalVationSalary = (BigDecimal) dataParam.getObject("probationAdditionalVationSalary");
		BigDecimal totalOffsetDays = (BigDecimal) dataParam.getObject("salOffsetVacation");
		boolean isCanOverRun  = (totalOffsetDays.compareTo(BigDecimal.ZERO)<0);
		if(!isLeave&&isFullTime&&isRegular){
			salAuthal = salAuthal.add(fullTimeAward);
			fullTimeParam.put("BP_ID",KeyGenerator.instance().genKey());
			fullTimeParam.put("USER_ID",userId);
			fullTimeParam.put("BP_DATE",date);
			fullTimeParam.put("BP_TYPE","FULLTIME");
			fullTimeParam.put("BP_MONEY",fulltimeAwardMoney);
			salBonus = salBonus.add(BigDecimal.valueOf(fulltimeAwardMoney));
		}
		if(isFeb&&isAutoAdditionalVation){
			additionalVationParam.put("BP_ID",KeyGenerator.instance().genKey());
			additionalVationParam.put("USER_ID",userId);
			additionalVationParam.put("BP_DATE",date);
			additionalVationParam.put("BP_TYPE","ADDITIONALVATION");
			additionalVationParam.put("BP_MONEY",beforeYearDecemberRewordSalary);
			salAuthal = salAuthal.add(beforeYearDecemberRewordSalary);
			salBonus = salBonus.add(beforeYearDecemberRewordSalary);
		}
		if(isFeb&&probationAdditionalVationSalary.compareTo(BigDecimal.ZERO)>0){
			additionalVationParam.put("BP_ID",KeyGenerator.instance().genKey());
			additionalVationParam.put("USER_ID",userId);
			additionalVationParam.put("BP_DATE",date);
			additionalVationParam.put("BP_TYPE","ADDITIONALVATION");
			additionalVationParam.put("BP_MONEY",probationAdditionalVationSalary.doubleValue());
			salAuthal = salAuthal.add(probationAdditionalVationSalary);
			salBonus = salBonus.add(probationAdditionalVationSalary);
		}
		if(isProbationOverRun||isRegularOverRun){
			overRunParam.put("BP_ID",KeyGenerator.instance().genKey());
			overRunParam.put("USER_ID",userId);
			overRunParam.put("BP_DATE",date);
			overRunParam.put("BP_TYPE","OVERRUN");
			if(isProbationOverRun&&isRegularOverRun&&isCanOverRun){
				overRunParam.put("BP_MONEY",(probationOverRunSalary.add(regulartionOverRunSalary)).abs());
				salBonus = salBonus.add(probationOverRunSalary.add(regulartionOverRunSalary));
				salAuthal = salAuthal.add(regulartionOverRunSalary).add(probationOverRunSalary);
			}else if(isProbationOverRun&&!isCanOverRun){
				overRunParam.put("BP_MONEY",probationOverRunSalary.abs());
				salAuthal = salAuthal.add(probationOverRunSalary);
				salBonus = salBonus.add(probationOverRunSalary);
			}else if(isRegularOverRun&&isCanOverRun){
				overRunParam.put("BP_MONEY",regulartionOverRunSalary.abs());
				salBonus = salBonus.add(regulartionOverRunSalary);
				salAuthal = salAuthal.add(regulartionOverRunSalary);
			}
		}
		if(!MapUtil.isNullOrEmpty(overRunDayRecordMap)&&overRunDayRecordMap.containsKey(userId)){
			updateOverRunParamList.add(overRunParam);
		}else if(!MapUtil.isNullOrEmpty(overRunParam)){
			insertOverRunParamList.add(overRunParam);
		}
		if(!MapUtil.isNullOrEmpty(fullTimeRecordMap)&&fullTimeRecordMap.containsKey(userId)){
			updateFullTimeParamList.add(fullTimeParam);
		}else if(!MapUtil.isNullOrEmpty(fullTimeParam)){
			insertfullTimeParamList.add(fullTimeParam);
		}
		if(!MapUtil.isNullOrEmpty(additionalVationRecordMap)&&additionalVationRecordMap.containsKey(userId)){
			updateAdditionalVationParamList.add(additionalVationParam);
		}else if(!MapUtil.isNullOrEmpty(additionalVationParam)){
			insertAdditionalVationParamList.add(additionalVationParam);
		}
		dataParam.put("salActual",salAuthal);
		dataParam.put("salBonus",salBonus);
		return dataParam;
	}
	private DataParam calculateBeforeYearProbationDays(DataParam dataParam,Date regularTime,
			String userId, HashMap<String, List<DataRow>> totalLeaveRecordsMap, HashMap<String, List<DataRow>> beforeYearOffesetVationDaysMap,
			HashMap<String, List<DataRow>> totalOverTimeRecordsMap) {
		BigDecimal beforeYearDecemberRewordSalary = (BigDecimal) dataParam.getObject("beforeYearDecemberRewordSalary");
		BigDecimal beforeYearDecemberOffsetVationDay = (BigDecimal) dataParam.getObject("beforeYearDecemberOffsetVationDay");
		BigDecimal beforeYearDecemberDayMoney = (BigDecimal) dataParam.getObject("beforeYearDecemberDayMoney");
		Double probationAdditionalVationDays = 0.0;
		BigDecimal beforeYearTotalOffsetVationDay = new BigDecimal("0.0");
		BigDecimal empProbation = new BigDecimal("0.0");
		BigDecimal validDays = new BigDecimal("0.0");
		BigDecimal probationAdditionalVationSalary = new BigDecimal("0.00");
		Double totalRegularLeaveDay = 0.0;
		Double totalOverTime = 0.0;
		Date befroreRegularMonth = DateUtil.getDateAdd(DateUtil.getBeginOfMonth(regularTime), DateUtil.DAY, -1);
		String regularYearMonth = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, regularTime);
		String regularYearStr = regularYearMonth.substring(0,4);
		String regularMonthStr = regularYearMonth.substring(5,7);
		int regularYear = Integer.parseInt(regularYearStr);
		int regularMonth = Integer.parseInt(regularMonthStr);
		if(!MapUtil.isNullOrEmpty(beforeYearOffesetVationDaysMap)){
			List<DataRow> beforeYearOffesetVationDayList = beforeYearOffesetVationDaysMap.get(userId);
			for(int i=0;i<beforeYearOffesetVationDayList.size();i++){
				DataRow beforeYearOffesetVationDayRow = beforeYearOffesetVationDayList.get(i);
				BigDecimal offsetDecimal = (BigDecimal) beforeYearOffesetVationDayRow.get("SAL_OFFSET_VACATION");
				String salMonth = (String) beforeYearOffesetVationDayRow.get("SAL_MONTH");
				String salYear = (String) beforeYearOffesetVationDayRow.get("SAL_YEAR");
				int salMonthInt = Integer.parseInt(salMonth);
				int salYearInt = Integer.parseInt(salYear);
				if(salYearInt!=regularYear){
					break;
				}
				if(salMonthInt<regularMonth){
					beforeYearTotalOffsetVationDay = beforeYearTotalOffsetVationDay.add(offsetDecimal);
				}else if(salMonthInt==regularMonth){
					empProbation =(BigDecimal) beforeYearOffesetVationDayRow.get("EMP_PROBATION"); 
					validDays = (BigDecimal) beforeYearOffesetVationDayRow.get("SAL_VALID_DAYS");
					continue;
				}else{
					continue;
				}
			}
		}
		if(!MapUtil.isNullOrEmpty(totalOverTimeRecordsMap)){
			List<DataRow> totalOverTimeRecordsList = totalOverTimeRecordsMap.get(userId);
			if(!ListUtil.isNullOrEmpty(totalOverTimeRecordsList)){
				for(int i=0;i<totalOverTimeRecordsList.size();i++){
					DataRow beforeYearOffesetVationDayRow = totalOverTimeRecordsList.get(i);
					Date wotDate = (Date) beforeYearOffesetVationDayRow.get("WOT_DATE");
					String wotTimeStr = (String) beforeYearOffesetVationDayRow.get("WOT_TIME");
					Double wotTime = Double.parseDouble(wotTimeStr);
					Date workOverToDate = DateUtil.getDateAdd(wotDate, DateUtil.DAY,wotTime.intValue());
					if(DateUtil.getDateDiff(wotDate, regularTime, DateUtil.YEAR)!=0){
						break;
					}
					if(DateUtil.getDateDiff(befroreRegularMonth,wotDate,DateUtil.MONTH)==0&&DateUtil.getDateDiff(befroreRegularMonth,workOverToDate,DateUtil.DAY)>0){
						Long dateDiff = DateUtil.getDateDiff(befroreRegularMonth,workOverToDate,DateUtil.DAY);
						totalOverTime+=dateDiff.doubleValue();
					}else if(DateUtil.getDateDiff(wotDate, regularTime, DateUtil.MONTH)==0){
						if(DateUtil.getDateDiff(regularTime,wotDate,DateUtil.DAY)>=0){
							continue;
						}else if(DateUtil.getDateDiff(regularTime,wotDate,DateUtil.DAY)<0&&DateUtil.getDateDiff(regularTime,workOverToDate,DateUtil.DAY)>0){
							Long dateDiff = DateUtil.getDateDiff(regularTime,workOverToDate,DateUtil.DAY);
							totalOverTime+=wotTime-dateDiff.doubleValue();
						}else if(DateUtil.getDateDiff(regularTime,wotDate,DateUtil.DAY)<0&&DateUtil.getDateDiff(regularTime,workOverToDate,DateUtil.DAY)<=0){
							totalOverTime+=wotTime;
						}else{
							continue;
						}
					}else{
						continue;
					}
				}
			}
		}
		if(!MapUtil.isNullOrEmpty(totalLeaveRecordsMap)){
			List<DataRow> totalLeaveRecords = totalLeaveRecordsMap.get(userId);
			if(!ListUtil.isNullOrEmpty(totalLeaveRecords)){
				for(int i=0;i<totalLeaveRecords.size();i++){
					DataRow totalLeaveRow = totalLeaveRecords.get(i);
					Date leaSdate = (Date) totalLeaveRow.get("LEA_SDATE");
					String leaDayStr = (String) totalLeaveRow.get("LEA_DAYS");
					Date leaEdate = (Date) totalLeaveRow.get("LEA_EDATE");
					Double leaDay = Double.parseDouble(leaDayStr);
					if(DateUtil.getDateDiff(leaSdate, regularTime, DateUtil.MONTH)==0){
						if(DateUtil.getDateDiff(leaSdate, regularTime, DateUtil.DAY)==0){
							totalRegularLeaveDay+=leaDay;
						}else if(DateUtil.getDateDiff(regularTime,leaSdate,DateUtil.DAY)>0){
							totalRegularLeaveDay+=leaDay;
						}else if(DateUtil.getDateDiff(regularTime,leaSdate, DateUtil.DAY)<0&&DateUtil.getDateDiff(regularTime,leaEdate, DateUtil.DAY)>0){
							Date beginDateWeekDay = DateUtil.getBeginOfWeek(leaSdate);
							Long weekDateDiff = DateUtil.getDateDiff(beginDateWeekDay,regularTime, DateUtil.DAY);
							Long dateDiff = DateUtil.getDateDiff(leaSdate,regularTime, DateUtil.DAY);
							int weeks = weekDateDiff.intValue()/7;
							totalRegularLeaveDay = totalRegularLeaveDay+leaDay-dateDiff.intValue()+(weeks*2);
						}else{
							continue;
						}
					}else if(DateUtil.getDateDiff(regularTime,leaSdate, DateUtil.MONTH)>0){
						totalRegularLeaveDay+=leaDay;
					}else{
						continue;
					}
				}
			}
		}
		probationAdditionalVationDays = beforeYearTotalOffsetVationDay.doubleValue()+totalOverTime-totalRegularLeaveDay;
		if(validDays.compareTo(BigDecimal.ZERO)!=0){
			probationAdditionalVationSalary = empProbation.divide(validDays, RoundingMode.HALF_UP).multiply(BigDecimal.valueOf(probationAdditionalVationDays));
		}
		if(probationAdditionalVationDays>0){
			beforeYearDecemberOffsetVationDay = beforeYearDecemberOffsetVationDay.subtract(BigDecimal.valueOf(probationAdditionalVationDays));
			beforeYearDecemberRewordSalary = beforeYearDecemberDayMoney.multiply(beforeYearDecemberOffsetVationDay);
		}
		dataParam.put("probationAdditionalVationDays",probationAdditionalVationDays);
		dataParam.put("probationAdditionalVationSalary",probationAdditionalVationSalary);
		dataParam.put("beforeYearDecemberRewordSalary",beforeYearDecemberRewordSalary);
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
		DataParam updateParam = new DataParam();
		String dateStr = year+"-"+month+"-31";
		String currentDateStr = year+"-"+month+"-01";
		Date date = DateUtil.getDate(currentDateStr);
		Date jan = DateUtil.getBeginOfYear(date);
		Double totalOverTimeDays = 0.0;
		Double totalLeaveDays = 0.0;
		BigDecimal totalOffsetDayDecimal=new BigDecimal(0.0);
		Date currentDate = DateUtil.getDate(dateStr);
		Date beforeMonthLastDay = DateUtil.getDateAdd(date, DateUtil.DAY, -1);
		String yearMonth = year+"-"+month;
		String beginOfYearDateStr = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL, jan);
		String beforeMonthLastDayStr = DateUtil.format(DateUtil.YYMMDD_HORIZONTAL,beforeMonthLastDay);
		String statementId = sqlNameSpace+"."+"getUserAnnualLeaveDays";
		DataRow userAnnualLeaveDaysRow = this.daoHelper.getRecord(statementId, new DataParam("userId",userId));
		statementId = sqlNameSpace + "." + "currentMonthLeaveDayRecords";
		HashMap<String,List<DataRow>> currentMonthDaysMap = queryRecords(
				"USER_ID", statementId, new DataParam("yearMonth",yearMonth));
		statementId = sqlNameSpace + "." + "findOverTimeRecords";
		HashMap<String,List<DataRow>> overTimeRecordMap = queryRecords(
				"USER_ID",statementId, new DataParam("yearMonth",yearMonth));
		statementId = sqlNameSpace+"."+"leaveDayRecords";
		DataRow leaveDaysRow = this.daoHelper.getRecord(statementId, new DataParam("userId",userId,"sdate",beginOfYearDateStr,"edate",beforeMonthLastDayStr));
		statementId = sqlNameSpace+"."+"overTimeDayRecords";
		DataRow overTimeRow = this.daoHelper.getRecord(statementId, new DataParam("userId",userId,"sdate",beginOfYearDateStr,"edate",beforeMonthLastDayStr));
		HashMap<String,DataRow> totalOverTimeDaysMap =  this.daoHelper.queryRecords("USER_ID", statementId, new DataParam("yearMonth",yearMonth));
		statementId = sqlNameSpace+"."+"getMonthlyInfo";
		DataRow monthlyInfoRow = this.daoHelper.getRecord(statementId, new DataParam("year",year,"month",month,"userId",userId));
		Date regularTime = (Date)userAnnualLeaveDaysRow.get("EMP_REGULAR_TIME");
		String annualLeaveDays = (String) userAnnualLeaveDaysRow.get("EMP_ANNUAL_LEAVE_DAYS");
		BigDecimal annualLeaveDaysDecimal = new BigDecimal(annualLeaveDays);
		DataParam param = new DataParam();
		if(!MapUtil.isNullOrEmpty(overTimeRow)){
			totalOverTimeDays = (Double) overTimeRow.get("WOT_DAYS");
		}
		if(!MapUtil.isNullOrEmpty(leaveDaysRow)){
			totalLeaveDays = (Double) leaveDaysRow.get("LEAVE_DAYS");
		}
		param = calculateLeaveDays(param, regularTime,userId,null,null,currentMonthDaysMap);
		param = buildOverTimeRecords(param,regularTime,date,currentDate, userId,totalOverTimeDaysMap,overTimeRecordMap,null);
		BigDecimal thisMonthTotalOverDayDecimal = (BigDecimal) param.getObject("thisMonthTotalOverDayDecimal");
		Double totalRegulationOverTime = (Double) param.getObject("totalRegulationOverTime");
		Double totalProbationOverTime = (Double) param.getObject("totalProbationOverTime");
		Double regulationLeaveDay = (Double) param.getObject("regulationLeaveDay");
		Double probationLeaveDay = (Double) param.getObject("probationLeaveDay");
		Double totalLeaveDay = (Double) param.getObject("totalLeaveDay");
		if(DateUtil.getDateDiff(jan, date, DateUtil.DAY)==0){
			totalOffsetDayDecimal = BigDecimal.valueOf(totalOverTimeDays-totalLeaveDays).add(annualLeaveDaysDecimal);
		}else if(DateUtil.getDateDiff(regularTime, date, DateUtil.MONTH)>0){
			totalOffsetDayDecimal = thisMonthTotalOverDayDecimal.subtract(BigDecimal.valueOf(totalLeaveDay)).subtract(BigDecimal.valueOf(totalLeaveDays)).add(annualLeaveDaysDecimal).add(BigDecimal.valueOf(totalOverTimeDays));
		}else if(DateUtil.getDateDiff(regularTime, date, DateUtil.MONTH)==0){
			Double probationOffsetVacationDay = totalProbationOverTime-probationLeaveDay;
			Double regulartionOffsetVacationDay = totalRegulationOverTime-regulationLeaveDay+annualLeaveDaysDecimal.doubleValue();
			if(probationOffsetVacationDay<0){
				probationOffsetVacationDay = probationOffsetVacationDay+totalOverTimeDays-totalLeaveDays;
				if(probationOffsetVacationDay<0){
					totalOffsetDayDecimal = BigDecimal.valueOf(regulartionOffsetVacationDay);
				}else{
					totalOffsetDayDecimal = thisMonthTotalOverDayDecimal.subtract(BigDecimal.valueOf(totalLeaveDay)).subtract(BigDecimal.valueOf(totalLeaveDays)).add(annualLeaveDaysDecimal).add(BigDecimal.valueOf(totalOverTimeDays));
				}
			}else{
				totalOffsetDayDecimal = thisMonthTotalOverDayDecimal.subtract(BigDecimal.valueOf(totalLeaveDay)).subtract(BigDecimal.valueOf(totalLeaveDays)).add(annualLeaveDaysDecimal).add(BigDecimal.valueOf(totalOverTimeDays));
			}	
		}else{
			totalOffsetDayDecimal = thisMonthTotalOverDayDecimal.subtract(BigDecimal.valueOf(totalLeaveDay)).subtract(BigDecimal.valueOf(totalLeaveDays)).add(BigDecimal.valueOf(totalOverTimeDays));
		}
		updateParam.put("SAL_ID",monthlyInfoRow.get("SAL_ID"));
		updateParam.put("SAL_OFFSET_VACATION",totalOffsetDayDecimal);
		updateParam.put("SAL_OVERTIME",thisMonthTotalOverDayDecimal);
		updateParam.put("SAL_LEAVE",totalLeaveDay);
		statementId = sqlNameSpace+"."+"updateOffsetVacationrecord";
		this.daoHelper.updateRecord(statementId, updateParam);
	}
	@Override
	public void recalculation(String year,String month){
		
	}
	@Override
	public List<DataRow> findFiveMonthsSalayInfos(DataParam param) {
		String statementId = sqlNameSpace+"."+"findFiveMonthsSalayInfos";
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}
}
