package com.omalley.housemanager.coordinators;

import com.omalley.housemanager.domain.GroceryItem;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;

public interface IGroceryItemCoordinator
{

    void saveGroceryItem(GroceryItem groceryItemParams);

    void handleEntryFromInventory(GroceryItem groceryItemParams);

    void handleEntryFromItem(GroceryItem groceryItemParams);
}
