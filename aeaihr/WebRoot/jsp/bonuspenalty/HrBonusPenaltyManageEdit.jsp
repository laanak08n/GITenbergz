<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>奖惩管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <aeai:previlege code="edit"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td></aeai:previlege>
   <aeai:previlege code="detail"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="doSubmit({actionType:'save'})"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td></aeai:previlege>
   <aeai:previlege code="back"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="goToBack();"><input value="&nbsp;" type="button" class="backImgBtn" title="返回" />返回</td></aeai:previlege>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>姓名</th>
	<td><input id="USER_NAME" label="USER_NAME" name="USER_NAME" type="text" value="<%=pageBean.inputValue("USER_NAME")%>" size="24" class="text" readonly="readonly"/>
	<input id="USER_ID" label="USER_ID" name="USER_ID" type="hidden" value="<%=pageBean.inputValue("USER_ID")%>" size="24" class="text" />
	<img id="userIdSelectImage" src="images/sta.gif" width="16" height="16" onclick="openUserIdBox()" />
</td>
</tr>
<tr>
	<th width="100" nowrap>日期</th>
	<td><input id="BP_DATE" label="日期" name="BP_DATE" type="text" value="<%=pageBean.inputDate("BP_DATE")%>" size="24" class="text" readonly="readonly"/><img id="BP_DATEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>类型</th>
	<td>&nbsp;<%=pageBean.selectRadio("BP_TYPE")%>
</td>
</tr>
<tr>
	<th width="100" nowrap>金额</th>
	<td><input id="BP_MONEY" label="金额" name="BP_MONEY" type="text" value="<%=pageBean.inputValue("BP_MONEY")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="BP_DESC" label="描述" name="BP_DESC" cols="60" rows="5" class="textarea"><%=pageBean.inputValue("BP_DESC")%></textarea>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="BP_ID" name="BP_ID" value="<%=pageBean.inputValue4DetailOrUpdate("BP_ID","")%>" />
</form>
<script language="javascript">
initCalendar('BP_DATE','%Y-%m-%d','BP_DATEPicker');
requiredValidator.add("USER_ID");
requiredValidator.add("BP_DATE");
datetimeValidators[0].set("yyyy-MM-dd").add("BP_DATE");
requiredValidator.add("BP_MONEY");
numValidator.add("BP_MONEY");
initDetailOpertionImage();
var userIdBox;
function openUserIdBox(){
	var handlerId = "UserListSelectList"; 
	if (!userIdBox){
		userIdBox = new PopupBox('userIdBox','请选择人员      ',{size:'normal',width:'300',top:'2px'});
	}
	var url = 'index?'+handlerId+'&targetId=USER_ID&targetName=USER_NAME';
	userIdBox.sendRequest(url);
}
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
