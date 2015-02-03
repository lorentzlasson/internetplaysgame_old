'use strict';

angular.module('publicApp')
.controller('NavBarCtrl', function ($scope, $modal) {
	$scope.showInstructions = function(){	
		var modalInstance = $modal.open({
			templateUrl: 'views/instructionsModal.html',
			controller: 'InstructionsCtrl',
			size: 'lg'
		});
	};
});
