package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeManageImpl;

public class SecurityRoleTreeManageImpl
        extends TreeManageImpl
        implements SecurityRoleTreeManage {
    public SecurityRoleTreeManageImpl() {
        super();
        this.idField = "ROLE_ID";
        this.nameField = "ROLE_NAME";
        this.parentIdField = "ROLE_PID";
        this.sortField = "ROLE_SORT";
    }
    
    public void deleteCurrentRecord(String currentId){
    	super.deleteCurrentRecord(currentId);
    	String statementId = "SecurityAuthorizationConfig.delRoleAuthRelation";
    	DataParam param = new DataParam("ROLE_ID",currentId);
    	this.daoHelper.deleteRecords(statementId, param);
    }

	@Override
	public List<DataRow> queryAuthRecords(DataParam param) {
		String statementId = "SecurityRole.queryAuthRecords";
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}
}
