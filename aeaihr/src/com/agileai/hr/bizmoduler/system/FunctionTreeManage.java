package com.agileai.hr.bizmoduler.system;

import java.util.List;

import com.agileai.hotweb.bizmoduler.core.TreeManage;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;

public interface FunctionTreeManage
        extends TreeManage {
	public FuncMenu getFunction(String functionId);
	public List<FuncMenu> getFuncMenuList();

	public FuncHandler getFuncHandler(String handlerId);
	public Operation getOperation(String operationId);
	
	public void clearFuncTreeCache();
}
