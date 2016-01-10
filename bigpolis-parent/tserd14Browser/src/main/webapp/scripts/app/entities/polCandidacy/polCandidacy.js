'use strict';

angular.module('tserd14BrowserApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('polCandidacy', {
                parent: 'entity',
                url: '/polCandidacys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tserd14BrowserApp.polCandidacy.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/polCandidacy/polCandidacys.html',
                        controller: 'PolCandidacyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('polCandidacy');
                        $translatePartialLoader.addPart('polOffice');
                        $translatePartialLoader.addPart('geoAdminRegion');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('polCandidacy.detail', {
                parent: 'entity',
                url: '/polCandidacy/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tserd14BrowserApp.polCandidacy.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/polCandidacy/polCandidacy-detail.html',
                        controller: 'PolCandidacyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('polCandidacy');
                        $translatePartialLoader.addPart('polOffice');
                        $translatePartialLoader.addPart('geoAdminRegion');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PolCandidacy', function($stateParams, PolCandidacy) {
                        return PolCandidacy.get({id : $stateParams.id});
                    }]
                }
            })
            .state('polCandidacy.new', {
                parent: 'polCandidacy',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polCandidacy/polCandidacy-dialog.html',
                        controller: 'PolCandidacyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    office: null,
                                    region: null,
                                    electionDay: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('polCandidacy', null, { reload: true });
                    }, function() {
                        $state.go('polCandidacy');
                    })
                }]
            })
            .state('polCandidacy.edit', {
                parent: 'polCandidacy',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polCandidacy/polCandidacy-dialog.html',
                        controller: 'PolCandidacyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PolCandidacy', function(PolCandidacy) {
                                return PolCandidacy.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('polCandidacy', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('polCandidacy.delete', {
                parent: 'polCandidacy',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polCandidacy/polCandidacy-delete-dialog.html',
                        controller: 'PolCandidacyDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PolCandidacy', function(PolCandidacy) {
                                return PolCandidacy.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('polCandidacy', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
