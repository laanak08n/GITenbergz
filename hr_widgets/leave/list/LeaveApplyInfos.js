angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$state){
	var url = '/aeaihr/services/Leave/rest/find-all-record/';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson;
	});	
	
	$scope.showDetails = function(id) {
		$state.go('tab.rawards-leave-info', {"leaId": id});
	}; 	
});