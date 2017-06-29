package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.InventoryDaoCustom;
import com.omalley.housemanager.domain.Inventory;

@Transactional
public class InventoryDaoCustomImpl implements InventoryDaoCustom
{
    @PersistenceContext
    EntityManager em;


    @SuppressWarnings("unchecked")
    @Override
    public List<Inventory> findAllZeroQuantityItems()
    {
        return this.em.createQuery("SELECT u FROM Inventory u where u.quantity < 1").getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Inventory> returnAllItems()
    {
        return this.em.createQuery("FROM Inventory").getResultList();
    }


    @Override
    public void updateInventory(Inventory inventory)
    {
        this.em.merge(inventory);
        this.em.flush();

    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> findLikeBrandName(String hint)
    {

        return this.em.createQuery("SELECT DISTINCT(u.brand) FROM Inventory u where UPPER(u.brand) LIKE :name").setParameter("name",
                                                                                                                             hint.toUpperCase()
                                                                                                                                     + "%").getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> findLikeTypeName(String hint)
    {

        return this.em.createQuery("SELECT DISTINCT(u.type) FROM Inventory u where UPPER(u.type) LIKE :name").setParameter("name",
                                                                                                                           hint.toUpperCase()
                                                                                                                                   + "%").getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> findLikeMeasurementName(String hint)
    {

        return this.em.createQuery("SELECT DISTINCT(u.measurement) FROM Inventory u where UPPER(u.measurement) LIKE :name").setParameter("name",
                                                                                                                                         hint.toUpperCase()
                                                                                                                                                 + "%").getResultList();
    }
}
