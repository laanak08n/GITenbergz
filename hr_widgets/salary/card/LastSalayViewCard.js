angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,$state,AppKit){
	AppKit.isLogin().success(function(data, status, headers, config){
		if (data.result=='true'){
			$scope.userLogin = "isLogin";
			AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/m1/index.cv#/tab/home",
				"success":function(){
					var url = '/aeaihr/services/Salary/rest/get-last-salay-record/';
					var promise = AppKit.getJsonApi(url);
					promise.success(function(rspJson){
						  $scope.labels = ['基本工资', '绩效工资', '补贴', '奖金', '总工资', '保险', '个税'];
						  $scope.data = rspJson.data;
						  $scope.month = rspJson.month;
					});
				}
			})
		}
	})
	
    $scope.showStats = function(){
    	$state.go("tab.salary-infos");
    };
});

