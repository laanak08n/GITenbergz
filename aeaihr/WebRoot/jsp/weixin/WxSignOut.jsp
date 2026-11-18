<!doctype html>
<%@ page language="java" pageEncoding="UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<meta charset="utf-8">
<title>微信签退</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
<%@include file="/jsp/inc/resource4jqm.inc.jsp"%>
<style type="text/css">
.ui-select .ui-btn{
	padding: 3px 2px;
}
.ui-btn ui-input-btn{
	margin-top: 3px;
}
.ui-select .ui-btn span{
	text-align:left;
	padding-left:3px;
}
#status{
	color:blue;
	border-style:solid; 
	border-width:0px; 
	border-color:blue;
	display:none;
}
</style>
<script type="text/javascript">
$(function(){
	<%if (request.getAttribute("errorMsg") != null){%>
	$("#status").css("color","red");
	$("#status").show();
	<%}else if (pageBean.getStringValue("resultMsg") != null){%>
	$("#status").css("color","green");
	$("#status").show();
	<%}%>
});

function showBeforeDay(){
	doSubmit({actionType:'showBeforeDay'});
}
function showNextDay(){
	doSubmit({actionType:'showNextDay'});
}
function showToday(){
	doSubmit({actionType:'showToday'});
}
function bindWxUser(){
	window.location.href="resource?WxBind";
}
</script>
</head>
<body>
<form action="resource?WxSignOut" name="form1" id="form1" method="post" style="padding:7px 5px">
<div id="status" class="status">
<%
String statusMsg = (String)request.getAttribute("errorMsg");
if (statusMsg == null){
	statusMsg = pageBean.getStringValue("resultMsg");	
}
%>
<%=statusMsg%>
</div>
<%if (pageBean.isValid(pageBean.inputTime("date"))){%>
<input type="hidden" name="date" id="date" value="<%=pageBean.inputTime("date")%>" style="text-align:center;">
<div class="ui-grid-a" style="margin:10px 0px 5px 0px;">
	<div class="ui-block-a" style="text-align: center;"><%=pageBean.inputTime("date")%></div>
	<div class="ui-block-b" style="text-align: center;"><%=pageBean.inputValue("week")%></div>
</div>
<div class="ui-grid-b" style="margin:10px 0px 5px 0px;">
	<div class="ui-block-a" style="text-align: center;"><a href="javascript:showBeforeDay()" rel="external" data-ajax="false">上一天</a></div>
	<div class="ui-block-b" style="text-align: center;"><a href="javascript:showToday()" rel="external" data-ajax="false">今  天</a></div>
	<div class="ui-block-c" style="text-align: center;"><a href="javascript:showNextDay()" rel="external" data-ajax="false">下一天</a></div>
</div>
<%}%>
<% if (pageBean.isValid(pageBean.inputTime("date"))){%>
<div>
	<ul data-role="listview">
<%
if (pageBean.getRsList() != null && pageBean.getRsList().size() > 0){
%>
	<%
	for(int i=0;i < pageBean.getRsList().size();i++){
	%>		
	<li>
	<div>
	<span style="float:right;"><%=pageBean.inputTime(i,"ATD_OUT_TIME").substring(11,16)%><span style="margin-left:15px">（<%=i+1%>）</span></span>
	<span><%=pageBean.labelValue(i, "USER_ID_NAME")%></span>
	</div>
	<div><%=pageBean.inputTime(i,"ATD_OUT_PLACE")%></div>
	</li>
	<%}%>
<%}else{%>
	<li>
	<div>没有记录！</div>
	</li>
<%}%>
	</ul>
</div>
<%}%>
<input type="hidden" name="actionType" id="actionType" value=""/>
</form>
</body>
</html>