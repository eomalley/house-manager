package com.omalley.housemanager.dao;



import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.omalley.housemanager.domain.Ingredient;
import com.omalley.housemanager.domain.Item;
import com.omalley.housemanager.domain.Recipe;



public interface IngredientDao extends CrudRepository<Ingredient, Long>
{
	List<Ingredient> findByRecipe(Recipe recipe);
	List<Ingredient> findByItem(Item inventory);
}

