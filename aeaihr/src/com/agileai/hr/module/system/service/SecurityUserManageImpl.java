package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManageImpl;
import com.agileai.util.CryptionUtil;
import com.agileai.util.StringUtil;

public class SecurityUserManageImpl 
		extends TreeAndContentManageImpl
		implements 	SecurityUserManage {
	public SecurityUserManageImpl() {
		super();
		this.columnIdField = "GRP_ID";
		this.columnParentIdField = "GRP_PID";
		this.columnSortField = "GRP_SORT";
	}

	@Override
	public DataRow getCopyContentRecord(DataParam param) {
		DataRow result=null;
		String statementId = sqlNameSpace+"."+"queryCopyTreeContentRelation";		
		List<DataRow> tempRecords = this.daoHelper.queryRecords(statementId, param);
		if (tempRecords.size()>=1){
			result=tempRecords.get(0);
		}
		return result;		
	}

	@Override
	public DataRow getMoveContentRecord(DataParam param) {
		DataRow result=null;
		String statementId = sqlNameSpace+"."+"queryMoveTreeContentRelation";		
		List<DataRow> tempRecords = this.daoHelper.queryRecords(statementId, param);
		if (tempRecords.size()>=1){
			result=tempRecords.get(0);
		}
		return result;		
	}

	@Override
	public DataRow getEmpCodeRecord(DataParam param) {
		DataRow result=null;
		String statementId = sqlNameSpace+"."+"queryEmpCodeRelation";		
		result = this.daoHelper.getRecord(statementId, param);
		return result;	
	}
	
	
	
	
	
	@Override
	public void deleteSecurityUserRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"deleteSecurityUserRecord";
		this.daoHelper.deleteRecords(statementId,param);
	}

	@Override
	public void deleteSecurityUserRelation(DataParam param) {
		String statementId = sqlNameSpace+"."+"deleteSecurityUserRelation";
		this.daoHelper.deleteRecords(statementId,param);
	}

	@Override
	public void createtContentRecord(String tabId,DataParam param) {
		String statementId = sqlNameSpace+"."+"insert"+StringUtil.upperFirst(tabId)+"Record";
		String curTableName = tabIdAndTableNameMapping.get(tabId);
		processDataType(param, curTableName);
		this.daoHelper.insertRecord(statementId, param);
		String tableMode = tabIdAndTableModeMapping.get(tabId);
		if (TableMode.Many2ManyAndRel.equals(tableMode)){
			statementId = sqlNameSpace+"."+"insert"+StringUtil.upperFirst(tabId)+"Relation";
			this.daoHelper.insertRecord(statementId, param);			
		}
	}

	@Override
	public List<DataRow> findPOSTreeRecords(DataParam param) {
		String statementId = this.sqlNameSpace+"."+"queryPOSTreeRecords";
		return this.daoHelper.queryRecords(statementId, param);
	}

	@Override
	public void createEmpRecord(String tabId,DataParam param) {
		String statementId = sqlNameSpace+"."+"insert"+StringUtil.upperFirst(tabId)+"Record";
		String curTableName = tabIdAndTableNameMapping.get(tabId);
		processDataType(param, curTableName);
		this.daoHelper.insertRecord(statementId, param);
	}

	@Override
	public String getEmpPassWordRecord(String empCode,String passType) {
		String result = null;
		String statementId = this.sqlNameSpace+"."+"getEmpPassWordRecord";
		DataParam param = new DataParam();
		param.put("EMP_CODE",empCode);
		DataRow row = this.daoHelper.getRecord(statementId, param);
		if(row!=null){
			result = row.getString(passType);
		}
		return result;
	}

	@Override
	public String updateEmpPassWordRecord(String empCode, String newpassword) {
		String statementId = this.sqlNameSpace+"."+"updateEmpPassWordRecord";
		String password = this.SendBackPassword(newpassword, empCode);
		String EMP_PWD_SEND=this.toSendPassword(password, empCode);
		DataParam param = new DataParam();
		param.put("EMP_PWD_ENTER",CryptionUtil.md5Hex(password));
		param.put("EMP_PWD_SEND",EMP_PWD_SEND);
		param.put("EMP_CODE",empCode);
		this.daoHelper.updateRecord(statementId, param);
		return EMP_PWD_SEND;
	}

	@Override
	public void updateEmpModifierRecord(DataParam param) {
		String statementId = this.sqlNameSpace+"."+"updateEmpModifierRecord";
		this.daoHelper.updateRecord(statementId, param);
	}

	@Override
	public DataRow getSysuserRecord(DataParam param) {
		DataRow result = new DataRow();
		String statementId = this.sqlNameSpace+"."+"getSysuserRecord";
		result = this.daoHelper.getRecord(statementId, param);
		return result;
	}

	protected String SendBackPassword(String sendPassword,String secretKey){
		String Password = "";
		String SendSK = "";
		StringBuffer SBSK = new StringBuffer(secretKey); 
		if(SBSK.length()>=8){
		SendSK = secretKey.substring(0, 8);
	}
	else{
		while(SBSK.length()<8){
			SBSK=SBSK.append("x");
		}
		SendSK = SBSK.toString();
	}
	Password=CryptionUtil.decryption(sendPassword, SendSK);
	return Password;
	}

	protected String toSendPassword(String password,String secretKey){
		String SendPW = "";
		String SendSK = "";
		StringBuffer SBSK = new StringBuffer(secretKey); 
		if(SBSK.length()>=8){
		SendSK = secretKey.substring(0, 8);
		}
		else{
			while(SBSK.length()<8){
				SBSK=SBSK.append("x");
			}
			SendSK = SBSK.toString();
		}
		SendPW=CryptionUtil.encryption(password, SendSK);
		return SendPW;
	}
	
	@Override
	public List<DataRow> queryUserRelationRecords(DataParam param) {
		String statementId = this.sqlNameSpace + "." + "querySecurityUserRelation";
		return this.daoHelper.queryRecords(statementId, param);
	}
	@Override
	public List<DataRow> queryUserGroupRelationRecords(DataParam param) {
		String statementId = "SecurityGroup8Associates.findUserRoleRelationRecords";
		return this.daoHelper.queryRecords(statementId, param);
	}
	@Override
	public List<DataRow> findUserAuthRecords(DataParam param) {
		String statementId = this.sqlNameSpace + "." + "findUserAuthRecords";
		return this.daoHelper.queryRecords(statementId, param);
	}
	@Override
	public List<DataRow> findUserRoleGroupRecords(DataParam param) {
		String statementId = "SecurityGroup8Associates.findUserRoleRelationRecords";
		return this.daoHelper.queryRecords(statementId, param);
	}
}
