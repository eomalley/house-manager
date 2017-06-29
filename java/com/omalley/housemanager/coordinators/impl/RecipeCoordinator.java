package com.omalley.housemanager.coordinators.impl;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omalley.housemanager.coordinators.IIngredientCoordinator;
import com.omalley.housemanager.coordinators.IItemCoordinator;
import com.omalley.housemanager.coordinators.IMeasurementCoordinator;
import com.omalley.housemanager.coordinators.IRecipeCoordinator;
import com.omalley.housemanager.dao.IngredientDao;
import com.omalley.housemanager.dao.InventoryDao;
import com.omalley.housemanager.dao.RecipeDao;
import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Ingredient;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;
import com.omalley.housemanager.domain.MeasurementType;
import com.omalley.housemanager.domain.Recipe;

@Component
public class RecipeCoordinator implements IRecipeCoordinator
{

    private static final String SUBTRACT_IDENTIFIER_STRING = "subtract";

    @Autowired
    IIngredientCoordinator ingredientCoord;

    @Autowired
    RecipeDao recipeDao;

    @Autowired
    IItemCoordinator itemCoord;

    @Autowired
    IMeasurementCoordinator measurementCoord;

    @Autowired
    IngredientDao ingredientDao;
    
    @Autowired
    InventoryDao inventoryDao;

    public static final class SortByName implements Comparator<Recipe>
    {
        @Override
        public int compare(Recipe s1, Recipe s2)
        {
            return s1.getName().compareTo(s2.getName());
        }
    }


    @Override
    public Set<Ingredient> getIngredientsForRecipeName(String recipeName)
    {
        List<Recipe> recipe = this.recipeDao.findByName(recipeName);
        if(recipe.size() == 1)
        {
            return recipe.get(0).getIngredientSet();
        }
        return null;
    }


    @Override
    public Set<Recipe> getRecipesForItem(Item item)
    {
        List<Ingredient> ingredients = this.getIngredientCoord().getIngredientsForItem(item);
        Set<Recipe> recipeSet = new HashSet<Recipe>();
        for(Ingredient eaIngredient : ingredients)
        {
            recipeSet.add(eaIngredient.getRecipe());
        }
        return recipeSet;
    }


    public IIngredientCoordinator getIngredientCoord()
    {
        return this.ingredientCoord;
    }


    public void setIngredientCoord(IIngredientCoordinator ingredientCoord)
    {
        this.ingredientCoord = ingredientCoord;
    }


    public RecipeDao getRecipeDao()
    {
        return this.recipeDao;
    }


    public void setRecipeDao(RecipeDao recipeDao)
    {
        this.recipeDao = recipeDao;
    }


    @Override
    public void saveRecipe(Recipe recipe, List<Ingredient> ingredientsList)
    {
        if(recipe != null)
        {
            this.setIngredients(recipe, ingredientsList);
            this.setUpper(recipe);
            if(recipe.getId() != 0)
            {
                this.recipeDao.updateRecipe(recipe);
            }
            else
            {
                this.recipeDao.save(recipe);
            }
        }

    }


    @Override
    public void saveRecipe(Recipe recipe)
    {
        if(recipe != null)
        {
            this.setUpper(recipe);
            if(recipe.getId() != 0)
            {
                this.recipeDao.updateRecipe(recipe);
            }
            else
            {
                this.recipeDao.save(recipe);
            }
        }

    }


    private void setIngredients(Recipe recipe, List<Ingredient> ingredientsList)
    {
        Set<Ingredient> ingredients = new HashSet<Ingredient>();
        for(Ingredient eaIngredient : ingredientsList)
        {
            eaIngredient.setItem(this.itemCoord.createOrFindItemByName(eaIngredient.getItemName()));
            eaIngredient.setRecipe(recipe);
            ingredients.add(eaIngredient);
        }
        recipe.setIngredientSet(ingredients);
    }


    private void setUpper(Recipe recipe)
    {
        if(recipe.getName() != null)
        {
            recipe.setName(recipe.getName().toUpperCase());
        }
        if(recipe.getType() != null)
        {
            recipe.setType(recipe.getType().toUpperCase());
        }
        if(recipe.getStyle() != null)
        {
            recipe.setStyle(recipe.getStyle().toUpperCase());
        }

    }


    @Override
    public void calculateIngredients(List<Recipe> recipes)
    {
        for(Recipe eaRecipe : recipes)
        {
            for(Ingredient eaIngredient : eaRecipe.getIngredientSet())
            {
                BigDecimal requiredAmount = eaIngredient.getAmount();
                MeasurementType requiredMeasurement = eaIngredient.getMeasurement();
                boolean hasEnough = false;
                for(Inventory eaInventory : eaIngredient.getItem().getInventoryList())
                {
                    if(this.measurementCoord.doesInventoryContain(eaInventory, requiredAmount, requiredMeasurement))
                    {
                        hasEnough = true;
                        break;
                    }
                }
                if(hasEnough)
                {
                    eaRecipe.setAvailableIngredientCount(eaRecipe.getAvailableIngredientCount() + 1);
                }
                else
                {
                    eaRecipe.setShortIngredientCount(eaRecipe.getShortIngredientCount() + 1);
                }
            }
        }

    }


    @Override
    public void gatherIngredients(Recipe recipe)
    {
        for(Ingredient eaIngredient : recipe.getIngredientSet())
        {
            BigDecimal requiredAmount = eaIngredient.getAmount();
            MeasurementType requiredMeasurement = eaIngredient.getMeasurement();
            for(Inventory eaInventory : eaIngredient.getItem().getInventoryList())
            {
                if(this.measurementCoord.doesInventoryContain(eaInventory, requiredAmount, requiredMeasurement))
                {
                    eaIngredient.getAvailableInventory().add(eaInventory);
                }
            }

        }
    }


    @Override
    public void decrementInventory(Map<String, Inventory> inventoryToDecrement)
    {
        for(Map.Entry<String, Inventory> eaEntry : inventoryToDecrement.entrySet())
        {
            Ingredient ingredient = this.ingredientDao.findOne(Long.valueOf(eaEntry.getKey()));
            if(ingredient != null)
            {
                AdjustmentBean bean = new AdjustmentBean();
                bean.setAdjustmentType(SUBTRACT_IDENTIFIER_STRING);
                bean.setQuantityAdjust(ingredient.getAmount());
                bean.setMeasurementAdjust(ingredient.getMeasurement());
                this.measurementCoord.adjustInventory(eaEntry.getValue(), bean);
                this.inventoryDao.save(eaEntry.getValue());
            }
        }

    }

}
