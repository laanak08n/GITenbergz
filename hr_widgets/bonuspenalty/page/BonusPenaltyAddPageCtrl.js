angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit){
	$scope.info = {"userName":"","bpType":"selected","bpDate": "","bpMonry":"","bpDesc":""};
	$scope.saveInfo = function(){
		alert($scope.info);
//		var url = "/aeaihr/index?MobileLeave&actionType=createLeaveInfo";
//		AppKit.postJsonApi(url,$scope.info).then(function(response){
//			if ("success" == response.data){
//				$scope.loadData();
//				AppKit.successPopup();				
//			}else{
//				AppKit.errorPopup();
//			}
//		});
	}
});