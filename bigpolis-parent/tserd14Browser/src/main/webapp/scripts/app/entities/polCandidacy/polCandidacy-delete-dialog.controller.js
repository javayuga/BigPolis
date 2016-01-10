'use strict';

angular.module('tserd14BrowserApp')
	.controller('PolCandidacyDeleteController', function($scope, $uibModalInstance, entity, PolCandidacy) {

        $scope.polCandidacy = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PolCandidacy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
