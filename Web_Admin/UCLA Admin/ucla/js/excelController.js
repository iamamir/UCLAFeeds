myApp.controller('ExcelController', 
['$scope','$http','postData','$state','$stateParams','$rootScope', '$location','messageCenterService',
function($scope,$http,postData,$state,$stateParams,$rootScope, $location,messageCenterService) {
	
$scope.saveExcelData;
$scope.fileName;
$scope.excelStatus;
$scope.isDisabled = false;
$scope.filePath = "Select File";
$scope.file;
$rootScope.logoutShowHide = true;
$scope.isSaveDisabled = false;

$scope.extension;

$scope.stopTimer;
$scope.counter =0;

var progress = document.getElementById("progressBar");

 $scope.uploadExcelFileData = function() {	
		$scope.file = $scope.myFile;
		$scope.fileName = $scope.file.name;
		
		$scope.extension = $scope.file.type;
		
		if($scope.extension == "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet" || $scope.extension == "application/vnd.ms-excel")
		{			
				$scope.isSaveDisabled = false;
				 progress.style.width = ($scope.counter+1) +'%';
                 progress.style.backgroundColor = "#D79315"; 
				 $scope.counter ++;
				 
			if($scope.counter<100)
			{	 
			setTimeout(function(){ 
			$scope.uploadExcelFileData();
			}, 100);	 
			}			
			$scope.isDisabled = true;			
		}
		
		else {
			 $scope.fileName ="";
			 $scope.isDisabled = true;
             $scope.filePath = "Select File";
			 $scope.myFile = '';
			 messageCenterService.add('danger', 'Please Select An Excel File.', {status: messageCenterService.status.next, timeout: 2000});
		     }
 
 };
 	  	   	 
 $scope.saveDataToDatabase = function() {
	 
	 if($scope.counter != 100)
	 {
		 $scope.isSaveDisabled = true;
		 messageCenterService.add('danger', 'Please Upload File First.', {status: messageCenterService.status.next, timeout: 3000});
	 }
	  
	 if( $scope.counter == 100){
		 
	 	$scope.saveExcelData['path'] = 'createService';	
	    var data = postData.submit("webservices.php", $scope.saveExcelData, $scope).then(function() {
			
		if($scope.response.statusCode==200){
		messageCenterService.add('success', 'File Successfully Save', {status: messageCenterService.status.next, timeout: 3000});
		$scope.isSaveDisabled = true;
		$scope.myFile = '';
		
	 }
	 
	 if($scope.response.statusCode!=200){
		messageCenterService.add('danger', 'File Format is incorrect.', {status: messageCenterService.status.next, timeout: 2000});
		$scope.isSaveDisabled = true;
		$scope.myFile = '';
	 }
	 
		console.log($scope.response.data);
		
	});
	progress.style.width = "0%";
	$scope.fileName ="";
	$scope.filePath = "Select File";
	$scope.counter = 0;
	$scope.callOneTime = true;
	$scope.isDisabled = false;

	 }
 }
 
$scope.excelDataExtractor = function() {
var X = XLSX;
var rABS = typeof FileReader !== "undefined" && typeof FileReader.prototype !== "undefined" && typeof FileReader.prototype.readAsBinaryString !== "undefined";

function to_json(workbook) {
	var result = {};
	workbook.SheetNames.forEach(function(sheetName) {
		var roa = X.utils.sheet_to_row_object_array(workbook.Sheets[sheetName]);
		if(roa.length > 0){
			result[sheetName] = roa;
		}
	});
	return result;
}

function process_wb(wb) {
	var output = "";		
	output = JSON.stringify(to_json(wb), 1, 1);	
	
	var excelData = to_json(wb);
	var data = excelData;	
	$scope.saveExcelData = excelData;
}

var xlf = document.getElementById('xlf');

function handleFile(e) {
		
	var files = e.target.files;
	var f = files[0];
	{
		var reader = new FileReader();
		var name = f.name;
		
		$scope.filePath = name;
		$scope.isDisabled = false;
		
		reader.onload = function(e) {
			
			if(typeof console !== 'undefined') console.log("onload", new Date(), rABS);
			var data = e.target.result;		
			  {
				var wb;				
				if(rABS) {
				
					wb = X.read(data, {type: 'binary'});
				} 
				process_wb(wb);
			}
		};
		if(rABS) reader.readAsBinaryString(f);
		else reader.readAsArrayBuffer(f);
	}
}

if(xlf.addEventListener) 

xlf.addEventListener('change', handleFile, false);
}

}])
.config(function($urlRouterProvider, $stateProvider) {

   var 

		excel = {
	        name: 'excel',
	        url: '/excel',
	        templateUrl: 'upload.html',
	        controller: 'ExcelController',
			data: {
				pageTitle: 'Upload Page'
			}, 
	    };
		 
	    $stateProvider
			.state('excel', excel)
   
})

.directive('fileModel', ['$parse', function ($parse) {
    return {
        restrict: 'A',
        link: function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;
            
            element.bind('change', function(){
                scope.$apply(function(){
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
	
}]);