angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit){
	
	$scope.info = {"userName":"","bpType":"","bpDate": "","bpMonry":"","bpDesc":""};
	
	$scope.updateInfo = function(){
		var url = "";
		AppKit.postJsonApi(url,JSON.stringify($scope.info)).then(function(response){
			if ("success" == response.data){
				AppKit.successPopup();		
			}else{
				AppKit.errorPopup();
			}
		});
	}
});