'use strict';

angular.module('publicApp')
	.directive('resize', function ($window) {
		return function (scope, element) {
			//var w = angular.element($window);
			scope.getWindowDimensions = function () {
				return {
					'w': element[0].offsetWidth
				};
			};
			scope.$watch(scope.getWindowDimensions, function (newValue) {
				scope.windowWidth = newValue.w;
				scope.style = function () {
					return {
						'height': (newValue.w) + 'px'
					};
				};
			}, true);
		}
	})