angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit){
	$scope.search = function(){
		var url = '';
		var promise = AppKit.getJsonApi(url);
		promise.success(function(rspJson){
			$scope.bpDate = rspJson.bpDate;
			$scope.bpType = rspJson.bpType;
			$scope.bpMonry = rspJson.bpMonry;
			$scope.bpDesc = rspJson.bpDesc;
		});
	}
	}; 	
});