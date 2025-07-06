package com.agileai.hr.module.system.service;

import java.util.List;

import org.ecside.core.bean.PageList;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.QueryModelServiceImpl;
import com.agileai.util.StringUtil;

public class SystemLogQueryImpl extends QueryModelServiceImpl implements
		SystemLogQuery {
	public PageList<DataRow> findRecords(DataParam param) {
		String ec_p = param.get("ec_p");
		if (StringUtil.isNullOrEmpty(ec_p)){
			param.put("ec_p","1");
		}
		String ec_rd = param.get("ec_rd");
		if (StringUtil.isNullOrEmpty(ec_rd)){
			param.put("ec_rd","15");
		}
		
		String statementId = sqlNameSpace+"."+"queryRecordsCount";
		DataRow countRow = this.daoHelper.getRecord(statementId, param);
		int totalCount = countRow.getInt("TOTAL_COUNT");		
		
		int currentPageNum = param.getInt("ec_p");
		int pageSizePerPage = param.getInt("ec_rd");
		
		int startNum = (currentPageNum-1)*pageSizePerPage+1;
		int endNum = currentPageNum * pageSizePerPage;
		if (endNum > totalCount){
			endNum = totalCount;
		}
		param.put("_startNum_",(startNum-1));
		param.put("_endNum_",endNum);
		param.put("_stepRange_",(endNum-startNum+1));
		
		PageList<DataRow> result = new PageList<DataRow>();
		statementId = sqlNameSpace+"."+"queryRecords";
		List<DataRow> temp = this.daoHelper.queryRecords(statementId, param);
		result.addAll(temp);
		result.setTotalNumber(totalCount);
		
		return result;
	} 
}
