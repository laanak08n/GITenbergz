angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state,$stateParams){

	$scope.info = {"salBasic":"","salPerformance":"","salSubsidy":"","salInsure": "","salHousingFund":"","salBonus":"","salShould":"","salTotal":"","salActual":"","salRemarks":""};
	
	var url = '/aeaihr/services/Salary/rest/get-record/'+$stateParams.salId;
	var promise = AppKit.getJsonApi(url);
	promise.success(function(rspJson){
		$scope.info = rspJson;
	});
	
	var url = '/aeaihr/services/Salary/rest/get-map-record/'+$stateParams.salId;
	var promise = AppKit.getJsonApi(url);
	promise.success(function(rspJson){
		$scope.labels = ["基本工资", "绩效工资", "补贴","奖金"];
		$scope.data = rspJson;
	});

});