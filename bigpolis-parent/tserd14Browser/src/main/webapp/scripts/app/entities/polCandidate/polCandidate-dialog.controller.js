'use strict';

angular.module('tserd14BrowserApp').controller('PolCandidateDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PolCandidate', 'PolCandidacy',
        function($scope, $stateParams, $uibModalInstance, entity, PolCandidate, PolCandidacy) {

        $scope.polCandidate = entity;
        $scope.polcandidacys = PolCandidacy.query();
        $scope.load = function(id) {
            PolCandidate.get({id : id}, function(result) {
                $scope.polCandidate = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tserd14BrowserApp:polCandidateUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.polCandidate.id != null) {
                PolCandidate.update($scope.polCandidate, onSaveSuccess, onSaveError);
            } else {
                PolCandidate.save($scope.polCandidate, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
}]);
