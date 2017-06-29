package com.omalley.housemanager.dao.custom;

import java.util.List;

import com.omalley.housemanager.domain.GroceryItem;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;

public interface GroceryItemDaoCustom
{

    void updateGroceryItem(GroceryItem groceryItem);


    List<GroceryItem> returnAllGroceryItems();

    GroceryItem findByInventoryAndItem(Inventory inventory, Item item);

}
