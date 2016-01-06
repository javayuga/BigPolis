'use strict';

angular.module('tserd14BrowserApp')
    .factory('errorHandlerInterceptor', function ($q, $rootScope) {
        return {
            'responseError': function (response) {
                if (!(response.status == 401 && response.data.path.indexOf("/api/account") == 0 )){
	                $rootScope.$emit('tserd14BrowserApp.httpError', response);
	            }
                return $q.reject(response);
            }
        };
    });