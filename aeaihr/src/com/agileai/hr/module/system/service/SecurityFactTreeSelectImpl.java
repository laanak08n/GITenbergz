package com.agileai.hr.module.system.service;

import java.util.ArrayList;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeSelectServiceImpl;

public class SecurityFactTreeSelectImpl
        extends TreeSelectServiceImpl
        implements SecurityFactTreeSelect {
    public SecurityFactTreeSelectImpl() {
        super();
    }

    @Override
	public List<DataRow> findChildGroupRecords(String parentId) {
		String statementId = sqlNameSpace+".findChildGroupRecords";
		DataParam param = new DataParam("parentId",parentId);
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}

	@Override
	public void addFactAuthRelation(String resourceType, String resourceId,
			List<String> factIdList) {
		String statementId = this.sqlNameSpace + ".retrieveFactLists";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < factIdList.size();i++){
			String factId = factIdList.get(i);
			DataParam param = new DataParam();
			param.put("RG_AUTH_ID",KeyGenerator.instance().genKey());
			param.put("RG_ID",factId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			paramList.add(param);
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public List<DataRow> retrieveFactList(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".retrieveFactList";
		DataParam param = new DataParam("resourceType",resourceType,"resourceId",resourceId);
		return this.daoHelper.queryRecords(statementId, param);
	}

	@Override
	public void addFactAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, List<String> factIdList) {
		String statementId = this.sqlNameSpace + ".addFactAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
			for (int j=0;j < factIdList.size();j++){
				String factId = factIdList.get(j);
				DataParam param = new DataParam();
				param.put("RG_AUTH_ID",KeyGenerator.instance().genKey());
				param.put("RG_ID",factId);
				param.put("RES_TYPE",resourceType);
				param.put("RES_ID",resourceId);
				paramList.add(param);
			}			
		}
		this.daoHelper.batchInsert(statementId, paramList);
	}

	@Override
	public void delFactAuthRelation(String resourceType, String resourceId,
			String factId) {
		String statementId = this.sqlNameSpace + ".delFactAuthRelation";
		DataParam param = new DataParam();
		param.put("RG_ID",factId);
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
		
	}

	@Override
	public void delFactAuthRelation(List<String> resourceTypes,
			List<String> resourceIds, String factId) {
		String statementId = this.sqlNameSpace + ".delFactAuthRelation";
		List<DataParam> paramList = new ArrayList<DataParam>();
		for (int i=0;i < resourceTypes.size();i++){
			String resourceType = resourceTypes.get(i);
			String resourceId = resourceIds.get(i);
					
			DataParam param = new DataParam();
			param.put("RG_ID",factId);
			param.put("RES_TYPE",resourceType);
			param.put("RES_ID",resourceId);
			paramList.add(param);
		}
		this.daoHelper.batchDelete(statementId, paramList);
		
	}

	@Override
	public void delFactAuthRelations(String resourceType, String resourceId) {
		String statementId = this.sqlNameSpace + ".delFactAuthRelation";
		DataParam param = new DataParam();
		param.put("RES_TYPE",resourceType);
		param.put("RES_ID",resourceId);
		this.daoHelper.deleteRecords(statementId, param);
	}

	@Override
	public void delFactAuthRelations(List<String> resourceTypes,
			List<String> resourceIds) {
		String statementId = this.sqlNameSpace + ".delFactAuthRelation";
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
	public List<DataRow> findRootGroupRecords(String parentId) {
		String statementId = sqlNameSpace+".findRootGroupRecords";
		DataParam param = new DataParam("parentId",parentId);
		List<DataRow> result = this.daoHelper.queryRecords(statementId, param);
		return result;
	}

}
