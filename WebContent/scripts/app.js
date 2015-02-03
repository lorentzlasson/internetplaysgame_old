'use strict';

angular
  .module('publicApp', [
    'ngRoute',
    'ui.bootstrap',
    'ngNotify'
  ])
  .config(function ($routeProvider) {
    $routeProvider
      .when('/game', {
        templateUrl: 'views/game.html',
        controller: 'GameCtrl'
      })
      .when('/highscore', {
        templateUrl: 'views/highScore.html',
        controller: 'HighScoreCtrl'
      })
      .when('/admin', {
        templateUrl: 'views/admin.html',
        controller: 'AdminCtrl'
      })
      .otherwise({
        redirectTo: '/game'
      });
  })