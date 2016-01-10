'use strict';

describe('Controller Tests', function() {

    describe('PolCandidacy Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPolCandidacy, MockPolCandidate, MockPolParty;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPolCandidacy = jasmine.createSpy('MockPolCandidacy');
            MockPolCandidate = jasmine.createSpy('MockPolCandidate');
            MockPolParty = jasmine.createSpy('MockPolParty');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PolCandidacy': MockPolCandidacy,
                'PolCandidate': MockPolCandidate,
                'PolParty': MockPolParty
            };
            createController = function() {
                $injector.get('$controller')("PolCandidacyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tserd14BrowserApp:polCandidacyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
