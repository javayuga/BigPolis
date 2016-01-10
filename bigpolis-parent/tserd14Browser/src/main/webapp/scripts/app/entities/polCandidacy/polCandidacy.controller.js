'use strict';

angular.module('tserd14BrowserApp')
    .controller('PolCandidacyController', function ($scope, $state, PolCandidacy, PolCandidacySearch, ParseLinks) {

        $scope.polCandidacys = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            PolCandidacy.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.polCandidacys = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            PolCandidacySearch.query({query: $scope.searchQuery}, function(result) {
                $scope.polCandidacys = result;
            }, function(response) {
                if(response.status === 404) {
                    $scope.loadAll();
                }
            });
        };

        $scope.refresh = function () {
            $scope.loadAll();
            $scope.clear();
        };

        $scope.clear = function () {
            $scope.polCandidacy = {
                office: null,
                region: null,
                electionDay: null,
                id: null
            };
        };
    });
