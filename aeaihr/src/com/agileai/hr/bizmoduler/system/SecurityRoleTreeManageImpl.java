package com.agileai.hr.bizmoduler.system;

import com.agileai.domain.DataParam;
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
}
