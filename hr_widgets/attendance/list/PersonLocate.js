angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit){
	var url = '/map/services/DataProvider/rest/static-data/SimpleListJson';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson.listinfo;
	});	
	
	$scope.isActive=true;
	$scope.setActive=function(lable){
		if('first'==lable){
			$scope.isActive=true;
		}else if('second'==lable){
			$scope.isActive=false;
		}
	}
});