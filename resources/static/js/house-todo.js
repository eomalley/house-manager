(function() {
    'use strict';

    house.controller('toDoController', toDoController);

    toDoController.$inject = ['$scope', '$routeParams', '$route', '$http', 'houseUtils', '$uibModal', '$compile'];

    function toDoController($scope, $routeParams, $route, $http, houseUtils, $uibModal, $compile) {
        var vm = this;
        vm.editToDo = editToDo;
        vm.deleteToDo = deleteToDo;  
        vm.addToDo = addToDo;  
        vm.completeToDo = completeToDo;
        vm.updateScratch = updateScratch;
        vm.refreshScratch = refreshScratch; 
        vm.refresh = refresh; 
        vm.scratch = {};

        vm.gridOptions = houseUtils.getDefaultGridOptions(); 
        vm.gridOptions.paginationPageSize = 5;
        vm.gridOptions.minRowsToShow = vm.gridOptions.paginationPageSize;
        vm.gridOptions.virtualizationThreshold = vm.gridOptions.paginationPageSize;
       
        vm.gridOptions.columnDefs =[
	                                    {
	                                    	field: 'name',
                                            cellClass: 'cellText'
	                                    },
                                        {
                                            field: 'location',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            field: 'dateEntered',
                                            cellClass: 'cellText',
                                            cellFilter: 'date:"MM/dd/yy"',
                                            width: '105'
                                        },
                                        {
                                            field: 'enteredBy',
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
                                            field: 'completedOn',
                                            cellClass: 'cellText',
                                            cellFilter: 'date:"MM/dd/yy"',
                                            width: '105'
                                        },
                                        {
                                            field: 'completedBy',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Importance',
                                            field: 'importance',
                                            cellTemplate: '<uib-rating ng-model="row.entity.importance" max="10" state-on="\x27fa fa-star\x27" state-off="\x27fa fa-star-o\x27" readonly="true"></uib-rating>',
                                            cellClass: 'cellRating',
                                            width: '170'
                                        },                                       
                                        {
                                            displayName: 'Actions',
                                            field: 'id',
                                            cellTemplate:   '<button class="btn btn-info" ng-click="grid.appScope.toDoController.completeToDo(row.entity.id)">' +
                                            				'   <i class="fa fa-check-square-o"></i></button>&nbsp;' +
                                            				'<button class="btn btn-warning" ng-click="grid.appScope.toDoController.editToDo(row.entity.id)">' +
                                                            '   <i class="fa fa-edit"></i>' +  
                                                            '</button>&nbsp;' +
                                                            '<button class="btn btn-danger" ng-click="grid.appScope.toDoController.deleteToDo(row.entity.id)">' +
                                                            '   <i class="fa fa-trash-o"></i>' +
                                                            '</button>',
                                            enableFiltering: false,
                                            enableSorting: false,
                                            cellClass: 'cellActions',
                                            width: '*',
                                            minWidth: '130'
                                        }
                                   ];

        getToDoData();
        getScratchData();

        function getToDoData() {
            $http.get('../todo/getAll').success(function(data) {
            	angular.forEach(data, function(value, key) {
                                       
                    if(moment().isAfter(value.dueDate)) {
                    	value.dueDateColor = 'red';
                    } else {
                    	value.dueDateColor = 'green';
                    }
                });
            	vm.gridOptions.data = data;
                vm.gridOptions.minRowsToShow = vm.gridOptions.paginationPageSize;
                vm.gridOptions.virtualizationThreshold = vm.gridOptions.paginationPageSize;
            });
        };

        function getScratchData() {
            $http.get('../todo/scratch').success(function(data) {
                vm.scratch = data;
            });
        };

        function updateScratch() {
            $http.post('/todo/scratch', {
                "scratch": JSON.stringify(vm.scratch)
            }).success(function() {
                getScratchData();
            });
        };
     
        function editToDo(id) {
            $http.get('/todo', {
                    params: {
                        id: id
                    }
                })
                .success(function(data) {

                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'html/todo-entry-modal.html',
                        controller: 'addEditToDoModalCtrl',
                        controllerAs: 'addEditToDoModalCtrl',
                        resolve: {
                            toDoParams: function() {
                                return data;
                            }
                        }
                    });


                    modalInstance.result.then(function() {
                    	setTimeout(function(){ 
                    		getToDoData(); 
                    	}, 1000);
                    }, function() {

                    });
                })
                .error(function() {
                    console.log("fail");
                });
        };

        function deleteToDo(id) {
            var modalInstance = $uibModal.open({ 
                animation: true,
                templateUrl: 'html/delete-todo-modal.html',
                controller: 'deleteToDoModalCtrl',
                controllerAs: 'deleteToDoModalCtrl',
                resolve: {
                    id: function() { 
                        return id;  
                    }
                }
            });

            modalInstance.result.then(function() {
            	setTimeout(function(){ 
            		getToDoData(); 
            	}, 1000);
            }, function() {

            });
        };

        function addToDo() {

            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/todo-entry-modal.html',
                controller: 'addEditToDoModalCtrl',
                controllerAs: 'addEditToDoModalCtrl',
                resolve: {
                    toDoParams: function() {
                        return {};
                    }
                }
            });

            modalInstance.result.then(function(toDoParams) {
            	setTimeout(function(){ 
            		getToDoData(); 
            	}, 1000);
            }, function() {

            });
        };
        
        function completeToDo(id) {
        	$http.get('/todo', {
                params: {
                    id: id
                }
            })
            .success(function(data) {
            	$http.get('/username').then(function(userData) {
            		 data.completedBy = userData.data.name;
                });
                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'html/complete-todo-modal.html',
                    controller: 'completeToDoModalCtrl',
                    controllerAs: 'completeToDoModalCtrl',
                    resolve: {
                        toDoParams: function() {
                            return data;
                        }
                    }
                });


                modalInstance.result.then(function() {
                	setTimeout(function(){ 
                		getToDoData(); 
                	}, 1000);
                }, function() {

                });
            })
            .error(function() {
                console.log("fail");
            });
            
        };
        
        

        function refresh() {
        	getToDoData();
        }

        function refreshScratch() {
            getScratchData();
        };
    };
})();

(function() {
    'use strict';

    house.controller('addEditToDoModalCtrl', addEditToDoModalCtrl);

    addEditToDoModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'toDoParams', 'houseUtils'];

    function addEditToDoModalCtrl($scope, $uibModalInstance, $http, toDoParams, houseUtils) {
        var vm = this;
        vm.toDoParams = toDoParams;
        vm.ok = ok;
        vm.cancel = cancel;
        vm.openDueDate = openDueDate;
        vm.openEnterDate = openEnterDate;
        
        vm.dueDateOpen = false;
        vm.enterDateOpen = false;

        function openDueDate(event) {
            vm.dueDateOpen = true;
        }
        function openEnterDate(event) {
            vm.enterDateOpen = true;
        }



        initModal();

        function initModal() {
            if (!toDoParams.id) {
                vm.title = 'Add New To Do';
                $http.get('/username').then(function(userData) {
                	vm.toDoParams.enteredBy = userData.data.name;
               });
                vm.toDoParams.dateEntered = new Date();
            } else {
                vm.title = 'Edit To Do';
                
            }
        }

        function ok() {
            $http.post('/todo', {
                "toDoParams": JSON.stringify(vm.toDoParams)
            }).success(function() {
                console.log("success save");
            }).error(function() {
                console.log("save fail");
            });

            $uibModalInstance.close(vm.toDoParams);
        };

        function cancel() {
            $uibModalInstance.dismiss('cancel');
        };
       
    };
})();

(function() {
    'use strict';

    house.controller('deleteToDoModalCtrl', deleteToDoModalCtrl);

    deleteToDoModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'id', 'houseUtils'];

    function deleteToDoModalCtrl($scope, $uibModalInstance, $http, id, houseUtils) {
        var vm = this;
        vm.ok = ok;
        vm.cancel = cancel;

        $http.get('/todo', {
            params: {
                id: id
            }
        }).success(function(data) {
            vm.toDo = data;
        });

        function ok() {
            $http.delete('/todo', {
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

    house.controller('completeToDoModalCtrl', completeToDoModalCtrl);

    completeToDoModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'toDoParams', 'houseUtils'];

    function completeToDoModalCtrl($scope, $uibModalInstance, $http, toDoParams, houseUtils) {
        var vm = this;
        vm.toDoParams = toDoParams;
        vm.ok = ok;
        vm.cancel = cancel;
        vm.getUserNames = getUserNames;
        vm.openCompleteDate = openCompleteDate;
        vm.openEnterDate = openEnterDate;
        
        vm.completeDateOpen = false;
        vm.enterDateOpen = false;

        function openCompleteDate(event) {
            vm.completeDateOpen = true;
        }
        function openEnterDate(event) {
            vm.enterDateOpen = true;
        }
        
        function getUserNames(val) {
            return $http.get('/username/getusers', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };

        function ok() {
            $http.post('/todo', {
                "toDoParams": JSON.stringify(vm.toDoParams)
            }).success(function() {
                console.log("success save");
            }).error(function() {
                console.log("save fail");
            });

            $uibModalInstance.close(vm.toDoParams);
        };

        function cancel() {
            $uibModalInstance.dismiss('cancel');
        };
       
    };
})();

