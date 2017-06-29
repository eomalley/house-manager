package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ItemDaoCustom;
import com.omalley.housemanager.domain.Item;

@Transactional
public class ItemDaoCustomImpl implements ItemDaoCustom
{
    @PersistenceContext
    EntityManager em;


    @SuppressWarnings("unchecked")
    @Override
    public List<Item> findLikeItem(String name)
    {
        return this.em.createQuery("SELECT u FROM Item u where UPPER(u.name) LIKE :name").setParameter("name",
                                                                                                       name.toUpperCase()
                                                                                                               + "%").getResultList();
    }


    @Override
    public void updateItem(Item item)
    {
        this.em.merge(item);
        this.em.flush();

    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Item> returnAllItems()
    {
        return this.em.createQuery("FROM Item").getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<Item> returnAllEssentials()
    {
        return this.em.createQuery("SELECT u FROM Item u where u.essential = :bool").setParameter("bool", true).getResultList();
    }
}
