package com.agileai.hr.controller.evection;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import com.agileai.domain.DataParam;
import com.agileai.domain.DataRow;
import com.agileai.hotweb.annotation.PageAction;
import com.agileai.hotweb.bizmoduler.core.MasterSubService;
import com.agileai.hotweb.controller.core.MasterSubEditMainHandler;
import com.agileai.hotweb.domain.FormSelectFactory;
import com.agileai.hotweb.domain.core.User;
import com.agileai.hotweb.renders.AjaxRenderer;
import com.agileai.hotweb.renders.LocalRenderer;
import com.agileai.hotweb.renders.RedirectRenderer;
import com.agileai.hotweb.renders.ViewRenderer;
import com.agileai.hr.bizmoduler.evection.HrEvectionManage;
import com.agileai.hr.common.PrivilegeHelper;
import com.agileai.util.DateUtil;

public class HrEvectionManageEditHandler extends MasterSubEditMainHandler {
	public HrEvectionManageEditHandler() {
		super();
		this.listHandlerClass = HrEvectionManageListHandler.class;
		this.serviceId = buildServiceId(HrEvectionManage.class);
		this.baseTablePK = "EVE_ID";
		this.defaultTabId = "_base";
	}

	public ViewRenderer prepareDisplay(DataParam param) {
		User user = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user);
		
		String operaType = param.get(OperaType.KEY);
		
		if ("insert".equals(operaType)) {
			setAttribute("doInsertEdit", true);
			setAttribute("doApprove", false);
			setAttribute("doSignIn", false);
			setAttribute("doSubmit", false);
		}
		if ("update".equals(operaType)) {
			setAttribute("doInsertEdit", true);
			setAttribute("doApprove", false);
			setAttribute("isComeFromDetail", true);
			setAttribute("doSignIn", false);
			setAttribute("doSubmit", true);
			if (isReqRecordOperaType(operaType)) {
				DataRow record = getService().getMasterRecord(param);
				this.setAttributes(record);
			}
		}
		if ("approve".equals(operaType)) {
			setAttribute("doInsertEdit", false);
			setAttribute("doApprove", true);
			setAttribute("doSignIn", true);
			if (!isReqRecordOperaType(operaType)) {
				DataRow record = getService().getMasterRecord(param);
				this.setAttributes(record);
				this.setAttribute(
						"EVE_APPROVE_USER_NAME",
						this.getAttribute("EVE_APPROVE_USER_NAME",
								user.getUserName()));
				this.setAttribute("EVE_APPROVE_USER",
						this.getAttribute("EVE_APPROVE_USER", user.getUserId()));
				if (this.getAttribute("EVE_APPROVE_TIME") == null) {
					String date = DateUtil.getDateByType(
							DateUtil.YYMMDDHHMI_HORIZONTAL, new Date());
					this.setAttribute("EVE_APPROVE_TIME", date);
				}
			}
		}
		if (operaType.equals("detail")) {
			if (isReqRecordOperaType(operaType)) {
				DataRow record = getService().getMasterRecord(param);
				if (record != null) {
					if (!privilegeHelper.isApprove()
							&& record.get("STATE").equals("drafe")) {
						setAttribute("doInsertEdit", true);
						setAttribute("isComeFromDetail", true);
						setAttribute("doApprove", false);
						setAttribute("doSignIn", false);

					} else if (!privilegeHelper.isApprove()
							&& record.get("STATE").equals("submitted")) {
						setAttribute("doInsertEdit", false);
						setAttribute("isComeFromDetail", false);
						setAttribute("doApprove", false);
						setAttribute("doSignIn", false);
					} else if (!privilegeHelper.isApprove()
							&& record.get("STATE").equals("approved")) {
						setAttribute("doInsertEdit", false);
						setAttribute("isComeFromDetail", false);
						setAttribute("doApprove", false);
						setAttribute("doSignIn", true);
					} else if (privilegeHelper.isApprove() && record.get("STATE").equals("drafe")) {
						setAttribute("doInsertEdit", true);
						setAttribute("isComeFromDetail", true);
						setAttribute("doApprove", false);
						setAttribute("doSignIn", false);
						setAttribute("doSubmit", true);
						if (user.getUserId().equals(
								record.get("EVE_APPLY_USER"))) {
							setAttribute("doInsertEdit", true);
							setAttribute("isComeFromDetail", true);
						} else {
							setAttribute("doInsertEdit", false);
							setAttribute("isComeFromDetail", false);
						}
					} else if (privilegeHelper.isApprove()
							&& record.get("STATE").equals("submitted")) {
						setAttribute("doInsertEdit", false);
						setAttribute("isComeFromDetail", false);
						setAttribute("doApprove", true);
						setAttribute("doSignIn", true);
							DataRow recordVeiw = getService().getMasterRecord(param);
							this.setAttributes(recordVeiw);
							this.setAttribute("EVE_APPROVE_USER_NAME", this
									.getAttribute("EVE_APPROVE_USER_NAME",
											user.getUserName()));
							this.setAttribute(
									"EVE_APPROVE_USER",
									this.getAttribute("EVE_APPROVE_USER",
											user.getUserId()));
							if (this.getAttribute("EVE_APPROVE_TIME") == null) {
								String date = DateUtil.getDateByType(
										DateUtil.YYMMDDHHMI_HORIZONTAL,
										new Date());
								this.setAttribute("EVE_APPROVE_TIME", date);
							}

					} else if (privilegeHelper.isApprove()
							&& record.get("STATE").equals("approved")) {
						setAttribute("doInsertEdit", false);
						setAttribute("isComeFromDetail", false);
						setAttribute("doApprove", false);
						setAttribute("doSignIn", true);
					}
				}
				this.setAttributes(record);
			}
		}
		String currentSubTableId = param.get("currentSubTableId", defaultTabId);
		if (!currentSubTableId.equals(MasterSubService.BASE_TABLE_ID)) {
			String subRecordsKey = currentSubTableId + "Records";
			if (!this.getAttributesContainer().containsKey(subRecordsKey)) {
				List<DataRow> subRecords = getService().findSubRecords(
						currentSubTableId, param);
				this.setAttribute(currentSubTableId + "Records", subRecords);
			}
		}
		this.setAttribute("currentSubTableId", currentSubTableId);
		this.setAttribute("currentSubTableIndex",
				getTabIndex(currentSubTableId));
		String operateType = param.get(OperaType.KEY);
		this.setOperaType(operateType);
		processPageAttributes(param);
		return new LocalRenderer(getPage());
	}

	protected void processPageAttributes(DataParam param) {
		String selectedValue = getAttributeValue("APP_RESULT", "YES");
		setAttribute("APP_RESULT", FormSelectFactory.create("APP_RESULT")
				.addSelectedValue(selectedValue));

		initMappingItem("EXPE_TRANSPORTATION_WAY",
				FormSelectFactory.create("EXPE_TRANSPORTATION_WAY")
						.getContent());

		User user = (User) this.getUser();
		this.setAttribute("EVE_CODE_NAME",
				this.getAttribute("EVE_CODE_NAME", user.getUserName()));
		this.setAttribute("EVE_APPLY_USER",
				this.getAttribute("EVE_APPLY_USER", user.getUserId()));
		User user1 = (User) this.getUser();
		PrivilegeHelper privilegeHelper = new PrivilegeHelper(user1);
		if (privilegeHelper.isApprove()) {
			setAttribute("isApprove", true);

		}
		String date = DateUtil.getDateByType(DateUtil.YYMMDDHHMI_HORIZONTAL,
				new Date());
		if (this.getAttribute("EVE_REIMBURSEMENT_TIME") == null) {
			this.setAttribute("EVE_REIMBURSEMENT_TIME", date);
		}
		setAttribute("STATE", FormSelectFactory.create("STATE")
				.addSelectedValue(getOperaAttributeValue("STATE", "drafe")));

	}

	public ViewRenderer doSaveMasterRecordAction(DataParam param) {
		String operateType = param.get(OperaType.KEY);
		String responseText = "fail";
		if (OperaType.CREATE.equals(operateType)) {
			getService().createMasterRecord(param);
			responseText = param.get(baseTablePK);
		} else if (OperaType.UPDATE.equals(operateType)) {
			getService().updateMasterRecord(param);
			saveSubRecords(param);
			responseText = param.get(baseTablePK);
		}
		String masterRecordId = param.get("EVE_ID");
		this.getService().computeTotalMoney(masterRecordId);
		return new AjaxRenderer(responseText);
	}

	protected String[] getEntryEditFields(String currentSubTableId) {
		List<String> temp = new ArrayList<String>();

		return temp.toArray(new String[] {});
	}

	protected String getEntryEditTablePK(String currentSubTableId) {
		HashMap<String, String> primaryKeys = new HashMap<String, String>();
		primaryKeys.put("HrExpenses", "EXPE_ID");

		return primaryKeys.get(currentSubTableId);
	}

	protected String getEntryEditForeignKey(String currentSubTableId) {
		HashMap<String, String> foreignKeys = new HashMap<String, String>();
		foreignKeys.put("HrExpenses", "EVE_ID");

		return foreignKeys.get(currentSubTableId);
	}

	@PageAction
	public ViewRenderer submit(DataParam param) {
		DataParam dataParam = new DataParam("EVE_ID",param.get("EVE_ID"));
		List<DataRow> expenses = getService().findExpensesRecords(dataParam);
		if(expenses.size()==0){
			this.setErrorMsg("请填写费用清单!");
			return prepareDisplay(param);
		}
		param.put("STATE", "submitted");
		getService().updateMasterRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}

	@PageAction
	public ViewRenderer approve(DataParam param) {
		param.put("STATE", "approved");
		getService().approveMasterRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}

	@PageAction
	public ViewRenderer drafe(DataParam param) {
		param.put("STATE", "drafe");
		getService().updateMasterRecord(param);
		return new RedirectRenderer(getHandlerURL(listHandlerClass));
	}

	protected HrEvectionManage getService() {
		return (HrEvectionManage) this.lookupService(this.getServiceId());
	}
}
