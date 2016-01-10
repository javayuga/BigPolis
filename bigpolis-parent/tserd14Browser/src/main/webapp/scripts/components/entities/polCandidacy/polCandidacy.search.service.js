'use strict';

angular.module('tserd14BrowserApp')
    .factory('PolCandidacySearch', function ($resource) {
        return $resource('api/_search/polCandidacys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
