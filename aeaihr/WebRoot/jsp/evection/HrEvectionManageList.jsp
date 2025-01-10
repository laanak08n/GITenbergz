<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>出差信息</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
function controlUpdateBtn(stateResult){
	if(stateResult =='drafe'){
		enableButton("editImgBtn");
		disableButton("revokeApproval")
		  <%
		  if(pageBean.getBoolValue("isApprove")){
		  %>
		disableButton("approve");
		<%}
		%>
		enableButton("detailImgBtn");
		enableButton("deleteImgBtn");
	}
	if(stateResult =='submitted'){
		disableButton("editImgBtn");
		disableButton("revokeApproval")
		  <%
		  if(pageBean.getBoolValue("isApprove")){
		  %>
		enableButton("approve");
		<%}
			%>	
		enableButton("detailImgBtn");
		disableButton("deleteImgBtn");
	}
	if(stateResult =='approved'){
		disableButton("editImgBtn");
		enableButton("revokeApproval");
		  <%
		  if(pageBean.getBoolValue("isApprove")){
		  %>
		disableButton("approve");
		<%}
			%>
		enableButton("detailImgBtn");
		disableButton("deleteImgBtn");
	}
}
function controlCanEdit(userId,state){
	if (state == "drafe" && userId != "<%=pageBean.getStringValue("EVE_APPLY_USER")%>" ){
		disableButton("editImgBtn");
	}
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolBar" border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="doRequest('insertRequest')"><input id="createImgBtn" value="&nbsp;" title="新增" type="button" class="createImgBtn"  disabled="disabled"  />新增</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="doRequest('updateRequest')"><input id="editImgBtn" value="&nbsp;" title="编辑" type="button"  class="editImgBtn"  />编辑</td>
   <%
if(pageBean.getBoolValue("isApprove")){
%>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="R" align="center" onclick="doRequest('approveRequest')"><input id="approve" value="&nbsp;" title="核准" type="button" class="approveImgBtn"   />核准</td>
      <td  align="center"class="bartdx" onclick="doRequest('revokeApproveRequest')" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td>
     <%} %>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="doRequest('viewDetail')"><input id="detailImgBtn" value="&nbsp;" title="查看" type="button" class="detailImgBtn" />查看</td>   
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete($('#'+rsIdTagId).val());"><input id="deleteImgBtn" value="&nbsp;" title="删除"  type="button"  class="delImgBtn" />删除</td>
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable"><tr><td>
&nbsp;状态<select id="STATE" label="状态" name="STATE" class="select" onchange="doQuery()"><%=pageBean.selectValue("STATE")%></select>
&nbsp;起止日期<input id="sdate" label="开始时间" name="sdate" type="text" value="<%=pageBean.inputDate("sdate")%>" size="10" class="text" readonly="readonly"/><img id="sdatePicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
-<input id="edate" label="截止时间" name="edate" type="text" value="<%=pageBean.inputDate("edate")%>" size="10" class="text"  readonly="readonly"/><img id="edatePicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />

&nbsp;姓名<input id="user_Name" label="姓名" name="user_Name" type="text" value="<%=pageBean.inputValue("user_Name")%>" size="24" class="text" ondblclick="emptyText('user_Name')" /><input type="hidden" label="人员选择" id="USER_CODE" name="USER_CODE" value="<%=pageBean.inputValue("USER_CODE")%>" /><img id="userIdSelectImage" src="images/sta.gif" width="16" height="16" onclick="openUserIdBox()" />
&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
</td></tr></table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="出差信息.csv"
retrieveRowsCallback="process" xlsFileName="出差信息.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="15"
listWidth="100%" 
height="390px"
>
<ec:row styleClass="odd" ondblclick="clearSelection();doRequest('viewDetail')" oncontextmenu="selectRow(this,{EVE_ID:'${row.EVE_ID}'});controlUpdateBtn('${row.STATE}');controlCanEdit('${row.EVE_APPLY_USER}','${row.STATE}');refreshConextmenu()" onclick="selectRow(this,{EVE_ID:'${row.EVE_ID}'});controlUpdateBtn('${row.STATE}');controlCanEdit('${row.EVE_APPLY_USER}','${row.STATE}');">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="EVE_CODE_NAME"  title="姓名"   />
	
	<ec:column width="100" property="EVE_START_TIME" title="出差日期" cell="date" format="yyyy-MM-dd" />
	<ec:column width="100" property="EVE_DAYS" style="text-align:right;" title="出差天数"   />
    <ec:column width="100" property="EVE_TOGETHER" title="同行人"   />
	<ec:column width="100" property="EVE_SUBSIDY" style="text-align:right;" title="补助金额"   />
    <ec:column width="100" property="EVE_TOTAL_MONEY" style="text-align:right;background-color:yellow;" title="汇总费用" />
	<ec:column width="100" property="STATE" title="状态" mappingItem="STATE"/>
	<ec:column width="100" property="EVE_APPROVE_USER_NAME" title="核准人"   />
	<ec:column width="100" property="EVE_APPROVE_TIME" title="核准时间" cell="date" format="yyyy-MM-dd" />
    <ec:column width="100" property="APP_RESULT" title="核准结果"  mappingItem="APP_RESULT" />
</ec:row>
</ec:table>
<input type="hidden" name="EVE_ID" id="EVE_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<script language="JavaScript">
function openRequest(actionType)
{	
	var theForm = ele(formTagId);
	if ((actionType == approveRequestActionValue || actionType == approveRequestActionValue 
		 || actionType == viewDetailActionValue) && !isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	
	showSplash(waitMsg);
	$("#"+actionTypeTagId).val(actionType);
	theForm.target="_blank"
	theForm.submit();
	theForm.target="";
	$("#"+actionTypeTagId).val("");
	hideSplash();
	
}
requiredValidator.add("APP_RESULT");
setRsIdTag('EVE_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
initCalendar('sdate','%Y-%m-%d','sdatePicker');
initCalendar('edate','%Y-%m-%d','edatePicker');
datetimeValidators[0].set("yyyy-MM-dd").add("sdate");
var userIdBox;
function openUserIdBox(){
	var handlerId = "UserListSelectList"; 
	if (!userIdBox){
		userIdBox = new PopupBox('userIdBox','请选择人员      ',{size:'normal',width:'300',top:'2px'});
	}
	var url = 'index?'+handlerId+'&targetId=USER_CODE&targetName=user_Name';
	userIdBox.sendRequest(url);
} 
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
