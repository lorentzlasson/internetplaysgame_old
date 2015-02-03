'use strict';

angular.module('publicApp')
.factory('GameService', function ($http) {

  return {
    newUser: function () {
      return $http.post('/api/game/user/new');
    },

    disposeUser: function (userId) {
      return $http.post('/api/game/user/dispose?userId='+userId);
    },

    move: function (direction, userId) {
      return $http.get('/api/game/move/'+direction+"?userId="+userId);
    },

    reveal: function (userId) {
      return $http.get('/api/game/reveal?userId='+userId);
    },

    newGame: function (config) {
      return $http.post('/api/game/new?size='+config.size+'&gameMode='+config.gameMode+'&playerCap='+config.playerCap+'&adminKey='+config.adminKey);
    },

    endGame: function (adminKey) {
      return $http.post('/api/game/end?adminKey='+adminKey);
    },

    disposeInactives: function (adminKey) {
      return $http.post('/api/game/user/kickInactives?adminKey='+adminKey);
    },

    getHighScores: function(){
      return $http.get('/api/game/highscore');      
    },

    addHighScore: function(highScore){
      return $http.post('/api/game/highscore', highScore);      
    }
  };
});
