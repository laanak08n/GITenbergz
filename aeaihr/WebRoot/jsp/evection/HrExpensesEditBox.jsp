<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function saveRecord(){
	if (!validate()){
		return;
	}
	showSplash();
	postRequest('form1',{actionType:'save',onComplete:function(responseText){
		if (responseText == 'success'){
			hideSplash();
			parent.PopupBox.closeCurrent();
			parent.refreshPage();
		}
	}});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("noUpdate") & pageBean.getBoolValue("doDetail")){%>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveRecord()"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td>
   <%}%>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="parent.PopupBox.closeCurrent();"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>出发地</th>
	<td><input id="EXPE_DEPART" label="出发地" name="EXPE_DEPART" type="text" value="<%=pageBean.inputValue("EXPE_DEPART")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>目的地</th>
	<td><input id="EXPE_DESTINATION" label="目的地" name="EXPE_DESTINATION" type="text" value="<%=pageBean.inputValue("EXPE_DESTINATION")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>出发时间</th>
	<td><input id="EXPE_DEPART_TIME" label="出发时间" name="EXPE_DEPART_TIME" type="text" value="<%=pageBean.inputTime("EXPE_DEPART_TIME")%>" size="24" class="text" /><img id="EXPE_DEPART_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>到达时间</th>
	<td><input id="EXPE_COMEBACK_TIME" label="到达时间" name="EXPE_COMEBACK_TIME" type="text" value="<%=pageBean.inputTime("EXPE_COMEBACK_TIME")%>" size="24" class="text" /><img id="EXPE_COMEBACK_TIMEPicker" src="images/calendar.gif" width="16" height="16" alt="日期/时间选择框" />
</td>
</tr>
<tr>
	<th width="100" nowrap>交通方式</th>
	<td><select id="EXPE_TRANSPORTATION_WAY" label="交通方式" name="EXPE_TRANSPORTATION_WAY" class="select"><%=pageBean.selectValue("EXPE_TRANSPORTATION_WAY")%></select>
</td>
</tr>
<tr>
	<th width="100" nowrap>交通费用</th>
	<td><input id="EXPE_TRANSPORTATION_FEE" label="交通费用" name="EXPE_TRANSPORTATION_FEE" type="text" value="<%=pageBean.inputValue("EXPE_TRANSPORTATION_FEE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>住宿费用</th>
	<td><input id="EXPE_HOTEL" label="交通费用" name="EXPE_HOTEL" type="text" value="<%=pageBean.inputValue("EXPE_HOTEL")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>其他费用</th>
	<td><input id="EXPE_OTHER" label="其他费用" name="EXPE_OTHER" type="text" value="<%=pageBean.inputValue("EXPE_OTHER")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>备注</th>
	<td><textarea id="EXPE_REMARKS" label="备注" name="EXPE_REMARKS" cols="40" rows="3" class="textarea"><%=pageBean.inputValue("EXPE_REMARKS")%></textarea>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="EXPE_ID" name="EXPE_ID" value="<%=pageBean.inputValue4DetailOrUpdate("EXPE_ID","")%>" />
<input type="hidden" id="EVE_ID" name="EVE_ID" value="<%=pageBean.inputValue("EVE_ID")%>" />
</form>
<script language="javascript">
initCalendar('EXPE_DEPART_TIME','%Y-%m-%d %H:%M','EXPE_DEPART_TIMEPicker');
initCalendar('EXPE_COMEBACK_TIME','%Y-%m-%d %H:%M','EXPE_COMEBACK_TIMEPicker');
requiredValidator.add("EXPE_DEPART");
requiredValidator.add("EXPE_DESTINATION");
requiredValidator.add("EXPE_DEPART_TIME");
datetimeValidators[0].set("yyyy-MM-dd HH:mm").add("EXPE_DEPART_TIME");
requiredValidator.add("EXPE_COMEBACK_TIME");
datetimeValidators[1].set("yyyy-MM-dd HH:mm").add("EXPE_COMEBACK_TIME");
requiredValidator.add("EXPE_TRANSPORTATION_WAY");
requiredValidator.add("EXPE_TRANSPORTATION_FEE");
requiredValidator.add("EXPE_HOTEL");
requiredValidator.add("EXPE_OTHER");
initDetailOpertionImage();
numValidator.add("EXPE_TRANSPORTATION_FEE");
numValidator.add("EXPE_HOTEL");
numValidator.add("EXPE_OTHER");
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
