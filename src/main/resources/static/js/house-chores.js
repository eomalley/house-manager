(function() {
    'use strict';

    house.controller('choresController', choresController);
    
    choresController.$inject = ['$scope', '$routeParams', '$route', '$http', 'houseUtils', '$compile', '$uibModal'];


    function choresController($scope, $routeParams, $route, $http, houseUtils, $compile, $uibModal) {
	var vm = this;
    vm.editChore = editChore;
    vm.deleteChore = deleteChore;
    vm.choreScore = choreScore;
    vm.addChore = addChore;
    vm.completeChore = completeChore;
    vm.refresh = refresh;

    vm.gridOptions = houseUtils.getDefaultGridOptions(); 
       
    vm.gridOptions.columnDefs =[
                                    {
                                        field: 'name',
                                        cellClass: 'cellText'
                                    },
                                    {
                                        field: 'type',
                                        cellClass: 'cellText'
                                    },
                                    {
                                        displayName: 'Frequency',
                                        field: 'frequency',
                                        cellClass: 'cellText',
                                        cellTemplate: '<div class="ui-grid-cell-contents">{{row.entity.frequency}} {{row.entity.frequencyPeriod + ((row.entity.frequency > 1) ? \'S\' : \'\')}}</div>',
                                        width: '105' 
                                    },
                                    {
                                        displayName: 'Last Completed',
                                        field: 'lastCompleted',
                                        cellClass: 'cellText',
                                        cellTemplate: '<div class="{{row.entity.lastCompletedColor}} ui-grid-cell-contents">{{COL_FIELD CUSTOM_FILTERS}}</div>',
                                        cellFilter: 'date:"MM/dd/yy"',
                                        width: '105'
                                    },
                                    {
                                        field: 'lastCompletedBy',
                                        cellClass: 'cellText'
                                    },
                                    {
                                    	displayName: 'Due Date',
                                        field: 'dueDate',
                                        cellClass: 'cellText',
                                        cellTemplate: '<div class="{{row.entity.dueDateColor}} ui-grid-cell-contents">{{COL_FIELD CUSTOM_FILTERS}}</div>',
                                        cellFilter: 'date:"MM/dd/yy"',
                                        width: '105'
                                    },
                                    {
                                        field: 'assignee',
                                        cellClass: 'cellText'
                                    },
                                    {
                                        displayName: 'Difficulty',
                                        field: 'difficulty',
                                        cellTemplate: '<uib-rating ng-model="row.entity.difficulty" max="10" state-on="\x27fa fa-star\x27" state-off="\x27fa fa-star-o\x27" readonly="true"></uib-rating>',
                                        cellClass: 'cellRating',
                                        width: '170'
                                    },
                                    {
                                        displayName: 'Actions',
                                        field: 'name',
                                        cellTemplate: '<button class="btn btn-info" ng-click="grid.appScope.choresController.completeChore(row.entity.id)">' +
                                                    '   <i class="fa fa-check-square-o"></i></button>&nbsp;' + 
                                                    '<button class="btn btn-warning" ng-click="grid.appScope.choresController.editChore(row.entity.id)">' +
                                                    '   <i class="fa fa-edit"></i></button>&nbsp;' +
                                                    '<button class="btn btn-danger" ng-click="grid.appScope.choresController.deleteChore(row.entity.id)">' +
                                                    '   <i class="fa fa-trash-o"></i></button>',
                                        enableFiltering: false,
                                        enableSorting: false,
                                        cellClass: 'cellActions',
                                        width: '*',
                                        minWidth: '130'
                                    }
                               ];


    getChoresData();

    function getChoresData() {
        $http.get('../chore/getAll').success(function(data) {
            angular.forEach(data, function(value, key) {
                var lastCompleted = moment(new Date(value.lastCompleted));
                var deadline = lastCompleted.add(value.frequency, value.frequencyPeriod);
                if(moment().isAfter(deadline)) {
                    value.lastCompletedColor = 'red';
                } else {
                    value.lastCompletedColor = 'green'
                }
                
                if(moment().isAfter(value.dueDate)) {
                	value.dueDateColor = 'red';
                } else {
                	value.dueDateColor = 'green';
                }
            });
            vm.gridOptions.data = data;
            vm.gridOptions.minRowsToShow = vm.gridOptions.paginationPageSize;
            vm.gridOptions.virtualizationThreshold = vm.gridOptions.paginationPageSize;
            
        })
    }

    function deleteChore(id) {
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'html/delete-chore-modal.html',
            controller: 'deleteChoreModalCtrl',
            controllerAs: 'deleteChoreModalCtrl',
            resolve: {
                id: function() {
                    return id;
                }
            }
        });
        modalInstance.result.then(function() {
        	setTimeout(function(){ 
        		getChoresData();
        	}, 1000);
        }, function() {

        });
    };

    function editChore(id) {
        $http.get('/chore', {
            params: {
                id: id
            }
        }).success(function(data) {

            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/chore-entry-modal.html',
                controller: 'addEditChoreModalCtrl',
                controllerAs: 'addEditChoreModalCtrl',
                resolve: {
                    choreParams: function() {
                        return data;
                    }
                }
            });

            modalInstance.result.then(function() {
            	setTimeout(function(){ 
            		getChoresData();
            	}, 1000);
            }, function() {

            });
        }).error(function() {
            console.log("fail");
        });
    };
    
    function choreScore() {
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'html/chore-score-modal.html',
            controller: 'choreScoreModalCtrl',
            controllerAs: 'choreScoreModalCtrl'
        });

        modalInstance.result.then(function() {
        	setTimeout(function(){ 
        		getChoresData();
        	}, 1000);
        }, function() {

        });
    };

    function completeChore(id) {
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'html/complete-chore-modal.html',
            controller: 'completeChoreModalCtrl',
            controllerAs: 'completeChoreModalCtrl',
            resolve: {
                id: function() {
                    return id;
                }
            }
        });

        modalInstance.result.then(function() {
        	setTimeout(function(){ 
        		getChoresData();
        	}, 1000);
        }, function() {

        });
    };

    function addChore() {
        var modalInstance = $uibModal.open({
            animation: true,
            templateUrl: 'html/chore-entry-modal.html',
            controller: 'addEditChoreModalCtrl',
            controllerAs: 'addEditChoreModalCtrl',
            resolve: {
                choreParams: function() {
                    return {};
                }
            }
        });

        modalInstance.result.then(function(ChoreentoryParams) {
        	setTimeout(function(){ 
        		getChoresData();
        	}, 1000);
        }, function() {

        });
    };
    
    function refresh() {
    	getChoresData();
    }

    };
})();

(function() {
    'use strict';

    house.controller('deleteChoreModalCtrl', deleteChoreModalCtrl);

    deleteChoreModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'id', 'houseUtils'];

    function deleteChoreModalCtrl($scope, $uibModalInstance, $http, id, houseUtils) {
        var vm = this;
        vm.ok = ok;
        vm.cancel = cancel;

        $http.get('/chore', {
            params: {
                id: id
            }
        }).success(function(data) {
            vm.chore = data;
        });

        function ok() {
            $http.delete('/chore', {
                params: {
                    id: id
                } 
            }).success(function() { 
                console.log("success delete");
            }).error(function() {
                console.log("delete fail");
            });

            $uibModalInstance.close(); 
        };

        function cancel() {
            $uibModalInstance.dismiss('cancel');
        };
    };
})();

(function() {
    'use strict';

    house.controller('addEditChoreModalCtrl', addEditChoreModalCtrl);

    addEditChoreModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'choreParams', 'houseUtils'];

    function addEditChoreModalCtrl($scope, $uibModalInstance, $http, choreParams, houseUtils) {
        var vm = this;
        vm.today = new Date();
        vm.choreParams = choreParams;
        vm.ok = ok;
        vm.cancel = cancel;
        vm.getChoreTypes = getChoreTypes;
        vm.getUserNames = getUserNames;
        vm.frequencyPeriods = houseUtils.getFrequencyPeriods();
        vm.openDate = openDate;
        vm.openDueDate = openDueDate;
        vm.dateOpen = false;
        vm.dueDateOpen = false;
        
        function openDate(event) {
        	vm.dateOpen = true;
        };
        
        function openDueDate(event) {
            vm.dueDateOpen = true;
        };


        initModal();
        
        function getChoreTypes(val) {
            return $http.get('/chore/types', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };
        
        function getUserNames(val) {
            return $http.get('/username/getusers', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };
      
        function initModal() {
            if (!choreParams.id) {
                vm.title = 'Add New chore';

            } else {
                vm.title = 'Edit chore';
            }
        }

        function ok() {
            $http.post('/chore', {
                "choreParams": JSON.stringify(vm.choreParams)
            }).success(function() {
                console.log("success save");
            }).error(function() {
                console.log("save fail");
            });

            $uibModalInstance.close(vm.choreParams);
        };

        function cancel() {
            $uibModalInstance.dismiss('cancel');
        };

        

    };
})();

(function() {
    'use strict';

    house.controller('choreScoreModalCtrl', choreScoreModalCtrl);

    choreScoreModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'houseUtils'];

    function choreScoreModalCtrl($scope, $uibModalInstance, $http, houseUtils) {
        var vm = this;
        vm.ok = ok;
        vm.names = [];
        vm.scores = {};
        vm.openFromDate = openFromDate;
        vm.openToDate = openToDate;
        vm.fromDate = moment().subtract(1, 'month').toDate();
        vm.toDate = moment().toDate();
        vm.getHistories = getHistories;

        vm.from = {
                    dateOpen: false
                  };

        vm.to = {
                    dateOpen: false
                };
        
        function openFromDate(event) {
            vm.from.dateOpen = true;
        };

        function openToDate(event) {
            vm.to.dateOpen = true;
        };

        var colDefs = [
                        {
                            displayName: 'Name',
                            field: 'chore.name',
                            cellClass: 'cellText',
                            
                         },
                         {
                            displayName: 'Completed',
                            field: 'lastDone',
                            cellClass: 'cellText',
                            cellFilter: 'date:"MM/dd/yy"'
                         },
                         {
                            displayName: 'Difficulty',
                            field: 'chore.difficulty',
                            cellClass: 'cellText'
                         },
                       ];

        getHistories();
        function getHistories() {
            $http.get('/chore/scores', {
                params: {
                    dateFrom: vm.fromDate.getTime(),
                    dateTo: vm.toDate.getTime()
                }
            }).success(function(data) {
                angular.forEach(data, function(value, key) {
                    vm.names.push(key);
                    var options = houseUtils.getDefaultGridOptions();
                    options.minRowsToShow = value.length,
                    options.virtualizationThreshold = value.length,
                    options.columnDefs = colDefs;
                    options.data = value;
                    options.enableFiltering = false;
                    vm.scores[key] = options;
                });

                 

                
            });
        };   



        function ok() {
            
            $uibModalInstance.close(); 
        };

    };
})();

(function() {
    'use strict';

    house.controller('completeChoreModalCtrl', completeChoreModalCtrl);

    completeChoreModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'houseUtils', 'id'];

    function completeChoreModalCtrl($scope, $uibModalInstance, $http, houseUtils, id) {
        var vm = this;
        vm.ok = ok;
        vm.cancel = cancel;
        vm.dateOpen = false; 
        vm.openDate = openDate;


        function openDate(event) {
            vm.dateOpen = true;
        }

        $http.get('/chore', {
            params: {
                id: id
            }
        }).success(function(data) {
        	
            vm.choreParams = data;
            $http.get('/username').then(function(data) {
            	vm.choreParams.lastCompletedBy = data.data.name;
            });
            
        });

        function ok() {
            $http.post('/chore/complete', {
                "choreParams": JSON.stringify(vm.choreParams)
            }).success(function() { 
                console.log("success delete");
            }).error(function() {
                console.log("delete fail");
            });

            $uibModalInstance.close(); 
        };

        function cancel() {
            $uibModalInstance.dismiss('cancel');
        };


    };
})();