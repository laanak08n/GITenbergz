angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$timeout){
	var url = '/aeaihr/services/BonusPenalty/rest/find-all-record';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson;
	});	
	
	$scope.showDetails = function(id) {
		alert(id);
	}; 	
});