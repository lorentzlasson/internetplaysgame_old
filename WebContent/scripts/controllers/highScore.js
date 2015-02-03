'use strict';

angular.module('publicApp')
.controller('HighScoreCtrl', function ($scope, GameService, UtilService) {

	GameService.getHighScores().success(function(data){
		if(data.message){
			UtilService.displayMessage(data.message);
		}
		else{
			$scope.highScores = data.other;
		};
	});
});
