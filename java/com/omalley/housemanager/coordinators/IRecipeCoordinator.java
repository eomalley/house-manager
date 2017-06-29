package com.omalley.housemanager.coordinators;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.omalley.housemanager.domain.Ingredient;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;
import com.omalley.housemanager.domain.Recipe;

public interface IRecipeCoordinator
{
    Set<Ingredient> getIngredientsForRecipeName(String recipeName);


    public Set<Recipe> getRecipesForItem(Item item);


    void saveRecipe(Recipe recipeParams, List<Ingredient> ingredientsList);


    void calculateIngredients(List<Recipe> recipes);


    void gatherIngredients(Recipe recipe);


    void saveRecipe(Recipe recipe);


    void decrementInventory(Map<String, Inventory> inventoryToDecrement);
}
