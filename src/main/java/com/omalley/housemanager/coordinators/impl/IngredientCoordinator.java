package com.omalley.housemanager.coordinators.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omalley.housemanager.coordinators.IIngredientCoordinator;
import com.omalley.housemanager.coordinators.IInventoryCoordinator;
import com.omalley.housemanager.dao.IngredientDao;
import com.omalley.housemanager.dao.ItemDao;
import com.omalley.housemanager.domain.Ingredient;
import com.omalley.housemanager.domain.Item;

@Component
public class IngredientCoordinator implements IIngredientCoordinator
{
	
	@Autowired
	IngredientDao ingredientDao;
	
	@Autowired
	IInventoryCoordinator invenCoord;
	
	@Autowired
	ItemDao itemDao;	

	@Override
	public List<Ingredient> getIngredientsForItem(Item name)
	{
		
		List<Ingredient> ingredients = null;
		if(name != null) {
			ingredients = this.getIngredientDao().findByItem(name);
		}
		return ingredients;
	}

	public IngredientDao getIngredientDao()
	{
		return ingredientDao;
	}

	public void setIngredientDao(IngredientDao ingredientDao)
	{
		this.ingredientDao = ingredientDao;
	}

	public IInventoryCoordinator getInvenCoord()
	{
		return invenCoord;
	}

	public void setInvenCoord(IInventoryCoordinator invenCoord)
	{
		this.invenCoord = invenCoord;
	}

}
