angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,$state,AppKit,$rootScope,$filter){
	$scope.initSignOutInfo=function(){
		var location = "123.432588,41.795673";
		if($scope.options&&$scope.options.lng&&$scope.options.lat){
			location = $scope.options.lng+","+$scope.options.lat;
		}
		var url = '/aeaihr/services/Attendance/rest/find-around-building/'+location;
		var promise = AppKit.getJsonApi(url);
		promise.success(function(rspJson){
			$scope.positioninfos= rspJson;
			$scope.atdInTime=new Date();
			$scope.results=rspJson.pois;
			if($scope.positioninfos.pois[0]){
				$scope.titleName=$scope.positioninfos.pois[0].name;
				$scope.titleAddress=$scope.positioninfos.pois[0].address;
				var locationArr = $scope.positioninfos.pois[0].location.split(",");
				$scope.mapOptions={"lng":locationArr[0],"lat":locationArr[1]};
				$scope.lng = locationArr[0];
				$scope.lat = locationArr[1];
			} 
		});
	}
	$scope.initSignOutInfo();
	$scope.getSignOutState=function(){
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						$scope.tadyTime=new Date();
						$scope.tadyTime = $filter("date")($scope.tadyTime, "yyyy-MM-dd");
						var url = '/aeaihr/services/Attendance/rest/get-signin-state/'+$scope.tadyTime;
						var promise = AppKit.getJsonApi(url);
						promise.success(function(rspJson){
							$scope.isSignOut = rspJson.isSignOut
						});
					}
				})
			}
		})
	}
	$scope.getSignOutState();
	
	$scope.setPosition=function(obj){
		$scope.mapOptions={"lng":obj.location.lng,"lat":obj.location.lat};
		$scope.lng=obj.location.lng;
		$scope.lat=obj.location.lat;
		$scope.atdInTime=new Date();
		$scope.titleName=obj.name;
		$scope.titleAddress=obj.address;
	}
	
	$scope.doConfirm=function(){
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/${navCode}/index.cv#/tab/home",
					"success":function(){
						if($scope.atdInTime&&$scope.titleName){
							var parameterJson={"lng":$scope.lng,"lat":$scope.lat,"name":$scope.titleName,"address":$scope.titleAddress};
							var parameter=JSON.stringify(parameterJson); 
							var url ='/aeaihr/services/Attendance/rest/signOut';
							AppKit.postJsonApi(url,parameter).then(function(rspJson){
								if("success"==rspJson.data){
									AppKit.successPopup({"title":"签退成功!"});
									$state.go("tab.home");
								}
								AppKit.hideMask();
							}); 
						}else{
							AppKit.successPopup({"title":"地址不能为空!"});
						}
					}
				})
			}
		})
	}
	
});


