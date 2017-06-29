package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.RecipeDaoCustom;
import com.omalley.housemanager.domain.Recipe;

@Transactional
public class RecipeDaoCustomImpl implements RecipeDaoCustom
{
    @PersistenceContext
    EntityManager em;


    @SuppressWarnings("unchecked")
    @Override
    public List<Recipe> returnAllItems()
    {
        return this.em.createQuery("FROM Recipe").getResultList();
    }


    @Override
    public void updateRecipe(Recipe recipe)
    {
        this.em.merge(recipe);
        this.em.flush();

    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> findLikeType(String hint)
    {

        return this.em.createQuery("SELECT DISTINCT(u.type) FROM Recipe u where UPPER(u.type) LIKE :name").setParameter("name",
                                                                                                                        hint.toUpperCase()
                                                                                                                                + "%").getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> findLikeStyle(String hint)
    {

        return this.em.createQuery("SELECT DISTINCT(u.style) FROM Recipe u where UPPER(u.style) LIKE :name").setParameter("name",
                                                                                                                          hint.toUpperCase()
                                                                                                                                  + "%").getResultList();
    }
}
