angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state){
	
	$scope.info = {"bpId":"","userId":"","bpType":"","bpDate": "","bpMonry":"","bpDesc":""};
	
	$scope.saveInfo = function(){
		var url = "/aeaihr/services/BonusPenalty/rest/add-pun-info";
		AppKit.postJsonApi(url,JSON.stringify($scope.info)).then(function(response){
			if ("success" == response.data){
				AppKit.successPopup();
				$state.go("tab.rawards-punishment-infos");
			}else{
				AppKit.errorPopup();
			}
		});
	}
});