<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>工资额度设定</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="doSubmit({actionType:'save'});"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="确定"/>确定</td>
   <td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="javascript:parent.PopupBox.closeCurrent();"><input value="&nbsp;" type="button" class="closeImgBtn" title="关闭" />关闭</td>   
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">

<tr>
	<th width="100" nowrap>工资额度</th>
	<td><input id="salaryLimit" label="工资额度" name="salaryLimit" type="text" value="<%=pageBean.inputValue("salaryLimit")%>" size="24" class="text" />
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
</form>
<script language="javascript">
numValidator.add("salaryLimit");
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
