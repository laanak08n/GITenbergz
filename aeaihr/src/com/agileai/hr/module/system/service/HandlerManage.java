package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.hotweb.bizmoduler.core.MasterSubService;

public interface HandlerManage
        extends MasterSubService {
	
	public List<String> retrieveHandlerIdList(String funcId);
	public List<String> retrieveUserIdList(String funcId);
	public List<String> retrieveRoleIdList(String funcId);
	public List<String> retrieveGroupIdList(String funcId);
}
