angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$filter,$stateParams){
	$scope.userId=$stateParams.userId;
	$scope.currentMonth=$stateParams.currentMonth;
 
	$scope.LoadLocationInfos=function(){
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						var url = '/aeaihr/services/Attendance/rest/find-location-infos/'+$scope.currentMonth+'/'+$scope.userId;
						var promise = AppKit.getJsonApi(url);
						promise.success(function(rspJson){
							$scope.locationInfos = rspJson.locationInfos
						});
					}
				})
			}
		})
	}
	$scope.LoadLocationInfos();
	
});