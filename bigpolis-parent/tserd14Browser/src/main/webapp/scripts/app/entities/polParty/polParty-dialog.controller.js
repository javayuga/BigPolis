'use strict';

angular.module('tserd14BrowserApp').controller('PolPartyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PolParty', 'PolCandidacy',
        function($scope, $stateParams, $uibModalInstance, entity, PolParty, PolCandidacy) {

        $scope.polParty = entity;
        $scope.polcandidacys = PolCandidacy.query();
        $scope.load = function(id) {
            PolParty.get({id : id}, function(result) {
                $scope.polParty = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tserd14BrowserApp:polPartyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.polParty.id != null) {
                PolParty.update($scope.polParty, onSaveSuccess, onSaveError);
            } else {
                PolParty.save($scope.polParty, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
