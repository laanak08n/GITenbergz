package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeSelectServiceImpl;
import com.agileai.hr.cxmodule.SecurityUserTreeSelect;

public class SecurityUserTreeSelectImpl
        extends TreeSelectServiceImpl
        implements SecurityUserTreeSelect {
    public SecurityUserTreeSelectImpl() {
        super();
    }
    
	@Override
	public List<DataRow> findChildGroupRecords(String parentId) {
		String statementId = sqlNameSpace+".findChildGroupRecords";
		DataParam param = new DataParam("parentId",parentId);
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}    
}
