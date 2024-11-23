<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ page language="java" import="java.util.*" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<%
String currentSubTableId = pageBean.getStringValue("currentSubTableId");
String currentSubTableIndex = pageBean.getStringValue("currentSubTableIndex");
%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>出差信息</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<style type="text/css">
.reSubmittedImgBtn{
	background: url(./images/submitted.gif) no-repeat center center; width: 16px; height: 16px; border: none;
	margin:0px 0px 0px 1px;
	cursor:pointer;
}
.markable{
	background-color:yellow;
	color:black;
}
</style>
<script language="javascript">
function stateSubmit(){
	doSubmit({actionType:'submit'});
}
function stateApprove(){
	doSubmit({actionType:'approve'});
}
function changeSubTable(subTableId){
	$('#currentSubTableId').val(subTableId);
	doSubmit({actionType:'changeSubTable'});
}
function refreshPage(){
	doSubmit({actionType:'changeSubTable'});
}
function addEntryRecord(subTableId){
	$('#currentSubTableId').val(subTableId);
	doSubmit({actionType:'addEntryRecord'});
}
function deleteEntryRecord(subTableId){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (confirm('确认要删除该条记录吗？')){
		$('#currentSubTableId').val(subTableId);
		doSubmit({actionType:'deleteEntryRecord'});	
	}
}
function checkEntryRecords(subTableId){
	var result = true;
	var currentRecordSize = $('#currentRecordSize').val();
	return result;
}
var insertSubRecordBox;
function insertSubRecordRequest(title,handlerId){
	if (!insertSubRecordBox){
		insertSubRecordBox = new PopupBox('insertSubRecordBox',title,{size:'normal',height:'370px',top:'10px'});
	}
	var url = 'index?'+handlerId+'&operaType=insert&EVE_ID='+$('#EVE_ID').val();
	insertSubRecordBox.sendRequest(url);	
}
var viewSubRecordBox;
function viewSubRecordRequest(operaType,title,handlerId,subPKField){
	clearSelection();
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (!viewSubRecordBox){
		viewSubRecordBox = new PopupBox('viewSubRecordBox',title,{size:'normal',height:'370px',top:'10px'});
	}
	var url = 'index?'+handlerId+'&operaType='+operaType+'&'+subPKField+'='+$("#"+subPKField).val();
	viewSubRecordBox.sendRequest(url);
}
function deleteSubRecord(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	if (confirm('确认要删除该条记录吗？')){
		doSubmit({actionType:'deleteSubRecord'});	
	}
}
function doMoveUp(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	doSubmit({actionType:'moveUp'});
}
function doMoveDown(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}	
	doSubmit({actionType:'moveDown'});
}
function stateDrafe(){
	doSubmit({actionType:'drafe'});
}
function saveMasterRecord(){
	if (validate()){
		if (ele("currentSubTableId")){
			var subTableId = $("#currentSubTableId").val();
			if (!checkEntryRecords(subTableId)){
				return;
			}
		}
		showSplash();
		postRequest('form1',{actionType:'saveMasterRecord',onComplete:function(responseText){
			if ("fail" != responseText){
				$('#operaType').val('update');
				$('#EVE_ID').val(responseText);
				doSubmit({actionType:'prepareDisplay'});
			}else{
				hideSplash();
				writeErrorMsg('保存操作出错啦！');
			}
		}});
	}
}
function revokeApproval(){
	doSubmit({actionType:'revokeApproval'});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div style="padding-top:7px;">
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div class="photobg1" id="tabHeader">
<div class="newarticle1" onclick="changeSubTable('_base')">基础信息</div>
<%if (!"insert".equals(pageBean.getOperaType())){%>
 <div class="newarticle1" onclick="changeSubTable('HrExpenses')">费用清单</div>
<%}%>
</div>
<div class="photobox newarticlebox" id="Layer0" style="height:auto;">
<div style="margin:2px;">
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("doInsertEdit")){%>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveMasterRecord()"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
 <%if(pageBean.getBoolValue("doSubmit")){ %>
   <td  align="center"class="bartdx"onclick="stateSubmit();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="submitImgBtn" id="submitImgBtn" title="提交" />提交</td>
    <%}%>
    <%}%>
<%if(pageBean.getBoolValue("doApprove")){ %>
   <td  align="center"class="bartdx"onclick="stateDrafe();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="reSubmittedImgBtn" id="drafeImgBtn" title="反提交" />反提交</td>  
   <td  align="center"class="bartdx"onclick="stateApprove();" onmouseover="onMover(this);" onmouseout="onMout(this);"><input value="&nbsp;"type="button" class="approveImgBtn" id="approveImgBtn" title="核准" />核准</td>
   <%}%>
   <%if(pageBean.getBoolValue("doRevokeApprove")){ %>
      <td  align="center"class="bartdx" onclick="revokeApproval()" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td>
 <%}%>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="goToBack();"><input value="&nbsp;" type="button" class="backImgBtn" title="返回" />返回</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>出差人姓名</th>
	<td><input name="EVE_CODE_NAME" type="text" class="text" id="EVE_CODE_NAME" value="<%=pageBean.inputValue("EVE_CODE_NAME")%>" size="24" readonly="readonly" label="出差人编码" />
	  <input name="EVE_APPLY_USER" type="hidden" class="text" id="EVE_APPLY_USER"   value="<%=pageBean.inputValue("EVE_APPLY_USER")%>" size="24" label="出差人编码"  />
    </td>
	<td>&nbsp;</td>
</tr>
<tr>
	<th width="100" nowrap>报销时间</th>
	<td><input name="EVE_REIMBURSEMENT_TIME" type="text" class="text" id="EVE_REIMBURSEMENT_TIME" value="<%=pageBean.inputTime("EVE_REIMBURSEMENT_TIME")%>" size="24" readonly="readonly" label="报销时间" /></td>
</tr>
<tr>
	<th width="100" nowrap>同行人</th>
	<td><input name="EVE_TOGETHER" type="text"  class="text" id="EVE_TOGETHER" value="<%=pageBean.inputValue("EVE_TOGETHER")%>" size="24"  label="同行人" <%if(pageBean.getBoolValue("isComeFromApprove")){ %> readonly="readonly"  <%}%> />
</td>
</tr>
<tr>
	<th width="100" nowrap>出差日期</th>
	<td><input id="EVE_START_TIME" label="出差日期" name="EVE_START_TIME" type="text" value="<%=pageBean.inputDate("EVE_START_TIME")%>" size="24" <%if(pageBean.getBoolValue("isComeFromApprove")){ %> readonly="readonly"  <%}%> class="text" /><img id="EVE_START_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>结束日期</th>
	<td><input id="EVE_OVER_TIME" label="结束日期" name="EVE_OVER_TIME" type="text" value="<%=pageBean.inputDate("EVE_OVER_TIME")%>" size="24" <%if(pageBean.getBoolValue("isComeFromApprove")){ %> readonly="readonly"  <%}%> class="text" /><img id="EVE_OVER_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>出差天数</th>
	<td><input id="EVE_DAYS" label="出差天数" name="EVE_DAYS" type="text" value="<%=pageBean.inputValue("EVE_DAYS")%>" size="24" <%if(pageBean.getBoolValue("isComeFromApprove")){ %> readonly="readonly"  <%}%> class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>补助金额</th>
	<td><input id="EVE_SUBSIDY" label="补助金额" name="EVE_SUBSIDY" type="text" value="<%=pageBean.inputValue("EVE_SUBSIDY")%>" size="24" <%if(pageBean.getBoolValue("isComeFromApprove")){ %> readonly="readonly"  <%}%> class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>汇总费用</th>
	<td><input name="EVE_TOTAL_MONEY" type="text" class="text markable" id="EVE_TOTAL_MONEY" value="<%=pageBean.inputValue("EVE_TOTAL_MONEY")%>" size="24" readonly="readonly" label="汇总费用" />
</td>
</tr>
<tr>
	<th width="100" nowrap>状态</th>
	<td>
	<input id="STATE_TEXT" label="状态" name="STATE_TEXT" type="text" value="<%=pageBean.selectedText("STATE")%>" size="24"  class="text" readonly="readonly"/>
	<input id="STATE" label="状态" name="STATE" type="hidden" value="<%=pageBean.selectedValue("STATE")%>" />
   </td>
</tr>
<tr>
	<th width="100" nowrap>出差事由</th>
	<td><textarea id="EVE_REASON" label="出差事由" name="EVE_REASON" cols="40" rows="3" <%if(pageBean.getBoolValue("isComeFromApprove")){ %> readonly="readonly"  <%}%> class="textarea"><%=pageBean.inputValue("EVE_REASON")%></textarea>
</td>
</tr>
<%if(pageBean.getBoolValue("doSignIn")){ %>
<tr>
	<th width="100" nowrap>核准人姓名</th>
	<td><input name="EVE_APPROVE_USER_NAME" type="text" class="text" id="EVE_APPROVE_USER_NAME" value="<%=pageBean.inputValue("EVE_APPROVE_USER_NAME")%>" 
size="24" readonly="readonly"  label="核准人姓名" />
	  <input name="EVE_APPROVE_USER" type="hidden" class="text" id="EVE_APPROVE_USER" value="<%=pageBean.inputValue("EVE_APPROVE_USER")%>" size="24" hidden="hidden" label="核准人"  /></td>
</tr>

<tr>
	<th width="100" nowrap>核准时间</th>
	<td><input name="EVE_APPROVE_TIME" type="text" class="text" id="EVE_APPROVE_TIME" value="<%=pageBean.inputDate("EVE_APPROVE_TIME")%>" size="24" readonly="readonly"  label="核准时间" />
</td>
</tr>
<tr>
	<th width="100" nowrap>核准结果</th>
	<td><select id="APP_RESULT" label="核准结果" name="APP_RESULT" class="select"><%=pageBean.selectValue("APP_RESULT")%></select></td>
</tr>
<tr>
	<th width="100" nowrap>核准意见</th>
	<td><textarea name="EVE_APPROVE_OPINION" cols="40" rows="3" <%if(!pageBean.getBoolValue("isApprove")){ %> readonly="readonly" <%}%> class="textarea" id="EVE_APPROVE_OPINION" label="核准意见"><%=pageBean.inputValue("EVE_APPROVE_OPINION")%></textarea></td>
</tr>
<%}%>
</table>
</div>
</div>
<%if (!"insert".equals(pageBean.getOperaType())){%>
<%if ("HrExpenses".equals(currentSubTableId)){ %>
<div class="photobox newarticlebox" id="Layer2" style="height:auto;">
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("isComeFromDetail")){%>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="insertSubRecordRequest('费用清单','HrExpensesEditBox')"><input value="&nbsp;" title="新增" type="button" class="createImgBtn" />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="viewSubRecordRequest('update','费用清单','HrExpensesEditBox','EXPE_ID')"><input value="&nbsp;" title="编辑" type="button" class="editImgBtn" />编辑</td>  
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="deleteSubRecord()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" />删除</td>
   <%}%>
</tr>   
   </table>
</div>
<div style="margin:2px;">
<%
List param1Records = (List)pageBean.getAttribute("HrExpensesRecords");
pageBean.setRsList(param1Records);
%>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="费用清单.csv"
retrieveRowsCallback="process" xlsFileName="费用清单.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize|export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="auto"
>
<ec:row styleClass="odd"  ondblclick="viewSubRecordRequest('update','费用清单','HrExpensesEditBox','EXPE_ID')" onclick="selectRow(this,{EXPE_ID:'${row.EXPE_ID}'})">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="EXPE_DEPART" title="出发地"   />
	<ec:column width="100" property="EXPE_DESTINATION" title="目的地"   />
	<ec:column width="100" property="EXPE_DEPART_TIME" title="出发时间" cell="date" format="yyyy-MM-dd HH:mm" />
	<ec:column width="100" property="EXPE_COMEBACK_TIME" title="到达时间" cell="date" format="yyyy-MM-dd HH:mm" />
	<ec:column width="100" property="EXPE_TRANSPORTATION_WAY" title="交通方式" mappingItem="EXPE_TRANSPORTATION_WAY" />
	<ec:column width="100" property="EXPE_TRANSPORTATION_FEE" title="交通费用"   />
    <ec:column width="100" property="EXPE_HOTEL" title="住宿费用"   />
	<ec:column width="100" property="EXPE_OTHER" title="其他费用"   />
</ec:row>
</ec:table>
</div>
</div>
<input type="hidden" name="EXPE_ID" id="EXPE_ID" value=""/>
<script language="javascript">
setRsIdTag('EXPE_ID');
numValidator.add("EVE_DAYS");
numValidator.add("EVE_SUBSID");

</script>
<%}%>


<input type="hidden" id="currentSubTableId" name="currentSubTableId" value="<%=pageBean.inputValue("currentSubTableId")%>" />
<%if (!"_base".equals(pageBean.inputValue("currentSubTableId"))){%>
<script language="javascript">
$("#Layer0").hide();
</script>
<%}%>
<%}%>
<script language="javascript">
new Tab('tab','tabHeader','Layer',<%=currentSubTableIndex%>);
<%if (!"_base".equals(pageBean.inputValue("currentSubTableId"))){%>
$("#Layer0").hide();
<%}%>
</script>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" name="comeFrome" id="comeFrome" value="<%=pageBean.inputValue("comeFrome")%>"/>
<input type="hidden" id="EVE_ID" name="EVE_ID" value="<%=pageBean.inputValue("EVE_ID")%>" />
</div>
</form>
<script language="javascript">
initCalendar('EVE_START_TIME','%Y-%m-%d','EVE_START_TIMEPicker');
initCalendar('EVE_OVER_TIME','%Y-%m-%d','EVE_OVER_TIMEPicker');

requiredValidator.add("EVE_REIMBURSEMENT_TIME");
datetimeValidators[0].set("yyyy-MM-dd HH:mm").add("EVE_REIMBURSEMENT_TIME");
requiredValidator.add("EVE_START_TIME");
datetimeValidators[1].set("yyyy-MM-dd HH:mm").add("EVE_START_TIME");
requiredValidator.add("EVE_OVER_TIME");
datetimeValidators[2].set("yyyy-MM-dd HH:mm").add("EVE_OVER_TIME");
requiredValidator.add("EVE_DAYS");
intValidator.add("EVE_DAYS");
requiredValidator.add("EVE_SUBSIDY");
requiredValidator.add("EVE_REASON");
numValidator.add("EVE_SUBSIDY");
requiredValidator.add("EXPE_TRANSPORTATION_WAY");
lengthValidators[0].set(60).add("EVE_APPROVE_OPINION");
numValidator.add("EVE_TOTAL_MONEY");
lengthValidators[1].set(60).add("EVE_REASON");
initDetailOpertionImage();
$(function(){
	resetTabHeight(80);
});
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
