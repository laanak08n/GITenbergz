package com.agileai.hr.common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import org.springframework.dao.DataAccessException;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.common.DaoHelper;

public class DaoExtendsHelper{
	
	public HashMap<String,List<DataRow>> queryRecords(String indexFieldName,String statementId,DataParam param) throws DataAccessException {
		HashMap<String,List<DataRow>> result = new HashMap<String,List<DataRow>>();
		DaoHelper daoHelper = new DaoHelper();
		List<DataRow> recordList = daoHelper.queryRecords(statementId, param);
		HashMap<String, DataRow> recordMap = daoHelper.queryRecords(indexFieldName, statementId, param);
		if (recordList != null && recordList.size() > 0){
			int count = recordList.size();
			Iterator<String> it = recordMap.keySet().iterator();
			while (it.hasNext()) {
				String indexField = it.next();
				List<DataRow> list = new ArrayList<DataRow>();
				for (int i = 0; i < count; i++) {
					if (indexField.equals(String.valueOf(recordList.get(i).get(indexFieldName)))) {
						list.add(recordList.get(i));
					}
				}
				result.put(indexField, list);
			}
		}
		return result;
	}	
}
