<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<link href="css/bootstrap.min.css" rel="stylesheet">
<link href="css/font-awesome.min.css" rel="stylesheet">
<link rel="stylesheet" type="text/css" media="screen" href="css/overide.css" />
<link rel="stylesheet" type="text/css" media="screen" href="css/trirand/ui.jqgrid-bootstrap.css?<%=themeVersion%>" />
<link rel="stylesheet" href="css/default/easyui.css"  type="text/css" />
<link rel="stylesheet" href="css/icon.css"  type="text/css" />
<style type="text/css">
.color2 .b2, .color2 .b3, .color2 .b4, .color2 .b5, .color2 .b6, .color2 .b7, .color2 .content, .color2 .b9 {
    border-color: #ddd;
}
.sharp h3.portletTitle {
    height: 31px;
}
.calcWidth {
	width: calc(100% - 230px);
	float: left;
}
#errorMsg{
	height: 20px !important;
}
</style>
<script src="js/jquery.easyui.min.js" language="javascript"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/ecmascript" src="js/trirand/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="js/trirand/i18n/grid.locale-en.js"></script>
<script language="javascript">
var currentEvenSource;
var currentParamItemIndex = -1;
var currentGridItemIndex = -1;
var currentFormItemCode;

var operaRequestBox;
function openContentRequestBox(operaType,title,handlerId,subPKField,tableMode){
	var curColumnName = $("#curColumnName").val();
	if ('insert' != operaType && !isSelectedRow()){
		jAlert('请先选中一条记录!');
		return;
	}
	if (!operaRequestBox){
		operaRequestBox = new PopupBox('operaRequestBox',title,{size:'big',top:'2px'});
	}
	var columnIdValue = "";
	if ('Many2ManyAndRel'==tableMode){
		columnIdValue = $("#curColumnId").val();
		if ('insert' == operaType){
			columnIdValue = $("#columnId").val();
		}
	}
	else{
		columnIdValue = $("#columnId").val();	
	}
	var url = 'index?'+handlerId+'&GRP_ID='+columnIdValue+'&operaType='+operaType+'&'+subPKField+'='+$("#"+subPKField).val();
	operaRequestBox.sendRequest(url);
}

function getShowChildNodeRecords(){
	var showChildNodeRecords = "N";
	if($('#showChildNodeRecords').is(':checked')){
		showChildNodeRecords = $('#showChildNodeRecords').val();
	}
	return showChildNodeRecords;
}

function showFilterBox(){
	$('#filterBox').show();
	var clientWidth = $(document.body).width();
	var tuneLeft = (clientWidth - $("#filterBox").width())/2-2;	
	$("#filterBox").css('left',tuneLeft);	
}
function doRemoveContent(){
	if (!isSelectedRow()){
		jAlert('请先选中一条记录!');
		return;
	}
	jConfirm('确认要移除该条记录吗？',function(r){
		if(r){
			postRequest('form1',{actionType:'removeContent',onComplete:function(responseText){
				if (responseText == 'true'){
					writeErrorMsg("该信息有角色关联，请先删除关联！");
				}else if(responseText == 'unique'){
					jConfirm('该信息只关联当前组织，删除会将该用户彻底删除，确认要删除吗？',function(rr){
						if (rr){
							isDelete();
						}
					})
				}else{
					$('#queryTable').trigger('reloadGrid');
				}
			}});
		}
	});
}
function isDelete(){
	postRequest("form1",{actionType:"doDeleteAction",onComplete:function(responseText){
		if(responseText.indexOf("role")>=0){
			if(responseText.indexOf("auth")>=0){
				writeErrorMsg("该用户有角色关联和权限关联，请先删除关联！");
			}else{
				writeErrorMsg("该用户有角色关联，请先删除关联！");
			}
		}else{
			if(responseText.indexOf("auth")>=0){
				writeErrorMsg("该用户有权限关联，请先删除关联！");
			}else{
				$("#queryTable").jqGrid("delRowData", currentParamItemIndex); 
				$('#queryTable').trigger('reloadGrid');
			}
		}
	}});
}

function isSelectedTree(){
	if (isValid($('#columnId').val())){
		return true;
	}else{
		return false;
	}
}
var targetTreeBox;
function openTargetTreeBox(curAction){
	var columnIdValue = $("#columnId").val();
	if (!isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	if (curAction == 'copyContent' || curAction == 'moveContent'){
		if (!isSelectedRow()){
			jAlert('请先选中一条记录!');
			return;
		}
		columnIdValue = $("#curColumnId").val()
	}	
	if (!targetTreeBox){
		targetTreeBox = new PopupBox('targetTreeBox','请选择目标分组',{size:'normal',width:'300px',top:'2px'});
	}
	var handlerId = "SecurityUserManagerTreePick";
	var url = 'index?'+handlerId+'&GRP_ID='+columnIdValue+'&USER_ID='+$("#USER_ID").val();
	targetTreeBox.sendRequest(url);
	$("#actionType").val(curAction);
	
}
function doChangeParent(){
	var curAction = $('#actionType').val();
	postRequest('form1',{actionType:curAction,onComplete:function(responseText){
		if (responseText == 'success'){
			if (curAction == 'moveTree'){
				refreshTree();			
			}else{
				refreshContent($("#targetParentId").val());		
			}
		}else if(responseText=="parentRoot"){
			writeErrorMsg('出错啦！该节点有子节点不能关联！');
		}
		else{
			writeErrorMsg('出错啦！该节点内已经有啦！');
		}
	}});
} 
function clearFilter(){
	$("#filterBox input[type!='button'],select").val('');
}
function changeTab(tabId){
	$('#_tabId_').val(tabId);
	if(tabId=="EmployeePOM"){
		$("#tab_tab_0_Td02").text("人员列表");
		$('#Layer0').css('display','none');
		$('#Layer1').css('display','block');
	}
	else{
		$("#tab_tab_0_Td02").text("人员管理");
		$('#Layer1').css('display','none');
		$('#Layer0').css('display','block');
	}
}
function refreshTree(){
	doQuery();
}
function refreshContent(curNodeId){
	if (curNodeId){
		$('#columnId').val(curNodeId);
	}
	changeTab("_base_");
	var tabId = $("#_tabId_").val();
	if (tabId == '_base_'){
		$('#queryTable').trigger('reloadGrid');
	}else{
		showOrgPosEmpList();
	}
}
function showOrgPosEmpList(){ 	
	var curColumnName = $("#curColumnName").val();
	var url = "index?SecurityUserGRManageList&RG_ID="+$("#rgId").val();
	$('#OrgPosEmpFrame').attr('src',url);
}
function saveTreeBaseRecord(){
	postRequest('form1',{actionType:'saveTreeBaseRecord',onComplete:function(responseText){
		if (responseText == 'success'){
			refreshTree();
		}else {
			writeErrorMsg('保存基本信息出错啦！');
		}
	}});	
}
function selectButton(){
	var ISSYNC=$("#ISSYNC").val();
	if(ISSYNC=='Y'){
		disableButton('doIssync');
		setImgDisabled('doIssync',true);
	}
	else{
		enableButton('doIssync');
		setImgDisabled('doIssync',false);
	}
}
function doDelete(){
	var userId = $('#'+rsIdTagId).val()
	if (!isValid(userId)){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	jConfirm("您确认要删除这条信息吗？",function(changeRelease){
		if(changeRelease){
			isDelete();
		}
	})
}

var resetPasswordBox;
function openResetPasswordBox(){
	if (!isSelectedRow()){
		jAlert('请先选中一条记录!');
		return;
	}
	var title = '重置用户密码';
	if (!resetPasswordBox){
		resetPasswordBox = new PopupBox('resetPasswordBox',title,{size:'normal',top:'2px'});
	}
	var url = 'index?ResetPassword&USER_ID='+$("#USER_ID").val();
	resetPasswordBox.sendRequest(url);
}
function showPos(){
	if(!$("input[name='treeState']:checked").val()){
		$("#columnId").val("00000000-0000-0000-00000000000000000");
		refreshContent($("#columnId").val());
	}
	doQuery();
}
function doQueryAction(){
    $("#queryTable").jqGrid('setGridParam',{
        url:'index?SecurityUserList&actionType=retrieveTableJson',
    	mtype: "POST",
        postData:{'userCode':$('#userCode').val(),'userName':$('#userName').val(),'showChildNodeRecords':getShowChildNodeRecords()},
        page:1 
    }).trigger("reloadGrid"); 
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div style="width: 100%;">
	<div style="width:230px; float: left;">
	    <div id="leftTree" class="sharp color2" style="margin-top:8px; float: none;">
			<b class="b2"></b><b class="b3"></b><b class="b4"></b>
		    <div class="content">
			    <h3 class="portletTitle">&nbsp;&nbsp;组织列表&nbsp;&nbsp;&nbsp;
			   		<span style="text-align:right;float: right;vertical-align: middle;"><input name="treeState" id="treeState" type="checkbox" value="true" onclick="showPos()" <%=pageBean.checked(pageBean.inputValue("treeState"))%> >&nbsp;<span style="float: inherit;">显示角色&nbsp;&nbsp;</span></input></span>
			    </h3>       
			    <div id="treeArea" style="overflow:auto; height:420px;width:230px;background-color:#F9F9F9;padding-top:5px;padding-left:5px;">
					<ul id="tt" class="easyui-tree" data-options="url:'<%=pageBean.getHandlerURL()%>&actionType=retrieveTreeJson&resourceType=<%=pageBean.inputValue("resourceType")%>&resourceId=<%=pageBean.inputValue("resourceId")%>&factId=<%=pageBean.inputValue("factId")%>&treeState=<%=pageBean.inputValue("treeState")%>',method:'get',onlyLeafCheck:true,animate:false">
					</ul>
				</div>
		    	<b class="b9"></b>
		    </div>
	    </div>
	    <input type="hidden" name="curColumnName" id="curColumnName" value="" />
	    <input type="hidden" id="targetParentId" name="targetParentId" value="" />
    </div>
	<div class="calcWidth">
		<ul class="nav nav-tabs" role="tablist">    
		  <li role="presentation" class="active" id="_base_"><a id="tab_tab_0_Td02" href="#list-page" role="tab" data-toggle="tab">人员管理</a></li>    
		</ul>
		<div id="Layer0" style="height:auto;">
			<div style="padding:0 2px;">
				<div id="filterBox" class="sharp color2" style="position:absolute;top:30px;display:none; z-index:10; width:480px;">
					<b class="b9"></b>
					<div class="content">
					<h3>&nbsp;&nbsp;条件过滤框</h3>
					<table class="detailTable" cellpadding="0" cellspacing="0" style="width:99%;margin:1px;">
					<tr>
						<th width="100" nowrap>用户编码</th>
						<td><input id="userCode" label="用户编码" name="userCode" type="text" value="<%=pageBean.inputValue("userCode")%>" size="10" class="text" />
					</td>
					</tr>
					<tr>
						<th width="100" nowrap>用户名称</th>
						<td><input id="userName" label="用户名称" name="userName" type="text" value="<%=pageBean.inputValue("userName")%>" size="10" class="text" />
					</td>
					</tr>
					</table>
					<div style="width:100%;text-align:center;">
					<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQueryAction()" />
					&nbsp;&nbsp;
					<input type="button" name="button" id="button" value="清空" class="formbutton" onclick="clearFilter()" />
					&nbsp;&nbsp;<input type="button" name="button" id="button" value="关闭" class="formbutton" onclick="javascript:$('#filterBox').hide();" /></div>
					</div>
					<b class="b9"></b>
				</div>
				<div class="tab-content">    
					<div role="tabpanel" class="tab-pane active" id="list-page">
						<div class="container-fluid">
							<div class="row">
							   	<div class="form-group">&nbsp;
									<button type="button" class="btn btn-success btn-sm" type="button" onclick="openContentRequestBox('insert','用户信息','SecurityUserEdit','USER_ID')"><span class="glyphicon glyphicon-plus"></span>&nbsp;新增</button>
								    <button type="button" class="btn btn-primary btn-sm" type="button" onclick="openContentRequestBox('update','用户信息','SecurityUserEdit','USER_ID')"><span class="glyphicon glyphicon-zoom-in"></span>&nbsp;编辑</button>
								    <button type="button" class="btn btn-info btn-sm" type="button" onclick="openContentRequestBox('copy','用户信息','SecurityUserEdit','USER_ID')"><span class="glyphicon glyphicon-book"></span>&nbsp;复制</button>
								    <button type="button" class="btn btn-warning btn-sm" type="button" onclick="openContentRequestBox('detail','用户信息','SecurityUserEdit','USER_ID')"><span class="glyphicon glyphicon-list-alt"></span>&nbsp;查看</button>		
								    <button type="button" class="btn btn-primary btn-sm" type="button" onclick="openResetPasswordBox()"><span class="glyphicon glyphicon-repeat"></span>&nbsp;重置</button>
								    <button type="button" class="btn btn-warning btn-sm" type="button" onclick="showFilterBox()"><span class="glyphicon glyphicon-eye-open"></span>&nbsp;过滤</button>  
								    <button type="button" class="btn btn-danger btn-sm" type="button" onclick="doDelete();"><span class="glyphicon glyphicon-trash"></span>&nbsp;删除</button>
								    <button type="button" class="btn btn-danger btn-sm" type="button" onclick="doRemoveContent()"><span class="glyphicon glyphicon-remove"></span>&nbsp;移除</button>
								    <button type="button" class="btn btn-success btn-sm" type="button" onclick="openTargetTreeBox('copyContent')"><span class="glyphicon glyphicon-send"></span>&nbsp;分发</button>  
								    <button type="button" class="btn btn-info btn-sm" type="button" onclick="openTargetTreeBox('moveContent')"><span class="glyphicon glyphicon-random"></span>&nbsp;迁移</button>
								    <span style="float:right;height:28px;line-height:28px;"><input style="vertical-align:middle; margin-top:-2px; margin-bottom:1px;" name="showChildNodeRecords" type="checkbox" id="showChildNodeRecords" onclick="doQueryAction()" value="Y" <%=pageBean.checked(pageBean.inputValue("showChildNodeRecords"))%> />&nbsp;显示子节点记录&nbsp;</span>  
									<div class="col-md-11" id="queryTableGroup">
							            <table id="queryTable"></table>
							            <div id="jqGridPager"></div>
									</div>
								</div>	
							</div>
						</div>
					</div>    
				</div>
				<input type="hidden" id="_tabId_" name="_tabId_" value="<%=pageBean.inputValue("_tabId_")%>" />
				<input type="hidden" name="columnId" id="columnId" value="<%=pageBean.inputValue("columnId")%>" />
				<input type="hidden" name="USER_ID" id="USER_ID" value="<%=pageBean.inputValue("USER_ID")%>" />
				<input type="hidden" name="curColumnId" id="curColumnId" value="<%=pageBean.inputValue("curColumnId")%>" />
				<input type="hidden" name="rgId" id="rgId" value="<%=pageBean.inputValue("rgId")%>" />
				<script language="JavaScript">
				setRsIdTag('USER_ID');
				var ectableMenu = new EctableMenu('contextMenu','ec_table');
				</script>
			</div>
		</div>
		<div id="Layer1" style="height:auto;display:none; ">
			<iframe id="OrgPosEmpFrame" src="" width="100%" height="500px"; frameborder="0" scrolling="no"></iframe>
		</div>
	</div>
</div>
<input type="hidden" name="actionType" id="actionType" />
<script language="javascript">
var tab = new Tab('tab','tabHeader','Layer',0);
tab.focus(<%=pageBean.inputValue("_tabIndex_")%>);
$(function(){
	resetTreeHeight(87);
});
window.onload=function(){
	var curTrees=document.getElementsByClassName("nodeSel");
	for(var i=0;i<curTrees.length;i++){
		$("#curColumnName").val(curTrees[i].innerText);
		$("#ORG_NAME").val(curTrees[i].innerText);
	}
}

$(function(){
	loadParamAreaContent();
});

$(function(){
	$('#tt').tree({
		onClick: function(node){
			if(!$('#tt').tree('isLeaf',node.target)){
				changeTab("_base_");
				$('#columnId').val(node.id);
				$("#queryTable").jqGrid('setGridParam',{
			         url:'index?SecurityUserList&actionType=retrieveTableJson',
			     	 mtype: "POST",
			         postData:{'columnId':node.id,'showChildNodeRecords':getShowChildNodeRecords()},
			         page:1
			    }).trigger("reloadGrid");
			}else{
				changeTab("EmployeePOM");
				if (node.id){
					$('#columnId').val(node.id);
				}
				if (node.rgId){
					$('#rgId').val(node.rgId);
				}
				showOrgPosEmpList();
			}
		},
		onLoadSuccess:function(node,data){  
			var node = $('#tt').tree('find', '<%=pageBean.getStringValue("rootColumnId")%>');
			$('#tt').tree('select', node.target);
		}
	});
})

function loadParamAreaContent(){
	var url = "index?SecurityUserList&actionType=retrieveTableJson";
	$("#queryTable").jqGrid({
		url: url,
		mtype: "POST",
		styleUI : 'Bootstrap',
		datatype: "json",
		colModel: [
			{label: '序号', name: 'ID', width: 80 },
			{label: '编码', name: 'USER_CODE', width: 100 },
			{label: '名称', name: 'USER_NAME', width: 100 },
			{label: '性别', name: 'USER_SEX', width: 100 },
			{label: '状态', name: 'USER_STATE', width: 100 },
			{label: '所属群组', name: 'GRP_NAME', width: 100 },
			{label: '编码ID', name: 'USER_ID', hidden:true, width: 100 },
			{label: '子节点ID', name: 'GRP_ID', hidden:true, width: 100 }
		],
		postData:{'showChildNodeRecords':getShowChildNodeRecords()},
		viewrecords: true,
		height: 360,
		width:$("#queryTableGroup").width()+70,
        autowidth: false,  
        shrinkToFit: true,  			
		rowNum: 10,
		pager: "#jqGridPager",
		rowList : [ 10, 15, 30 ],
		onSelectRow:function(rowid,status){
			if (currentParamItemIndex > 0){
				$("#queryTable .jqgrow:eq("+currentParamItemIndex+")").removeClass("success");
				$("#queryTable .jqgrow:eq("+currentParamItemIndex+")").attr("aria-selected",false);				
			}			
			currentParamItemIndex = rowid-1;
			
			$("#queryTable .jqgrow:eq("+currentParamItemIndex+")").addClass("success");
			$("#queryTable .jqgrow:eq("+currentParamItemIndex+")").attr("aria-selected",true);	
		},
		ondblClickRow:function(rowid,iRow,iCol,e){
			var rowData = $("#queryTable").jqGrid('getRowData',rowid);
			openContentRequestBox('detail','用户信息','SecurityUserEdit','USER_ID');
		},
		onCellSelect:function(rowid,iRow,iCol,e){
			var rowData = $("#queryTable").jqGrid('getRowData',rowid);
			$('#USER_ID').val(rowData.USER_ID);
			$('#curColumnId').val(rowData.GRP_ID);
			$("queryTable").jqGrid('setSelection',rowData.USER_ID);
		},
		loadComplete:function(){
			if (currentGridItemIndex > 0){
				$("#queryTable .jqgrow:eq("+currentGridItemIndex+")").addClass("success");
				$("#queryTable .jqgrow:eq("+currentGridItemIndex+")").attr("aria-selected",true);				
			}
			$('#USER_ID').val("");
		},
		gridComplete: function() {  
	        $("#queryTable").closest(".ui-jqgrid-bdiv").css({  
	            "overflow-x": "hidden"  
	        });  
		} 
	});
}

$(function(){
	$(window).resize(function(){  
	    $("#queryTable").setGridWidth($("#queryTableGroup").width()+70);
	});
})
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>