'use strict';

angular.module('tserd14BrowserApp')
    .factory('PolPartySearch', function ($resource) {
        return $resource('api/_search/polPartys/:query', {}, {
            'query': { method: 'GET', isArray: true}
        });
    });
