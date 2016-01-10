'use strict';

angular.module('tserd14BrowserApp')
    .factory('PolCandidacy', function ($resource, DateUtils) {
        return $resource('api/polCandidacys/:id', {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    data = angular.fromJson(data);
                    data.electionDay = DateUtils.convertLocaleDateFromServer(data.electionDay);
                    return data;
                }
            },
            'update': {
                method: 'PUT',
                transformRequest: function (data) {
                    data.electionDay = DateUtils.convertLocaleDateToServer(data.electionDay);
                    return angular.toJson(data);
                }
            },
            'save': {
                method: 'POST',
                transformRequest: function (data) {
                    data.electionDay = DateUtils.convertLocaleDateToServer(data.electionDay);
                    return angular.toJson(data);
                }
            }
        });
    });
