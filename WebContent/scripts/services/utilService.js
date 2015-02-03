'use strict';

angular.module('publicApp')
.factory('UtilService', function (ngNotify) {

	return {
		displayMessage: function (message) {
			var type = "";
			if (message.severityLevel) {
				switch(message.severityLevel){
					case 1:
					type = "info"
					break;

					case 2:
					type = "warn"
					break;

					case 3:
					type = "error"
					break;
				}				
			}
			ngNotify.set(message.text, type);
		}
	};
});
