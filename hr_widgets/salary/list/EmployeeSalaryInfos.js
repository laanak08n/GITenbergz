angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$state){
	AppKit.isLogin().success(function(data, status, headers, config){
		if (data.result=='true'){
			$scope.userLogin = "isLogin";
			AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/hr/index.cv#/tab/home",
				"success":function(){
					var url = '/aeaihr/services/Salary/rest/find-all-record/';
					AppKit.getJsonApi(url).success(function(rspJson){
						$scope.listInfo = rspJson;
					});	
				}
			})
		}
	})
	
	$scope.showDetails = function(id) {
		$state.go('tab.salary-info', {"salId": id});
	}; 	
});