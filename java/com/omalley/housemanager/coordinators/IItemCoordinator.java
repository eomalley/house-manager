package com.omalley.housemanager.coordinators;

import java.util.List;

import com.omalley.housemanager.domain.Item;

public interface IItemCoordinator
{
    public Item createOrFindItemByName(String name);


    public void saveItem(Item itemParams);


    public List<Item> getBelowThresholdItems(List<Item> items);


    List<Item> getItemInventoryQuantities(List<Item> items);

}
