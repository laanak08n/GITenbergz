<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<%@ page contentType="text/html; charset=UTF-8" errorPage="/jsp/frame/Error.jsp" %>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<html>
<head>
<title>主页面</title>
<meta http-equiv="Content-Type" content="text/html;charset=UTF-8">
<script type="text/javascript" src="js/jquery-1.7.1.js"></script>
<script type="text/javascript" src="js/util.js"></script>
<link href="css/style.css" rel="stylesheet" type="text/css">
<style type="text/css">
.footer {
	height: 31px;
	line-height: 31px;
	font-size: 12px;
	text-align: center;
	color: #666666;
	width: 99%;
}
.mainContent{
	font-size: 14px;
	margin:8px 10px;
	line-height:25px;
}

div#wrap {
 padding-top: 87px;
}
</style>
</head>
<body>
<div id="wrap">
<table width="96%"  border="0" align="center">
  <tr>
    <td width="80" align="center"><img src="images/index/mainpic.jpg"></td>
    <td>
<div class="mainContent">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;AEAI HR是数通畅联软件自主开发的一款人力资源软件，用来内部使用协助管理公司人力、薪酬等事务，该软件现已开源并上传至开源中国。</div>
<div class="mainContent">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;AEAI HR人力资源系统包括一些核心的人力资源管理业务功能，例如薪酬管理、考勤管理、绩效管理、加班申请、出差申请等模块，满足企业人力资源信息化的基本要求。
AEAI HR人力资源系统内部已预置演示账户，用于快速了解、掌握该系统。AEAI HR系统是是采用AEAI DP开发完成，使用过程中如有其他功能需求，可以通过AEAI DP开发平台（开源）进行扩展开发，满足个性化需求。</div>
<div class="mainContent">
&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; 沈阳数通畅联软件技术有限公司是耕耘于应用系统集成领域的专业技术团队，提供基于Java体系的SOA整合产品，主要包括应用集成平台（AEAI ESB）、门户集成平台（AEAI Portal）、流程集成平台（AEAI BPM）、应用开发平台（AEAI DP，也称Miscdp）、主数据管理平台（AEAI MDM）。其他产品更多详情请参见官网：www.agileai.com,或致电：024-22962011。</div>
</td>
  </tr>
</table>
</div>
</body>
</html>
<script language="javascript">
if (parent.topright.document && parent.topright.document.getElementById('currentPath'))
parent.topright.document.getElementById('currentPath').innerHTML='系统主页面';
</script>
