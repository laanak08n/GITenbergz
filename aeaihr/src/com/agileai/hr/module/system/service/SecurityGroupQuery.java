package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.QueryModelService;

public interface SecurityGroupQuery
        extends QueryModelService {
	public void addGroupTreeRelation(DataParam param);
	public List<DataRow> queryPickTreeRecords(DataParam param);
	public void delGroupTreeRelation(String roleId,String groupId);
	public List<DataRow>  queryChildRecords(String rolId);
	public List<DataRow>  queryTreeorgNameRecords(String grpId);
	public List<DataRow>  queryTreeorgTypeRecords(String rolId);
}
