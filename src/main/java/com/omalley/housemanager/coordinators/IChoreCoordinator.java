package com.omalley.housemanager.coordinators;

import java.util.Date;
import java.util.List;
import java.util.Map;

import com.omalley.housemanager.domain.Chore;
import com.omalley.housemanager.domain.ChoreHistory;

public interface IChoreCoordinator
{
    public void updateChore(Chore chore);


    public void completeChore(Chore chore);


    public Map<String, List<ChoreHistory>> getScores(Date from, Date to);
}
