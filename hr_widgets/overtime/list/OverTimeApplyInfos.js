angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$state){
	
	var url = '/aeaihr/services/Overtime/rest/find-all-record/';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson.datas;
	});	
	
	$scope.showDetails = function(id) {
		$state.go('tab.overtime-apply-info', {"wotId":id});
	}; 	
});