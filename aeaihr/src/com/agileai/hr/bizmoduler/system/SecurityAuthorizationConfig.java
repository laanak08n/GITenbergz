package com.agileai.hr.bizmoduler.system;

import java.util.List;

import com.agileai.domain.DataRow;

public interface SecurityAuthorizationConfig {
	public List<DataRow> retrieveGroupList(String resourceType,String resourceId);
	public List<DataRow> retrieveRoleList(String resourceType,String resourceId);
	public List<DataRow> retrieveUserList(String resourceType,String resourceId);
	
	public void addUserAuthRelation(String resourceType,String resourceId,List<String> userIdList);
	public void addRoleAuthRelation(String resourceType,String resourceId,List<String> roleIdList);
	public void addGroupAuthRelation(String resourceType,String resourceId,List<String> groupIdList);
	
	public void addUserAuthRelation(List<String> resourceTypes,List<String> resourceIds,List<String> userIdList);
	public void addRoleAuthRelation(List<String> resourceTypes,List<String> resourceId,List<String> roleIdList);
	public void addGroupAuthRelation(List<String> resourceType,List<String> resourceId,List<String> groupIdList);
	

	public void delUserAuthRelation(String resourceType,String resourceId,String userId);
	public void delRoleAuthRelation(String resourceType,String resourceId,String roleId);
	public void delGroupAuthRelation(String resourceType,String resourceId,String groupId);

	public void delUserAuthRelation(List<String> resourceTypes,List<String> resourceIds,String userId);
	public void delRoleAuthRelation(List<String> resourceTypes,List<String> resourceIds,String roleId);
	public void delGroupAuthRelation(List<String> resourceTypes,List<String> resourceIds,String groupId);
	
	
	public void delUserAuthRelations(String resourceType,String resourceId);
	public void delRoleAuthRelations(String resourceType,String resourceId);
	public void delGroupAuthRelations(String resourceType,String resourceId);

	public void delUserAuthRelations(List<String> resourceTypes,List<String> resourceIds);
	public void delRoleAuthRelations(List<String> resourceTypes,List<String> resourceIds);
	public void delGroupAuthRelations(List<String> resourceTypes,List<String> resourceIds);
	
	public void delPortletAuthRelations(String portletId);
	
	public DataRow retrieveUserRecord(String userCode);
	public List<DataRow> retrieveRoleRecords(String userId);
	public List<DataRow> retrieveGroupRecords(String userId);
	
	public List<String> retrieveMenuIdList(String userId);
	public List<String> retrieveHandlerIdList(String userId);
	public List<String> retrieveOperationIdList(String userId);
	
	public DataRow retriveUserInfoRecord(String userCode);
	public void modifyUserPassword(String userCode,String userPwd);
}
