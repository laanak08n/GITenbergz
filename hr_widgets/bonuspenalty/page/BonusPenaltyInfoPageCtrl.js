angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$stateParams){
	var url = '/aeaihr/services/BonusPenalty/rest/find-all-record';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson;
	});	
	
	var url = '/aeaihr/services/FormSelectUtil/rest/userList/';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.userList = rspJson;
	});
	
	$scope.info = {"userId":"","bpType":"","bpDate": "","bpMonry":"","bpDesc":""};
	
	var url = '/aeaihr/services/BonusPenalty/rest/get-record/'+$stateParams.bpId;
	var promise = AppKit.getJsonApi(url);
	promise.success(function(rspJson){
		$scope.info = rspJson;
		$scope.info.bpDate = new Date($scope.info.bpDate);
	});
	$stateParams.piId;
});