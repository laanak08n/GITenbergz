package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;

public interface SecurityGroupManage
        extends TreeAndContentManage {
	
	public void deleteTreeRecords(String currentId);
	void delGroupTreeRelation(DataParam param);
	public List<DataRow> queryRelationRecords (String currentId);
	public List<DataRow> queryTreeorgNameRecords (String currentId);
	public List<DataRow> queryTreeTypeRecords (DataParam param);
}
