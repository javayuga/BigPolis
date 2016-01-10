'use strict';

angular.module('tserd14BrowserApp')
    .controller('PolCandidateDetailController', function ($scope, $rootScope, $stateParams, entity, PolCandidate, PolCandidacy) {
        $scope.polCandidate = entity;
        $scope.load = function (id) {
            PolCandidate.get({id: id}, function(result) {
                $scope.polCandidate = result;
            });
        };
        var unsubscribe = $rootScope.$on('tserd14BrowserApp:polCandidateUpdate', function(event, result) {
            $scope.polCandidate = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
