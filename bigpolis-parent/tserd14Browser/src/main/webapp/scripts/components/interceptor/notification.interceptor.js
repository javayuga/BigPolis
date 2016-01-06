 'use strict';

angular.module('tserd14BrowserApp')
    .factory('notificationInterceptor', function ($q, AlertService) {
        return {
            response: function(response) {
                var alertKey = response.headers('X-tserd14BrowserApp-alert');
                if (angular.isString(alertKey)) {
                    AlertService.success(alertKey, { param : response.headers('X-tserd14BrowserApp-params')});
                }
                return response;
            }
        };
    });
