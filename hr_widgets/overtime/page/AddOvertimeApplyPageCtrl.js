angular.module('${menuCode}')
.filter('to_trusted', ['$sce', function ($sce) {
	return function (text) {
	    return $sce.trustAsHtml(text);
	};
}])
.controller("${widgetCode}Ctrl",function($scope,AppKit,$state){
	
	$scope.info={"wotPlace":"","wotOverTimeDate":"","wotTime":"","wotDesc":"","wotParticipant":""};
	
	$scope.saveInfo = function(){
		AppKit.isLogin().success(function(data, status, headers, config){
			if (data.result=='true'){
				$scope.userLogin = "isLogin";
				AppKit.secuityOperation("aeaihr",{"backURL":"/map/repository/genassets/hr/index.cv#/tab/home",
					"success":function(){
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
				})
			}
		})
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