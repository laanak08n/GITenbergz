angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state,$stateParams){
	var url = '/aeaihr/services/BonusPenalty/rest/find-all-record';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.listInfo = rspJson;
	});	
	
	var url = '/aeaihr/services/FormSelectUtil/rest/userList/';
	AppKit.getJsonApi(url).success(function(rspJson){
		$scope.userList = rspJson;
	});
	
	$scope.info = {"bpId":"","userId":"","bpType":"","bpDate": "","bpMonry":"","bpDesc":""};
	
	var url = '/aeaihr/services/BonusPenalty/rest/get-record/'+$stateParams.bpId;
	var promise = AppKit.getJsonApi(url);
	promise.success(function(rspJson){
		$scope.info = rspJson;
		$scope.info.bpDate = new Date($scope.info.bpDate);
	});
	
	$scope.updateInfo = function (){
		var url = "/aeaihr/services/BonusPenalty/rest/update-pun-info/";
		AppKit.postJsonApi(url,JSON.stringify($scope.info)).then(function(response){
			if ("success" == response.data){
				AppKit.successPopup();		
			}else{
				AppKit.errorPopup();
			}
		});
	}
	
	$scope.deleteInfo = function (){
		AppKit.confirm({operaType:'delete',action:function(){
			var url = "/aeaihr/services/BonusPenalty/rest/delete-pun-info/"+$scope.info.bpId;
			AppKit.getJsonApi(url).success(function(rspJson){
				AppKit.successPopup();
				$state.go("tab.rawards-punishment-infos");
			});
		}});
	}
	
	$scope.isValidSaveInfo = function(){
		var info = $scope.info;
		if (info.userId && info.userId != '' && info.bpType && info.bpType!='' && info.bpDate && info.bpDate!='' 
			&& info.bpMonry && info.bpMonry!=''){
			return true;
		}
		else{
			return false;
		}
	};
});