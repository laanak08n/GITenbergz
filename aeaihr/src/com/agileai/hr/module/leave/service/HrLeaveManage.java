package com.agileai.hr.module.leave.service;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;

public interface HrLeaveManage extends StandardService {
	DataRow getNowRecord(DataParam param);
	void approveRecord(DataParam param);
}
