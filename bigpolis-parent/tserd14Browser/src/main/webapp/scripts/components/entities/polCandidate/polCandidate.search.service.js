'use strict';

angular.module('tserd14BrowserApp')
    .factory('PolCandidateSearch', function ($resource) {
        return $resource('api/_search/polCandidates/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
