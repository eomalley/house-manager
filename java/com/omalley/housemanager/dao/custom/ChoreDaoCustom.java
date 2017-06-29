package com.omalley.housemanager.dao.custom;

import java.util.List;

import com.omalley.housemanager.domain.Chore;

public interface ChoreDaoCustom
{
    public List<Chore> returnAllItems();


    List<String> findLikeType(String hint);


    void updateChore(Chore chore);

}
