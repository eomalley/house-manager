package com.omalley.housemanager.coordinators;

import java.util.List;

import com.omalley.housemanager.domain.Ingredient;
import com.omalley.housemanager.domain.Item;

public interface IIngredientCoordinator
{
	public List<Ingredient> getIngredientsForItem(Item name);
}
