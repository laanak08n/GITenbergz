<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.agileai.com" prefix="aeai"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title>薪资管理</title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<style type="text/css">
.markable{
	background-color:yellow;
	color:black;
}
.markable0{
	background-color:darkorchid;
	color:white;
}
</style>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div id="__ParamBar__" style="float: right;">&nbsp;</div>
<div id="__ToolBar__">
<table border="0" cellpadding="0" cellspacing="1">
<tr>
<%if(pageBean.getBoolValue("hasRight")){%>
  <%if(pageBean.getBoolValue("doEdit&Save")){%>
   <aeai:previlege code="edit"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="enableSave()" ><input value="&nbsp;" type="button" class="editImgBtn" id="modifyImgBtn" title="编辑" />编辑</td></aeai:previlege>
   <aeai:previlege code="save"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="doSubmit({actionType:'save'})"><input value="&nbsp;" type="button" class="saveImgBtn" id="saveImgBtn" title="保存" />保存</td></aeai:previlege>
   <aeai:previlege code="reset"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="doSubmit({actionType:'annualLeaveRecalculation'})"><input value="&nbsp;" type="button" class="resetPasswordImgBtn" id="resetPasswordImgBtn" title="年假重新计算" />年假重新计算</td></aeai:previlege>
	<%}%>   
  <%if((pageBean.selectedValue("SAL_STATE")).equals("0")){%>
   <aeai:previlege code="approve"><td  align="center"class="bartdx"onclick="stateApprove();" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="approveImgBtn" id="SAL_STATE"  title="核准" />核准</td></aeai:previlege>
   <%}else{%>
   <aeai:previlege code="revokeApproval"><td  align="center"class="bartdx" onclick="revokeApproval()" onmouseover="onMover(this);" onmouseout="onMout(this);"  ><input value="&nbsp;"type="button" class="revokeApproveImgBtn" id="revokeApproval" title="反核准" />反核准</td></aeai:previlege>
   <%}%>
  <%}%>
   <aeai:previlege code="back"><td onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="goToBack();"><input value="&nbsp;" type="button" class="backImgBtn" title="返回" />返回</td></aeai:previlege>
</tr>
</table>
</div>
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>人员姓名</th>
	<td>
    <input name="SAL_NAME" type="text" class="text" id="SAL_NAME" value="<%=pageBean.inputValue("SAL_NAME")%>" size="24" readonly="readonly" label="人员姓名" />
    <input id="SAL_USER" label="人员编码" name="SAL_USER" type="hidden" value="<%=pageBean.inputValue("SAL_USER")%>" size="24"  class="text" />
	  
</td>
	<th width="100" nowrap>基本工资</th>
	<td colspan="3"><input name="SAL_BASIC" type="text" class="text" id="SAL_BASIC" value="<%=pageBean.inputValue("SAL_BASIC")%>" size="24" readonly="readonly" label="基本工资" />
</td>
</tr>
<tr>
	<th width="100" nowrap>年</th>
	<td><input name="SAL_YEAR" type="text" class="text" id="SAL_YEAR" value="<%=pageBean.inputValue("SAL_YEAR")%>" size="24" readonly="readonly" label="年" />
</td>
	<th width="100" nowrap>绩效工资</th>
	<td colspan="3"><input name="SAL_PERFORMANCE" type="text" class="text" id="SAL_PERFORMANCE" value="<%=pageBean.inputValue("SAL_PERFORMANCE")%>" size="24" readonly="readonly" label="绩效工资" />
</td>
</tr>
<tr>
	<th width="100" nowrap>月</th>
	<td><input name="SAL_MONTH" type="text" class="text" id="SAL_MONTH" value="<%=pageBean.inputValue("SAL_MONTH")%>" size="24" readonly="readonly" label="月" />
</td>
	<th width="100" nowrap>补贴</th>
	<td colspan="3"><input name="SAL_SUBSIDY" type="text" class="text" id="SAL_SUBSIDY" value="<%=pageBean.inputValue("SAL_SUBSIDY")%>" size="24" readonly="readonly" label="补贴" />
</td>
</tr>
<tr>
	<th width="100" nowrap>有效工作天数</th>
	<td><input name="SAL_VALID_DAYS" type="text" class="text" id="SAL_VALID_DAYS" value="<%=pageBean.inputValue("SAL_VALID_DAYS")%>" size="24" readonly="readonly" label="有效工作天数" />
</td>
	<th width="100" nowrap>奖金</th>
	<td colspan="3"><input id="SAL_BONUS" label="奖金" name="SAL_BONUS" type="text" class="text markable0" value="<%=pageBean.inputValue("SAL_BONUS")%>" size="24" maxlength="6" <%if(!pageBean.getBoolValue("hasRight")&& pageBean.getBoolValue("isComeFromDetail")){ %> readonly="readonly" <%}%>  />
</td>
</tr>
<tr>
	<th width="100" nowrap>出勤天数</th>
	<td><input name="SAL_WORK_DAYS" type="text" class="text" id="SAL_WORK_DAYS" value="<%=pageBean.inputValue("SAL_WORK_DAYS")%>" size="24" readonly="readonly" label="出勤天数" />
</td>
	<th width="100" nowrap>保险</th>
	<td colspan="3"><input name="SAL_INSURE" type="text" class="text" id="SAL_INSURE" value="<%=pageBean.inputValue("SAL_INSURE")%>" size="24" readonly="readonly" label="绩效工资" />
</td>
</tr>
<tr>
	<th width="100" nowrap>加班天数</th>
	<td><input name="SAL_OVERTIME" type="text" class="text" id="SAL_OVERTIME" value="<%=pageBean.inputValue("SAL_OVERTIME")%>" size="24" readonly="readonly" label="加班天数" />
</td>
	<th width="100" nowrap>实发工资</th>
	<td colspan="3"><input name="SAL_ACTUAL" type="text" class="text markable" id="SAL_ACTUAL" value="<%=pageBean.inputValue("SAL_ACTUAL")%>" size="24" readonly="readonly" label="补贴" />
</td>
</tr>
<tr>
	<th width="100" nowrap>请假天数</th>
	<td><input name="SAL_LEAVE" type="text" class="text" id="SAL_LEAVE" value="<%=pageBean.inputValue("SAL_LEAVE")%>" size="24" readonly="readonly" label="请假天数" />
</td>
	<th width="100" nowrap>总工资</th>
	<td colspan="3"><input name="SAL_TOTAL" type="text" class="text markable" id="SAL_TOTAL" value="<%=pageBean.inputValue("SAL_TOTAL")%>" size="24"  readonly="readonly" label="总工资" />
</td>
</tr>
<tr>
	<th width="100" height="19" nowrap>年请假数</th>
    <td><input name="SAL_YEAR_LEAVE" type="text" class="text" id="SAL_YEAR_LEAVE" value="<%=pageBean.inputValue("SAL_YEAR_LEAVE")%>" size="24" readonly="readonly" label="年请假数" />
</td>
	<th width="100" nowrap>个人所得税</th>
	<td colspan="3"><input name="SAL_TAX" type="text" class="text" id="SAL_TAX" value="<%=pageBean.inputValue("SAL_TAX")%>" size="24" readonly="readonly" label="绩效工资" />
</td>
</tr>
<tr>
	<th width="100" nowrap>抵扣假期</th>
	<td><input name="SAL_OFFSET_VACATION" type="text" class="text" id="SAL_OFFSET_VACATION" value="<%=pageBean.inputValue("SAL_OFFSET_VACATION")%>" size="24" readonly="readonly" label="抵扣假期" />
</td>
	<th width="100" height="19" nowrap>状态</th>
    <td colspan="3"><input id="SAL_STATE_NAME" label="状态" name="SAL_STATE_NAME" type="text" value="<%=pageBean.selectedText("SAL_STATE")%>" size="24" class="text" readonly="readonly"/>
	<input id="SAL_STATE" label="状态" name="SAL_STATE" type="hidden" value="<%=pageBean.selectedValue("SAL_STATE")%>" />
</td>
</tr>
<tr>
	<th width="100" nowrap>备注</th>
	<td colspan="3"><textarea id="SAL_REMARKS" label="备注" name="SAL_REMARKS" cols="60" rows="5" class="textarea"><%=pageBean.inputValue("SAL_REMARKS")%></textarea>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" value=""/>
<input type="hidden" name="operaType" id="operaType" value="<%=pageBean.getOperaType()%>"/>
<input type="hidden" id="SAL_ID" name="SAL_ID" value="<%=pageBean.inputValue("SAL_ID")%>" />
</form>
<script language="javascript">
$('#SAL_REMARKS').inputlimiter({
	limit: 100,
	remText: '还可以输入  %n 字 /',
	limitText: '%n 字',
	zeroPlural: false
	});
function stateApprove(){
	doSubmit({actionType:'approve'});
}
function revokeApproval(){
	doSubmit({actionType:'revokeApproval'});
}
numValidator.add("SAL_VALID_DAYS");
numValidator.add("SAL_WORK_DAYS");
numValidator.add("SAL_OVERTIME");
numValidator.add("SAL_LEAVE");
numValidator.add("SAL_BASIC");
numValidator.add("SAL_PERFORMANCE");
numValidator.add("SAL_SUBSIDY");
numValidator.add("SAL_BONUS");
numValidator.add("SAL_TOTAL");
requiredValidator.add("SAL_BONUS");
initDetailOpertionImage();
</script>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
