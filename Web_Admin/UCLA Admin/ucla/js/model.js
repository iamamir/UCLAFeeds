

(function(angular) {
angular.module('post', [])
.factory('postData', function($http) {
 
  return {
        submit: function(url,userData,$scope){
		
			
		var response=$http.post(url,userData)
							
			            .success(function(data,status) {
  
  							$scope.response=data;
  																					

			                }).error(function(data,status){
							
							$scope.response=data;
							
			                }); 
							
			return response;			
	    
		}
       
    } 
  
});
})(window.angular);


