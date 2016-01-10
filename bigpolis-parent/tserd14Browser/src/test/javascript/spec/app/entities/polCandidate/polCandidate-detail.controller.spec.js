'use strict';

describe('Controller Tests', function() {

    describe('PolCandidate Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPolCandidate, MockPolCandidacy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPolCandidate = jasmine.createSpy('MockPolCandidate');
            MockPolCandidacy = jasmine.createSpy('MockPolCandidacy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PolCandidate': MockPolCandidate,
                'PolCandidacy': MockPolCandidacy
            };
            createController = function() {
                $injector.get('$controller')("PolCandidateDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tserd14BrowserApp:polCandidateUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
