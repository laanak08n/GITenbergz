angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$timeout,$stateParams){
	$scope.sourceCode = '${menuCode}';
	var menuCode = '${menuCode}';
	var siginImg = "/map/repository/widgets/hr_widgets/attendance/image/signin.png";
	var sigoutImg = "/map/repository/widgets/hr_widgets/attendance/image/signout.png";
	$scope.isShow = false;
	$scope.whichShow = "";
	if("signin-info"==menuCode){
		$scope.whichShow = siginImg;
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						var url = '/aeaihr/services/Attendance/rest/get-signIn-info/';
						var promise = AppKit.getJsonApi(url);
						promise.success(function(rspJson){
							$scope.isSignIn = rspJson.isSignIn;
							$scope.isShow = ($scope.isSignIn=='Y');
							$scope.mapOptions = rspJson.placeInfo;
							$scope.atdInTime = rspJson.atdInTime;
							$scope.titleName = rspJson.name;
							$scope.titleAddress = rspJson.address;
						});
					}
				})
			}
		})
	}else if("signout-info"==menuCode){
		$scope.whichShow = sigoutImg;
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						var url ='/aeaihr/services/Attendance/rest/get-signOut-info/';
						var promise = AppKit.getJsonApi(url);
						promise.success(function(rspJson){
							$scope.isSignOut = rspJson.isSignOut;
							$scope.isShow =($scope.isSignOut=='Y');
							$scope.mapOptions = rspJson.placeInfo;
							$scope.atdInTime = rspJson.atdOutTime;
							$scope.titleName = rspJson.name;
							$scope.titleAddress = rspJson.address;
						});
					}
				})
			}
		})

		
	}
	
});