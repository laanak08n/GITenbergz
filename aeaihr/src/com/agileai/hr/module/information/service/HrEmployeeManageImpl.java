package com.agileai.hr.module.information.service;

import java.util.ArrayList;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.MasterSubServiceImpl;
import com.agileai.util.CryptionUtil;

public class HrEmployeeManageImpl
        extends MasterSubServiceImpl
        implements HrEmployeeManage {
    public HrEmployeeManageImpl() {
        super();
    }
    
    public void createMasterRecord(DataParam param) {
   		String statementId = sqlNameSpace+"."+"insertMasterRecord";
   		processDataType(param, tableName);
   		processPrimaryKeys(tableName,param);
   		this.daoHelper.insertRecord(statementId, param);
    }
    
	public void updateMasterRecord(DataParam param) {
   		String statementId = sqlNameSpace+"."+"updateMasterRecord";
   		processDataType(param, tableName);
   		this.daoHelper.updateRecord(statementId, param);
   	}
	
	public String approveRecord(DataParam param) {
		String result = "用户信息同步失败";
		String statementId = sqlNameSpace + "." + "approveRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);
		
		DataParam userCodeParam = new DataParam();
		userCodeParam.put("userCode", param.get("EMP_CODE"));
		statementId = sqlNameSpace + "." + "getSecurityUserCode";
		DataRow dataRow = this.daoHelper.getRecord(statementId, userCodeParam);
		
		if(dataRow == null){
	   		statementId = sqlNameSpace+"."+"insertSecurityUserRecord";
	   		DataParam userParam = new DataParam();
	   		String userId = KeyGenerator.instance().genKey();
	   		userParam.put("USER_ID",userId);
	   		userParam.put("USER_CODE",param.get("EMP_CODE"));
	   		userParam.put("USER_NAME",param.get("EMP_NAME"));
	   		String encPassword = CryptionUtil.md5Hex(param.get("EMP_CODE"));
	   		userParam.put("USER_PWD",encPassword);
	   		userParam.put("USER_SEX",param.get("EMP_SEX"));
	   		String strDesc = "普通用户";
	   		userParam.put("USER_DESC",strDesc);
	   		String strState = "0";
	   		userParam.put("USER_STATE",strState);
	   		String strSort = "3";
	   		userParam.put("USER_SORT",strSort);
	   		userParam.put("USER_MAIL",param.get("EMP_EMAIL"));
	   		userParam.put("USER_PHONE",param.get("EMP_TEL"));
	   		this.daoHelper.insertRecord(statementId, userParam);
	   		
	   		statementId = sqlNameSpace+"."+"insertSecurityUserGroupRelRecord";
	   		userParam.put("GRP_ID", param.get("EMP_NOW_DEPT"));
	   		userParam.put("USER_ID",param.get("USER_ID"));
	   		this.daoHelper.insertRecord(statementId, userParam);
	   		result = "用户信息同步成功";
		}else{
	   		statementId = sqlNameSpace+"."+"updateSecurityUserRecord";
	   		DataParam userParam = new DataParam();
	   		userParam.put("USER_CODE",param.get("EMP_CODE"));
	   		userParam.put("USER_NAME",param.get("EMP_NAME"));
	   		String encPassword = CryptionUtil.md5Hex(param.get("EMP_CODE"));
	   		userParam.put("USER_PWD",encPassword);
	   		userParam.put("USER_SEX",param.get("EMP_SEX"));
	   		String strDesc = "普通用户";
	   		userParam.put("USER_DESC",strDesc);
	   		String strSort = "3";
	   		userParam.put("USER_SORT",strSort);
	   		userParam.put("USER_MAIL",param.get("EMP_EMAIL"));
	   		userParam.put("USER_PHONE",param.get("EMP_TEL"));
	   		this.daoHelper.updateRecord(statementId, param);
	   		result = "用户信息同步成功";
		}
		return result;
	}
	
	
	@Override
	public void revokeApprovalRecords(String empId) {
		DataParam param = new DataParam("EMP_ID", empId,"EMP_STATE","drafe");
		String statementId = sqlNameSpace+"."+"revokeApprovalRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);
		
	}

    public String[] getTableIds() {
        List<String> temp = new ArrayList<String>();

        temp.add("_base");
        temp.add("HrEducation");
        temp.add("HrExperience");
        temp.add("HrWorkPerformance");

        return temp.toArray(new String[] {  });
    }
}
