<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@page import="javax.ccpp.SetAttribute"%>
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>考勤系统</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script type="text/javascript">
var editBox;
function showEditBox(operType){
	clearSelection();
	if (!editBox){
		editBox = new PopupBox('editBox','详细信息',{size:'big',height:'400px',top:'30px'});
	}
	var url = "<%=pageBean.getHandlerURL()%>&actionType="+operType;
	editBox.sendRequest(url);
}
</script>

</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="A" align="center" onclick="showEditBox('insertRequest')"><input id="singInImgBtn" value="&nbsp;" title="新增" type="button" class="createImgBtn" />签到</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="E" align="center" onclick="showEditBox('updateRequest')"><input id="singOutImgBtn" value="&nbsp;" title="编辑" type="button" class="editImgBtn" />签退</td>
</tr>
</table>
</div>
<div id="__ParamBar__">
<table class="queryTable"><tr>
  <td>
&nbsp;日期
<input id="adtDate" label="签到时间" name="adtDate"   type="text" value="<%=pageBean.inputValue("adtDate")%>" size="10" class="text" /><img id="adtDatePicker" style="vertical-align:middle" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />

&nbsp;<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
&nbsp;<input type="submit" name="button" id="button" value="上一天" class="formbutton" onclick="doSubmit({actionType:'beforeDay'})" />
&nbsp;<input type="submit" name="button" id="button" value="下一天" class="formbutton" onclick="doSubmit({actionType:'nextDay'})" />
</td>
</tr>
</table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="考勤系统.csv"
retrieveRowsCallback="process" xlsFileName="考勤系统.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="15"
listWidth="100%" 
height="390px"
>
<ec:row styleClass="odd" onclick="selectRow(this,{ATD_ID:'${row.ATD_ID}'})">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="USER_ID_NAME" title="姓名"   />
	<ec:column width="100" property="ATD_IN_TIME" title="签到时间" cell="date" format="yyyy-MM-dd HH:mm" />
	<ec:column width="100" property="ATD_IN_PLACE" title="签到地点"   />
	<ec:column width="100" property="ATD_OUT_TIME" title="签退时间" cell="date" format="yyyy-MM-dd HH:mm" />
	<ec:column width="100" property="ATD_OUT_PLACE" title="签退地点"   />
</ec:row>
</ec:table>
<input type="hidden" name="ATD_ID" id="ATD_ID" value="" />
<input type="hidden" name="actionType" id="actionType" />
<script language="JavaScript">
setRsIdTag('ATD_ID');
initCalendar('adtDate','%Y-%m-%d','adtDatePicker');
datetimeValidators[0].set("yyyy-MM-dd").add("adtDate");
<%
if(!pageBean.getBoolValue("canSignIn")){
%>
disableButton("singInImgBtn");
<%}%>

<%
if(!pageBean.getBoolValue("canSignOut")){
%>
disableButton("singOutImgBtn");
<%}%>
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
