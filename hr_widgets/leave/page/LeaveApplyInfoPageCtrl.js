angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state,$stateParams){
	var url = '/aeaihr/services/Leave/rest/find-all-record';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson;
	});	
	
	var url = '/aeaihr/services/FormSelectUtil/rest/userList/';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.userList = rspJson;
	});
	
	$scope.info = {"leaId":"","leaType":"","leaSdate":"","leaEdate": "","leaDays":"","leaCause":""};
	
	var url = '/aeaihr/services/Leave/rest/get-record/'+$stateParams.leaId;
	var promise = AppKit.getJsonApi(url);
	promise.success(function(rspJson){
		$scope.info = rspJson;
		$scope.info.leaSdate = new Date($scope.info.leaSdate);
		$scope.info.leaEdate = new Date($scope.info.leaEdate);
	});
	
	$scope.updateInfo = function (){
		var url = "/aeaihr/services/Leave/rest/update-leave-info";
		AppKit.postJsonApi(url,JSON.stringify($scope.info)).then(function(response){
			if ("success" == response.data){
				AppKit.successPopup();
				$state.go("tab.leave-infos");
			}else{
				AppKit.errorPopup();
			}
		});
	}
	$scope.deleteInfo = function (){
		AppKit.confirm({operaType:'delete',action:function(){
			var url = "/aeaihr/services/Leave/rest/delete-leave-info/"+$scope.info.leaId;
			AppKit.getJsonApi(url).success(function(rspJson){
				AppKit.successPopup();
				$state.go("tab.leave-infos");
			});
		}});
	}
	
	$scope.isValidSaveInfo = function(){
		var info = $scope.info;
		if (info.leaType && info.leaType!='' && info.leaSdate && info.leaSdate!='' 
			&& info.leaEdate && info.leaEdate!='' && info.leaDays && info.leaDays!='' && info.leaCause && info.leaCause!=''){
			return true;
		}
		else{
			return false;
		}
	};
});