package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;
import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.GroceryItemDaoCustom;
import com.omalley.housemanager.domain.GroceryItem;

@Transactional
public class GroceryItemDaoCustomImpl implements GroceryItemDaoCustom
{
    @PersistenceContext
    EntityManager em;


    @Override
    public void updateGroceryItem(GroceryItem groceryItem)
    {
        this.em.merge(groceryItem);
        this.em.flush();

    }


    @SuppressWarnings("unchecked")
    @Override
    public List<GroceryItem> returnAllGroceryItems()
    {
        return this.em.createQuery("FROM GroceryItem").getResultList();
    }


    @Override
    public GroceryItem findByInventoryAndItem(Inventory inventory, Item item)
    {
        return (GroceryItem) this.em.createQuery("SELECT u FROM GroceryItem u where u.item.name = :itemName and u.inventory.brand = :invBrand").setParameter("itemName", item.getName()).setParameter("invBrand", inventory.getBrand()).getSingleResult();
    }
}
