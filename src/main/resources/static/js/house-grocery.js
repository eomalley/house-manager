(function() {
    'use strict';

    house.controller('groceryListController', groceryListController);

    groceryListController.$inject = ['$scope', '$routeParams', '$route', '$http', 'houseUtils', '$uibModal', '$compile'];

    function groceryListController($scope, $routeParams, $route, $http, houseUtils, $uibModal, $compile) {
        var vm = this;

        vm.addGroceryItem = addGroceryItem;
        vm.editGroceryItem = editGroceryItem;
        vm.markOffGroceryItem = markOffGroceryItem;
        vm.deleteGroceryItem = deleteGroceryItem;
        vm.refresh = refresh;
        vm.gridOptions = houseUtils.getDefaultGridOptions(); 
       
        vm.gridOptions.columnDefs =[ 
                                        {
                                            displayName: 'Name',
                                            field: 'item.name',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Brand',
                                            field: 'specificBrand',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Quantity',
                                            field: 'quantity',
                                            cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD}} {{row.entity.measurement + ((row.entity.quantity > 1) ? \'S\' : \'\')}}<div>',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            field: 'store',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Actions',
                                            field: 'type',
                                            cellTemplate:  '<button class="btn btn-info" ng-click="grid.appScope.groceryController.markOffGroceryItem(row.entity.id)">' +
				                                            '   <i class="fa fa-file-o"></i>' + 
				                                            '</button>&nbsp;' +
                                            	           '<button class="btn btn-warning" ng-click="grid.appScope.groceryController.editGroceryItem(' + 'row.entity.id' + ')">' +
                                                           '   <i class="fa fa-edit"></i>' +
                                                            '</button>&nbsp;' + '<button class="btn btn-danger" ' +
                                                            ' ng-click="grid.appScope.groceryController.deleteGroceryItem(row.entity.id)">' +
                                                            '<i class="fa fa-trash-o"></i></button>',
                                            enableFiltering: false,
                                            enableSorting: false,
                                            cellClass: 'cellActions',
                                            width: '*',
                                            minWidth: '90'
                                            
                                        }                            
                                   ];

        getItemData();
        
        function getItemData() {
            $http.get('../groceryList/getAll').success(function(data) {
                vm.gridOptions.data = data;
            })
        };
        
        function addGroceryItem(id) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/add-grocery-modal.html',
                controller: 'addGroceryItemModalCtrl',
                controllerAs: 'addGroceryItemModalCtrl',
                resolve: {
                	groceryItemId: function() {
                        return '';
                    },
                    itemId: function() {
                        return '';
                    },
                    invId: function() {
                        return '';
                    }
                    
                }
            });
            modalInstance.result.then(function() {
            	setTimeout(function(){ 
            		getGroceryData();
            	}, 1000);
            }, function() {

            });
        };
        
        function editGroceryItem(id) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/add-grocery-modal.html',
                controller: 'addGroceryItemModalCtrl',
                controllerAs: 'addGroceryItemModalCtrl',
                resolve: {
                	groceryItemId: function() {
                        return id;
                    },
                    itemId: function() {
                        return '';
                    },
                    invId: function() {
                        return '';
                    }
                    
                }
            });
            modalInstance.result.then(function() {
            	setTimeout(function(){ 
            		getGroceryData();
            	}, 1000);
            }, function() {

            });
        };

        function deleteGroceryItem(id) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/delete-grocery-modal.html',
                controller: 'deleteGroceryItemModalCtrl',
                controllerAs: 'deleteGroceryItemModalCtrl',
                resolve: {
                    id: function() {
                        return id;
                    }
                }
            });
            modalInstance.result.then(function() {
            	setTimeout(function(){ 
                	getGroceryData();
            	}, 1000);
            }, function() {

            });
        };

       
		function markOffGroceryItem(id) {	
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/markoff-grocery-modal.html',
                controller: 'markOffGroceryModalCtrl',
                controllerAs: 'markOffGroceryModalCtrl',
                resolve: {
                    id: function() {
                        return id;
                    }
                    
                }
            });


            modalInstance.result.then(function() {
            	setTimeout(function(){ 
            		getGroceryData();
            	}, 1000);
            }, function() {

            });
        
        	};
        
        
        function refresh() {
        	getGroceryData();
        }
    };
})();

(function() {
    'use strict';

    house.controller('deleteGroceryItemModalCtrl', deleteGroceryItemModalCtrl);

    deleteGroceryItemModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'id', 'houseUtils'];

    function deleteGroceryItemModalCtrl($scope, $uibModalInstance, $http, id, houseUtils) {
        var vm = this;
        vm.ok = ok;
        vm.cancel = cancel;

        $http.get('/groceryList', {
            params: {
                id: id
            }
        }).success(function(data) {
            vm.item = data;
        });

        function ok() {
            $http.delete('/groceryList', {
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

    house.controller('markOffGroceryModalCtrl', markOffGroceryModalCtrl);

    markOffGroceryModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'id', 'houseUtils'];

    function markOffGroceryModalCtrl($scope, $uibModalInstance, $http, id, houseUtils) {
        var vm = this;
        vm.cancel = cancel;
        vm.getBrands = getBrands;
        vm.groceryParams = {};
        vm.ok = ok;
        vm.measurementTypes = houseUtils.getAllMeasurementTypes();

        
        $http.get('/groceryList', {
            params: {
                id: id
            }
        }).success(function(data) {
            vm.groceryParams = data;
        });
        
        function cancel() {
            $uibModalInstance.dismiss('cancel');
        };

        function getBrands(val) {
            return $http.get('/inventory/getBrandNames', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };

        function ok() {
            $http.post('/groceryList/markOff',
                JSON.stringify(vm.groceryParams)
            ).success(function() {
                console.log("success save");
            }).error(function() {
                console.log("save fail");
            });
            $uibModalInstance.close();
        };



    };
})();

(function() {
    'use strict';

    house.controller('addGroceryItemModalCtrl', addGroceryItemModalCtrl);

    addGroceryItemModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'groceryItemId', 'itemId', 'invId', 'houseUtils'];

    function addGroceryItemModalCtrl($scope, $uibModalInstance, $http, groceryItemId, itemId, invId, houseUtils) {
        var vm = this;
        vm.cancel = cancel;
        vm.getBrands = getBrands;
        vm.getLikeItems = getLikeItems;
        vm.getStores = getStores;
        vm.groceryParams = {};
        vm.ok = ok;
        vm.measurementTypes = houseUtils.getAllMeasurementTypes();
        
        if(groceryItemId != '') {
	        $http.get('/groceryList', {
	            params: {
	                id: groceryItemId
	            }
	        }).success(function(data) {
	            vm.groceryParams = data;

	        });
        }
        
        if(itemId != '') {
	        $http.get('/item', {
	            params: {
	                id: itemId
	            }
	        }).success(function(data) {
	            vm.groceryParams.item = data;
	            vm.groceryParams.itemName = data.name;
	            vm.groceryParams.specificBrand = data.favoriteBrand;
	            vm.groceryParams.measurement = data.measurement;
	        });
        }
        
        if(invId != '') {
        	$http.get('/inventory', {
                params: {
                    id: invId
                }
            }).success(function(data) {
            	vm.groceryParams.itemName = data.item.name;
            	vm.groceryParams.item = data.item;
                vm.groceryParams.inventory = data;
                vm.groceryParams.specificBrand = data.brand;
            });
        }
        
        console.log(vm.groceryParams.inventory == undefined);

        function cancel() {
            $uibModalInstance.dismiss('cancel');
        };

        function getBrands(val) {
            return $http.get('/inventory/getBrandNames', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };
        
        function getLikeItems(val) {
            return $http.get('/item/getLikeItems', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };
        
        function getStores(val) {
            return $http.get('/groceryList/getStores', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };

        function ok() {
            $http.post('/groceryList',
                JSON.stringify(vm.groceryParams)
            ).success(function() {
                console.log("success save");
            }).error(function() {
                console.log("save fail");
            });
            $uibModalInstance.close();
        };



    };
})();