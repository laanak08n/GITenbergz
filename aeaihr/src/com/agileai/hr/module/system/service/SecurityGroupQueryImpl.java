package com.agileai.hr.module.system.service;

import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.QueryModelServiceImpl;

public class SecurityGroupQueryImpl
        extends QueryModelServiceImpl
        implements SecurityGroupQuery {
    public SecurityGroupQueryImpl() {
        super();
    }

	@Override
	public void addGroupTreeRelation(String roleId, String[] groupIds) {
		String statementId = this.sqlNameSpace+".addGroupTreeRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < groupIds.length;i++){
			DataParam dataParam = new DataParam("roleId",roleId,"groupId",groupIds[i]);
			paramList.add(dataParam);
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void delGroupTreeRelation(String roleId, String groupId) {
		String statementId = this.sqlNameSpace+".delGroupTreeRelation";
		DataParam param = new DataParam("roleId",roleId,"groupId",groupId);
		this.daoHelper.deleteRecords(statementId, param);
	}
}
