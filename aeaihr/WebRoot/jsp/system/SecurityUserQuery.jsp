<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>321</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
function selectRequest(idValue,nameValue){
	parent.ele(ele('targetId').value).value= idValue;
	parent.ele(ele('targetName').value).value= nameValue;
	parent.PopupBox.closeCurrent();
}
function setSelectTempValue(idValue,nameValue){
	ele('targetIdValue').value = idValue;
	ele('targetNameValue').value = nameValue;
}
function doSelectRequest(){
	if (!isValid(ele('targetIdValue').value)){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	 var ids = "";
	$("input[name='USER_ID']:checked").each(function(){   
		ids = ids+$(this).val()+",";
	});
	if (ids.length > 0){
		ids = ids.substring(0,ids.length-1);
	}
	
	postRequest('form1',{actionType:'SaveUser',onComplete:function(responseText){
		 if (responseText == 'success'){
				parent.refreshContent(); 
				parent.PopupBox.closeCurrent();
			}
	 }});
	
}
ECSideUtil.checkAll=function(checkcontrolObj,checkboxname,formid){

	var form=ECSideList[formid].ECForm;
	if (!form.elements[checkboxname]){ return;}
	var checked=false;
	if (checkcontrolObj.className=="checkedboxHeader"){
		checked=true;
		$("#targetIdValue").val(""); 
		checkcontrolObj.className="checkboxHeader";
	}else{
		$("#targetIdValue").val("全选"); 
		checkcontrolObj.className="checkedboxHeader";
	}
	if (!form.elements[checkboxname].length){ 
		if (!form.elements[checkboxname].disabled){
			form.elements[checkboxname].checked = !checked;
		}
		return;
	}
	for(i = 0; i < form.elements[checkboxname].length; i++) {
		if (!form.elements[checkboxname][i].disabled){
			form.elements[checkboxname][i].checked = !checked;
		}
	}
};
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>
	<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="V" align="center" onclick="doSelectRequest()"><input value="&nbsp;" type="button" class="saveImgBtn" title="选择" />选择</td>
	<td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="B" align="center" onclick="javascript:parent.PopupBox.closeCurrent();"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td>        
</tr>
</table>
</div>
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="321.csv"
retrieveRowsCallback="process" xlsFileName="321.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="auto" 
>
<ec:row styleClass="odd" ondblclick="selectRequest('${row.USER_ID}','${row.USER_NAME}')" onclick="setSelectTempValue('${row.USER_ID}','${row.USER_ID}')">
	<ec:column width="20" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="25" style="text-align:center" property="USER_ID" cell="checkbox" headerCell="checkbox" />
	<ec:column width="100" property="USER_CODE" title="编码"   />
	<ec:column width="100" property="USER_NAME" title="姓名"   />
	<ec:column width="100" property="USER_STATE" title="状态"  mappingItem="USER_STATE" />
</ec:row>
</ec:table>
<input type="hidden" id="columnId" name="columnId" value="<%=pageBean.inputValue("columnId")%>" />
<input type="hidden" id="_tabId_" name="_tabId_" value="<%=pageBean.inputValue("_tabId_")%>" />
<input type="hidden" name="actionType" id="actionType" />
<input type="hidden" name="USER_IDS" id="USER_IDS" value="<%=pageBean.inputValue("USER_ID")%>" />
<input type="hidden" name="RG_ID" id="RG_ID" value="<%=pageBean.inputValue("RG_ID")%>" />
<input type="hidden" name="URG_ID" id="URG_ID" value="<%=pageBean.inputValue("URG_ID")%>" />
<input type="hidden" name="targetId" id="targetId" value="<%=pageBean.inputValue("targetId")%>" />
<input type="hidden" name="targetName" id="targetName" value="<%=pageBean.inputValue("targetName")%>" />
<input type="hidden" name="targetIdValue" id="targetIdValue" value="" />
<input type="hidden" name="targetNameValue" id="targetNameValue" value="" />
<script language="JavaScript">
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
