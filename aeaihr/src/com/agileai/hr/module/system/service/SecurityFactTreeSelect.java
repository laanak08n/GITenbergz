package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeSelectService;

public interface SecurityFactTreeSelect
        extends TreeSelectService {
	List<DataRow> findChildGroupRecords(String id);
	public void addFactAuthRelation(String resourceType, String resourceId,List<String> factIdList);
	List<DataRow> retrieveFactList(String resourceType, String resourceId);
	public void addFactAuthRelation(List<String> resourceTypes,List<String> resourceId,List<String> factIdList);
	public void delFactAuthRelation(String resourceType,String resourceId,String factId);
	public void delFactAuthRelation(List<String> resourceTypes,List<String> resourceIds,String factId);
	public void delFactAuthRelations(String resourceTypes,String resourceIds);
	public void delFactAuthRelations(List<String> resourceTypes,List<String> resourceIds);
	
	List<DataRow> findRootGroupRecords(String id);
}
