angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state){
	
	$scope.info={"wotPlace":"","wotOverTimeDate":"","wotTime":"","wotDesc":"","wotParticipant":""};
	
	$scope.saveInfo = function(){
		var url = "/aeaihr/services/Overtime/rest/add-overtime-info";
		AppKit.postJsonApi(url,JSON.stringify($scope.info)).then(function(response){
			if ("success" == response.data){
				AppKit.successPopup();
				$state.go("tab.overtime-apply-infos");
			}else{
				AppKit.errorPopup();
			}
		});
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