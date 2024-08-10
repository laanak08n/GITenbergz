package com.agileai.hr.bizmoduler.system;

import java.util.List;

import com.agileai.hotweb.bizmoduler.core.OperationAuthInterceptor;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;
import com.agileai.util.StringUtil;

public class HotwebAuthInterceptor implements OperationAuthInterceptor {

	@Override
	public boolean authenticate(Object userObject, BaseHandler handler,String actionType) {
		boolean result = true;
		if (userObject instanceof User){
			User user = (User)userObject;
			HotwebAuthHelper menuHelper = new HotwebAuthHelper(user);
			String currentFuncId = menuHelper.getCurrentFuncId();
			if (!StringUtil.isNullOrEmpty(currentFuncId) && !user.isAdmin()){
				FuncMenu funcMenu = menuHelper.getFunction(currentFuncId);
				if (funcMenu != null && !funcMenu.isFolder()){
					List<FuncHandler> funcHandlers = funcMenu.getHandlers();
					if (funcHandlers.size() > 1){
						String handlerCode = handler.getHandlerId();
						FuncHandler funcHandler = getHandler(funcHandlers, handlerCode);
						
						if (funcHandler != null && funcHandler.getOperations().size() > 0){
							List<Operation> operations = funcHandler.getOperations();
							for (int i=0;i < operations.size();i++){
								Operation operation = operations.get(i);
								String tempActionType = operation.getActionType();
								
								if (actionType.equals(tempActionType)){
									String operationId = operation.getOperId();
									result = user.containResouce(Resource.Type.Operation, operationId);
									break;
								}
							}
						}
					}
				}
			}
		}
		return result;
	}
	private FuncHandler getHandler(List<FuncHandler> funcHandlers,String handlerCode){
		FuncHandler result = null;
		for (int i=0;i < funcHandlers.size();i++){
			FuncHandler funcHandler = (FuncHandler)funcHandlers.get(i);
			if (handlerCode.equals(funcHandler.getHandlerCode())){
				result = funcHandler;
				break;
			}
		}
		return result;
	}
}
