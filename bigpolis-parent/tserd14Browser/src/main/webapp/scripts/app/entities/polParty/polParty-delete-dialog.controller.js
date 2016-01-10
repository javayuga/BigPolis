'use strict';

angular.module('tserd14BrowserApp')
	.controller('PolPartyDeleteController', function($scope, $uibModalInstance, entity, PolParty) {

        $scope.polParty = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PolParty.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
