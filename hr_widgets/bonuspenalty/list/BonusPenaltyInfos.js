angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$timeout,$state){
	var url = '/aeaihr/services/BonusPenalty/rest/find-all-record';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson;
	});	
	
	$scope.showDetails = function(id) {
		$state.go('tab.rawards-punishment-add', {bp_Id: id});
	}; 	
});