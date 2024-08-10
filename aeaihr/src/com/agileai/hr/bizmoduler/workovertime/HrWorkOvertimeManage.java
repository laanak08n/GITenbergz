package com.agileai.hr.bizmoduler.workovertime;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;

public interface HrWorkOvertimeManage
        extends StandardService {
	 DataRow getNowRecord(DataParam param);

	void approveRecord(DataParam param);
}
