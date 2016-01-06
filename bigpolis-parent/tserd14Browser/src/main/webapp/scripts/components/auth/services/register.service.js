'use strict';

angular.module('tserd14BrowserApp')
    .factory('Register', function ($resource) {
        return $resource('api/register', {}, {
        });
    });


