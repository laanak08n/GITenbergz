function _loadPage(funURL,tabIndex,tabTitle,canClose){
	$("#sidebar-menu > li.treeview > ul > li > ul > li.active").removeClass('active');
	if ($("#sidebar-menu > li.treeview > ul > li > ul > li > a[menu-id='"+tabIndex+"']").length > 0){
		$("#sidebar-menu > li.treeview > ul > li > ul > li > a[menu-id='"+tabIndex+"']").parent().addClass("active");	
	}	
	
	var tab = $(".menuTabs .page-tabs-content a[data-id='"+tabIndex+"']");
	if (tab.length > 0){
		if (!tab.hasClass("active")){
			$(".menuTabs .page-tabs-content a.active").removeClass("active");	
			tab.addClass("active");
			
			$(".mainContent").find("iframe.PortalPage").hide();
			$("#PortalPageIframe"+tabIndex).show();
		}
	}else{
		showLoading(true,'正在加载页面...');
		
		$(".menuTabs .page-tabs-content a.active").removeClass("active");
		var u = '<a href="javascript:void(0);" class="active menuTab" data-id="' + tabIndex + '">' + tabTitle;
		if (canClose){
			u = u + '<i class="fa fa-remove"><\/i><\/a>';	
		}else{
			u = u + '<\/i><\/a>';
		}
		
		
		$(".menuTabs .page-tabs-content").append(u);
		
		var f = '<iframe class="PortalPage" id="PortalPageIframe' + tabIndex + '" width="100%" height="100%" src="' + funURL + '" frameborder="0" seamless><\/iframe>'; 
		$(".mainContent").find("iframe.PortalPage").hide();
		$(".mainContent").append(f);
		$(".mainContent iframe:visible").load(function() {
	        showLoading(false,"");
		});
		
		if (canClose){
			$(".menuTabs .page-tabs-content a[data-id='"+tabIndex+"'] i.fa-remove").click(function(){
				closeTab(tabIndex);
			});			
		}
		
		$(".menuTabs .page-tabs-content a[data-id='"+tabIndex+"']").click(function(){
			showTab(tabIndex);
		});
	}
}

function doLoadFuncIframe(funURL,tabIndex,tabTitle){
	_loadPage(funURL,tabIndex,tabTitle,true);
}

function closeTab(tabIndex){
	var prev = $(".menuTabs .page-tabs-content a[data-id='"+tabIndex+"']").prev();
	var prevIndex = prev.attr("data-id");
	
	$(".menuTabs .page-tabs-content a[data-id='"+tabIndex+"']").remove();
	$(".mainContent iframe#PortalPageIframe"+tabIndex).remove();
	
	showTab(prevIndex);
}

function showTab(tabIndex){
	var tab = $(".menuTabs .page-tabs-content a[data-id='"+tabIndex+"']");
	if (tab.length > 0){
		if (!tab.hasClass("active")){
			$(".menuTabs .page-tabs-content a.active").removeClass("active");	
			tab.addClass("active");
			
			$(".mainContent").find("iframe.PortalPage").hide();
			$("#PortalPageIframe"+tabIndex).show();
		}
	}
	focusMenu(tabIndex);
}

function doLoadPortalIframe(funURL,tabIndex,tabTitle){
	_loadPage(funURL,tabIndex,tabTitle,true);
}

function doLoadDefaultPage(funURL,tabIndex,tabTitle){
	_loadPage(funURL,tabIndex,tabTitle,false);
}


function doParseLoadPortalIframe(menuURL,pageId){
	
}

function resetThemeHeight(menuOuterHeigth,mainOuterHeight){
    $("#content-wrapper").find(".mainContent").height($(window).height() - mainOuterHeight);
    $("#sidebar-menu").height($(window).height() - menuOuterHeigth)
}

function showLoading(isShow,text){
	var t = $("#loading_background,#loading_manage");
	isShow?t.show():t.hide();
	$("#loading_manage").html(text);
	$("#loading_manage").css("left", ($("body").width() - $("#loading_manage").width()) / 2 - 54);
	$("#loading_manage").css("top", ($("body").height() - $("#loading_manage").height()) / 2)
}

function rollLeft(){
	var visiableWidth = $(window).width()-$(".main-sidebar").width()-40*4;
	var tabWidth = $("div.page-tabs-content").width();
	var marginLeft = parseInt($("div.page-tabs-content").css("margin-left").replace("px",""));
	
	if (tabWidth + marginLeft > visiableWidth){
		marginLeft = marginLeft - 10;
		$("div.page-tabs-content").css("margin-left",marginLeft);
	}
}

function rollRight(){
	var marginLeft = parseInt($("div.page-tabs-content").css("margin-left").replace("px",""));
	if (marginLeft < 0){
		marginLeft = marginLeft + 10;
		$("div.page-tabs-content").css("margin-left",marginLeft);
	}
}

function reloadTab(){
	var tabIndex = $(".menuTabs .page-tabs-content a.active").attr("data-id");
	var src = $(".mainContent iframe#PortalPageIframe"+tabIndex).attr("src");
	$(".mainContent iframe#PortalPageIframe"+tabIndex).attr("src",src);
}
function closeTabCurrent(){
	var tabIndex = $(".menuTabs .page-tabs-content a.active").attr("data-id");
	if (tabIndex == "index"){
		jAlert("首页面不能关闭！");
	}else{
		closeTab(tabIndex);		
	}
}
function closeAllTab(){
	$(".menuTabs .page-tabs-content a").each(function(){
		var tabIndex = $(this).attr("data-id");
		if (tabIndex != 'index'){
			closeTab(tabIndex);	
		}
	});
}
function closeOtherTab(){
	var curIndex = $(".menuTabs .page-tabs-content a.active").attr("data-id");
	$(".menuTabs .page-tabs-content a").each(function(){
		var tabIndex = $(this).attr("data-id");
		if (tabIndex != 'index' && curIndex != tabIndex){
			closeTab(tabIndex);	
		}
	});	
	showTab(curIndex);
}

function focusMenu(menuId){
	if (menuId != 'index'){
		$("#sidebar-menu > li.treeview > ul > li > a").parent().each(function(){
			if ($(this).hasClass("active")){
				$(this).removeClass("active");
			}
		});
		$("#sidebar-menu > li.treeview > ul > li > ul > li > a").parent().each(function(){
			if ($(this).hasClass("active")){
				$(this).removeClass("active");
			}
		});
		
		if ($("#sidebar-menu > li.treeview > ul > li > a[menu-id='"+menuId+"']").length > 0){
			$("#sidebar-menu > li.active").removeClass("active");
			
			if (!$("#sidebar-menu > li.treeview > ul > li > a[menu-id='"+menuId+"']").closest("li.treeview").hasClass("active")){
				$("#sidebar-menu > li.treeview > ul > li > a[menu-id='"+menuId+"']").closest("li.treeview").addClass("active")
			}
			$("#sidebar-menu > li.treeview > ul > li > a[menu-id='"+menuId+"']").parent().addClass("active");			
		}
		else{
			$("#sidebar-menu > li.active").removeClass("active");
			if (!$("#sidebar-menu > li.treeview > ul > li > ul > li > a[menu-id='"+menuId+"']").closest("li.treeview").hasClass("active")){
				$("#sidebar-menu > li.treeview > ul > li > ul > li > a[menu-id='"+menuId+"']").closest("li.treeview").addClass("active")
				$("#sidebar-menu > li.treeview > ul > li > ul > li > a[menu-id='"+menuId+"']").closest("li").parent().parent().addClass("active")
			}
			$("#sidebar-menu > li.treeview > ul > li > ul > li > a[menu-id='"+menuId+"']").parent().addClass("active");			
		}
	}else{
		$("#sidebar-menu > li.active").removeClass("active");
	}
}
function dateCompare(sdate,edate){
	if (sdate > edate){
		writeErrorMsg('起始日期不能大于结束日期！');
		return false;
	}
	return true;
}


