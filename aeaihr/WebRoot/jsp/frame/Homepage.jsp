<%@ page contentType="text/html;charset=UTF-8"%>
<jsp:useBean id="pageBean" scope="request" class="com.agileai.hotweb.domain.PageBean"/>
<% 
String menuTreeJson = pageBean.inputValue("menuTreeJson");
String userName = pageBean.inputTime("userName");
String frameThemeVer = "20170522";
%>
<!DOCTYPE html>
<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">    
    <meta name="viewport" content="width=device-width">
    <meta http-equiv="X-UA-Compatible" content="IE=edge">
	<title>AEAI HR人力资源系统</title>
    <link href="css/font-awesome.min.css?<%=frameThemeVer%>" rel="stylesheet">
    <link href="css/bootstrap.min.css?<%=frameThemeVer%>" rel="stylesheet">
    <script src="js/respond.min.js?<%=frameThemeVer%>"></script>
    <script src="js/html5shiv.min.js?<%=frameThemeVer%>"></script>	
	<link href="css/style.css?<%=frameThemeVer%>" rel="stylesheet" type="text/css">
	
    <link href="css/index.css?<%=frameThemeVer%>" rel="stylesheet">
    <link href="css/_all-skins.css?<%=frameThemeVer%>" rel="stylesheet">
	<link href="css/jquery.alerts.css?<%=frameThemeVer%>" rel="stylesheet">    
    <link href="css/frame.css?<%=frameThemeVer%>" rel="stylesheet">
    <link href="css/boxy.css?<%=frameThemeVer%>" rel="stylesheet" type="text/css">	
</head>
<body class="skin-blue-light sidebar-mini" style="overflow: hidden;">
    <div class="wrapper">
        <!--头部信息-->
        <header class="main-header">
            <a href="javascript:void(0)" class="logo">
                <span class="logo-mini">HR</span>
                <span class="logo-lg">AEAI HR<strong>人力资源</strong></span>
            </a>
            <nav class="navbar navbar-static-top">
                <a class="sidebar-toggle">
                    <span class="sr-only">Toggle navigation</span>
                </a>
                <div class="navbar-custom-menu">
                    <ul class="nav navbar-nav">
                    	<!-- 
                        <li class="dropdown messages-menu">
                            <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown">
                                <i class="fa fa-envelope-o "></i><span class="label label-success">4</span>测试连接
                            </a>
                        </li>
                         -->
                        <li class="dropdown user user-menu">
                            <a href="javascript:void(0)" class="dropdown-toggle" data-toggle="dropdown" aria-expanded="true">
                                <img src="images/frame/System.jpg" class="user-image" alt="User Image">
                                <span class="hidden-xs"><%=userName%><i class="fa fa-caret-down" style="padding-left:5px;"></i></span>
                            </a>
                            <ul class="dropdown-menu pull-right">
                                <li><a class="menuItem" href="javascript:openModifyPasswordBox()"><i class="fa fa-user"></i>修改密码</a></li>
                                <li class="divider"></li>
                                <li><a href="javascript:void(0);" onclick="logout()"><i class="ace-icon fa fa-power-off"></i>安全退出</a></li>
                            </ul>
                        </li>
                    </ul>
                </div>
            </nav>
        </header>
        <!--左边导航-->
        <div class="main-sidebar">
            <div class="sidebar">
                <ul class="sidebar-menu" id="sidebar-menu" style="height: 539px;">
                    <li class="header" style="padding-left:19px;font-size:13px"><i class="fa fa-th-large" style="margin-right: 5px;"></i>导航菜单</li>
            </div>
        </div>
        <!--中间内容-->
        <div id="content-wrapper" class="content-wrapper">
			<div class="content-tabs">
				<button class="roll-nav roll-left tabLeft">
					<i class="fa fa-backward"></i>
				</button>
				<nav class="page-tabs menuTabs">
					<div class="page-tabs-content" style="margin-left: 0px;">
				</nav>
				<button class="roll-nav roll-right tabRight" style="right:80px;">
					<i class="fa fa-forward" style="margin-left:3px;"></i>
				</button>
				<button class="roll-nav roll-right fullscreen" style="right:40px;">
					<i class="fa fa-arrows-alt"></i>
				</button>
				<div class="roll-nav roll-right" style="right:0px;">
					<button class="dropdown" data-toggle="dropdown" style="right: 0px;" aria-expanded="false">
						<i class="fa fa-gear" style="padding-left: 3px;"></i>
					</button>
					<ul class="dropdown-menu dropdown-menu-right">
						<li><a class="tabReload" href="javascript:void(0);">刷新当前</a></li>
						<li><a class="tabCloseCurrent" href="javascript:void(0);">关闭当前</a></li>
						<li><a class="tabCloseAll" href="javascript:void(0);">全部关闭</a></li>
						<li><a class="tabCloseOther" href="javascript:void(0);">关闭其它</a></li>
					</ul>
				</div>			
			</div>
            <div class="content-iframe" style="overflow: hidden;">
                <div class="mainContent" id="content-main" style="height: 522px;">
                </div>
            </div>
        </div>
    </div>
    <div id="loading_background" class="loading_background" style="display: none;"></div>
    <div id="loading_manage" style="left: 629px; top: 291.5px; display: none;"></div>  
</body>

</html>
<script src="js/jquery-1.10.2.min.js?<%=frameThemeVer%>"></script>
<script src="js/bootstrap.min.js?<%=frameThemeVer%>"></script>
<script src="js/jquery-migrate-1.2.1.js?<%=frameThemeVer%>"></script>	
<script src="js/global.js?<%=frameThemeVer%>"></script>
<script src="js/jquery.alerts.js?<%=frameThemeVer%>"></script>	
<script src="js/extend.js?<%=frameThemeVer%>"></script>
<script src="js/PopupBox.js?<%=frameThemeVer%>" language="javascript"></script>

<script type="text/javascript">
$.ajaxSetup({cache:false});

$(".sidebar-toggle").click(function() {
    $("body").hasClass("sidebar-collapse") ? ($("body").removeClass("sidebar-collapse"), $("#sidebar-menu").removeClass("sidebar-menu2")) 
    		: ($("body").addClass("sidebar-collapse"), $("#sidebar-menu").addClass("sidebar-menu2"));
});

var menudata = <%=menuTreeJson%>;
var menuHtml = "";

$(function(){
	PopupBox.contextPath="";
	createMenuBar();
	
	$("button.fullscreen").click(function(){
		if (isFullscreen()){
			exitFullScreen();
		}else{
			fullScreen();
		}		
	});	
	
    var leftTimer;
    var rightTimer;
	$("button.tabLeft").click(function(){
		rollLeft();
	});
	$("button.tabLeft").mousedown(function(){
		leftTimer = setInterval(function() {
			rollLeft();
        },100);
	});
	$("button.tabLeft").mouseup(function(){
		clearInterval(leftTimer);
	});

	$("button.tabRight").click(function(){
		rollRight();
	});	
	$("button.tabRight").mousedown(function(){
		rightTimer = setInterval(function() {
			rollRight();
        },100);
	});	
	$("button.tabRight").mouseup(function(){
		clearInterval(rightTimer);
	});		
	
	$("a.tabReload").click(function(){
		reloadTab();
	});
	$("a.tabCloseCurrent").click(function(){
		closeTabCurrent();
	});
	$("a.tabCloseAll").click(function(){
		closeAllTab();
	});
	$("a.tabCloseOther").click(function(){
		closeOtherTab();
	});			
	
	$(window).load(function() {
		resetThemeHeight(73,90);
		doLoadDefaultPage('index?MainWin','index','首页');
	});	
	$(window).resize(function() {
		resetThemeHeight(73,90);	 
	});
})

function createMenuBar(){
	var data = menudata.menus;
	for(var i = 0; i < data.length; i++) {
		var itemData = data[i];
		var itemType = itemData.type;
		var itemText = itemData.text;
		var itemName = itemData.name;
		
		var icon = itemData.icon;
		if (icon == ""){
			icon = "fa-desktop";
		}
		
		if ("index" == itemData.name){
			continue;
		}			
		
		if (itemType == 'func'){
			var itemURL = itemData.href;
			var itemTarget = itemData.target;
			menuHtml = menuHtml + '<li class="treeview">';			
			menuHtml = menuHtml + '<a href="javascript:void(0)" menu-id="'+itemName+'" href="'+itemURL+'" onclick="'+itemURL+'"><i class="fa '+icon+'"></i><span>'+itemText+'</span></a>';
			menuHtml = menuHtml + '</li>'; 
		}
		else if (itemType == 'link'){
			var itemURL = itemData.href;
			var itemTarget = itemData.target;
			menuHtml = menuHtml + '<li class="treeview">';			
			menuHtml = menuHtml + '<a hideFocus="true" href="'+itemURL+'" target="'+itemTarget+'"><i class="fa '+icon+'"></i><span>'+itemText+'</span></a>';
			menuHtml = menuHtml + '</li>';
		}
		else{
			if (itemData.selected){
				menuHtml = menuHtml + '<li class="treeview active">';				
			}else{
				menuHtml = menuHtml + '<li class="treeview">';
			}
			var itemURL = "";
			if (itemData.href){
				itemURL = itemData.href;					
			}else{
				itemURL = 'javascript:void(0)';
			}
			
			menuHtml = menuHtml + '<a href="javascript:void(0)" menu-id="'+itemName+'" onclick="'+itemURL+'"><i class="fa '+icon+'"></i><span>'+itemText+'</span><i class="fa fa-angle-left pull-right"></i></a>';
			buildMenuBarHtml(itemData);
			menuHtml = menuHtml + '</li>'; 
		}
	}
	$(".sidebar-menu").append(menuHtml);
	
	$("ul.sidebar-menu > li > a").each(function(){
		if ($(this).has("i.pull-right")){
			$(this).click(function(){
				if($(this).parent().hasClass("active")){
					$(this).parent().removeClass("active");
				}else{
					$(".sidebar-menu li.active").removeClass("active");
					$(this).parent().addClass("active");
				}
			});
		}
	});
	
	$("#sidebar-menu > li.treeview > ul > li > a").each(function(){
		if ($(this).has("i.pull-right")){
			$(this).click(function(){
				if($(this).parent().hasClass("active")){
					$(this).parent().removeClass("active");
				}else{
					$("#sidebar-menu > li.treeview > ul > li.active").removeClass("active");
					$(this).parent().addClass("active");
				}
			});
		}
	});
}

function buildMenuBarHtml(itemData){
	if(itemData.type == 'folder'){
		menuHtml = menuHtml + '<ul class="treeview-menu">'; 
		for (var i=0;i < itemData.menus.length;i++){
			var submenuData = itemData.menus[i];
			var itemType = submenuData.type;
			var itemText = submenuData.text;	
			var itemName = submenuData.name;
			
			var icon = submenuData.icon;
			if (icon == ""){
				icon = "fa-file-o";
			}
			
			if (itemType == 'func'){
				menuHtml = menuHtml + '<li>';
				var itemURL = submenuData.href;
				var itemTarget = itemData.target;
				menuHtml = menuHtml + '<a href="javascript:void(0)" menu-id="'+itemName+'" class="menuItem" onclick="'+itemURL+'"><i class="fa '+icon+'"></i>'+itemText+'</a>';
				menuHtml = menuHtml + '</li>'; 
			}
			else if (itemType == 'link'){
				menuHtml = menuHtml + '<li>';
				var itemURL = submenuData.href;
				var itemTarget = submenuData.target;
				menuHtml = menuHtml + '<a href="javascript:void(0)" menu-id="'+itemName+'" class="menuItem" hideFocus="true" href="'+itemURL+'" target="'+itemTarget+'"><i class="fa '+icon+'"></i>'+itemText+'</a>';
				menuHtml = menuHtml + '</li>';
			}
			else if(itemType == 'folder'){
				menuHtml = menuHtml + '<li>';
				var itemURL = "";
				if (submenuData.href){
					itemURL = submenuData.href;					
				}else{
					itemURL = 'javascript:void(0)';
				}
				menuHtml = menuHtml + '<a href="javascript:void(0)" menu-id="'+itemName+'" class="menuItem" hideFocus="true" onclick="'+itemURL+'"><i class="fa '+icon+'"></i>'+itemText+'<i class="fa fa-angle-left pull-right"></i></a>';
				
				menuHtml = menuHtml + '<ul class="treeview-menu">';
				
				for (var j=0;j < submenuData.menus.length;j++){
					var subChildMenuData = submenuData.menus[j];
					var itemType = subChildMenuData.type;
					var itemText = subChildMenuData.text;
					var subItemName = subChildMenuData.name;
					
					var subIcon = subChildMenuData.icon;
					if (subIcon == ""){
						subIcon = "fa-file-text-o";
					}
					
					if (itemType == 'func'){
						menuHtml = menuHtml + '<li>';	
						var itemURL = subChildMenuData.href;
						var itemTarget = subChildMenuData.target;
						
						menuHtml = menuHtml + '<a href="javascript:void(0)" menu-id="'+subItemName+'" class="menuItem" onclick="'+itemURL+'"><i class="fa '+subIcon+'"></i><span>'+itemText+'</span></a>';
						menuHtml = menuHtml + '</li>'; 
					}
					else if (itemType == 'link'){
						menuHtml = menuHtml + '<li>';					
						
						var itemURL = subChildMenuData.href;
						var itemTarget = subChildMenuData.target;
						menuHtml = menuHtml + '<a href="javascript:void(0)" menu-id="'+subItemName+'" class="menuItem" hideFocus="true" href="'+itemURL+'" target="'+itemTarget+'"><i class="fa '+subIcon+'"></i><span>'+itemText+'</span></a>';
						menuHtml = menuHtml + '</li>';
					}
					else {
						//
					}
				}
				menuHtml = menuHtml + '</ul></li>';
			}
		}
		menuHtml = menuHtml + '</ul>'; 
	}	
}
</script>