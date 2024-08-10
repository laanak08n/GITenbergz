package com.agileai.hr.bizmoduler.salary;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;

public interface HrSalaryManage
        extends StandardService {
	
	public DataRow retrieveValidDays(String year,String month);
	
	public void createValidDayRecord(DataParam param);
	public void updateValidDayRecord(DataParam param);
	
	public void gatherData(String year,String month);
	public void computeTotalMoney(String masterRecordId);
	public void approveRecord(DataParam param);
	public void revokeApprovalRecords(String salId);
}
