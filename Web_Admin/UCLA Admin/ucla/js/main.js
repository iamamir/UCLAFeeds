var myApp = angular.module('myApp',['post','ngAnimate','ui.router','MessageCenterModule','ngActivityIndicator'])

.run(['$rootScope', '$location', '$state', '$stateParams', function($rootScope, $location, $state, $stateParams) {
    
	$rootScope.mySession={};
	$rootScope.mySession.email = window.sessionStorage['email'];
	$rootScope.mySession.loginStatus = window.sessionStorage['loginStatus'];
	
	$rootScope.$watch('mySession', function (){
	
	console.log("watch called");
	if($rootScope.mySession.email != "" && angular.isDefined($rootScope.mySession.email))
	{
		console.log("Excel");
		$location.path('/excel');
	}
	else{
		console.log("Login");
		$location.path('/login');	
	    }

	},true);

}]);





