angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit){
	$scope.createInfo = function() {
		$state.go("tab.rawards-punishment-add");
	}; 	
});