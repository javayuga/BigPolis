'use strict';

angular.module('tserd14BrowserApp')
	.controller('PolCandidateDeleteController', function($scope, $uibModalInstance, entity, PolCandidate) {

        $scope.polCandidate = entity;
        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.confirmDelete = function (id) {
            PolCandidate.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        };

    });
