package com.omalley.housemanager.dao.custom;

import java.util.List;

import com.omalley.housemanager.domain.Recipe;

public interface RecipeDaoCustom
{

    public List<Recipe> returnAllItems();


    void updateRecipe(Recipe recipe);


    List<String> findLikeType(String hint);


    List<String> findLikeStyle(String hint);

}
