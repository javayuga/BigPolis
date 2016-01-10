'use strict';

angular.module('tserd14BrowserApp')
    .controller('PolCandidateController', function ($scope, $state, PolCandidate, PolCandidateSearch, ParseLinks) {

        $scope.polCandidates = [];
        $scope.predicate = 'id';
        $scope.reverse = true;
        $scope.page = 1;
        $scope.loadAll = function() {
            PolCandidate.query({page: $scope.page - 1, size: 20, sort: [$scope.predicate + ',' + ($scope.reverse ? 'asc' : 'desc'), 'id']}, function(result, headers) {
                $scope.links = ParseLinks.parse(headers('link'));
                $scope.totalItems = headers('X-Total-Count');
                $scope.polCandidates = result;
            });
        };
        $scope.loadPage = function(page) {
            $scope.page = page;
            $scope.loadAll();
        };
        $scope.loadAll();


        $scope.search = function () {
            PolCandidateSearch.query({query: $scope.searchQuery}, function(result) {
                $scope.polCandidates = result;
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
            $scope.polCandidate = {
                fullName: null,
                alias: null,
                id: null
            };
        };
    });
