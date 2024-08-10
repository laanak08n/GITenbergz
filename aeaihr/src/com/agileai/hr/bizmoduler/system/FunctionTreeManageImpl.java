package com.agileai.hr.bizmoduler.system;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeManageImpl;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;
import com.agileai.util.StringUtil;

public class FunctionTreeManageImpl
        extends TreeManageImpl
        implements FunctionTreeManage {
	private HashMap<String,FuncMenu> functionMap = new HashMap<String,FuncMenu>();
	private List<FuncMenu> funcMenuList = new ArrayList<FuncMenu>();

	private HashMap<String,Operation> operationMap = new HashMap<String,Operation>();
	private HashMap<String,FuncHandler> handlerMap = new HashMap<String,FuncHandler>();
	
    public FunctionTreeManageImpl() {
        super();
        this.idField = "FUNC_ID";
        this.nameField = "FUNC_NAME";
        this.parentIdField = "FUNC_PID";
        this.sortField = "FUNC_SORT";
    }
    
	public FuncMenu getFunction(String functionId){
		this.init();
		return functionMap.get(functionId);
	}

	public List<FuncMenu> getFuncMenuList(){
		this.init();
		return funcMenuList;
	}
	
	private synchronized void initFuntions(){
		List<DataRow> records = findTreeRecords(new DataParam());
		if (records != null){
			for (int i=0;i < records.size();i++){
				DataRow row = records.get(i);
				FuncMenu funcMenu = new FuncMenu();
				String funcId = row.stringValue("FUNC_ID");
				funcMenu.setFuncId(funcId);
				funcMenu.setFuncName(row.stringValue("FUNC_NAME"));
				funcMenu.setFuncPid(row.stringValue("FUNC_PID"));
				funcMenu.setFuncType(row.stringValue("FUNC_TYPE"));
				String funcUrl = null;

				String handlerURL = row.stringValue("HANLER_URL");
				if (!StringUtil.isNullOrEmpty(handlerURL)){
					funcUrl = handlerURL; 
				}else{
					String handlerCode = row.stringValue("HANLER_CODE");
					funcUrl = "index?"+handlerCode;
				}
				funcMenu.setFuncUrl(funcUrl);
				
				funcMenuList.add(funcMenu);
				functionMap.put(funcId, funcMenu);
			}
			for (int i=0;i < funcMenuList.size();i++){
				FuncMenu funcMenu = funcMenuList.get(i);
				String parentId = funcMenu.getFuncPid();
				if (!StringUtil.isNullOrEmpty(parentId)){
					FuncMenu parent = functionMap.get(parentId);
					parent.getChildren().add(funcMenu);
				}
			}
		}
	}

	private synchronized void initHandlers(){
		String statementId = "syshandler.findMasterRecords";
		List<DataRow> handlerRecords = this.daoHelper.queryRecords(statementId, new DataParam());
		if (handlerRecords != null){
			for (int i=0;i < handlerRecords.size();i++){
				DataRow row = handlerRecords.get(i);
				String handlerId = row.stringValue("HANLER_ID");
				String handlerCode = row.stringValue("HANLER_CODE");
				String handlerType = row.stringValue("HANLER_TYPE");
				String handlerURL = row.stringValue("HANLER_URL");
				String funcId = row.stringValue("FUNC_ID");
				FuncHandler handler = new FuncHandler();
				handler.setHandlerId(handlerId);
				handler.setHandlerCode(handlerCode);
				handler.setHandlerType(handlerType);
				handler.setHandlerURL(handlerURL);
				handler.setFunctionId(funcId);
				
				handlerMap.put(handlerId, handler);
				
				FuncMenu funcMenu = this.functionMap.get(funcId);
				funcMenu.getHandlers().add(handler);
			}
		}else{
			handlerMap.put("-1",new FuncHandler());
		}
	}
	
	private synchronized void initOperatons(){
		String statementId = "syshandler.findSysOperationRecords";
		List<DataRow> operationRecords = this.daoHelper.queryRecords(statementId, new DataParam());
		if (operationRecords != null){
			for (int i=0;i < operationRecords.size();i++){
				DataRow row = operationRecords.get(i);
				String operaId = row.stringValue("OPER_ID");
				String handlerId = row.stringValue("HANLER_ID");
				String operaCode = row.stringValue("OPER_CODE");
				String operaName = row.stringValue("OPER_NAME");
				String actionType = row.stringValue("OPER_ACTIONTPYE");
				
				Operation operation = new Operation();
				operation.setHandlerId(handlerId);
				operation.setOperCode(operaCode);
				operation.setOperName(operaName);
				operation.setOperId(operaId);
				operation.setActionType(actionType);
				
				this.operationMap.put(operaId, operation);
				
				FuncHandler funcHandler = this.handlerMap.get(handlerId);
				funcHandler.getOperations().add(operation);
			}
		}else{
			this.operationMap.put("-1", new Operation());
		}
	}
	
	public void insertChildRecord(DataParam param) {
		String parentId = param.get(idField);
		String newMenuSort = String.valueOf(this.getNewMaxSort(parentId));
		String funcId = KeyGenerator.instance().genKey();
		String handlerCode = param.get("CHILD_MAIN_HANDLER");
		String handlerId = KeyGenerator.instance().genKey();
		String funcType = param.get("CHILD_FUNC_TYPE");
		param.put("CHILD_"+sortField,newMenuSort);
		param.put("CHILD_"+idField,funcId);
		if ("funcnode".equals(funcType)){
			param.put("CHILD_MAIN_HANDLER",handlerId);			
		}
		param.put("CHILD_"+parentIdField,param.get(idField));
		String statementId = this.sqlNameSpace+"."+"insertTreeRecord";
		this.daoHelper.insertRecord(statementId, param);

		if ("funcnode".equals(funcType)){
			statementId = "syshandler.insertMasterRecord";
			DataParam insertParam = new DataParam();
			insertParam.put("HANLER_ID",handlerId);
			insertParam.put("HANLER_CODE",handlerCode);
			insertParam.put("FUNC_ID",funcId);
			insertParam.put("HANLER_TYPE","MAIN");
			this.daoHelper.insertRecord(statementId, insertParam);			
		}
	}
	
	public void deleteCurrentRecord(String currentId) {
		String statementId = "syshandler.findMasterRecords";
		DataParam param = new DataParam("funcId",currentId);
		List<DataRow> handlerRecords = this.daoHelper.queryRecords(statementId, param);
		if (handlerRecords != null && handlerRecords.size() > 0){
			for (int i=0;i < handlerRecords.size();i++){
				DataRow row = handlerRecords.get(i);
				String handlerId = row.stringValue("HANLER_ID");
				String tableId = "SysOperation";
				DataParam deleteParam = new DataParam("HANLER_ID",handlerId);
				statementId = "syshandler.delete"+StringUtil.upperFirst(tableId)+"Records";
				this.daoHelper.deleteRecords(statementId, deleteParam);
				
				statementId = "syshandler.deleteMasterRecord";				
				this.daoHelper.deleteRecords(statementId, deleteParam);
			}
		}
		DataParam authparam = new DataParam();
		authparam.put("RES_TYPE",Resource.Type.Menu);
		authparam.put("RES_ID",currentId);
		
		statementId = "SecurityAuthorizationConfig.delRoleAuthRelation";
		this.daoHelper.deleteRecords(statementId, authparam);
		
		statementId = "SecurityAuthorizationConfig.delUserAuthRelation";
		this.daoHelper.deleteRecords(statementId, authparam);
		
		statementId = "SecurityAuthorizationConfig.delGroupAuthRelation";
		this.daoHelper.deleteRecords(statementId, authparam);
		
		statementId = this.sqlNameSpace+"."+"deleteTreeRecord";
		this.daoHelper.deleteRecords(statementId, currentId);
	}
	
	private void init(){
		if (this.functionMap.isEmpty()){
			this.initFuntions();
		}
		if (this.handlerMap.isEmpty()){
			this.initHandlers();
		}
		if (this.operationMap.isEmpty()){
			this.initOperatons();
		}	
	}
	
	@Override
	public FuncHandler getFuncHandler(String handlerId) {
		this.init();
		return this.handlerMap.get(handlerId);
	}

	@Override
	public Operation getOperation(String operationId) {
		this.init();
		return operationMap.get(operationId);
	}

	@Override
	public void clearFuncTreeCache() {
		this.funcMenuList.clear();
		this.functionMap.clear();
		this.handlerMap.clear();
		this.operationMap.clear();
	}
}
