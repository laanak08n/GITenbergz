angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$timeout,$stateParams){
	$scope.initSigoutInfo = function(){
		var menuCode = '${menuCode}';
		$scope.sigoutImg = "/map/repository/widgets/hr_widgets/attendance/image/signout.png";
		$scope.isShow = false;
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						var url = '/aeaihr/services/Attendance/rest/get-signOut-info/';
						var promise = AppKit.getJsonApi(url);
						promise.success(function(rspJson){
							$scope.isSignOut = rspJson.isSignOut;
							$scope.isShow = ($scope.isSignOut=='Y');
							if($scope.isShow){
								$scope.mapOptions = rspJson.placeInfo;
								$scope.atdInTime = rspJson.atdOutTime;
								$scope.titleName = rspJson.name;
								$scope.titleAddress = rspJson.address;
							}
						});
					}
				})
			}
		})
	}	
	$scope.initSigoutInfo();
	$scope.singOutChecked=function(obj){
		return obj.address == $scope.titleAddress;
	}
	
});
