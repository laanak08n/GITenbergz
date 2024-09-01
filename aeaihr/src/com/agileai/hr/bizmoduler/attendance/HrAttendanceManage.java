package com.agileai.hr.bizmoduler.attendance;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.StandardService;

public interface HrAttendanceManage
        extends StandardService {
	public DataRow retrieveUserInfo(DataParam param);
	public void bindUserWxOpenId(String userCode,String wxOpenId);
}
