var house = angular.module('house', ['ngRoute', 'ngAnimate', 'ui.bootstrap' , 'ngFileUpload' ,
                                     'ui.grid', 'ui.grid.resizeColumns', 'ui.grid.moveColumns', 
                                     'ui.grid.pagination', 'ui.grid.autoResize'
                                     ]);

house.service('houseUtils', ['uiGridConstants', function(uiGridConstants) {

    var rConvertibleMeasurementTypes = [ //
        'PINCH', //
        'TEASPOON', //
        'TABLESPOON', //
        'OUNCE', //
        'CUP', //
        'GALLON', //
    ];
    this.getConvertibleMeasurementTypes = function() {
        return rConvertibleMeasurementTypes;
    };

    var rNonConvertibleMeasurementTypes = [ //
        'POUND', //
        'PIECE', //
        'BOX', //
        'WHOLE', //
        'CAN' //
    ];
    this.getNonConvertibleMeasurementTypes = function() {
        return rNonConvertibleMeasurementTypes;
    };

    this.getAllMeasurementTypes = function() {
        return rConvertibleMeasurementTypes.concat(rNonConvertibleMeasurementTypes);
    };
    
    var rFrequencyPeriods = ['DAY', //
                             'WEEK', //
                             'MONTH', //
                             'YEAR' //
                             ];
    
    this.getFrequencyPeriods = function() {
    	return rFrequencyPeriods;
    };

    this.getDefaultGridOptions = function() {
      return {
	    	  
              data: [{}], 
              enableColumnResizing: true,
              enableColumnMoving: true,
              paginationPageSizes: [5, 10, 25, 50, 100],
              paginationPageSize: 10,
              enableSorting: true,
              enableFiltering: true,
              enableScrollbars: false,
              enableHorizontalScrollbar:  uiGridConstants.scrollbars.NEVER,
              enableVerticalScrollbar:  uiGridConstants.scrollbars.NEVER,
              rowHeight: 42,
              minRowsToShow: 10,
              virtualizationThreshold: 10,
              columnDefs: [{}]
      };
    }; 


}]);


house.controller('indexController', indexController)

indexController.$inject = ['$scope', '$route', '$routeParams', '$location'];
function indexController($scope, $routeParams, $location, $route) {
		var vm = this;
		vm.getInventory = getInventory;
		vm.getRecipes = getRecipes;
		vm.getChores = getChores;
		vm.getToDo = getToDo;
		vm.getItems = getItems;
		vm.getGroceryList = getGroceryList;

	    function getItems() {
	      if(vm.itemsSelected) {
	        return 'html/items.html';
	      }
	    };
		
		function getInventory() {
			if(vm.invSelected) {
				return 'html/inventory.html';
			}
		};
		
		function getRecipes() {
			if(vm.recipesSelected) {
				return 'html/recipes.html';
			}
		};
		
		function getChores() {
			if(vm.choresSelected) {
				return 'html/chores.html';
			}
		};
		
		function getToDo() {
			if(vm.toDoSelected) {
				return 'html/todo.html';
			}
		};
		
		function getGroceryList() {
			if(vm.groceryListSelected) {
				return 'html/groceryList.html';
			}
		};

};

house.directive("imageResize", [
  "$parse", function($parse) {
    return {
      link: function(scope, elm, attrs) {
        var imagePercent;
        imagePercent = $parse(attrs.imagePercent)(scope);
        imageSize = $parse(attrs.imageSize)(scope);
        var usePercent = imagePercent ? true : false;
        return elm.one("load", function() {
          var canvas, ctx, neededHeight, neededWidth;
          if(usePercent) {
	          neededHeight = elm[0].height * imagePercent / 100;
	          neededWidth = elm[0].width * imagePercent / 100;
          } else {
        	  
        	  neededHeight = elm[0].height / elm[0].width * 200;
              neededWidth = imageSize;
          }
          canvas = document.createElement("canvas");
          canvas.width = neededWidth;
          canvas.height = neededHeight;
          ctx = canvas.getContext("2d");
          ctx.drawImage(elm[0], 0, 0, neededWidth, neededHeight);
          return elm.attr('src', canvas.toDataURL("image/jpeg"));
        });
      }
    };
  }
]);

house.directive('errSrc', function() {
	  return {
	    link: function(scope, element, attrs) {
	      element.bind('error', function() {
	        if (attrs.src != attrs.errSrc) {
	          attrs.$set('src', attrs.errSrc);
	        }
	      });
	    }
	  }
	});
