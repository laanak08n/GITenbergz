package com.agileai.hr.controller;

import java.util.ArrayList;
import java.util.List;

import org.codehaus.jettison.json.JSONArray;
import org.codehaus.jettison.json.JSONObject;

import com.agileai.domain.DataParam;
import com.agileai.hotweb.bizmoduler.frame.FunctionManage;
import com.agileai.hotweb.common.BeanFactory;
import com.agileai.hotweb.controller.core.BaseHandler;
import com.agileai.hotweb.domain.TreeBuilder;
import com.agileai.hotweb.domain.TreeModel;
import com.agileai.hotweb.domain.core.Resource;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.domain.system.FuncMenu;
import com.agileai.hotweb.filter.HotwebUserCacher;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.util.StringUtil;

/**
 * 功能菜单处理类
 * @author zhang_ji_yu@163.com
 */
public class HomepageHandler extends BaseHandler{
	private static final String CurrentFuncList = "funcsList";
	public static final String AutherHandlerCodes = HotwebUserCacher.AutherHandlerCodes;
	public static final String FuncMenu = "funcmenu";
	public static final String FuncNode = "funcnode";
	public static final String FUNC_ID_TAG = "funcId";

	private FunctionManage functionManage = null;
	
	public static class MenuJsonTypes{
		public static final String Func = "func";
		public static final String Link = "link";
		public static final String Folder = "folder";
	}
	
	public FuncMenu getFunction(String functionId){
		if (functionManage == null){
			BeanFactory beanFactory = BeanFactory.instance();
			this.functionManage = (FunctionManage)beanFactory.getBean("functionTreeManageService");			
		}
		return functionManage.getFunction(functionId);
	}
	
	public HomepageHandler(){
		super();
	}
	
	public ViewRenderer prepareDisplay(DataParam param){
		User user = (User)this.getUser();
		List<FuncMenu> authedFuncList = this.getAuthedFuncList(user);
		String menuTreeJson = this.getMenuBarJson(authedFuncList,user);
		this.setAttribute("menuTreeJson", menuTreeJson);
		this.setAttribute("userName", user.getUserName());
		return new LocalRenderer(getPage());
	}
	
	@SuppressWarnings("unchecked")
	private List<FuncMenu> getAuthedFuncList(User user){
		List<FuncMenu> result = null;
		BeanFactory beanFactory = BeanFactory.instance();
		FunctionManage functionTreeManage = (FunctionManage)beanFactory.getBean("functionTreeManageService");
		if (user.isAdmin()){
			result = functionTreeManage.getFuncMenuList();
		}else{
			if (!user.getTempProperties().containsKey(CurrentFuncList)){
				List<FuncMenu> funcs = new ArrayList<FuncMenu>();
				List<FuncMenu> funcList = functionTreeManage.getFuncMenuList();
				for (int i=0;i < funcList.size();i++){
					FuncMenu function = funcList.get(i);
					String functionId = function.getFuncId();
					if (user.containResouce(Resource.Type.Menu, functionId)){
						funcs.add(function);						
					}
				}
				user.getTempProperties().put(CurrentFuncList, funcs);
			}
			result = (List<FuncMenu>)user.getTempProperties().get(CurrentFuncList);			
		}
		return result;
	}
	
	private String getMenuBarJson(List<FuncMenu> authedFuncList,User user) {
		JSONObject menuBarJsonObject = new JSONObject();
		try {
			TreeBuilder treeBuilder = new TreeBuilder(authedFuncList,"funcId","funcName","funcPid");
			treeBuilder.setTypeKey("funcType");
			treeBuilder.setPojoList(true);
	        TreeModel treeModel = treeBuilder.buildTreeModel();
	        String rootFuncId = treeModel.getId();
	        String rootFuncName = treeModel.getName();
	        
            menuBarJsonObject.put("type","menubar");
    		menuBarJsonObject.put("name", rootFuncId);
    		menuBarJsonObject.put("text", rootFuncName);
    		JSONArray menuItemJsonArray = new JSONArray();

    		List<TreeModel> treeModels = treeModel.getChildren();
    		for (int i=0;i < treeModels.size();i++){
    			TreeModel child = treeModels.get(i);
                String curFuncId = child.getId();
                String curFuncName = child.getName();
                
                FuncMenu function = this.getFunction(curFuncId);
        		String mainHandlerURL = function.getFuncUrl();
                
				JSONObject menuItemJsonObject = new JSONObject();
				menuItemJsonObject.put("name",curFuncId);
				menuItemJsonObject.put("text",curFuncName);
				
				String funcIcon = function.getFuncIcon();
				
            	if (FuncNode.equals(child.getType())){
            		if (StringUtil.isNullOrEmpty(funcIcon)){
            			funcIcon = "fa-envira";
            		}
            		menuItemJsonObject.put("icon", funcIcon);
            		menuItemJsonObject.put("type",MenuJsonTypes.Func);
            		
            		StringBuffer href = new StringBuffer();
					href.append("javascript:doLoadFuncIframe('");
					href.append(mainHandlerURL);
					href.append("','");
					href.append(curFuncId);
					href.append("','");
					href.append(curFuncName);
					href.append("');");
					menuItemJsonObject.put("href",href.toString());
            	} 
            	else{
            		if (StringUtil.isNullOrEmpty(funcIcon)){
            			funcIcon = "fa-folder-o";
            		}
            		menuItemJsonObject.put("icon", funcIcon);
            		menuItemJsonObject.put("type",MenuJsonTypes.Folder);
            	}
                
            	JSONArray childMenuJsonArray = new JSONArray();
            	menuItemJsonObject.put("menus", childMenuJsonArray);
            	this.loopBuildMenuJson(child,user,childMenuJsonArray);
                	
                menuItemJsonArray.put(menuItemJsonObject);
    		}
    		menuBarJsonObject.put("menus", menuItemJsonArray);
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
        return menuBarJsonObject.toString();
	}
	
	private void loopBuildMenuJson(TreeModel treeModel,User user,JSONArray menuItemJsonArray){
		try {
			List<TreeModel> treeModels = treeModel.getChildren();
			for (int i=0;i < treeModels.size();i++){
				TreeModel child = treeModels.get(i);
	            String curFuncId = child.getId();
	            String curFuncName = child.getName();
	            
	    		FuncMenu function = this.getFunction(curFuncId);
	    		String mainHandlerURL = function.getFuncUrl();
	            
				JSONObject menuItemJsonObject = new JSONObject();
				menuItemJsonObject.put("name",curFuncId);
				menuItemJsonObject.put("text",curFuncName);
				
				String funcIcon = function.getFuncIcon();
				
				if (FuncNode.equals(child.getType())){
            		if (StringUtil.isNullOrEmpty(funcIcon)){
            			funcIcon = "fa-envira";
            		}
            		
	        		menuItemJsonObject.put("icon", funcIcon);
	        		menuItemJsonObject.put("type",MenuJsonTypes.Func);
	        		
	        		StringBuffer href = new StringBuffer();
					href.append("javascript:doLoadFuncIframe('");
					href.append(mainHandlerURL);
					href.append("','");
					href.append(curFuncId);
					href.append("','");
					href.append(curFuncName);
					href.append("');");
					menuItemJsonObject.put("href",href.toString());
	        	} 
	        	else{
            		if (StringUtil.isNullOrEmpty(funcIcon)){
            			funcIcon = "fa-folder-o";
            		}
	        		menuItemJsonObject.put("icon", funcIcon);
	        		menuItemJsonObject.put("type",MenuJsonTypes.Folder);
	        	}
	            
            	JSONArray childMenuJsonArray = new JSONArray();
            	menuItemJsonObject.put("menus", childMenuJsonArray);
            	this.loopBuildMenuJson(child,user,childMenuJsonArray);
	            
            	menuItemJsonArray.put(menuItemJsonObject);
			}
		} catch (Exception e) {
			log.error(e.getLocalizedMessage(), e);
		}
	}
}
