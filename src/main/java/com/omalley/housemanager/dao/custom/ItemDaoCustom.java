package com.omalley.housemanager.dao.custom;

import java.util.List;

import com.omalley.housemanager.domain.Item;

public interface ItemDaoCustom
{
    List<Item> findLikeItem(String name);


    void updateItem(Item item);


    List<Item> returnAllItems();


    List<Item> returnAllEssentials();
}
