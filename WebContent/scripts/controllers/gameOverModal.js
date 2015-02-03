'use strict';
'use strict';

angular.module('publicApp')
.controller('GameOverModalCtrl', function ($scope, $modalInstance, user) {

  $scope.highScore = {
    nickName: "",
    score: user.achievedScore
  };

  $scope.ok = function () {
    $modalInstance.close($scope.highScore);
  };

  $scope.cancel = function () {
    $modalInstance.dismiss('cancel');
  };
});
