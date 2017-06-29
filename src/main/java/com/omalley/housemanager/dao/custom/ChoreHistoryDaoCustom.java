package com.omalley.housemanager.dao.custom;

import java.util.Date;
import java.util.List;

import com.omalley.housemanager.domain.ChoreHistory;

public interface ChoreHistoryDaoCustom
{
    public List<ChoreHistory> getChoresForTimeFrame(Date date);


    public List<ChoreHistory> getChoresBetween(Date from, Date to);
}
