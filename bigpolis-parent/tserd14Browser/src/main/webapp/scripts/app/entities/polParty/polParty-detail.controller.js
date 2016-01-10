'use strict';

angular.module('tserd14BrowserApp')
    .controller('PolPartyDetailController', function ($scope, $rootScope, $stateParams, entity, PolParty, PolCandidacy) {
        $scope.polParty = entity;
        $scope.load = function (id) {
            PolParty.get({id: id}, function(result) {
                $scope.polParty = result;
            });
        };
        var unsubscribe = $rootScope.$on('tserd14BrowserApp:polPartyUpdate', function(event, result) {
            $scope.polParty = result;
        });
        $scope.$on('$destroy', unsubscribe);

    });
