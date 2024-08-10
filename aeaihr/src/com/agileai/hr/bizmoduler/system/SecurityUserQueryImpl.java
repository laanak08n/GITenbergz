package com.agileai.hr.bizmoduler.system;

import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.core.QueryModelServiceImpl;

public class SecurityUserQueryImpl
        extends QueryModelServiceImpl
        implements SecurityUserQuery {
    public SecurityUserQueryImpl() {
        super();
    }
	@Override
	public void addUserTreeRelation(String roleId, String[] groupIds) {
		String statementId = this.sqlNameSpace+".addUserTreeRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < groupIds.length;i++){
			DataParam dataParam = new DataParam("roleId",roleId,"userId",groupIds[i]);
			paramList.add(dataParam);
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void delUserTreeRelation(String roleId, String groupId) {
		String statementId = this.sqlNameSpace+".delUserTreeRelation";
		DataParam param = new DataParam("roleId",roleId,"userId",groupId);
		this.daoHelper.deleteRecords(statementId, param);
	}    
}
