'use strict';

angular.module('publicApp')
.controller('AdminCtrl', function ($scope, GameService, UtilService) {
  $scope.gameModes = ["DEFAULT", "SAFE", "DANGER"];

  $scope.config = {
    size: 4,
    playerCap: 5,
    gameMode: $scope.gameModes[0]
  }

  $scope.newGame = function(){
    execGamePromise(GameService.newGame($scope.config));
  };

  $scope.endGame = function(){
    execGamePromise(GameService.endGame($scope.adminKey));
  };

  $scope.disposeInactives = function(){
    execGamePromise(GameService.disposeInactives($scope.adminKey));
  };

  function execGamePromise(promise){
    promise.success(function(data){
      if(data.message){
        UtilService.displayMessage(data.message);
      }
      else {
        console.log(data);
      }
    })
  }
});
