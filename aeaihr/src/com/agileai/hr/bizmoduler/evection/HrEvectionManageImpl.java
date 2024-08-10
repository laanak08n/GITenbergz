package com.agileai.hr.bizmoduler.evection;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.bizmoduler.core.MasterSubServiceImpl;

public class HrEvectionManageImpl extends MasterSubServiceImpl implements
		HrEvectionManage {	
	public HrEvectionManageImpl() {
		super();
	}

	public String[] getTableIds() {
		List<String> temp = new ArrayList<String>();

		temp.add("_base");
		temp.add("HrExpenses");

		return temp.toArray(new String[] {});
	}

	@Override
	public void computeTotalMoney(String masterRecordId) {
		String statementId = sqlNameSpace + "." + "getMasterRecord";
		DataParam param = new DataParam("EVE_ID", masterRecordId);
		DataRow masterRecord = this.daoHelper.getRecord(statementId, param);

		BigDecimal eve_subsidy = (BigDecimal) masterRecord.get("EVE_SUBSIDY");
		if (eve_subsidy == null) {
			eve_subsidy = new BigDecimal("0.00");
		}

		statementId = sqlNameSpace + "." + "getTotalGatherRecord";
		DataRow gatherRecord = this.daoHelper.getRecord(statementId, param);

		BigDecimal gather_transportation_fee = (BigDecimal) gatherRecord
				.get("GATHER_TRANSPORTATION_FEE");
		if (gather_transportation_fee == null) {
			gather_transportation_fee = new BigDecimal("0.00");
		}
		BigDecimal gather_expe_other = (BigDecimal) gatherRecord
				.get("GATHER_EXPE_OTHER");
		if (gather_expe_other == null) {
			gather_expe_other = new BigDecimal("0.00");
		}
		BigDecimal gather_expe_hotel = (BigDecimal) gatherRecord
				.get("GATHER_EXPE_HOTEL");
		if (gather_expe_hotel == null) {
			gather_expe_hotel = new BigDecimal("0.00");
		}
		
		BigDecimal eve_total_money = eve_subsidy.add(gather_transportation_fee)
				.add(gather_expe_other).add(gather_expe_hotel);
		
		statementId = sqlNameSpace + "." + "updateMasterGatherRecord";
		DataParam updateParam = new DataParam();
		updateParam.put("EVE_ID", masterRecordId, "EVE_TOTAL_MONEY",
				eve_total_money);
		this.daoHelper.updateRecord(statementId, updateParam);

	}

	public void approveMasterRecord(DataParam param) {
		String statementId = sqlNameSpace+"."+"approveMasterRecord";
		processDataType(param, tableName);
		this.daoHelper.updateRecord(statementId, param);

	}
	
}
