angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state,$stateParams){
	
	$scope.info = {"wotPlace":"","wotOverTimeDate":"","wotTime":"","wotParticipant": "","wotDesc":""};
	
	var url = '/aeaihr/services/Overtime/rest/get-record/'+$stateParams.wotId;
	var promise = AppKit.getJsonApi(url);
	promise.success(function(rspJson){
		$scope.info = rspJson;
	});
	
	$scope.updateInfo = function (){
		var url = "/aeaihr/services/Overtime/rest/update-overtime-info";
		AppKit.postJsonApi(url,JSON.stringify($scope.info)).then(function(response){
			if ("success" == response.data){
				AppKit.successPopup();
				$state.go("tab.overtime-apply-infos");
			}else{
				AppKit.errorPopup();
			}
		});
	}
	$scope.deleteInfo = function (){
		AppKit.confirm({operaType:'delete',action:function(){
			var url = "/aeaihr/services/Overtime/rest/delete-overtime-info/"+$scope.info.wotId;
			AppKit.getJsonApi(url).success(function(rspJson){
				AppKit.successPopup();
				$state.go("tab.overtime-apply-infos");
			});
		}});
	}
	
	$scope.isValidSaveInfo = function(){
		var info = $scope.info;
		if (info.wotPlace && info.wotPlace != '' && info.wotOverTimeDate && info.wotOverTimeDate!='' && info.wotTime && info.wotTime!='' 
			&& info.wotDesc && info.wotDesc!='' && info.wotParticipant && info.wotParticipant!=''){
			return true;
		}
		else{
			return false;
		}
	};
});