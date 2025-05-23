package com.agileai.hr.controller.information;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.controller.core.MasterSubEditPboxHandler;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.information.HrEmployeeManage;
import com.agileai.util.StringUtil;

public class HrEducationEditBoxHandler
        extends MasterSubEditPboxHandler {
    public HrEducationEditBoxHandler() {
        super();
        this.serviceId = buildServiceId(HrEmployeeManage.class);
        this.subTableId = "HrEducation";
    }
    public ViewRenderer prepareDisplay(DataParam param) {
		String operaType = param.get(OperaType.KEY);
		if ("update".equals(operaType)){
			DataRow record = getService().getSubRecord(subTableId, param); 
			this.setAttributes(record);	
			DataParam empParam = new DataParam("EMP_ID", record.get("EMP_ID"));
			DataRow empRecord = getService().getMasterRecord(empParam);
			if (empRecord.get("EMP_STATE").equals("drafe")) {
				setAttribute("doDetail", true);
			}else{
				setAttribute("doDetail", false);
			}
		}
		if ("detail".equals(operaType)){
			DataRow record = getService().getSubRecord(subTableId, param); 
			this.setAttributes(record);	
			DataParam empParam = new DataParam("EMP_ID", record.get("EMP_ID"));
			DataRow empRecord = getService().getMasterRecord(empParam);
			if (empRecord.get("EMP_STATE").equals("drafe")) {
				setAttribute("doDetail", true);
			}else{
				setAttribute("doDetail", false);
			}
		}
		if("insert".equals(operaType)){
			setAttribute("doDetail", true);
		}
			this.setOperaType(operaType);
			processPageAttributes(param);
		return new LocalRenderer(getPage());
	}
    protected void processPageAttributes(DataParam param) {
        if (!StringUtil.isNullOrEmpty(param.get("EMP_ID"))) {
            this.setAttribute("EMP_ID", param.get("EMP_ID"));
        }
    }

    protected HrEmployeeManage getService() {
        return (HrEmployeeManage) this.lookupService(this.getServiceId());
    }
}
