(function() {
    'use strict';

    house.controller('itemController', itemController);

    itemController.$inject = ['$scope', '$routeParams', '$route', '$http', 'houseUtils', '$uibModal', '$compile'];

    function itemController($scope, $routeParams, $route, $http, houseUtils, $uibModal, $compile) {
        var vm = this;

        vm.addItem = addItem;
        vm.editItem = editItem;
        vm.deleteItem = deleteItem;
        vm.addToGrocery = addToGrocery;
        vm.refresh = refresh;
        vm.generateEssentials = generateEssentials;
        vm.gridOptions = houseUtils.getDefaultGridOptions(); 
       
        vm.gridOptions.columnDefs =[ 
                                        {
                                            displayName: 'Name',
                                            field: 'name',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Favorite Brand',
                                            field: 'favoriteBrand',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Essential',
                                            field: 'essential',
                                            cellTemplate:'<div class="green" ng-show="row.entity.essential"><i class="fa fa-check"></i></div>',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Minimum QTY',
                                            field: 'threshold',
                                            cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD}} {{row.entity.measurement + ((row.entity.threshold > 1) ? \'S\' : \'\')}}<div>',
                                            cellClass: 'cellText'
                                        },
                                        {
                                        	displayName: 'Measurable QTY',
                                            field: 'convertableQuantity',    
                                            cellTemplate: '<div class="ui-grid-cell-contents">{{COL_FIELD}} {{row.entity.convertableQuantityMeasurement + ((row.entity.convertableQuantity > 1) ? \'S\' : \'\')}}<div>',
                                            cellClass: 'cellText'
                                        },

                                        {
                                        	displayName: 'Individual QTY',
                                            field: 'nonConverts',
                                            cellClass: 'cellText',
                                            cellTemplate: '<div ng-show="row.entity.nonConvertCount > 1"><div uib-popover="{{row.entity.nonConverts}}" popover-trigger="mouseenter">MULTIPLE</div></div> <div ng-show="row.entity.nonConvertCount <= 1">{{row.entity.nonConverts}}</div>'
                                            
                                        },
                                        {
                                        	displayName: 'Inventory Types',
                                            field: 'inventorySize',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Actions',
                                            field: 'type',
                                            cellTemplate:  '<button class="btn btn-info" ng-click="grid.appScope.itemController.addToGrocery(row.entity.id)">' +
				                                            '   <i class="fa fa-file-o"></i>' + 
				                                            '</button>&nbsp;' +
                                            	           '<button class="btn btn-warning" ng-click="grid.appScope.itemController.editItem(' + 'row.entity.id' + ')">' +
                                                           '   <i class="fa fa-edit"></i>' +
                                                            '</button>&nbsp;' + '<a uib-popover="{{row.entity.deleteMsg}}" popover-trigger="mouseenter"popover-placement="left"' +
                                                            '> <button class="btn btn-danger" ' +
                                                            ' ng-click="grid.appScope.itemController.deleteItem(row.entity.id)" ng-disabled="row.entity.inventorySize > 0">' +
                                                            '<i class="fa fa-trash-o"></i></button></a>',
                                            enableFiltering: false,
                                            enableSorting: false,
                                            cellClass: 'cellActions',
                                            width: '*',
                                            minWidth: '90'
                                            
                                        }                            
                                   ];

        getItemData();
        
        function getItemData() {
            $http.get('../item/getAll').success(function(data) {
                vm.gridOptions.data = data;
            }).success(function (data) {
                angular.forEach(data, function(value,key) {
                    var nonConverts = '';
                    value.nonConvertCount = 0;
                    for(key in value.nonConvertableQuantity) {
                        nonConverts = nonConverts + key + ': ' + value.nonConvertableQuantity[key] + '     ';
                        value.nonConvertCount++
                    }
                    value.nonConverts = nonConverts;
                    if(value.convertableQuantityMeasurement == null) {
                    	value.convertableQuantityMeasurement = '';
                    }
                    if(value.convertableQuantity == 0) {
                    	value.convertableQuantity = '';
                    }
                    if(value.measurement == null) {
                    	value.measurement = '';
                    }
                    if(!value.essential) {
                    	value.threshold = '';
                    	value.measurement = '';
                    }
                    if(value.inventorySize > 0)
                    {
                    	value.deleteMsg = "Inventory Types must be 0 to delete Item";
                    }
                    
                })
                //wherein we convert the map of measurementype/bigdecimal to something readable
                //list of objects, then do a ng option? add the column for convertable/nonConvertable here (or both)
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
                        return id;
                    },
                    invId: function() {
                        return '';
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

        function deleteItem(id) {
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/delete-item-modal.html',
                controller: 'deleteItemModalCtrl',
                controllerAs: 'deleteItemModalCtrl',
                resolve: {
                    id: function() {
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

        //though this needs to be called from the result list
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
                    	getItemData();
                	}, 1000);
                }, function() {

                });
            }).error(function() {
                console.log("fail");
            });
        };

		function addItem(id) {	
            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/item-entry-modal.html',
                controller: 'editItemModalCtrl',
                controllerAs: 'editItemModalCtrl',
                resolve: {
                    itemParams: function() {
                        return {};
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
        
        
        function generateEssentials() {
        	var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/generate-items-modal.html',
                controller: 'generateItemsModalCtrl',
                controllerAs: 'generateItemsModalCtrl'
            });

            modalInstance.result.then(function() {

            }, function() {

            });
        };
        
        function refresh() {
        	getItemData();
        }
    };
})();

(function() {
    'use strict';

    house.controller('deleteItemModalCtrl', deleteItemModalCtrl);

    deleteItemModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'id', 'houseUtils'];

    function deleteItemModalCtrl($scope, $uibModalInstance, $http, id, houseUtils) {
        var vm = this;
        vm.ok = ok;
        vm.cancel = cancel;

        $http.get('/item', {
            params: {
                id: id
            }
        }).success(function(data) {
            vm.item = data;
        });

        function ok() {
            $http.delete('/item', {
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

    house.controller('editItemModalCtrl', editItemModalCtrl);

    editItemModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'itemParams', 'houseUtils'];

    function editItemModalCtrl($scope, $uibModalInstance, $http, itemParams, houseUtils) {
        var vm = this;
        vm.cancel = cancel;
        vm.getBrands = getBrands;
        vm.itemParams = itemParams;
        vm.ok = ok;
        vm.measurementTypes = houseUtils.getAllMeasurementTypes();

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
            $http.post('/item',
                JSON.stringify(vm.itemParams)
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

    house.controller('generateItemsModalCtrl', generateItemsModalCtrl);

    generateItemsModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'houseUtils'];

    function generateItemsModalCtrl($scope, $uibModalInstance, $http, houseUtils) {
    	var vm = this;
        vm.ok = ok;
        vm.addAll = addAll;
        
        
        vm.gridOptions = houseUtils.getDefaultGridOptions(); 
       
        vm.gridOptions.columnDefs =[
                                        {
                                            field: 'name',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Minimum QTY',
                                            field: 'threshold',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            field: 'inventorySize',
                                            cellClass: 'cellText'
                                        },
                                        {
                                        	field: 'convertableQuantity',    
                                        	cellTemplate: '<div class="red ui-grid-cell-contents">{{COL_FIELD}} {{row.entity.convertableQuantityMeasurement + ((row.entity.convertableQuantity > 1) ? \'S\' : \'\')}}<div>',
                                            cellClass: 'cellText'
                                        },
                                        {
                                        	field: 'nonConverts',
                                        	cellClass: 'cellText',
                                        	cellTemplate:'<div ng-repeat="item in row.entity[col.field]">{{item}}</div>'
                                        }
                                        
                                   ];

        function ok() {
            $uibModalInstance.dismiss('cancel');
        };

             

        getItemData();

        function getItemData() {
            $http.get('../item/lowEssentials').success(function(data) {
                vm.gridOptions.data = data;
            }).success(function (data) {
                angular.forEach(data, function(value,key) {
                    //console.log(value.nonConvertableQuantity);
                    var nonConverts = [];
                    for(key in value.nonConvertableQuantity) {
                        nonConverts.push(key + ': ' + value.nonConvertableQuantity[key]);
                    }
                    value.nonConverts = nonConverts;
                    if(value.convertableQuantityMeasurement == null) {
                    	value.convertableQuantityMeasurement = '';
                    }

                    
                })
            });
        };
        
        function addAll() {
        	$http.post('../item/lowEssentials');
        	$uibModalInstance.close();
        }
    };
})();
