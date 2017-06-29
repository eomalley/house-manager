(function() {
    'use strict';

    house.controller('recipeController', recipeController);

    recipeController.$inject = ['$scope', '$routeParams', '$route', '$http', 'houseUtils', '$uibModal', '$compile'];

    function recipeController($scope, $routeParams, $route, $http, houseUtils, $uibModal, $compile) {
        var vm = this;
        vm.editRecipe = editRecipe;
        vm.deleteRecipe = deleteRecipe;  
        vm.addRecipe = addRecipe;  
        vm.viewRecipe = viewRecipe; 
        vm.refresh = refresh;

        vm.gridOptions = houseUtils.getDefaultGridOptions(); 
       
        vm.gridOptions.columnDefs =[
	                                    {
	                                    	field: 'name',
                                            cellClass: 'cellText'
	                                    },
                                        {
	                                    	displayName: 'Meal',
                                            field: 'type',
                                            cellClass: 'cellText'
                                        },
                                        {
                                        	displayName: 'Cuisine',
                                            field: 'style',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            field: 'timesMade',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            field: 'lastMade',
                                            cellClass: 'cellText',
                                            cellFilter: 'date:"MM/dd/yy"'
                                        },
                                        {
                                        	displayName: 'Serving',
                                        	field: 'servingSize',
                                        	cellClass: 'cellText',
                                        	width: '70'
                                        },
                                        {
                                        	field: 'cookTime',
                                        	cellClass: 'cellText',
                                        },
                                        {
                                            displayName: 'Short Ingredients',
                                            field: 'shortIngredientCount',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Available Ingredients',
                                            field: 'availableIngredientCount',
                                            cellClass: 'cellText'
                                        },
                                        {
                                            displayName: 'Rating',
                                            field: 'rating',
                                            cellTemplate: '<uib-rating ng-model="row.entity.rating" max="10" state-on="\x27fa fa-heart pink\x27" state-off="\x27fa fa-heart-o\x27" readonly="true"></uib-rating>',
                                            
                                            width: '90'
                                        },                                       
                                        {
                                            displayName: 'Actions',
                                            field: 'id',
                                            cellTemplate: '<button class="btn btn-info" ng-click="grid.appScope.recipeController.viewRecipe(row.entity.id)">' +
                                                            '   <i class="fa fa-file-o"></i>' + 
                                                            '</button>&nbsp;' +
                                                            '<button class="btn btn-warning" ng-click="grid.appScope.recipeController.editRecipe(row.entity.id)">' +
                                                            '   <i class="fa fa-edit"></i>' +  
                                                            '</button>&nbsp;' +
                                                            '<button class="btn btn-danger" ng-click="grid.appScope.recipeController.deleteRecipe(row.entity.id)">' +
                                                            '   <i class="fa fa-trash-o"></i>' +
                                                            '</button>',
                                            enableFiltering: false,
                                            enableSorting: false,
                                            cellClass: 'cellActions',
                                            width: '*',
                                            minWidth: '130'
                                        }
                                   ];

        getRecipeData();

        function getRecipeData() {
            $http.get('../recipe/getAll').success(function(data) {
            	vm.gridOptions.data = data;
                vm.gridOptions.minRowsToShow = vm.gridOptions.paginationPageSize;
                vm.gridOptions.virtualizationThreshold = vm.gridOptions.paginationPageSize;
            });
        };
 
        

        function editRecipe(id) {

            $http.get('/recipeDetails/edit', {
                    params: {
                        id: id
                    }
                })
                .success(function(data) {

                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'html/recipe-entry-modal.html',
                        controller: 'addEditRecipeModalCtrl',
                        controllerAs: 'addEditRecipeModalCtrl',
                        size: 'lg',
                        resolve: {
                            recipeParams: function() {
                                return data;
                            }
                        }
                    });


                    modalInstance.result.then(function() {
                    	setTimeout(function(){ 
                    		getRecipeData(); 
                    	}, 1000);
                    }, function() {

                    });
                })
                .error(function() {
                    console.log("fail");
                });
        };

        function deleteRecipe(id) {
            var modalInstance = $uibModal.open({ 
                animation: true,
                templateUrl: 'html/delete-recipe-modal.html',
                controller: 'deleteRecipeModalCtrl',
                controllerAs: 'deleteRecipeModalCtrl',
                resolve: {
                    id: function() { 
                        return id;  
                    }
                }
            });

            modalInstance.result.then(function() {
            	setTimeout(function(){ 
            		getRecipeData(); 
            	}, 1000);
            }, function() {

            });
        };

        function addRecipe() {

            var modalInstance = $uibModal.open({
                animation: true,
                templateUrl: 'html/recipe-entry-modal.html',
                controller: 'addEditRecipeModalCtrl',
                controllerAs: 'addEditRecipeModalCtrl',
                size: 'lg',
                resolve: {
                    recipeParams: function() {
                        return {};
                    }
                }
            });

            modalInstance.result.then(function(recipeParams) {
            	
            	setTimeout(function(){ 
            		
            		getRecipeData(); 
            	}, 1000);
            }, function() {

            });
        };

        function viewRecipe(id) {
            $http.get('/recipeDetails/view', {
                    params: {
                        id: id
                    }
                })
                .success(function(data) {

                    var modalInstance = $uibModal.open({
                        animation: true,
                        templateUrl: 'html/view-recipe-modal.html',
                        controller: 'viewRecipeModalCtrl',
                        controllerAs: 'viewRecipeModalCtrl',
                        size: 'lg',
                        resolve: {
                            recipeParams: function() {
                                return data;
                            }
                        }
                    });


                    modalInstance.result.then(function() {
                    	setTimeout(function(){ 
                    		getRecipeData(); 
                    	}, 1000);
                    }, function() {

                    });
                })
                .error(function() {
                    console.log("fail");
                });
        }

        function refresh() {
        	getRecipeData();
        }
    };
})();

(function() {
    'use strict';

    house.controller('addEditRecipeModalCtrl', addEditRecipeModalCtrl);

    addEditRecipeModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'recipeParams', 'houseUtils', 'Upload'];

    function addEditRecipeModalCtrl($scope, $uibModalInstance, $http, recipeParams, houseUtils, Upload) {
        var vm = this;
        vm.recipeParams = recipeParams;
        vm.ok = ok;
        vm.cancel = cancel;
        vm.getRecipeTypes = getRecipeTypes;
        vm.getRecipeStyles = getRecipeStyles;
        vm.measurementTypes = houseUtils.getAllMeasurementTypes();
        vm.getLikeItems = getLikeItems;
        vm.getIngredients = getIngredients;
        vm.ingredients = vm.getIngredients(vm.recipeParams);
        vm.addIngredient = addIngredient;
        vm.removeIngredient = removeIngredient;
        vm.uploadFile = uploadFile;
        vm.openLastMade = openLastMade;
        
        function openLastMade($event) {
        	vm.lastMadeOpen = true;
        }

        
        initModal();
        
        function uploadFile() {
        	if(vm.imageFile) {

        		Upload.upload({
                    url: '/upload',
                    data: {file: vm.imageFile, id: vm.recipeParams.id}
                })
                .success(function() {
                    console.log('file uploaded');
                    vm.success = true;
                }).error(function(data) {
                    console.log('error status: ' + data);
                    vm.fail = true;
                })
        	}
        }

        function getRecipeTypes(val) {
            return $http.get('/recipe/getRecipeTypes', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };

        function getRecipeStyles(val) {
            return $http.get('/recipe/getRecipeStyles', {
                params: {
                    hint: val
                }
            }).then(function(response) {
                return response.data;
            });
        };

        function initModal() {
            if (!recipeParams.id) {
                vm.title = 'Add New Recipe';

            } else {
                vm.title = 'Edit Recipe';
            }
        }

        function ok() {
            $http.post('/recipe', {
                "recipeParams": JSON.stringify(vm.recipeParams),
                "ingredients" : JSON.stringify(vm.ingredients)
            }).success(function() {
                console.log("success save");
            }).error(function() {
                console.log("save fail");
            });

            $uibModalInstance.close(vm.recipeParams);
        };

        function cancel() {
            $uibModalInstance.dismiss('cancel');
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


        function addIngredient() {
            vm.ingredients.push({
            	itemName: '',
                amount: '',
                measurement: ''
            });
        }

        function removeIngredient(index) {
            vm.ingredients.splice(index, 1);
        }

        

        function getIngredients(recipeParams) {
            var ingredients = [];
            angular.forEach(vm.recipeParams.ingredientSet, function(value, key) {
                var ingredient = {};
                ingredient.itemName = value.item.name;
                ingredient.amount = value.amount;
                ingredient.measurement = value.measurement;
                ingredient.form = value.form;
                ingredients.push(ingredient);
            });
            if(ingredients.length == 0) {
                ingredients.push({
                itemName: '',
                amount: '',
                measurement: ''
            });
            } 
            return ingredients;
        }

    };
})();

(function() {
    'use strict';

    house.controller('deleteRecipeModalCtrl', deleteRecipeModalCtrl);

    deleteRecipeModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'id', 'houseUtils'];

    function deleteRecipeModalCtrl($scope, $uibModalInstance, $http, id, houseUtils) {
        var vm = this;
        vm.ok = ok;
        vm.cancel = cancel;

        $http.get('/recipeDetails/view', {
            params: {
                id: id
            }
        }).success(function(data) {
            vm.recipe = data;
        });

        function ok() {
            $http.delete('/recipe', {
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

    house.controller('viewRecipeModalCtrl', viewRecipeModalCtrl);

    viewRecipeModalCtrl.$inject = ['$scope', '$uibModalInstance', '$http', 'recipeParams', 'houseUtils'];

    function viewRecipeModalCtrl($scope, $uibModalInstance, $http, recipeParams, houseUtils) {
        var vm = this;
        vm.recipeParams = recipeParams;
        vm.ingredients = recipeParams.ingredientSet;
        vm.cancel = cancel;
        vm.decrement = {};
        vm.make = make;
        
        function cancel() {
        	
            $uibModalInstance.dismiss('cancel');
        };

        function make() {
            $http.post('/recipeDetails/view/make', {
                "recipeParams": JSON.stringify(vm.recipeParams),
                "decrement" : JSON.stringify(vm.decrement)
            }).success(function () {
                console.log("success make")
            }).error(function() {
                console.log("make fail");
            });

            $uibModalInstance.close(); 
        }
        
    };
})();