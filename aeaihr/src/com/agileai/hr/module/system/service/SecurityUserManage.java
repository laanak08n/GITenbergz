package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;

public interface SecurityUserManage extends 
      TreeAndContentManage {
	public DataRow getCopyContentRecord (DataParam param);
	public DataRow getMoveContentRecord (DataParam param);
	public DataRow getEmpCodeRecord (DataParam param);
	public void deleteSecurityUserRecord(DataParam param);
	public void deleteSecurityUserRelation(DataParam param);
	public  List<DataRow> findPOSTreeRecords(DataParam param);
	public void createEmpRecord(String tabId,DataParam param);
	public String getEmpPassWordRecord (String empCode,String passType);
	public String updateEmpPassWordRecord(String empCode, String newpassword);
	public void updateEmpModifierRecord(DataParam param);
	public DataRow getSysuserRecord(DataParam param);
	
	public List<DataRow> findUserRoleGroupRecords (String grpId, String userId, String roleId);
	public List<DataRow> queryUserRelationRecords(String userId);
	public List<DataRow> findUserAuthRecords (String grpId, String userId);
}
