package com.agileai.hr.module.information.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;

public interface HrEmployeeManage
        extends MasterSubService {
	String approveRecord(DataParam param);
	void revokeApprovalRecords(String empId);
	DataRow getSalaryLimitRecord();
	void updateSalaryLimitRecord(DataParam param);
}
