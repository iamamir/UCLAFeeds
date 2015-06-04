myApp.controller('HomeController', 
['$scope','$http','postData','$state','$stateParams','$rootScope', '$location',  
function($scope,$http,postData,$state,$stateParams,$rootScope, $location) {

$rootScope.$on('$stateChangeStart', 
function(event, toState, toParams, fromState, fromParams){ 
	
/*	if($rootScope.mySession.email == "")
			{
				$location.path('/login');
				$rootScope.logoutShowHide = false;
			}*/
			$scope.path=$location.path().split('/');
			if($scope.path[1]!="login" && (angular.isUndefined($rootScope.mySession.email) || $rootScope.mySession.email==""))
			{
			 
				event.preventDefault();
				//$location.path('/login');
			
			}
			
	
});

$scope.logout = function() 
    
    	{
                $rootScope.mySession.email		="";			
				window.sessionStorage['email']	="";
				$location.path('/login');	
				$rootScope.logoutShowHide = false;		
		};

}]) 
