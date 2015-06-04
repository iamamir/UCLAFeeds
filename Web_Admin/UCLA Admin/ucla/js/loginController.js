myApp.controller('loginHandler', 
['$scope','$http','postData','$state','$stateParams','$rootScope', '$location','messageCenterService', '$activityIndicator',
function($scope,$http,postData,$state,$stateParams,$rootScope, $location,messageCenterService, $activityIndicator) {
 
 $scope.disableButton=false;
 $scope.submitted=false;
 $scope.logoutShowHide = false;
 
 $scope._login={};

 
 $scope.authenticateUser = function()
    
    {			
		$scope.submitted=true;		       
		$scope.disableButton=true;
		$activityIndicator.startAnimating();
		
        var userData={ "email":$scope._login.email, "password":$scope._login.password, "type":"admin", "path":"LoginUser" };
        var data = postData.submit("webservices.php",userData,$scope).then(function() {
        
            if($scope.response.statusCode==3){
				messageCenterService.add('danger', 'Invalid username or password', {status: messageCenterService.status.next, timeout: 3000});
				$scope._login = {};
                
               }
        
		if($scope.response.statusCode==8){
           $scope.disableButton=false;
			 messageCenterService.add('danger', 'Your current user is not active', {status: messageCenterService.status.next, timeout: 3000});
			 $scope._login = {};
		}
		
			if($scope.response.statusCode==111){
           $scope.disableButton=false;
			 messageCenterService.add('danger', 'Invalid username or password', {status: messageCenterService.status.next, timeout: 3000});
			 $scope._login = {};
		}
		
            if($scope.response.statusCode==200){
               $activityIndicator.stopAnimating();            
               $rootScope.mySession.email=$scope._login.email;
			   
			   $rootScope.logoutShowHide = true;
			  
               window.sessionStorage['email']=$rootScope.mySession.email;
               window.sessionStorage['loginStatus'] = true;							
                }
				
				if($scope.response.statusCode!=200){
                 $activityIndicator.stopAnimating();	
				 $scope._login = {};			 	
                }
        
        });
		
		$scope.submitted=false;
        
      };
}])

.config(function($urlRouterProvider, $stateProvider) {

   var 
		login = {
	        name: 'login',
	        url: '/login',
	        templateUrl: 'login.html',
	        controller: 'loginHandler',
			data: {
				pageTitle: 'Login Page'
			}, 
	    };
	    $stateProvider
			.state('login', login);  
});