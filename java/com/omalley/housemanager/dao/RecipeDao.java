package com.omalley.housemanager.dao;

import java.util.Date;
import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.RecipeDaoCustom;
import com.omalley.housemanager.domain.Recipe;

@Transactional
@Repository(value = "recipeDao")
public interface RecipeDao extends CrudRepository<Recipe, Long>, RecipeDaoCustom
{
    List<Recipe> findByName(String name);


    List<Recipe> findByType(String type);


    List<Recipe> findByLastMade(Date lastMade);


    List<Recipe> findByTimesMade(String timesMade);

}
