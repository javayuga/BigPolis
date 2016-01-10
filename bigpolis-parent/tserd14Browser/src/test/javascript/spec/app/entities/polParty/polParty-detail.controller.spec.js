'use strict';

describe('Controller Tests', function() {

    describe('PolParty Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPolParty, MockPolCandidacy;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPolParty = jasmine.createSpy('MockPolParty');
            MockPolCandidacy = jasmine.createSpy('MockPolCandidacy');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity ,
                'PolParty': MockPolParty,
                'PolCandidacy': MockPolCandidacy
            };
            createController = function() {
                $injector.get('$controller')("PolPartyDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'tserd14BrowserApp:polPartyUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
