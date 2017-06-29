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

import com.omalley.housemanager.coordinators.IItemCoordinator;
import com.omalley.housemanager.coordinators.impl.ItemCoordinator;
import com.omalley.housemanager.dao.ItemDao;
import com.omalley.housemanager.domain.Item;

@Controller
public class ItemsController
{
    @Autowired
    ItemDao itemDao;

    @Autowired
    IItemCoordinator itemCoord;


    @ResponseBody
    @RequestMapping(value = "/item/getLikeItems", method = RequestMethod.GET)
    public Iterable<Item> getLikeItems(@RequestParam String hint)
    {
        return this.itemDao.findLikeItem(hint);
    }


    @ResponseBody
    @RequestMapping(value = "/item", method = RequestMethod.GET)
    public Item getAnItem(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        return this.itemDao.findOne(convertedId);
    }


    @ResponseBody
    @RequestMapping(value = "/item/getAll", method = RequestMethod.GET)
    public List<Item> getAllItems()
    {
        List<Item> items = this.itemCoord.getItemInventoryQuantities(this.itemDao.returnAllItems());
        Collections.sort(items, new ItemCoordinator.SortByName());
        return items;
    }


    @ResponseBody
    @RequestMapping(value = "/item/lowEssentials", method = RequestMethod.GET)
    public List<Item> getEssentialsBelowThreshold()
    {

        List<Item> items = this.itemCoord.getBelowThresholdItems(this.itemDao.returnAllEssentials());
        Collections.sort(items, new ItemCoordinator.SortByName());
        return items;
    }


    @ResponseBody
    @RequestMapping(value = "/item/lowEssentials", method = RequestMethod.POST)
    public void addAllToGroceryList()
    {
        // TODO

    }


    @ResponseBody
    @RequestMapping(value = "/item", method = RequestMethod.POST)
    public void itemUpdate(@RequestBody Item itemParams)
    {

        if(itemParams != null)
        {

            this.itemCoord.saveItem(itemParams);

        }

    }


    @ResponseBody
    @RequestMapping(value = "/item", method = RequestMethod.DELETE)
    public void deleteAnItem(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Item item = this.itemDao.findOne(convertedId);
        if(item != null && item.getInventoryList().isEmpty())
        {
            this.itemDao.delete(item);
        }
    }

}
