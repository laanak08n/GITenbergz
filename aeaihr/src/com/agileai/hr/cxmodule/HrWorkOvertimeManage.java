package com.agileai.hr.cxmodule;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;

public interface HrWorkOvertimeManage
        extends StandardService {
	 DataRow getNowRecord(DataParam param);

	void approveRecord(DataParam param);
	
	List<DataRow> findOvertimeList(DataParam param);
	void submitRecord(DataParam param);
}
