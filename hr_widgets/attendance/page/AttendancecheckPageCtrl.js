angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])

.filter('truncate',function(){ 
    return function(str,len){
        return str.substr(0,16);
    }
})
.controller("${widgetCode}Ctrl",function($scope,AppKit,$filter,$state){
	$scope.currentDay=new Date();
	$scope.currentMonth=new Date();
	
	$scope.LoadSigninInfos=function(){
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						$scope.currentDayParam = $filter("date")($scope.currentDay, "yyyy-MM-dd");
						var url = '/aeaihr/services/Attendance/rest/find-signin-infos/'+$scope.currentDayParam;
						var promise = AppKit.getJsonApi(url);
						promise.success(function(rspJson){
							$scope.signinInfos = rspJson.signinInfos
						});
					}
				})
			}
		})
	}
	$scope.LoadSigninInfos();
	
	$scope.LoadLocationInfos=function(){
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						$scope.currentMonthParam = $filter("date")($scope.currentMonth, "yyyy-MM");
						var url = '/aeaihr/services/Attendance/rest/find-location-infos/'+$scope.currentMonthParam+'/undefined';
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
	
	$scope.findLastDaySigninInfos=function(){
		$scope.currentDay=(new Date(($scope.currentDay/1000-86400)*1000))
		$scope.LoadSigninInfos();	
	}    
	$scope.findCurrentDaySigninInfos=function(){
		$scope.currentDay=new Date();
		$scope.LoadSigninInfos();
	}
	$scope.findFollowDaySigninInfos=function(){
		$scope.currentDay=(new Date(($scope.currentDay/1000+86400)*1000))
		$scope.LoadSigninInfos();		
	}
	
	$scope.findLastMonthLocations=function(){
		$scope.currentMonth=(new Date(($scope.currentMonth/1000-(86400*30))*1000))
		$scope.LoadLocationInfos();	
	}
	$scope.findThisMonthLocations=function(){
		$scope.currentMonth=new Date();
		$scope.LoadLocationInfos();	
	}
	$scope.findNextMonthLocations=function(){
		$scope.currentMonth=(new Date(($scope.currentMonth/1000+(86400*30))*1000))
		$scope.LoadLocationInfos();	
	}
	
	$scope.toPersonLocate=function(userId){
		$scope.forMartcurrentMonth = $filter("date")($scope.currentMonth, "yyyy-MM");
		$state.go("tab.person-locate",{"userId": userId,"currentMonth": $scope.forMartcurrentMonth});
	}
});

