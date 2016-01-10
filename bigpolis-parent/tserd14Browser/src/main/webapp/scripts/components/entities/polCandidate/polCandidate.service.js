'use strict';

angular.module('tserd14BrowserApp')
    .factory('PolCandidate', function ($resource, DateUtils) {
        return $resource('api/polCandidates/:id', {}, {
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
