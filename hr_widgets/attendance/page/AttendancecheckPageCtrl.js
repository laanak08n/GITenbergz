angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$filter){
	$scope.currentDay=new Date();
	$scope.currentMonth=new Date();
	$scope.lastDay =true;
	$scope.currentDayShow = true;
	$scope.nextDay = true;
	$scope.lastMonth = true;
	$scope.currentMonthShow = true;
	$scope.nextMonth = true;
	$scope.LoadSigninInfos=function(){
		$scope.currentDayParam = $filter("date")($scope.currentDay, "yyyy-MM-dd");
		var url = '/aeaihr/services/Attendance/rest/find-signin-infos/'+$scope.currentDayParam;
		var promise = AppKit.getJsonApi(url);
		promise.success(function(rspJson){
			$scope.signinInfos = rspJson.signinInfos;
		});
	}
	$scope.LoadSigninInfos();
	
	$scope.LoadLocationInfos=function(){
		$scope.currentMonthParam = $filter("date")($scope.currentMonth, "yyyy-MM");
		var url = '/aeaihr/services/Attendance/rest/find-location-infos/'+$scope.currentMonthParam+"/undefined";
		var promise = AppKit.getJsonApi(url);
		promise.success(function(rspJson){
			$scope.locationInfos = rspJson.locationInfos
		});
	}
	$scope.LoadLocationInfos();
	
	$scope.findLastDaySigninInfos=function(){
		$scope.currentDay=(new Date(($scope.currentDay/1000-86400)*1000))
		$scope.LoadSigninInfos();
		$scope.lastDay = false;
		$scope.currentDayShow = false;
	}    
	$scope.findCurrentDaySigninInfos=function(){
		$scope.currentDay=new Date();
		$scope.LoadSigninInfos();
		$scope.lastDay = true;
		$scope.currentDayShow = true;
	}
	$scope.findFollowDaySigninInfos=function(){
		$scope.currentDay=(new Date(($scope.currentDay/1000+86400)*1000))
		$scope.LoadSigninInfos();
		$scope.currentDayShow = false;
		$scope.nextDay = false;
	}
	
	$scope.findLastMonthLocations=function(){
		$scope.currentMonth=(new Date(($scope.currentMonth/1000-(86400*30))*1000))
		$scope.LoadLocationInfos();
		$scope.lastMonth = false;
		$scope.currentMonthShow = false;
	}
	$scope.findThisMonthLocations=function(){
		$scope.currentMonth=new Date();
		$scope.LoadLocationInfos();
		$scope.lastMonth = true;
		$scope.currentMonthShow = true;
		$scope.nextMonth = true;
	}
	$scope.findNextMonthLocations=function(){
		$scope.currentMonth=(new Date(($scope.currentMonth/1000+(86400*30))*1000))
		$scope.LoadLocationInfos();
		$scope.currentMonthShow = false;
		$scope.nextMonth = false;
	}
	$scope.toPersonLocate=function(userId){
		$scope.forMartcurrentMonth = $filter("date")($scope.currentMonth, "yyyy-MM");
		$state.go("tab.person-locate",{"userId": userId,"currentMonth": $scope.forMartcurrentMonth});
	}
});