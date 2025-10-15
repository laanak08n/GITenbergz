package com.agileai.hr.module.evection.service;

import java.util.List;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;

public interface HrEvectionManage
        extends MasterSubService {
	
	public void computeTotalMoney(String masterRecordId);
	public void approveMasterRecord(DataParam param);
	public void updateStateRecord(DataParam param);
	public List<DataRow> findExpensesRecords(DataParam param);
}
