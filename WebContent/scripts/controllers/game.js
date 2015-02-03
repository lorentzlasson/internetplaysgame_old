'use strict';

angular.module('publicApp')
.controller('GameCtrl', function ($scope, $modal, $timeout, GameService, UtilService) {
	var userKey;
	$scope.board;

	// Register new user
	GameService.newUser().success(function(data){
		if(data.message){
			UtilService.displayMessage(data.message);
		}
		else{
			userKey = data.game.user.key;
			$scope.board = data.game;
			mimicIncResources();

		}
	});

	function mimicIncResources(){
		var resSum = $scope.board.user.resource + $scope.board.user.tickingResource;
		if (resSum + 1 <=  20) {
			$scope.board.user.tickingResource++;			
		};
		$timeout(mimicIncResources, 1000);	
	}

	$scope.resolveEntityPath = function(entity){
		if (entity) {
			return "views/entities/"+entity.type+".html";
		};
	};

	// Removes user if window is closed
	window.onbeforeunload = function () {
		GameService.disposeUser(userKey).success(function(data){
			console.log(data);
		});
	};

	$scope.move = function(direction){
		var promise = GameService.move(direction, userKey);
		execGamePromise(promise);
	};

	$scope.reveal = function(){
		var promise = GameService.reveal(userKey);
		execGamePromise(promise);
	}

	function execGamePromise(promise){
		promise.success(function(data){
			if(data.message){
				UtilService.displayMessage(data.message);
			}
			else {
				$scope.board = data.game;
				$scope.board.user.tickingResource = 0;

				if (!$scope.board.user.alive) {
					handleGameOver();
				}
			}
		})
	}

	function handleGameOver () {
		var modalInstance = $modal.open({
			templateUrl: 'views/gameOverModal.html',
			controller: 'GameOverModalCtrl',
			size: 'sm',
			resolve: {
				user: function () {
					return $scope.board.user;
				}
			}
		});

		modalInstance.result.then(function (highScore) {
			GameService.addHighScore(highScore).success(function(data){
				if(data.message){
					UtilService.displayMessage(data.message);
				}
			});
		}, function () {
			console.log('Modal dismissed at: ' + new Date());
		});
	}

	$scope.keyBoardMove = function(evt) {
		var key = evt.keyIdentifier;
		switch(key)
		{
			case "Right":
			$scope.move("RIGHT");
			break;

			case "U+0044": // d
			$scope.move("RIGHT");
			break;

			case "Up":
			$scope.move("UP");
			break;

			case "U+0057": // w
			$scope.move("UP");
			break;

			case "Left":
			$scope.move("LEFT");
			break;

			case "U+0041": // a
			$scope.move("LEFT");
			break;

			case "Down":
			$scope.move("DOWN");
			break;

			case "U+0053": // s
			$scope.move("DOWN");
			break;				

			case "U+0020": // space
			$scope.reveal();
			break;
		}
	};
});
