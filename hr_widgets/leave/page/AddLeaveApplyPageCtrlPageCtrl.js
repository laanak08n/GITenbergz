angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state){

	$scope.info = {"userId":"","leaType":"","leaSdate":"","leaEdate": "","leaDays":"","leaCause":""};
	
	$scope.saveInfo = function(){
		var url = "/aeaihr/services/Leave/rest/add-leave-info";
		AppKit.postJsonApi(url,JSON.stringify($scope.info)).then(function(response){
			if ("success" == response.data){
				AppKit.successPopup();
				$state.go("tab.leave-infos");
			}else{
				AppKit.errorPopup();
			}
		});
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