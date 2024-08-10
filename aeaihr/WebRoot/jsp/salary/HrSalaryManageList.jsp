<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>薪资管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
var setValidDaysBox;
function setValidDays(){
	var title = '有效天数';
	if (!setValidDaysBox){
		setValidDaysBox = new PopupBox('setValidDaysBox',title,{size:'normal',height:'300px',top:'10px'});
	}
	var year = $("#salDate").val().substring(0,4);
	var month = $("#salDate").val().substring(5,7);
	var url = "index?HrValidDays&actionType=prepareDisplay&year="+year+"&month="+month;
	setValidDaysBox.sendRequest(url);	
}
function gatherData(){	
	doSubmit({actionType:'gather'});
}
function revokeApproval(){
	if (actionType != insertRequestActionValue && !isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	doSubmit({actionType:'revokeApproval'});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(!pageBean.getBoolValue("hasRight")){
	%>
<%if(pageBean.getBoolValue("canGather")){
	%>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="gatherData()" <%if(!pageBean.getBoolValue("validDays")){
	%>hidden="hidden"<%}%> ><input id="gatherImgBtn" value="&nbsp;" title="汇总" type="button"  class="createImgBtn" />汇总</td>
   <%}%>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="doRequest('updateRequest')"><input value="&nbsp;" title="编辑" type="button" id="edit" class="editImgBtn"  />编辑</td>
  <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="R" align="center" onclick="doRequest('approveRequest')"><input value="&nbsp;" title="核准" type="button" id="approve" class="approveImgBtn"   />核准</td>
  <td  align="center"class="bartdx"onclick="revokeApproval()" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td>  
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="setValidDays()" ><input value="&nbsp;" title="设置有效天数" type="button" class="editImgBtn" />设置有效天数</td>
    <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete($('#'+rsIdTagId).val());"><input id="deleteImgBtn" value="&nbsp;" title="删除"  type="button"  class="delImgBtn" />删除</td>  
   <%}%>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="doRequest('viewDetail')"><input value="&nbsp;" title="查看" type="button" class="detailImgBtn" />查看</td>    
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable"><tr><td>
&nbsp;年月<input id="salDate" label="时间" name="salDate" type="text" value="<%=pageBean.inputValue("salDate")%>"  size="10" class="text" />
&nbsp;&nbsp;
&nbsp;状态<select id="salState" label="状态" name="salState" class="select" onchange="doQuery()"><%=pageBean.selectValue("salState")%></select>
&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
&nbsp;<input type="submit" name="button" id="button" value="上一月" class="formbutton" onclick="doSubmit({actionType:'beforeMonth'})" />
&nbsp;<input type="submit" name="button" id="button" value="下一月" class="formbutton" onclick="doSubmit({actionType:'nextMonth'})" /></td></tr></table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="薪资管理.csv"
retrieveRowsCallback="process" xlsFileName="薪资管理.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="15"
listWidth="100%" 
height="390px"
>
<ec:row styleClass="odd" ondblclick="clearSelection();doRequest('viewDetail')" oncontextmenu="selectRow(this,{SAL_ID:'${row.SAL_ID}'});controlApproveBtn('${row.SAL_STATE}');refreshConextmenu()" onclick="selectRow(this,{SAL_ID:'${row.SAL_ID}'});controlApproveBtn('${row.SAL_STATE}');">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="SAL_NAME"  title="人员姓名"   />
	<ec:column width="100" property="SAL_YEAR_MONTH" title="年月"   />
	<ec:column width="100" property="SAL_VALID_DAYS" style="text-align:right;" title="有效天数"   />
	<ec:column width="100" property="SAL_WORK_DAYS" style="text-align:right;" title="出勤天数"   />
	<ec:column width="100" property="SAL_OVERTIME" style="text-align:right;" title="加班天数"   />
	<ec:column width="100" property="SAL_LEAVE" style="text-align:right;" title="请假天数"   />
	<ec:column width="100" property="SAL_BASIC" style="text-align:right;" title="基本工资"   />
	<ec:column width="100" property="SAL_PERFORMANCE" style="text-align:right;" title="绩效工资"   />
	<ec:column width="100" property="SAL_SUBSIDY" style="text-align:right;" title="补贴"   />
	<ec:column width="100" property="SAL_BONUS" style="text-align:right;" title="奖金"   />
    <ec:column width="100" property="SAL_INSURE" style="text-align:right;" title="保险"   />
    <ec:column width="100" property="SAL_TAX" style="text-align:right;" title="个人所得税"   />
	<ec:column width="100" property="SAL_TOTAL" style="text-align:right;background-color:yellow;" title="总工资"   />
     <ec:column width="100" property="SAL_ACTUAL" style="text-align:right;background-color:yellow;" title="实发工资"   />
	<ec:column width="100" property="SAL_STATE" title="状态"   mappingItem="SAL_STATE"/>
</ec:row>
</ec:table>
<input type="hidden" name="SAL_ID" id="SAL_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<script language="JavaScript">
function controlApproveBtn(stateResult){
if (stateResult == '1'){
		disableButton("approve");
		enableButton("revokeApproval");
		disableButton("deleteImgBtn");
	}else{
		enableButton("approve");
		disableButton("revokeApproval");
		enableButton("deleteImgBtn");
	}

if (stateResult == '1'){
		disableButton("edit");
	}else{
		enableButton("edit");
	}
}
setRsIdTag('SAL_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
