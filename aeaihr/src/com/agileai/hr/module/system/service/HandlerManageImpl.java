package com.agileai.hr.module.system.service;

import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.MasterSubServiceImpl;

public class HandlerManageImpl
        extends MasterSubServiceImpl
        implements HandlerManage {
    public HandlerManageImpl() {
        super();
    }

    public String[] getTableIds() {
        List<String> temp = new ArrayList<String>();
        temp.add("SysOperation");

        return temp.toArray(new String[] {  });
    }
    
	@Override
	public List<String> retrieveHandlerIdList(String funcId) {
		List<String> result = new ArrayList<String>();
		String statementId = this.sqlNameSpace + ".retrieveHanlerIdList";
		DataParam param = new DataParam("funcId",funcId);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		for (int i=0;i < records.size();i++){
			DataRow row = records.get(i);
			String hanlerId = row.stringValue("HANLER_ID");
			result.add(hanlerId);
		}
		return result;
	}

	@Override
	public List<String> retrieveUserIdList(String funcId) {
		List<String> result = new ArrayList<String>();
		String statementId = this.sqlNameSpace + ".retrieveUserIdList";
		DataParam param = new DataParam("funcId",funcId);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		for (int i=0;i < records.size();i++){
			DataRow row = records.get(i);
			String userId = row.stringValue("USER_ID");
			result.add(userId);
		}
		return result;
	}

	@Override
	public List<String> retrieveRoleIdList(String funcId) {
		List<String> result = new ArrayList<String>();
		String statementId = this.sqlNameSpace + ".retrieveRoleIdList";
		DataParam param = new DataParam("funcId",funcId);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		for (int i=0;i < records.size();i++){
			DataRow row = records.get(i);
			String roleId = row.stringValue("ROLE_ID");
			result.add(roleId);
		}
		return result;
	}

	@Override
	public List<String> retrieveGroupIdList(String funcId) {
		List<String> result = new ArrayList<String>();
		String statementId = this.sqlNameSpace + ".retrieveGroupIdList";
		DataParam param = new DataParam("funcId",funcId);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		for (int i=0;i < records.size();i++){
			DataRow row = records.get(i);
			String roleId = row.stringValue("GRP_ID");
			result.add(roleId);
		}
		return result;
	}
}
