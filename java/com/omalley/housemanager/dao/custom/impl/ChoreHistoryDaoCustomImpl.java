package com.omalley.housemanager.dao.custom.impl;

import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TemporalType;

import org.springframework.transaction.annotation.Transactional;

import com.omalley.housemanager.dao.custom.ChoreHistoryDaoCustom;
import com.omalley.housemanager.domain.ChoreHistory;

@Transactional
public class ChoreHistoryDaoCustomImpl implements ChoreHistoryDaoCustom
{
    @PersistenceContext
    EntityManager em;


    @SuppressWarnings("unchecked")
    @Override
    public List<ChoreHistory> getChoresForTimeFrame(Date date)
    {
        return this.em.createQuery("SELECT u FROM ChoreHistory u WHERE u.lastDone > :date").setParameter("date", date).getResultList();
    }


    @SuppressWarnings("unchecked")
    @Override
    public List<ChoreHistory> getChoresBetween(Date from, Date to)
    {
        return this.em.createQuery("SELECT u FROM ChoreHistory u WHERE u.lastDone BETWEEN :startDate AND :endDate").setParameter("startDate",
                                                                                                                                 from,
                                                                                                                                 TemporalType.DATE).setParameter("endDate",
                                                                                                                                                                 to,
                                                                                                                                                                 TemporalType.DATE).getResultList();
    }

}
