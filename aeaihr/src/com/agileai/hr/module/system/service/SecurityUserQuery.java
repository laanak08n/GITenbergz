package com.agileai.hr.module.system.service;

import com.agileai.hotweb.bizmoduler.core.QueryModelService;

public interface SecurityUserQuery
        extends QueryModelService {
	public void addUserTreeRelation(String roleId,String[] userIds);
	public void delUserTreeRelation(String roleId,String userId);
}
