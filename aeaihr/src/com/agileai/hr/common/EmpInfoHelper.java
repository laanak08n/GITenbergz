package com.agileai.hr.common;

import java.util.Date;

import com.agileai.util.DateUtil;

public class EmpInfoHelper {
	
	public boolean isInductionAboveMonth(Date inductionDate, String currentDate){
		if(currentDate.length() < 10){
			currentDate = currentDate + "-01";
		}
		Date date = DateUtil.getDateTime(currentDate);
		Date beginOfMonth = DateUtil.getBeginOfMonth(date);
		Date endOfMonth = DateUtil.getEndOfMonth(date);
		if(inductionDate.compareTo(beginOfMonth)>0 && inductionDate.compareTo(endOfMonth)<=0){
			return true;
		}
		return false;
	}
}
