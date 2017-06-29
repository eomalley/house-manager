package com.omalley.housemanager.coordinators.impl;

import java.util.Comparator;
import java.util.List;

import com.omalley.housemanager.dao.InventoryDao;
import com.omalley.housemanager.dao.ItemDao;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;
import org.springframework.util.Assert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omalley.housemanager.coordinators.IGroceryItemCoordinator;
import com.omalley.housemanager.dao.GroceryItemDao;
import com.omalley.housemanager.domain.GroceryItem;

@Component
public class GroceryItemCoordinator implements IGroceryItemCoordinator
{
    @Autowired
    GroceryItemDao groceryDao;

    @Autowired
    ItemDao itemDao;

    @Autowired
    InventoryDao inventoryDao;

    public static final class SortByItemName implements Comparator<GroceryItem>
    {

        @Override
        public int compare(GroceryItem groceryItem1, GroceryItem groceryItem2)
        {
            return groceryItem1.getItem().getName().toLowerCase().compareTo(groceryItem2.getItem().getName().toLowerCase());
        }

    }


    @Override
    public void saveGroceryItem(GroceryItem groceryItemParams)
    {
        Assert.notNull(groceryItemParams.getItemName(), "Can't make grocery list entry without item");

        if(groceryItemParams.getId() != 0)
        {
            this.groceryDao.updateGroceryItem(groceryItemParams);
        } else {
            List<Item> item = this.itemDao.findByName(groceryItemParams.getItemName());
            Assert.notNull(item, "Could not find existing item for entry");
            Assert.isTrue(item.size() == 1, "Duplicate Item names; corrupt data");
            groceryItemParams.setItem(item.get(0));
            if(groceryItemParams.getInventory() != null)
            {//this is dumb, search for item > search for grocery items for that item, if none create new, if some check & compare with inventory inc
                this.handleEntryFromInventory(groceryItemParams);
            } else {
                this.handleEntryFromItem(groceryItemParams);
            }
        }








    }


    @Override public void handleEntryFromInventory(GroceryItem groceryItemParams)
    {
        List<GroceryItem> items = groceryDao.findByInventory(groceryItemParams.getInventory());
        if(items.isEmpty()) {
            this.handleEntryFromItem(groceryItemParams);
        }
    }


    @Override public void handleEntryFromItem(GroceryItem groceryItemParams)
    {
        if(groceryItemParams.getSpecificBrand() != null || !groceryItemParams.getSpecificBrand().isEmpty())
        {
            List<Inventory> inventories = this.inventoryDao.findByBrand(groceryItemParams.getSpecificBrand());
            if(!inventories.isEmpty()) {
                for(Inventory eaInventory : inventories)
                {
                    if(eaInventory.getItem().equals(groceryItemParams.getItem()))
                    {
                        //something??
                    }
                }

            }
        }
        List<GroceryItem> items = groceryDao.findByItem(groceryItemParams.getItem());







        //at some point handle going to inv if there is a specific brand w/ inventory entry
    }
}
