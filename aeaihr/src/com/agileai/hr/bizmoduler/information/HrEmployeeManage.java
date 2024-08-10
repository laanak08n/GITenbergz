package com.agileai.hr.bizmoduler.information;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;

public interface HrEmployeeManage
        extends MasterSubService {
	void approveRecord(DataParam param);
	void revokeApprovalRecords(String empId);

}
