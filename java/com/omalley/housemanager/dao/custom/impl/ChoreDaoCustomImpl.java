package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ChoreDaoCustom;
import com.omalley.housemanager.domain.Chore;

@Transactional
public class ChoreDaoCustomImpl implements ChoreDaoCustom
{
    @PersistenceContext
    EntityManager em;


    @SuppressWarnings("unchecked")
    @Override
    public List<Chore> returnAllItems()
    {
        return this.em.createQuery("FROM Chore").getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<String> findLikeType(String hint)
    {
        return this.em.createQuery("SELECT DISTINCT(u.type) FROM Chore u where UPPER(u.type) LIKE :name").setParameter("name",
                                                                                                                       hint.toUpperCase()
                                                                                                                               + "%").getResultList();
    }


    @Override
    public void updateChore(Chore chore)
    {
        this.em.merge(chore);
        this.em.flush();

    }

}
