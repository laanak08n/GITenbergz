function logout(){
	window.location.href='index?Navigater&actionType=logout';
}

function fullScreen() {
	  var el = document.documentElement;
	  var rfs = el.requestFullScreen || el.webkitRequestFullScreen || el.mozRequestFullScreen || el.msRequestFullScreen;
	  if(typeof rfs != "undefined" && rfs) {
	    rfs.call(el);
	  } else if(typeof window.ActiveXObject != "undefined") {
	    var wscript = new ActiveXObject("WScript.Shell");
	    if(wscript != null) {
	        wscript.SendKeys("{F11}");
	    }
	  }
}

function isFullscreen(){
    return document.fullscreenElement    ||
           document.msFullscreenElement  ||
           document.mozFullScreenElement ||
           document.webkitFullscreenElement || false;
}

function exitFullScreen(){
	  var el = document;
	  var cfs = el.cancelFullScreen || el.webkitCancelFullScreen || el.mozCancelFullScreen || el.exitFullScreen;
	  if(typeof cfs != "undefined" && cfs) {
	    cfs.call(el);
	  } else if(typeof window.ActiveXObject != "undefined") {
	    var wscript = new ActiveXObject("WScript.Shell");
	    if(wscript != null) {
	        wscript.SendKeys("{F11}");
	    }
	  }
}

function openMenuTab(menuCode){
	$("a[menu-id='"+menuCode+"']").click();
}

var _modifyPasswordBox;
function openModifyPasswordBox(){
	if (!_modifyPasswordBox){
		_modifyPasswordBox = new PopupBox('_modifyPasswordBox','修改个人密码',{top:'50px',size:'normal',width:'500px',height:'280px'});
	}
	var url = 'index?ModifyPassword';
	_modifyPasswordBox.sendRequest(url);	
}