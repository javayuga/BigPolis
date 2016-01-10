'use strict';

angular.module('tserd14BrowserApp')
    .factory('PolParty', function ($resource, DateUtils) {
        return $resource('api/polPartys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    });
