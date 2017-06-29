package com.omalley.housemanager.dao.custom.impl;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ScratchDaoCustom;
import com.omalley.housemanager.domain.Scratch;

@Transactional
public class ScratchDaoCustomImpl implements ScratchDaoCustom
{
    @PersistenceContext
    EntityManager em;


    @SuppressWarnings("unchecked")
    @Override
    public Scratch getScratch()
    {
        List<Scratch> scratch = this.em.createQuery("FROM Scratch").getResultList();
        if(!scratch.isEmpty())
        {
            return scratch.get(0);
        }
        else
        {
            return null;
        }
    }


    @Override
    public void updateScratch(Scratch scratch)
    {
        this.em.merge(scratch);
        this.em.flush();
    }

}
