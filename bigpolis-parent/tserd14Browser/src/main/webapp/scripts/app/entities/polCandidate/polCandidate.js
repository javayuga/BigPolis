'use strict';

angular.module('tserd14BrowserApp')
    .config(function ($stateProvider) {
        $stateProvider
            .state('polCandidate', {
                parent: 'entity',
                url: '/polCandidates',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tserd14BrowserApp.polCandidate.home.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/polCandidate/polCandidates.html',
                        controller: 'PolCandidateController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('polCandidate');
                        $translatePartialLoader.addPart('global');
                        return $translate.refresh();
                    }]
                }
            })
            .state('polCandidate.detail', {
                parent: 'entity',
                url: '/polCandidate/{id}',
                data: {
                    authorities: ['ROLE_USER'],
                    pageTitle: 'tserd14BrowserApp.polCandidate.detail.title'
                },
                views: {
                    'content@': {
                        templateUrl: 'scripts/app/entities/polCandidate/polCandidate-detail.html',
                        controller: 'PolCandidateDetailController'
                    }
                },
                resolve: {
                    translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                        $translatePartialLoader.addPart('polCandidate');
                        return $translate.refresh();
                    }],
                    entity: ['$stateParams', 'PolCandidate', function($stateParams, PolCandidate) {
                        return PolCandidate.get({id : $stateParams.id});
                    }]
                }
            })
            .state('polCandidate.new', {
                parent: 'polCandidate',
                url: '/new',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polCandidate/polCandidate-dialog.html',
                        controller: 'PolCandidateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: function () {
                                return {
                                    fullName: null,
                                    alias: null,
                                    id: null
                                };
                            }
                        }
                    }).result.then(function(result) {
                        $state.go('polCandidate', null, { reload: true });
                    }, function() {
                        $state.go('polCandidate');
                    })
                }]
            })
            .state('polCandidate.edit', {
                parent: 'polCandidate',
                url: '/{id}/edit',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polCandidate/polCandidate-dialog.html',
                        controller: 'PolCandidateDialogController',
                        size: 'lg',
                        resolve: {
                            entity: ['PolCandidate', function(PolCandidate) {
                                return PolCandidate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('polCandidate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            })
            .state('polCandidate.delete', {
                parent: 'polCandidate',
                url: '/{id}/delete',
                data: {
                    authorities: ['ROLE_USER'],
                },
                onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                    $uibModal.open({
                        templateUrl: 'scripts/app/entities/polCandidate/polCandidate-delete-dialog.html',
                        controller: 'PolCandidateDeleteController',
                        size: 'md',
                        resolve: {
                            entity: ['PolCandidate', function(PolCandidate) {
                                return PolCandidate.get({id : $stateParams.id});
                            }]
                        }
                    }).result.then(function(result) {
                        $state.go('polCandidate', null, { reload: true });
                    }, function() {
                        $state.go('^');
                    })
                }]
            });
    });
