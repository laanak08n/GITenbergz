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
<script src="js/jquery.easyui.min.js" language="javascript"></script>
<script src="js/bootstrap.min.js"></script>
<script type="text/ecmascript" src="js/trirand/jquery.jqGrid.min.js"></script>
<script type="text/ecmascript" src="js/trirand/i18n/grid.locale-en.js"></script>
<script language="javascript">
var currentEvenSource;
var currentParamItemIndex = -1;
var currentGridItemIndex = -1;
var currentFormItemCode;

function refreshContent(curNodeId){
	if (curNodeId){
		$('#GRP_ID').val(curNodeId);
	}
	$('#queryTable').trigger('reloadGrid');
} 
function clearFilter(){
	$("#filterBox input[type!='button'],select").val('');
}
function doDeletePosemp(){
	if (!isSelectedRow()){
		jAlert('请先选中一条记录!');
		return;
	}
	jConfirm(confirmMsg,function(r){
		if (r){
			$("#queryTable").jqGrid("delRowData", currentParamItemIndex); 
			postRequest("form1",{actionType:"deletePosemp",onComplete:function(responseText){
				$('#queryTable').trigger('reloadGrid');				
			}});
		}
	});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<div class="tab-content">    
	<div role="tabpanel" class="tab-pane active" id="list-page">
		<div class="container-fluid">
			<div class="row">
			   	<div class="form-group">&nbsp;
					<button type="button" class="btn btn-success btn-sm" type="button" onclick="openUserSelectBox()"><span class="glyphicon glyphicon-plus"></span>&nbsp;添加</button>
				    <button type="button" class="btn btn-danger btn-sm" type="button" onclick="doDeletePosemp()"><span class="glyphicon glyphicon-remove"></span>&nbsp;移除</button>
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
<input type="hidden" id="USER_ID" name="USER_ID" value="" />
<input type="hidden" name="RG_ID" id="RG_ID" value="<%=pageBean.inputValue("RG_ID")%>" />
<input type="hidden" id="GRP_ID" name="GRP_ID" value="<%=pageBean.inputValue("GRP_ID")%>" />
<script language="JavaScript">
setRsIdTag('USER_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
<input type="hidden" name="actionType" id="actionType" />
<script language="javascript">
var tab = new Tab('tab','tabHeader','Layer',0);
tab.focus(<%=pageBean.inputValue("_tabIndex_")%>);
$(function(){
	resetTabHeight(80);
});
var openUserBox;
function openUserSelectBox(){
	var handlerId = "SecurityUserQuery"; 
	$("#GRP_ID").val($("#GRP_ID").val());
	if (!openUserBox){
		openUserBox = new PopupBox('openUserBox','添加角色',{size:'normal',width:'650',top:'2px'});
	}
	var url = 'index?'+handlerId+'&RG_ID='+$("#RG_ID").val()+'&GRP_ID='+$("#GRP_ID").val();
	openUserBox.sendRequest(url);	
}

$(function(){
	loadParamAreaContent();
});

function loadParamAreaContent(){
	var url = "index?SecurityUserGRManageList&actionType=tableJson";
	var rgId = $('#RG_ID').val();
	$("#queryTable").jqGrid({
		url: url,
		postData:{'RG_ID':rgId},
		mtype: "POST",
		styleUI : 'Bootstrap',
		datatype: "json",
		colModel: [
			{label: '序号', name: 'ID', width: 80 },
			{label: '编码', name: 'USER_CODE', width: 100 },
			{label: '名称', name: 'USER_NAME', width: 100 },
			{label: '性别', name: 'USER_SEX', width: 100 },
			{label: '状态', name: 'USER_STATE', width: 100 },
			{label: '电话', name: 'USER_PHONE', width: 100 },
			{label: '编码ID', name: 'USER_ID', hidden:true, width: 100 },
			{label: '子节点ID', name: 'GRP_ID', hidden:true, width: 100 }
		],
		viewrecords: true,
		height: 360,
		width:$("#queryTableGroup").width(),
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
			$("queryTable").jqGrid('setSelection',rowData.USER_ID);
		},
		loadComplete:function(){
			if (currentGridItemIndex > 0){
				$("#queryTable .jqgrow:eq("+currentGridItemIndex+")").addClass("success");
				$("#queryTable .jqgrow:eq("+currentGridItemIndex+")").attr("aria-selected",true);				
			}
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
	    $("#queryTable").setGridWidth($("#queryTableGroup").width());   
	});
})
</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>