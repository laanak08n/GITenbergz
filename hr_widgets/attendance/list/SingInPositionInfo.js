angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,AppKit,$ionicActionSheet,$timeout,$stateParams){
	$scope.initSiginInfo = function(){
		var menuCode = '${menuCode}';
		$scope.siginImg = "/map/repository/widgets/hr_widgets/attendance/image/signin.png";
		$scope.isShow = false;
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
							if($scope.isShow){
								$scope.mapOptions = rspJson.placeInfo;
								$scope.atdInTime = rspJson.atdInTime;
								$scope.titleName = rspJson.name;
								$scope.titleAddress = rspJson.address;
							}
						});
					}
				})
			}
		})
	}	
	$scope.initSiginInfo();
	$scope.singInChecked=function(obj){
		return obj.address == $scope.titleAddress;
	}
});
