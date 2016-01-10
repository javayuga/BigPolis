'use strict';

angular.module('tserd14BrowserApp')
    .controller('PolCandidacyDetailController', function ($scope, $rootScope, $stateParams, entity, PolCandidacy, PolCandidate, PolParty) {
        $scope.polCandidacy = entity;
        $scope.load = function (id) {
            PolCandidacy.get({id: id}, function(result) {
                $scope.polCandidacy = result;
            });
        };
        var unsubscribe = $rootScope.$on('tserd14BrowserApp:polCandidacyUpdate', function(event, result) {
            $scope.polCandidacy = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
