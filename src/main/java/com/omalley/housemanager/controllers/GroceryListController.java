package com.omalley.housemanager.controllers;

import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omalley.housemanager.coordinators.IGroceryItemCoordinator;
import com.omalley.housemanager.coordinators.impl.GroceryItemCoordinator;
import com.omalley.housemanager.dao.GroceryItemDao;
import com.omalley.housemanager.domain.GroceryItem;

@Controller
public class GroceryListController
{
    @Autowired
    GroceryItemDao groceryDao;

    @Autowired
    IGroceryItemCoordinator groceryCoord;


    @ResponseBody
    @RequestMapping(value = "/groceryList", method = RequestMethod.GET)
    public GroceryItem getAnItem(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        return this.groceryDao.findOne(convertedId);
    }


    @ResponseBody
    @RequestMapping(value = "/groceryList/getAll", method = RequestMethod.GET)
    public List<GroceryItem> getAllItems()
    {
        List<GroceryItem> items = this.groceryDao.returnAllGroceryItems();
        Collections.sort(items, new GroceryItemCoordinator.SortByItemName());
        return items;
    }


    @ResponseBody
    @RequestMapping(value = "/groceryList/markOff", method = RequestMethod.GET)
    public void markOffGroceryItem()
    {
        // TODO
    }


    @ResponseBody
    @RequestMapping(value = "/groceryList/getStores", method = RequestMethod.POST)
    public List<String> getLikeGroceryStores(String hint)
    {
        // TODO
        return null;
    }


    @ResponseBody
    @RequestMapping(value = "/groceryList", method = RequestMethod.POST)
    public void groceryItemUpdate(@RequestBody GroceryItem groceryItemParams)
    {

        if(groceryItemParams != null)
        {

            this.groceryCoord.saveGroceryItem(groceryItemParams);

        }

    }


    @ResponseBody
    @RequestMapping(value = "/groceryList", method = RequestMethod.DELETE)
    public void deleteGroceryListItem(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        GroceryItem item = this.groceryDao.findOne(convertedId);
        if(item != null)
        {
            this.groceryDao.delete(item);
        }
    }

}
