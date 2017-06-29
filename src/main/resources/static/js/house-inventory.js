(function() {
    'use strict';

    house.controller('inventoryController', inventoryController);

    inventoryController.$inject = ['$scope', '$routeParams', '$route', '$http', 'houseUtils', '$uibModal', '$compile'];

    function inventoryController($scope, $routeParams, $route, $http, houseUtils, $uibModal, $compile) {
        var vm = this;
        vm.editInv = editInv;
        vm.deleteInv = deleteInv;

        vm.editItem = editItem;
        vm.addToGrocery = addToGrocery;

        vm.addInv = addInv;
        vm.uploadCsv = uploadCsv;
        vm.refresh = refresh;
        vm.gridOptions = houseUtils.getDefaultGridOptions(); 
        
       
        vm.gridOptions.columnDefs =[ 
                                        {
                                            displayName: 'Name',
                                            field: 'item.name',
                                            cellTemplate: '<div  ng-click="grid.appScope.inventoryController.editItem(row.entity.item.id)" class="ui-grid-cell-contents">{{row.entity.item.name}}</div>',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Brand',
                                            field: 'brand',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Type',
                                            field: 'type',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Quantity',
                                            field: 'quantity',
                                            cellClass: 'cellText',
                                            cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD}} {{row.entity.measurement + ((row.entity.quantity > 1) ? \'S\' : \'\')}}<div>',
                                            width: '125'
                                        },
                                        {
                                            displayName: 'Last Bought',
                                            field: 'lastBought',
                                            cellClass: 'cellText',
                                            cellFilter: 'date:"MM/dd/yy"',
                                            width: '105'
                                            	
                                        },
                                        {
                                            displayName: 'Use By',
                                            field: 'useBy',
                                            cellTemplate: '<div class="{{row.entity.useByColor}} ui-grid-cell-contents">{{COL_FIELD CUSTOM_FILTERS}}</div>',
                                            cellClass: 'cellText',
                                            cellFilter: 'date:"MM/dd/yy"',
                                            width: '105'
                                        },
                                        {
                                            displayName: 'Rating',
                                            field: 'rating',
                                            cellTemplate: '<uib-rating ng-model="row.entity.rating" max="10" state-on="\x27fa fa-heart pink\x27" state-off="\x27fa fa-heart-o\x27" readonly="true"></uib-rating>',
                                            cellClass: 'cellRating',
                                            width: '180'
                                        },
                                        {
                                            displayName: 'Actions',
                                            field: 'type',
                                            cellTemplate:  '<button class="btn btn-info" ng-click="grid.appScope.inventoryController.addToGrocery(row.entity.id)">' +
				                                            '   <i class="fa fa-file-o"></i>' + 
				                                            '</button>&nbsp;' +
                                            	           '<button class="btn btn-warning" ng-click="grid.appScope.inventoryController.editInv(' + 'row.entity.id' + ')">' +
                                                           '   <i class="fa fa-edit"></i>' +
                                                            '</button>&nbsp;' +
                                                            '<button class="btn btn-danger" ng-click="grid.appScope.inventoryController.deleteInv(' + 'row.entity.id' + ')">' +
                                                            '   <i class="fa fa-trash-o"></i>' +
                                                            '</button>',
                                            enableFiltering: false,
                                            enableSorting: false,
                                            cellClass: 'cellActions',
                                            width: '*',
                                            minWidth: '120'
                                            
                                        }                            
                                   ];

        getInventoryData();
        
        function getInventoryData() {
            $http.get('../inventory/getAll').success(function(data) {
            	angular.forEach(data, function(value, key) {                                        
                    if(moment().add(2, 'day').isAfter(value.useBy)) {
                    	value.useByColor = 'red';
                    } else {
                    	value.useByColor = 'green';
                    }
                });
                vm.gridOptions.data = data;
                vm.gridOptions.minRowsToShow = vm.gridOptions.paginationPageSize;
                vm.gridOptions.virtualizationThreshold = vm.gridOptions.paginationPageSize;
            });
        };
        
        function addToGrocery(id) {
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
                        return id;
                    }
                    
                }
            });
            modalInstance.result.then(function() {
            	setTimeout(function(){ 
            		getItemData();
            	}, 1000);
            }, function() {

            });
        };

        function deleteInv(id) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/delete-inventory-modal.html',
                controller: 'deleteInvModalCtrl',
                controllerAs: 'deleteInvModalCtrl',
                resolve: {
                    id: function() {
                        return id;
                    }
                }
            });
            modalInstance.result.then(function() {
            	setTimeout(function(){ 
                	getInventoryData();
            	}, 1000);
            }, function() {

            });
        };

        function editInv(id) {
            $http.get('/inventory', {
                params: {
                    id: id
                }
            }).success(function(data) {

                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'html/inventory-entry-modal.html',
                    controller: 'addEditInvModalCtrl',
                    controllerAs: 'addEditInvModalCtrl',
                    resolve: {
                        inventoryParams: function() {
                            return data;
                        }
                    }
                });

                modalInstance.result.then(function(inventoryParams) {
                	setTimeout(function(){ 
                    	getInventoryData();
                	}, 1000);
                }, function() {

                });
            }).error(function() {
                console.log("fail");
            });
        };

        function editItem(id) {
        	
            $http.get('/item', {
                params: {
                    id: id
                }
            }).success(function(data) {

                var modalInstance = $uibModal.open({
                    animation: true,
                    templateUrl: 'html/item-entry-modal.html',
                    controller: 'editItemModalCtrl',
                    controllerAs: 'editItemModalCtrl',
                    resolve: {
                        itemParams: function() {
                            return data;
                        }
                    }
                });


                modalInstance.result.then(function() {
                	setTimeout(function(){ 
                    	getInventoryData();
                	}, 1000);
                }, function() {

                });
            }).error(function() {
                console.log("fail");
            });
        };
        
        function addInv() {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/inventory-entry-modal.html',
                controller: 'addEditInvModalCtrl',
                controllerAs: 'addEditInvModalCtrl',
                resolve: {
                    inventoryParams: function() {
                        return {};
                    }
                }
            });

            modalInstance.result.then(function(inventoryParams) {
            	setTimeout(function(){ 
                	getInventoryData();
            	}, 1000);
            }, function() {

            });
        };
        
        function uploadCsv() {
        	var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/inventory-upload-modal.html',
                controller: 'inventoryUploadModalCtrl',
                controllerAs: 'inventoryUploadModalCtrl'
            });

            modalInstance.result.then(function(inventoryParams) {
            	setTimeout(function(){ 
                	getInventoryData();
            	}, 1000);
            }, function() {
            	setTimeout(function(){ 
                	getInventoryData();
            	}, 1000);
            });
        };
        
        
        function refresh() {
        	getInventoryData();
        }
    };
})();


(function() {
    'use strict';

    house.controller('addEditInvModalCtrl', addEditInvModalCtrl);

    addEditInvModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'inventoryParams', 'houseUtils'];

    function addEditInvModalCtrl($scope, $uibModalInstance, $http, inventoryParams, houseUtils) {
        var vm = this;
        vm.adjustmentParams = {};
        vm.adjustmentSelected = adjustmentSelected;
        vm.cancel = cancel;
        vm.getLikeItems = getLikeItems;
        vm.getBrands = getBrands;
        vm.getTypes = getTypes;
        vm.inventoryParams = inventoryParams;
        vm.measurementTypes = houseUtils.getAllMeasurementTypes();
        vm.ok = ok;
        vm.updateAdjustmentTypes = updateAdjustmentTypes;
        
        vm.openLastBoughtDate = openLastBoughtDate;
        vm.openUseByDate = openUseByDate;
        
        vm.dateLastBoughtOpen = false;
        vm.dateUseByOpen = false;
        
        function openLastBoughtDate(event) {
        	vm.dateLastBoughtOpen = true;
        }
        
        function openUseByDate(event) {
        	vm.dateUseByOpen = true;
        }

        setModalType();
        updateAdjustmentTypes();
        
        $http.get('/username').then(function(data) {
        	vm.inventoryParams.lastUpdBy = data.data.name;
        });

        function adjustmentSelected() {
            if (!vm.disableAdjust) {
                if (vm.adjustmentParams) {
                    if (!vm.adjustmentParams.adjustmentType || vm.adjustmentParams.adjustmentType.length == 0) {
                        vm.disableAdjustEntry = true;
                        vm.adjustmentParams.quantityAdjust = null;
                        vm.adjustmentParams.measurementAdjust = null;
                    } else {
                        vm.disableAdjustEntry = false;
                    }
                }
            }
        };

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


        function getTypes(val) {
            return $http.get('/inventory/getTypeNames', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };

        function ok() {
            $http.post('/inventory', {

                "inventoryParams": JSON.stringify(vm.inventoryParams),
                "adjustmentParams": JSON.stringify(vm.adjustmentParams)

            }).success(function() {
                console.log("success save");
            }).error(function() {
                console.log("save fail");
            });
            $uibModalInstance.close(vm.inventoryParams);
        };

        

        function setModalType() {
            if (!inventoryParams.id) {
                vm.title = 'Add New Inventory';
                vm.disableAdjust = true;
                vm.disableAdjustEntry = true;
                
            } else {
                vm.title = 'Edit Inventory';
                vm.disableAdjust = false;
                vm.disableAdjustEntry = true;
            }
        }

        function updateAdjustmentTypes() {
            if (inventoryParams.measurement && inventoryParams.measurement.length > 0) {
                if ((houseUtils.getConvertibleMeasurementTypes().indexOf(inventoryParams.measurement)) > -1) {
                    vm.adjustmentTypes = houseUtils.getConvertibleMeasurementTypes();
                } else {
                    vm.adjustmentTypes = houseUtils.getNonConvertibleMeasurementTypes();
                }
            }
        };

    };
})();

(function() {
    'use strict';

    house.controller('deleteInvModalCtrl', deleteInvModalCtrl);

    deleteInvModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'id', 'houseUtils'];

    function deleteInvModalCtrl($scope, $uibModalInstance, $http, id, houseUtils) {
        var vm = this;
        vm.ok = ok;
        vm.cancel = cancel;

        $http.get('/inventory', {
            params: {
                id: id
            }
        }).success(function(data) {
            vm.inven = data;
        });

        function ok() {
            $http.delete('/inventory', {
                    params: {
                        id: id
                    }
                })
                .success(function() {
                    console.log("success delete");
                })
                .error(function() {
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

    house.controller('inventoryUploadModalCtrl', inventoryUploadModalCtrl);

    inventoryUploadModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'houseUtils', 'Upload'];

    function inventoryUploadModalCtrl($scope, $uibModalInstance, $http, houseUtils, Upload) {
    	var vm = this;
        vm.close = close;
        vm.uploadFile = uploadFile;
        vm.confirm = confirm;
        vm.gridOptions = houseUtils.getDefaultGridOptions(); 
        
        vm.gridOptions.columnDefs =[ 
                                        {
                                            displayName: 'Item Name',
                                            field: 'literalItemName',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            
                                            field: 'brand',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            
                                            field: 'type',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            
                                            field: 'quantity',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            
                                            field: 'measurement',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            
                                            field: 'notes',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            
                                            field: 'rating',
                                            cellClass: 'cellText'
                                        }
                                 ];
                                        
            
        
        function uploadFile() {
        	if(vm.csvFile) {

        		Upload.upload({
                    url: '/upload/csv',
                    data: {file: vm.csvFile}
                })
                .success(function(data) {
                    console.log('file uploaded');
                    vm.success = true;
                    vm.gridOptions.data = data;
                    vm.gridOptions.minRowsToShow = vm.gridOptions.paginationPageSize;
                    vm.gridOptions.virtualizationThreshold = vm.gridOptions.paginationPageSize;
                }).error(function(data) {
                    vm.errorMsg = 'Error ' + data.localizedMessage;
                    vm.fail = true;
                })
        	}
        }
        
        
        
        function confirm() {
        	$http.post('/inventory/addMultiple', {
                "inventoryList": JSON.stringify(vm.gridOptions.data)
            }).success(function() {
                    console.log("success save");
                }).error(function() {
                    console.log("save fail");
                }); 
        	$uibModalInstance.dismiss('confirm');
        }

        function close() {
            $uibModalInstance.dismiss('cancel');
        };

    };
})();


