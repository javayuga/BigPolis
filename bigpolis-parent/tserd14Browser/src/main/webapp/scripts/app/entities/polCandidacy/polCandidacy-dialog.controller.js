'use strict';

angular.module('tserd14BrowserApp').controller('PolCandidacyDialogController',
    ['$scope', '$stateParams', '$uibModalInstance', 'entity', 'PolCandidacy', 'PolCandidate', 'PolParty',
        function($scope, $stateParams, $uibModalInstance, entity, PolCandidacy, PolCandidate, PolParty) {

        $scope.polCandidacy = entity;
        $scope.polcandidates = PolCandidate.query();
        $scope.polpartys = PolParty.query();
        $scope.load = function(id) {
            PolCandidacy.get({id : id}, function(result) {
                $scope.polCandidacy = result;
            });
        };

        var onSaveSuccess = function (result) {
            $scope.$emit('tserd14BrowserApp:polCandidacyUpdate', result);
            $uibModalInstance.close(result);
            $scope.isSaving = false;
        };

        var onSaveError = function (result) {
            $scope.isSaving = false;
        };

        $scope.save = function () {
            $scope.isSaving = true;
            if ($scope.polCandidacy.id != null) {
                PolCandidacy.update($scope.polCandidacy, onSaveSuccess, onSaveError);
            } else {
                PolCandidacy.save($scope.polCandidacy, onSaveSuccess, onSaveError);
            }
        };

        $scope.clear = function() {
            $uibModalInstance.dismiss('cancel');
        };
        $scope.datePickerForElectionDay = {};

        $scope.datePickerForElectionDay.status = {
            opened: false
        };

        $scope.datePickerForElectionDayOpen = function($event) {
            $scope.datePickerForElectionDay.status.opened = true;
        };
}]);
