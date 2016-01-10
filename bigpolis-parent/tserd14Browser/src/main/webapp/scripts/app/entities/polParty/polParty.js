'use strict';

angular.module('tserd14BrowserApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('polParty', {
                parent: 'entity',
                url: '/polPartys',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tserd14BrowserApp.polParty.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/polParty/polPartys.html',
                        controller: 'PolPartyController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('polParty');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('polParty.detail', {
                parent: 'entity',
                url: '/polParty/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tserd14BrowserApp.polParty.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/polParty/polParty-detail.html',
                        controller: 'PolPartyDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('polParty');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PolParty', function($stateParams, PolParty) {
                        return PolParty.get({id : $stateParams.id});
                    }]
                }
            })
            .state('polParty.new', {
                parent: 'polParty',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polParty/polParty-dialog.html',
                        controller: 'PolPartyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    name: null,
                                    acronym: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('polParty', null, { reload: true });
                    }, function() {
                        $state.go('polParty');
                    })
                }]
            })
            .state('polParty.edit', {
                parent: 'polParty',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polParty/polParty-dialog.html',
                        controller: 'PolPartyDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PolParty', function(PolParty) {
                                return PolParty.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('polParty', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('polParty.delete', {
                parent: 'polParty',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polParty/polParty-delete-dialog.html',
                        controller: 'PolPartyDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PolParty', function(PolParty) {
                                return PolParty.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('polParty', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
