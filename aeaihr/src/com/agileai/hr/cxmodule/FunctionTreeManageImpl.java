package com.agileai.hr.cxmodule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.agileai.common.KeyGenerator;
import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeManageImpl;
import com.agileai.hotweb.common.GlobalCacheManager;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.system.FuncHandler;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.domain.system.Operation;
import com.agileai.hotweb.filter.HotwebUserCacher;
import com.agileai.util.StringUtil;

public class FunctionTreeManageImpl
        extends TreeManageImpl
        implements FunctionTreeManage {
	
	private static final String functionMapCacheKey = "functionMap";
	private static final String funcMenuListCacheKey = "funcMenuList";
	private static final String operationMapCacheKey = "operationMap";
	private static final String handlerMapCacheKey = "handlerMap";
	private static final String functionIdListMapCacheKey = "functionIdListMap";
	
	protected String appName = null;
	
    public FunctionTreeManageImpl() {
        super();
        this.idField = "FUNC_ID";
        this.nameField = "FUNC_NAME";
        this.parentIdField = "FUNC_PID";
        this.sortField = "FUNC_SORT";
    }
    @SuppressWarnings("unchecked")    
    private  HashMap<String,FuncMenu> getCacheFunctionMap(){
		HashMap<String,FuncMenu> functionMap = (HashMap<String,FuncMenu>)GlobalCacheManager.getCacheValue(functionMapCacheKey);
    	return functionMap;
    }
    @SuppressWarnings("unchecked")
	private List<FuncMenu> getCacheFuncMenuList(){
    	List<FuncMenu> funcMenuList = (List<FuncMenu>)GlobalCacheManager.getCacheValue(funcMenuListCacheKey);
    	return funcMenuList;
    }
    @SuppressWarnings("unchecked")
	private HashMap<String,Operation> getCacheOperationMap(){
    	HashMap<String,Operation> operationMap = (HashMap<String,Operation>)GlobalCacheManager.getCacheValue(operationMapCacheKey);
    	return operationMap;
    }
    @SuppressWarnings("unchecked")
	private HashMap<String,FuncHandler> getCacheHandlerMap(){
    	HashMap<String,FuncHandler> handlerMap = (HashMap<String,FuncHandler>)GlobalCacheManager.getCacheValue(handlerMapCacheKey);
    	return handlerMap;
    }
    @SuppressWarnings("unchecked")
	private HashMap<String,List<String>> getCacheFunctionIdListMap(){
    	HashMap<String,List<String>> functionIdListMap = (HashMap<String,List<String>>)GlobalCacheManager.getCacheValue(functionIdListMapCacheKey);
    	return functionIdListMap;
    }

    public FuncMenu getFunction(String functionId){
		this.init();
		return getCacheFunctionMap().get(functionId);
	}

	public List<FuncMenu> getFuncMenuList(){
		this.init();
		return getCacheFuncMenuList();
	}
	
	private synchronized void initFuntions(){
		List<DataRow> records = findTreeRecords(new DataParam());
		if (records != null){
			HashMap<String,FuncMenu> functionMap = new HashMap<String,FuncMenu>();
			List<FuncMenu> funcMenuList = new ArrayList<FuncMenu>();
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
			
			GlobalCacheManager.putCacheValue(functionMapCacheKey, functionMap);
			GlobalCacheManager.putCacheValue(funcMenuListCacheKey, funcMenuList);
		}
	}

	private synchronized void initHandlers(){
		String statementId = "syshandler.findMasterRecords";
		List<DataRow> handlerRecords = this.daoHelper.queryRecords(statementId, new DataParam());
		HashMap<String,FuncHandler> handlerMap = new HashMap<String,FuncHandler>();
		HashMap<String,List<String>> functionIdListMap = new HashMap<String,List<String>>();
		
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
				
				List<String> functionIdList = buildFunctionIdList(functionIdListMap,handlerCode);
				if (!functionIdList.contains(funcId)){
					functionIdList.add(funcId);
				}
				
				HashMap<String,FuncMenu> functionMap = getCacheFunctionMap();
				FuncMenu funcMenu = functionMap.get(funcId);
				funcMenu.getHandlers().add(handler);
			}
		}else{
			handlerMap.put("-1",new FuncHandler());
		}
		
		GlobalCacheManager.putCacheValue(handlerMapCacheKey, handlerMap);
		GlobalCacheManager.putCacheValue(functionIdListMapCacheKey, functionIdListMap);
	}
	
	private List<String> buildFunctionIdList(HashMap<String,List<String>> functionIdListMap,String handlerCode){
		if (!functionIdListMap.containsKey(handlerCode)){
			List<String> functionIdList = new ArrayList<String>();
			functionIdListMap.put(handlerCode, functionIdList);
		}
		return functionIdListMap.get(handlerCode);
	}
	
	private synchronized void initOperatons(){
		String statementId = "syshandler.findSysOperationRecords";
		List<DataRow> operationRecords = this.daoHelper.queryRecords(statementId, new DataParam());
		HashMap<String,Operation> operationMap = new HashMap<String,Operation>();
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
				
				operationMap.put(operaId, operation);
				
				HashMap<String,FuncHandler> handlerMap = getCacheHandlerMap();
				FuncHandler funcHandler = handlerMap.get(handlerId);
				funcHandler.getOperations().add(operation);
			}
		}else{
			operationMap.put("-1", new Operation());
		}
		
		GlobalCacheManager.putCacheValue(operationMapCacheKey, operationMap);
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
		HashMap<String,FuncMenu> functionMap = getCacheFunctionMap();
		HashMap<String,Operation> operationMap = getCacheOperationMap();
		HashMap<String,FuncHandler> handlerMap = getCacheHandlerMap();
		HashMap<String,List<String>> functionIdList = getCacheFunctionIdListMap();
		
		if (functionMap == null || functionMap.isEmpty()){
			this.initFuntions();
		}
		if (handlerMap == null || handlerMap.isEmpty()
				|| functionIdList == null || functionIdList.isEmpty()){
			this.initHandlers();
		}
		if (operationMap == null || operationMap.isEmpty()){
			this.initOperatons();
		}	
	}
	
	@Override
	public FuncHandler getFuncHandler(String handlerId) {
		this.init();
		HashMap<String,FuncHandler> handlerMap = getCacheHandlerMap();
		return handlerMap.get(handlerId);
	}

	@Override
	public List<String> getFunctionIdList(String handlerCode) {
		this.init();
		HashMap<String,List<String>> functionIdList = getCacheFunctionIdListMap();
		return functionIdList.get(handlerCode);
	}	
	
	@Override
	public Operation getOperation(String operationId) {
		this.init();
		HashMap<String,Operation> operationMap = getCacheOperationMap();
		return operationMap.get(operationId);
	}

	@Override
	public void clearFuncTreeCache() {
		HashMap<String,FuncMenu> functionMap = getCacheFunctionMap();
		HashMap<String,Operation> operationMap = getCacheOperationMap();
		HashMap<String,FuncHandler> handlerMap = getCacheHandlerMap();
		List<FuncMenu> funcMenuList = getCacheFuncMenuList();
		HashMap<String,List<String>> functionIdList = getCacheFunctionIdListMap();
		
		funcMenuList.clear();
		functionMap.clear();
		handlerMap.clear();
		operationMap.clear();
		functionIdList.clear();
		
		HotwebUserCacher.getInstance(appName).truncateUsers();
	}
	@Override
	public void setAppName(String appName) {
		if (StringUtil.isNullOrEmpty(this.appName)){
			this.appName = appName;
		}
	}
}
