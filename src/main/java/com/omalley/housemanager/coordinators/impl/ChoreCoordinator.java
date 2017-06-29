package com.omalley.housemanager.coordinators.impl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omalley.housemanager.coordinators.IChoreCoordinator;
import com.omalley.housemanager.dao.ChoreDao;
import com.omalley.housemanager.dao.ChoreHistoryDao;
import com.omalley.housemanager.domain.Chore;
import com.omalley.housemanager.domain.ChoreHistory;

@Component
public class ChoreCoordinator implements IChoreCoordinator
{

    @Autowired
    ChoreDao choreDao;

    @Autowired
    ChoreHistoryDao choreHistoryDao;


    @Override
    public void updateChore(Chore chore)
    {
        this.setUpper(chore);
        if(chore.getId() != 0)
        {
            this.choreDao.updateChore(chore);
        }
        else
        {
            this.choreDao.save(chore);
        }

    }


    private void setUpper(Chore chore)
    {
        if(chore.getName() != null)
        {
            chore.setName(chore.getName().toUpperCase());
        }
        if(chore.getLastCompletedBy() != null)
        {
            chore.setLastCompletedBy(chore.getLastCompletedBy().toUpperCase());
        }
        if(chore.getType() != null)
        {
            chore.setType(chore.getType().toUpperCase());
        }

    }


    @Override
    public void completeChore(Chore chore)
    {
        this.updateChore(chore);
        ChoreHistory hist = new ChoreHistory();
        hist.setChore(chore);
        hist.setLastDone(chore.getLastCompleted());
        hist.setLastDoneBy(chore.getLastCompletedBy());
        hist.setDateEntered(new Date());
        this.choreHistoryDao.save(hist);

    }


    @Override
    public Map<String, List<ChoreHistory>> getScores(Date from, Date to)
    {
        List<ChoreHistory> history = this.choreHistoryDao.getChoresBetween(from, to);
        Map<String, List<ChoreHistory>> map = new HashMap<String, List<ChoreHistory>>();
        for(ChoreHistory eaHist : history)
        {
            String name = eaHist.getLastDoneBy();
            List<ChoreHistory> list = map.get(name);
            if(list != null)
            {
                list.add(eaHist);
            }
            else
            {
                List<ChoreHistory> newList = new ArrayList<ChoreHistory>();
                newList.add(eaHist);
                map.put(name, newList);
            }
        }
        return map;
    }
}
