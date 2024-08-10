package com.agileai.hr.bizmoduler.evection;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;

public interface HrEvectionManage
        extends MasterSubService {
	
	public void computeTotalMoney(String masterRecordId);
	public void approveMasterRecord(DataParam param);
}
