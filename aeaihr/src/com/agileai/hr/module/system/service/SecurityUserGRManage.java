package com.agileai.hr.module.system.service;

import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.TreeAndContentManage;

public interface SecurityUserGRManage 
			extends TreeAndContentManage {
	public List<DataRow> doQueryEmpAction(DataParam param);
	public List<DataRow> queryPickFillRecords(DataParam param);
	void createURGMContentRecord(String urgId,String userId,String rgId);
	void deletPOSEMPContentRecord(String EOPR_ID);
	void deletTureContentRecord(String urgId);
	public DataRow queryURGRelation(DataParam param);
	public List<DataRow> findTreeRecords(DataParam param);

}
