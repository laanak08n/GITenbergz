package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeManage;

public interface SecurityRoleTreeManage
        extends TreeManage {
	public  List<DataRow> queryAuthRecords(DataParam param);
}
