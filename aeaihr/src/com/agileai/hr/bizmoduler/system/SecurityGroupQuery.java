package com.agileai.hr.bizmoduler.system;

import com.agileai.hotweb.bizmoduler.core.QueryModelService;

public interface SecurityGroupQuery
        extends QueryModelService {
	public void addGroupTreeRelation(String roleId,String[] groupIds);
	public void delGroupTreeRelation(String roleId,String groupId);
}
