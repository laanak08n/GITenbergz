<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function selectRequest(idValue,nameValue){
	parent.ele(ele('ROLE_ID').value).value= idValue;
	parent.ele(ele('GRP_ID').value).value= nameValue;
	parent.PopupBox.closeCurrent();
}
function setSelectTempValue(idValue,nameValue){
	ele('ROLE_ID').value = idValue;
	ele('targetNameValue').value = nameValue;
}
function doSelectRequest(){
	if (!isValid(ele('targetIdValue').value)){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	selectRequest(ele('targetNameValue').value,ele('targetNameValue').value);
}

var tree;
function fillIds()
{
	var chk = tree.Nodes;
	var tempId = '';
	for(var o in chk) {
		if(chk[o].checked){
			tempId=tempId+','+chk[o].ID;
		}
	}
	if(tempId.length > 0)
	{
		$('#targetIdValue').val(tempId.substring(1,tempId.length));
	}
}
function fillNames()
{
	var chk = tree.Nodes;
	var tempName = '';
	for(var o in chk) {
		if(chk[o].checked){
			tempName=tempName+','+chk[o].Text;
		}
	}
	if(tempName.length > 0)
	{
		$('#targetNameValue').val(tempName.substring(1,tempName.length));
	}
}

function saveRecord(){
	if (!validate()){
		return;
	}
	showSplash();
	postRequest('form1',{actionType:'save',onComplete:function(responseText){
		if (responseText == 'success'){
			parent.refreshContent();
			parent.PopupBox.closeCurrent();
		}else if(responseText == 'posChild'){
			writeErrorMsg('请添加下级角色！');
		}else if(responseText == 'isMenu'){
			writeErrorMsg('目录不可选！');
		}
	}});	
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<table class="detailTable" cellspacing="0" cellpadding="0">
  <tr>
    <td valign="top" nowrap>
<div id="selectTreeContainer" style="height:312px;overflow-y:auto" ondblclick="saveRecord()">
<script language="javascript">
<%=pageBean.inputValue("pickTreeSyntax")%>
</script>
</div>	
	</td>
  </tr>
  <tr>
    <td align="center">
    <input class="formbutton" type="button" name="Button23" value="确定" onclick="saveRecord()"/>&nbsp; &nbsp;
    <input class="formbutton" type="button" name="Button22" value="关闭" onclick="javascript:parent.PopupBox.closeCurrent();"/></td>
  </tr>
</table>
<input type="hidden" name="actionType" id="actionType" />
<input type="hidden" name="targetId" id="targetId" value="<%=pageBean.inputValue("targetId")%>" />
<input type="hidden" name="targetIdValue" id="targetIdValue" value="" />
<input type="hidden" name="targetNameValue" id="targetNameValue" value="" />
<input type="hidden" name="GRP_ID" id="GRP_ID" value="<%=pageBean.inputValue("GRP_ID")%>"/>
<input type="hidden" name="GRP_PID" id="GRP_PID" value="<%=pageBean.inputValue("GRP_PID")%>"/>
<input type="hidden" id="ROLE_ID" name="ROLE_ID" value="<%=pageBean.inputValue4DetailOrUpdate("ROLE_ID","")%>" />
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
