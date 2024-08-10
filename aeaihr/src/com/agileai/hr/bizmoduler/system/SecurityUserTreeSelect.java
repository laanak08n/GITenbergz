package com.agileai.hr.bizmoduler.system;

import java.util.List;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeSelectService;

public interface SecurityUserTreeSelect
        extends TreeSelectService {
	
	public List<DataRow> findChildGroupRecords(String parentId);
}
