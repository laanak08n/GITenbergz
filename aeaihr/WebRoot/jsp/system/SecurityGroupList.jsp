<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html;charset=UTF-8"%>
<%@ taglib uri="http://www.ecside.org" prefix="ec"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
<title></title>
<%@include file="/jsp/inc/resource.inc.jsp"%>
<script language="javascript">
var operaRequestBox;
function openContentRequestBox(operaType,title,handlerId,subPKField,tableMode){
	if ('insert' != operaType && !isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
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
	}else{
		columnIdValue = $("#columnId").val();	
	}
	var url = 'index?'+handlerId+'&GRP_ID='+columnIdValue+'&operaType='+operaType+'&'+subPKField+'='+$("#"+subPKField).val();
	operaRequestBox.sendRequest(url);
}

function showFilterBox(){
	$('#filterBox').show();
	var clientWidth = $(document.body).width();
	var tuneLeft = (clientWidth - $("#filterBox").width())/2-2;	
	$("#filterBox").css('left',tuneLeft);	
}

function doRemoveContent(){
	if (!isSelectedRow()){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	jConfirm('确认要移除该条记录吗？',function(r){
		if (r){
			postRequest('form1',{actionType:'isLastRelation',onComplete:function(responseText){
				if (responseText == 'true'){
					jConfirm('该信息只有一条关联记录，确认要删除吗？',function(rr){
						if (rr){
							doSubmit({actionType:'delete'});						
						}
					});
						
				}else{
					doSubmit({actionType:'removeContent'});
				}
			}});
		}		
	});
}

function isSelectedTree(){
	if (isValid($('#columnId').val())){
		return true;
	}else{
		return false;
	}
}
var operaTreeBox;
function openTreeRequestBox(operaType){
	var title = "组织信息管理";
	var handlerId = "SecurityGroupEdit";
	
	if ('insert' != operaType && !isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	if (!operaTreeBox){
		operaTreeBox = new PopupBox('operaTreeBox',title,{size:'normal',width:'500px',height:'360px',top:'2px'});
	}
	var url = '';
	if ('insert' == operaType){
		url = 'index?'+handlerId+'&operaType='+operaType+'&GRP_PID='+$("#columnId").val()+'&GRP_TYPE='+$("#GRP_TYPE").val();
	}else{
		url = 'index?'+handlerId+'&operaType='+operaType+'&GRP_ID='+$("#columnId").val()+'&GRP_TYPE='+$("#GRP_TYPE").val();
	}
	operaTreeBox.sendRequest(url);	
}
function refreshTree(){
	doQuery();
}
function refreshContent(curNodeId){
	if (curNodeId){
		$('#columnId').val(curNodeId);
	}
	doSubmit({actionType:'query'});
}

function deleteTreeNode(){
	if (!isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	jConfirm('确定要进行节点删除操做吗？',function(r){
		if (r){
			postRequest('form1',{actionType:'deleteTreeNode',onComplete:function(responseText){
				if (responseText == 'success'){
					$('#columnId').val("");
					doQuery();
				}else if (responseText == 'hasChild'){
					writeErrorMsg('该节点还有子节点，不能删除！');
				}else if(responseText == 'hasContent'){
					writeErrorMsg('有关联人员以及角色，不能删除！');
				}else if (responseText == 'roleContent'){
					writeErrorMsg('有关联角色，不能删除！');
				}else if (responseText == 'empContent'){
					writeErrorMsg('有关联人员，不能删除！');
				}  
			}});			
		};
	});

}
var targetTreeBox;
function openTargetTreeBox(curAction){
	var columnIdValue = $("#columnId").val();
	if (!isSelectedTree()){
		writeErrorMsg('请先选中一个树节点!');
		return;
	}
	if (!targetTreeBox){
		targetTreeBox = new PopupBox('targetTreeBox','请选择目标分组',{size:'normal',width:'300px',top:'2px'});
	}
	var handlerId = "SecurityGroupPick";
	var url = 'index?'+handlerId+'&GRP_ID='+columnIdValue;
	postRequest('form1',{actionType:'moveTree',onComplete:function(responseText){
		if (responseText == 'hasContent'){
			jConfirm("该部门有角色以及人员关联，是否迁移？",function(r){
				if(r){
					targetTreeBox.sendRequest(url);
					$("#actionType").val(curAction);
				}
			});
	    }else if(responseText == 'roleContent'){
			jConfirm("该部门有角色关联，是否迁移？",function(r){
				if(r){
					targetTreeBox.sendRequest(url);
					$("#actionType").val(curAction);
				}
			});
	    }else if(responseText == 'empContent'){
	    	jConfirm("该部门有人员关联，是否迁移？",function(r){
				if(r){
					targetTreeBox.sendRequest(url);
					$("#actionType").val(curAction);
				}
			});
	    }else if(responseText == 'success'){
	    	targetTreeBox.sendRequest(url);
			$("#actionType").val(curAction);
	    }else if (responseText == 'hasChild'){
			writeErrorMsg('该部门下还有部门，不能迁移！');
		}else if (responseText == 'isCompany'){
			writeErrorMsg('公司不能迁移！');
		}
	}});
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
		}else{
			writeErrorMsg('迁移父节点出错啦！');
		}
	}});
}

function moveRequest(moveAction){
	postRequest('form1',{actionType:moveAction,onComplete:function(responseText){
		if (responseText == 'success'){
			refreshTree();
		}else if (responseText == 'isFirstNode'){
			writeErrorMsg('该节点是同级第一个节点，不能上移！');
		}else if (responseText == 'isLastNode'){
			writeErrorMsg('该节点是同级最后一个节点，不能下移！');
		}
	}});
}

function clearFilter(){
	$("#filterBox input[type!='button'],select").val('');
}

function changeTab(tabId){
	$('#_tabId_').val(tabId);
	refreshContent();
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
function checkSave(){
	var result = false;
	if (validation.checkNull($('#GRP_NAME').val())){
		return true;
	}
	return result;
}

function doDelete(){
	var roleId = $('#'+rsIdTagId).val()
	if (!isValid(roleId)){
		writeErrorMsg('请先选中一条记录!');
		return;
	}
	jConfirm("您确认要删除这条数据吗？",function(r){
		if (r){
			$("#actionType").val('delete');
			postRequest('form1',{actionType:'delete',onComplete:function(responseText){
				if(responseText == 'hasEmpContent8Auth'){
					writeErrorMsg('该实际角色下有关联人员和资源，不能删除！');
				}else if(responseText == 'hasAuth'){
					writeErrorMsg('该实际角色下有关联资源，不能删除！');
				}else if(responseText =='empContent'){
					writeErrorMsg('该实际角色下有关联人员，不能删除！');
				}else if (responseText == 'success'){
					 $("#ROLE_ID").val("");
					 refreshTree();
				}else {
					writeErrorMsg('删除组织角色关联出错啦！');
				}
			}});	
		}
	});
}
</script>
</head>
<body>
<form action="<%=pageBean.getHandlerURL()%>" name="form1" id="form1" method="post">
<%@include file="/jsp/inc/message.inc.jsp"%>
<table width="100%" style="margin:0px;">
<tr>
	<td valign="top">
    <div id="leftTree" class="sharp color2" style="margin-top:0px;">
	<b class="b1"></b><b class="b2"></b><b class="b3"></b><b class="b4"></b>
    <div class="content">
    <h3 class="portletTitle">&nbsp;&nbsp;分组列表</h3>        
        <div id="treeArea" style="overflow:auto; height:420px;width:230px;background-color:#F9F9F9;padding-top:5px;padding-left:5px;">
    <%=pageBean.getStringValue("menuTreeSyntax")%></div>
    </div>
    <b class="b9"></b>
    </div>
    <input type="hidden" id="columnId" name="columnId" value="<%=pageBean.inputValue("columnId")%>" />
    <input type="hidden" id="targetParentId" name="targetParentId" value="" />     
    </td>
	<td width="85%" valign="top">
<div class="photobg1" id="tabHeader">
<div class="newarticle1" onclick="changeTab('_base_')">基本信息</div>
<div class="newarticle1" onclick="changeTab('Position')">角色信息</div>
</div>	
<div class="photobox newarticlebox" id="Layer<%=pageBean.inputValue("_tabIndex_")%>" style="height:auto;">
<%if ("_base_".equals(pageBean.inputValue("_tabId_"))){%>
<div id="__ToolBar__">
<table  id="_TreeToolBar_" border="0" cellpadding="0" cellspacing="1">
    <tr>
    <td	onmouseover="onMover(this);"  onmouseout="onMout(this);" class="bartdx" align="center" onclick="openTreeRequestBox('insert')"><input value="&nbsp;" title="新增" type="button" class="newImgBtn" id="newImgBtn" style="margin-right:0px;" />新增</td>
    <td	onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="saveTreeBaseRecord()"><input value="&nbsp;" title="保存" type="button" class="saveImgBtn"  id="saveImgBtn" style="margin-right:0px;" />保存</td>
    <td	onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx"  align="center" onclick="deleteTreeNode()"><input value="&nbsp;" title="删除" type="button" class="delImgBtn" id="delImgBtn"/>删除</td>
    <td	onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="openTargetTreeBox('moveTree')"><input value="&nbsp;" title="迁移" type="button" class="moveImgBtn" id="moveImgBtn" style="margin-right:0px;" />迁移</td>
    <td	onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="moveRequest('moveUp')"><input value="&nbsp;" title="上移" type="button" class="upImgBtn" id="upImgBtn" style="margin-right:0px;" />上移</td>
    <td	onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="moveRequest('moveDown')"><input value="&nbsp;" title="下移" type="button" class="downImgBtn"  id="downImgBtn" style="margin-right:0px;" />下移</td> 
    </tr>
    </table>
</div>
<div style="margin:auto 2px;">
<table class="detailTable" cellspacing="0" cellpadding="0">
<tr>
	<th width="100" nowrap>编码</th>
	<td><input id="GRP_CODE" label="编码" name="GRP_CODE" type="text" readOnly="readonly" value="<%=pageBean.inputValue("GRP_CODE")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>组织名称</th>
	<td><input id="GRP_NAME" label="组织名称" name="GRP_NAME" type="text" value="<%=pageBean.inputValue("GRP_NAME")%>" size="24" class="text" />
</td>
</tr>
<tr>
	<th width="100" nowrap>组织类型</th>
	<td>
	<input id="GRP_TYPE" label="组织类型" class="text markable" name="GRP_TYPE" type="text" readOnly="readonly" value="<%=pageBean.selectedText("GRP_TYPE")%>" size="24" class="text"/> 
</td>
</tr>
<tr>
	<th width="100" nowrap>组织级别</th>
	<td>
	<input id="GRP_RANK" label="组织类型" class="text markable" name="GRP_RANK" type="text" readOnly="readonly" value="<%=pageBean.selectedText("GRP_RANK")%>" size="24" class="text"/> 
</td>
</tr>
<tr>
	<th width="100" nowrap>描述</th>
	<td><textarea id="GRP_DESC" label="描述" name="GRP_DESC" cols="40" rows="3" class="textarea"><%=pageBean.inputValue("GRP_DESC")%></textarea>
</td>
</tr>
</table>
<input type="hidden" id="GRP_ID" name="GRP_ID" value="<%=pageBean.inputValue4DetailOrUpdate("GRP_ID","")%>" />
<input type="hidden" id="GRP_PID" name="GRP_PID" value="<%=pageBean.inputValue("GRP_PID")%>" />
<input type="hidden" id="GRP_SORT" name="GRP_SORT" value="<%=pageBean.inputValue("GRP_SORT")%>" />
<input type="hidden" id="_tabId_" name="_tabId_" value="<%=pageBean.inputValue("_tabId_")%>" />
</div>
<%}%>
<%if ("Position".equals(pageBean.inputValue("_tabId_"))){%>
<div id="__ToolBar__">
<table class="toolTable" border="0" cellpadding="0" cellspacing="1">
<tr>

   <td  onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" align="center" onclick="openposIdBox();" ><input value="&nbsp;" type="button" class="addImgBtn" id="addImgBtn" title="添加" />添加</td>
   <td  onmouseover="onMover(this);" onmouseout="onMout(this);" class="bartdx" hotKey="D" align="center" onclick="doDelete();"><input value="&nbsp;" title="删除" type="button" id="deleImgBtn" class="delImgBtn" />删除</td>
</tr>
</table>
</div>
<div style="margin:auto 2px;">
<ec:table 
form="form1"
var="row"
items="pageBean.rsList" csvFileName="角色信息.csv"
retrieveRowsCallback="process" xlsFileName="角色信息.xls"
useAjax="true" sortable="true"
doPreload="false" toolbarContent="navigation|pagejump |pagesize |export|extend|status"
width="100%" rowsDisplayed="10"
listWidth="100%" 
height="390px"
>
<ec:row styleClass="odd"  oncontextmenu="selectRow(this,{ROLE_ID:'${row.ROLE_ID}',RG_ID:'${row.RG_ID }',curColumnId:'${row.GRP_ID}'});refreshConextmenu()" 
onclick="selectRow(this,{ROLE_ID:'${row.ROLE_ID}',curColumnId:'${row.GRP_ID}',RG_ID:'${row.RG_ID}',POS_NAME:'${row.ROLE_NAME}'});">
	<ec:column width="50" style="text-align:center" property="_0" title="序号" value="${GLOBALROWCOUNT}" />
	<ec:column width="100" property="ROLE_CODE" title="编码"   />
	<ec:column width="100" property="ROLE_NAME" title="职位名称"   />
	<ec:column width="100" property="ROLE_STATE" title="状态"  mappingItem="ROLE_STATE" />
</ec:row>
</ec:table>
<div id="filterBox" class="sharp color2" style="position:absolute;top:30px;display:none; z-index:10; width:480px;">
<b class="b9"></b>
<div class="content">
<h3>&nbsp;&nbsp;条件过滤框</h3>
<table class="detailTable" cellpadding="0" cellspacing="0" style="width:99%;margin:1px;">
</table>
<div style="width:100%;text-align:center;">
<input type="button" name="button" id="button" value="查询" class="formbutton" onclick="doQuery()" />
&nbsp;&nbsp;
<input type="button" name="button" id="button" value="清空" class="formbutton" onclick="clearFilter()" />
&nbsp;&nbsp;<input type="button" name="button" id="button" value="关闭" class="formbutton" onclick="javascript:$('#filterBox').hide();" /></div>
</div>
<b class="b9"></b>
</div>
<input type="hidden" id="_tabId_" name="_tabId_" value="<%=pageBean.inputValue("_tabId_")%>" />
<input type="hidden" name="ROLE_ID" id="ROLE_ID" value="<%=pageBean.inputValue("ROLE_ID")%>" />
<input type="hidden" name="RG_ID" id="POR_ID" value="" />
<input type="hidden" name="ROLE_NAME" id="POS_NAME" value="" />
<input type="hidden" name="curColumnId" id="curColumnId" value="" />
</td>
<script language="JavaScript">
setRsIdTag('ROLE_ID');
var ectableMenu = new EctableMenu('contextMenu','ec_table');
</script>
</div>
<%}%>
</div>
</td>
</tr>
</table>
<input type="hidden" name="actionType" id="actionType" />
<script language="javascript">
requiredValidator.add("GRP_NAME");
var tab = new Tab('tab','tabHeader','Layer',0);
tab.focus(<%=pageBean.inputValue("_tabIndex_")%>);
$(function(){
	resetTreeHeight(80);	
	resetTabHeight(80);
});
var addRoleTreeBox;
function openposIdBox(){
	var handlerId = "SecurityGroupQuery";
	 if (!addRoleTreeBox){
		addRoleTreeBox = new PopupBox('addRoleTreeBox','添加角色',{size:'normal',width:'300',top:'2px'});
	}
	var url = 'index?'+handlerId+'&ROLE_ID='+$("#ROLE_ID").val()+'&GRP_ID='+$("#columnId").val();

	addRoleTreeBox.sendRequest(url);
}

<%if(pageBean.getBoolValue("isRootColumnId")){%>

setImgDisabled('saveImgBtn',true);
disableButton('saveImgBtn');
setImgDisabled('delImgBtn',true);
disableButton('delImgBtn');
setImgDisabled('moveImgBtn',true);
disableButton('moveImgBtn');
setImgDisabled('upImgBtn',true);
disableButton('upImgBtn');
setImgDisabled('downImgBtn',true); 
disableButton('downImgBtn');
<%}%>

</script>
</form>
</body>
</html>
<%@include file="/jsp/inc/scripts.inc.jsp"%>
