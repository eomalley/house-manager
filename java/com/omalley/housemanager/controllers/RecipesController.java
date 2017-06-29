package com.omalley.housemanager.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.type.MapType;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.omalley.housemanager.coordinators.IRecipeCoordinator;
import com.omalley.housemanager.coordinators.impl.RecipeCoordinator;
import com.omalley.housemanager.dao.RecipeDao;
import com.omalley.housemanager.domain.Ingredient;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Recipe;

@Controller
public class RecipesController
{

    @Autowired
    RecipeDao recipeDao;

    @Autowired
    IRecipeCoordinator recipeCoord;


    @ResponseBody
    @RequestMapping(value = "/recipe/getAll", method = RequestMethod.GET)
    public List<Recipe> getAllRecipes()
    {
        List<Recipe> recipes = this.recipeDao.returnAllItems();
        this.recipeCoord.calculateIngredients(recipes);
        Collections.sort(recipes, new RecipeCoordinator.SortByName());
        return recipes;
    }


    @ResponseBody
    @RequestMapping(value = "/recipeDetails/edit", method = RequestMethod.GET)
    public Recipe editDetails(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Recipe recipe = this.recipeDao.findOne(convertedId);
        return recipe;
    }


    @ResponseBody
    @RequestMapping(value = "/recipeDetails/view", method = RequestMethod.GET)
    public Recipe showDetails(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Recipe recipe = this.recipeDao.findOne(convertedId);
        this.recipeCoord.gatherIngredients(recipe);
        return recipe;
    }


    @ResponseBody
    @RequestMapping(value = "/recipeDetails/view/make", method = RequestMethod.POST)
    public void recipeMake(@RequestBody Map<String, String> recipePayload) throws JsonProcessingException, IOException
    {
        if(recipePayload != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            TypeFactory typeFactory = mapper.getTypeFactory();
            MapType mapType = typeFactory.constructMapType(HashMap.class, String.class, Inventory.class);
            Map<String, Inventory> inventoryToDecrement = mapper.readValue(recipePayload.get("decrement"), mapType);

            this.recipeCoord.decrementInventory(inventoryToDecrement);

            Recipe recipe = mapper.readValue(recipePayload.get("recipeParams"), Recipe.class);
            Recipe dbRecipe = this.recipeDao.findOne(recipe.getId());
            dbRecipe.setTimesMade(dbRecipe.getTimesMade() + 1);
            dbRecipe.setLastMade(new Date());
            this.recipeCoord.saveRecipe(dbRecipe);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/recipe", method = RequestMethod.POST)
    public void recipeUpdate(@RequestBody Map<String, String> recipePayload) throws JsonProcessingException,
        IOException
    {
        if(recipePayload != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            List<Ingredient> ingredientsList =
                    Arrays.asList(mapper.readValue(recipePayload.get("ingredients"), Ingredient[].class));

            Recipe recipe = mapper.readValue(recipePayload.get("recipeParams"), Recipe.class);

            this.recipeCoord.saveRecipe(recipe, ingredientsList);
        }

    }


    @ResponseBody
    @RequestMapping(value = "/recipe", method = RequestMethod.DELETE)
    public void deleteRecipe(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Recipe recipe = this.recipeDao.findOne(convertedId);
        if(recipe != null)
        {
            this.recipeDao.delete(recipe);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/recipe/getRecipeTypes", method = RequestMethod.GET)
    public List<String> getRecipeTypes(@RequestParam String hint)
    {
        return this.recipeDao.findLikeType(hint);
    }


    @ResponseBody
    @RequestMapping(value = "/recipe/getRecipeStyles", method = RequestMethod.GET)
    public List<String> getRecipeStyles(@RequestParam String hint)
    {
        return this.recipeDao.findLikeStyle(hint);
    }
}
