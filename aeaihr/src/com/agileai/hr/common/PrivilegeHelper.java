package com.agileai.hr.common;

import java.util.List;

import com.agileai.hotweb.domain.core.Role;
import com.agileai.hotweb.domain.core.User;

public class PrivilegeHelper {
	private User user = null;
	public PrivilegeHelper(User user){
		this.user = user;
	}
	
	public boolean isHRMASTER(){
		boolean result = false;
		List<Role> roleList = user.getRoleList();
		for (int i=0;i < roleList.size();i++){
			Role role = roleList.get(i);
			if ("HR_MASTER".equals(role.getRoleCode())){
				result = true;
				break;
			}
		}
		return result;
	}
	public boolean isApprove(){
		boolean result = false;
		List<Role> roleList = user.getRoleList();
		for (int i=0;i < roleList.size();i++){
			Role role = roleList.get(i);
			if ("APPROVE".equals(role.getRoleCode())){
				result = true;
				break;
			}
		}
		return result;
	}
	public boolean isSalMaster(){
		boolean result = false;
		List<Role> roleList = user.getRoleList();
		for (int i=0;i < roleList.size();i++){
			Role role = roleList.get(i);
			if ("SALARY_MASTER".equals(role.getRoleCode())){
				result = true;
				break;
			}
		}
		return result;
	}
	
}
