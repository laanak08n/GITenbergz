angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit){
	var url = '/aeaihr/services/FormSelectUtil/rest/userList/';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.userList = rspJson;
	});
	
	var url = '/aeaihr/services/FormSelectUtil/rest/codeList/BP_TYPE';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.bpTypeSelect = rspJson;
	});
});