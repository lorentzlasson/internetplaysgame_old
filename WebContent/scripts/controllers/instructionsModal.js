'use strict';
'use strict';

angular.module('publicApp')
.controller('InstructionsCtrl', function ($scope, $modalInstance) {

  $scope.ok = function () {
    $modalInstance.dismiss('ok');
  };
});
