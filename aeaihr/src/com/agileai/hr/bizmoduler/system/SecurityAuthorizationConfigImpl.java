package com.agileai.hr.bizmoduler.system;

import java.util.ArrayList;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.BaseService;
import com.agileai.hotweb.domain.core.Resource;

public class SecurityAuthorizationConfigImpl extends BaseService implements
		SecurityAuthorizationConfig {

	@Override
	public List<DataRow> retrieveGroupList(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".retrieveGroupList";
		DataParam param = new DataParam("resourceType",resourceType,"resourceId",resourceId);
		return this.daoHelper.queryRecords(statementId, param);
	}

	@Override
	public List<DataRow> retrieveRoleList(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".retrieveRoleList";
		DataParam param = new DataParam("resourceType",resourceType,"resourceId",resourceId);
		return this.daoHelper.queryRecords(statementId, param);
	}

	@Override
	public List<DataRow> retrieveUserList(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".retrieveUserList";
		DataParam param = new DataParam("resourceType",resourceType,"resourceId",resourceId);
		return this.daoHelper.queryRecords(statementId, param);
	}

	@Override
	public void addUserAuthRelation(String resourceType, String resourceId,
			List<String> userIdList) {
		String statementId = this.sqlNameSpace + ".addUserAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < userIdList.size();i++){
			String userId = userIdList.get(i);
			DataParam param = new DataParam();
			param.put("USER_AUTH_ID",KeyGenerator.instance().genKey());
			param.put("USER_ID",userId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			paramList.add(param);
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void addRoleAuthRelation(String resourceType, String resourceId,
			List<String> roleIdList) {
		String statementId = this.sqlNameSpace + ".addRoleAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < roleIdList.size();i++){
			String roleId = roleIdList.get(i);
			DataParam param = new DataParam();
			param.put("ROLE_AUTH_ID",KeyGenerator.instance().genKey());
			param.put("ROLE_ID",roleId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			paramList.add(param);
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void addGroupAuthRelation(String resourceType, String resourceId,
			List<String> groupIdList) {
		String statementId = this.sqlNameSpace + ".addGroupAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < groupIdList.size();i++){
			String groupId = groupIdList.get(i);
			DataParam param = new DataParam();
			param.put("GRP_AUTH_ID",KeyGenerator.instance().genKey());
			param.put("GRP_ID",groupId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			paramList.add(param);
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void delUserAuthRelation(String resourceType, String resourceId,
			String userId) {
		String statementId = this.sqlNameSpace + ".delUserAuthRelation";
		DataParam param = new DataParam();
		param.put("USER_ID",userId);
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
	}

	@Override
	public void delRoleAuthRelation(String resourceType, String resourceId,
			String roleId) {
		String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
		DataParam param = new DataParam();
		param.put("ROLE_ID",roleId);
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
	}

	@Override
	public void delGroupAuthRelation(String resourceType, String resourceId,
			String groupId) {
		String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
		DataParam param = new DataParam();
		param.put("GRP_ID",groupId);
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
	}

	@Override
	public void delUserAuthRelations(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".delUserAuthRelation";
		DataParam param = new DataParam();
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
	}

	@Override
	public void delRoleAuthRelations(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
		DataParam param = new DataParam();
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
	}

	@Override
	public void delGroupAuthRelations(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
		DataParam param = new DataParam();
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
	}

	@Override
	public void delPortletAuthRelations(String portletId) {
		DataParam delParam = new DataParam();
		delParam.put("RES_TYPE",Resource.Type.Portlet);
		delParam.put("RES_ID",portletId);
		
		String statementId = this.sqlNameSpace+".delRoleAuthRelation";
		this.daoHelper.deleteRecords(statementId, delParam);
		
		statementId = this.sqlNameSpace+".delUserAuthRelation";
		this.daoHelper.deleteRecords(statementId, delParam);
		
		statementId = this.sqlNameSpace+".delGroupAuthRelation";
		this.daoHelper.deleteRecords(statementId, delParam);
	}

	@Override
	public DataRow retrieveUserRecord(String userCode) {
		String statementId = this.sqlNameSpace+".getSecurityUserRecord";
		DataParam param = new DataParam("USER_CODE",userCode,"USER_STATE","1");
		return this.daoHelper.getRecord(statementId, param);
	}

	@Override
	public List<DataRow> retrieveRoleRecords(String userId) {
		String statementId = this.sqlNameSpace + ".retrieveRoleRecords";
		DataParam param = new DataParam("userId",userId);
		return this.daoHelper.queryRecords(statementId, param);
	}

	@Override
	public List<DataRow> retrieveGroupRecords(String userId) {
		String statementId = this.sqlNameSpace + ".retrieveGroupRecords";
		DataParam param = new DataParam("userId",userId);
		return this.daoHelper.queryRecords(statementId, param);
	}

	@Override
	public List<String> retrieveMenuIdList(String userId) {
		List<String> result = new ArrayList<String>();
		String statementId = this.sqlNameSpace + ".retrieveMenuIdList";
		DataParam param = new DataParam("userId",userId);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		for (int i=0;i < records.size();i++){
			DataRow row = records.get(i);
			String menuId = row.stringValue("FUNC_ID");
			result.add(menuId);
		}
		return result;
	}
	@Override
	public List<String> retrieveHandlerIdList(String userId) {
		List<String> result = new ArrayList<String>();
		String statementId = this.sqlNameSpace + ".retrieveHandlerIdList";
		DataParam param = new DataParam("userId",userId);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		for (int i=0;i < records.size();i++){
			DataRow row = records.get(i);
			String menuId = row.stringValue("HANDLER_ID");
			result.add(menuId);
		}
		return result;
	}

	@Override
	public List<String> retrieveOperationIdList(String userId) {
		List<String> result = new ArrayList<String>();
		String statementId = this.sqlNameSpace + ".retrieveOperationIdList";
		DataParam param = new DataParam("userId",userId);
		List<DataRow> records = this.daoHelper.queryRecords(statementId, param);
		for (int i=0;i < records.size();i++){
			DataRow row = records.get(i);
			String menuId = row.stringValue("OPER_ID");
			result.add(menuId);
		}
		return result;
	}
	
	@Override
	public void addUserAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, List<String> userIdList) {
		String statementId = this.sqlNameSpace + ".addUserAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			for (int j=0;j < userIdList.size();j++){
				String userId = userIdList.get(j);
				DataParam param = new DataParam();
				param.put("USER_AUTH_ID",KeyGenerator.instance().genKey());
				param.put("USER_ID",userId);
				param.put("RES_TYPE",resourceType);
				param.put("RES_ID",resourceId);
				paramList.add(param);
			}			
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void addRoleAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, List<String> roleIdList) {
		String statementId = this.sqlNameSpace + ".addRoleAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			for (int j=0;j < roleIdList.size();j++){
				String roleId = roleIdList.get(j);
				DataParam param = new DataParam();
				param.put("ROLE_AUTH_ID",KeyGenerator.instance().genKey());
				param.put("ROLE_ID",roleId);
				param.put("RES_TYPE",resourceType);
				param.put("RES_ID",resourceId);
				paramList.add(param);
			}			
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void addGroupAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, List<String> groupIdList) {
		String statementId = this.sqlNameSpace + ".addGroupAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			for (int j=0;j < groupIdList.size();j++){
				String groupId = groupIdList.get(j);
				DataParam param = new DataParam();
				param.put("GRP_AUTH_ID",KeyGenerator.instance().genKey());
				param.put("GRP_ID",groupId);
				param.put("RES_TYPE",resourceType);
				param.put("RES_ID",resourceId);
				paramList.add(param);
			}
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void delUserAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, String userId) {
		String statementId = this.sqlNameSpace + ".delUserAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			DataParam param = new DataParam();
			param.put("USER_ID",userId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			paramList.add(param);
		}
		this.daoHelper.batchDelete(statementId, paramList);
	}

	@Override
	public void delRoleAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, String roleId) {
		String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
					
			DataParam param = new DataParam();
			param.put("ROLE_ID",roleId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			paramList.add(param);
		}
		this.daoHelper.batchDelete(statementId, paramList);
	}

	@Override
	public void delGroupAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, String groupId) {
		String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			
			DataParam param = new DataParam();
			param.put("GRP_ID",groupId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			
			paramList.add(param);
		}
		this.daoHelper.batchDelete(statementId, paramList);
	}

	@Override
	public void delUserAuthRelations(List<String> resourceTypes,
			List<String> resourceIds) {
		String statementId = this.sqlNameSpace + ".delUserAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			DataParam param = new DataParam();
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			
			paramList.add(param);
		}
		this.daoHelper.batchDelete(statementId, paramList);
	}

	@Override
	public void delRoleAuthRelations(List<String> resourceTypes,
			List<String> resourceIds) {
		String statementId = this.sqlNameSpace + ".delRoleAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			DataParam param = new DataParam();
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			
			paramList.add(param);
		}
		this.daoHelper.batchDelete(statementId, paramList);
		
	}

	@Override
	public void delGroupAuthRelations(List<String> resourceTypes,
			List<String> resourceIds) {
		String statementId = this.sqlNameSpace + ".delGroupAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			DataParam param = new DataParam();
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			
			paramList.add(param);
		}
		this.daoHelper.batchDelete(statementId, paramList);
	}

	@Override
	public DataRow retriveUserInfoRecord(String userCode) {
		String statementId = "SecurityGroup8Associates.getSecurityUserRecord";
		DataParam param = new DataParam("USER_CODE",userCode);
		return this.daoHelper.getRecord(statementId, param);
	}

	@Override
	public void modifyUserPassword(String userCode,String userPwd) {
		String statementId = "SecurityGroup8Associates.updateSecurityUserPassword";
		DataParam param = new DataParam("USER_CODE",userCode,"USER_PWD",userPwd);
		this.daoHelper.updateRecord(statementId, param);
	}
}
