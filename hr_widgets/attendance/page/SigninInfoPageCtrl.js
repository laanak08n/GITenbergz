angular.module('${menuCode}')
.controller("${widgetCode}Ctrl",function($scope,$state,AppKit,$rootScope,$filter){
	
	
	$scope.getSignState=function(){
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/m1/index.cv#/tab/home",
					"success":function(){
						$scope.tadyTime=new Date();
						$scope.tadyTime = $filter("date")($scope.tadyTime, "yyyy-MM-dd");
						var url = '/aeaihr/services/Attendance/rest/get-signin-state/'+$scope.tadyTime;
						var promise = AppKit.getJsonApi(url);
						promise.success(function(rspJson){
							$scope.isSign = rspJson.isSign
						});
					}
				})
			}
		})
	}
	$scope.getSignState();
	
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
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/m1/index.cv#/tab/home",
					"success":function(){
						if($scope.atdInTime&&$scope.titleName){
							var parameterJson={"lng":$scope.lng,"lat":$scope.lat,"name":$scope.titleName,"address":$scope.titleAddress};
							var parameter=JSON.stringify(parameterJson); 
							var url ='/aeaihr/services/Attendance/rest/signIn';
							AppKit.postJsonApi(url,parameter).then(function(rspJson){
								if("success"==rspJson.data){
									AppKit.successPopup({"title":"签到成功!"});
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
	
	 var watcher=$rootScope.$watch("cpoint",function(newVal,oldVal){
		   if($rootScope.cpoint){
			   $scope.results=$rootScope.cpoint.poiList.pois
			   $scope.titleName=$scope.results[0].name;
			   $scope.titleAddress=$scope.results[0].address;
			   $scope.results[0].isSignIn='y';
			   $scope.atdInTime=new Date();
			   $scope.mapOptions={"lng":$scope.results[0].location.lng,"lat":$scope.results[0].location.lat};
			   $scope.lng=$scope.results[0].location.lng;
			   $scope.lat=$scope.results[0].location.lat;
		   }
	 })
});


