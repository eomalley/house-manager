package com.omalley.housemanager.controllers;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.omalley.housemanager.coordinators.IInventoryCoordinator;
import com.omalley.housemanager.coordinators.IItemCoordinator;
import com.omalley.housemanager.coordinators.IMeasurementCoordinator;
import com.omalley.housemanager.coordinators.impl.InventoryCoordinator;
import com.omalley.housemanager.dao.InventoryDao;
import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Inventory;

@Controller
public class InventoryController
{
    @Autowired
    IInventoryCoordinator inventoryCoord;

    @Autowired
    InventoryDao inventoryDao;

    @Autowired
    IItemCoordinator itemCoord;

    @Autowired
    IMeasurementCoordinator measurementCoord;


    @ResponseBody
    @RequestMapping(value = "/inventory/getAll", method = RequestMethod.GET)
    public List<Inventory> getAllInventory()
    {
        List<Inventory> inv = this.inventoryDao.returnAllItems();
        Collections.sort(inv, new InventoryCoordinator.SortInventoryByItemName());
        return inv;
    }


    @ResponseBody
    @RequestMapping(value = "/inventory/getBrandNames", method = RequestMethod.GET)
    public List<String> getBrandNames(@RequestParam String hint)
    {
        return this.inventoryDao.findLikeBrandName(hint);
    }


    @ResponseBody
    @RequestMapping(value = "/inventory/getTypeNames", method = RequestMethod.GET)
    public List<String> getTypeNames(@RequestParam String hint)
    {
        return this.inventoryDao.findLikeTypeName(hint);
    }


    @ResponseBody
    @RequestMapping(value = "/inventory/getMeasurementNames", method = RequestMethod.GET)
    public List<String> getMeasurementNames(@RequestParam String hint)
    {
        return this.inventoryDao.findLikeMeasurementName(hint);
    }


    @ResponseBody
    @RequestMapping(value = "/inventory", method = RequestMethod.POST)
    public void inventoryUpdate(@RequestBody Map<String, String> inventoryPayload) throws JsonProcessingException,
        IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        Inventory inventoryParams = mapper.readValue(inventoryPayload.get("inventoryParams"), Inventory.class);
        AdjustmentBean adjustmentParams =
                mapper.readValue(inventoryPayload.get("adjustmentParams"), AdjustmentBean.class);
        this.processInventoryParams(inventoryParams, adjustmentParams);

    }


    @ResponseBody
    @RequestMapping(value = "/inventory", method = RequestMethod.GET)
    public Inventory showDetails(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Inventory inv = this.inventoryDao.findOne(convertedId);
        if(inv != null)
        {
            inv.setLiteralItemName(inv.getItem().getName());
        }
        return inv;
    }


    @ResponseBody
    @RequestMapping(value = "/inventory", method = RequestMethod.DELETE)
    public void deleteInventory(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Inventory inv = this.inventoryDao.findOne(convertedId);
        if(inv != null)
        {
            this.inventoryDao.delete(inv);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/inventory/addMultiple", method = RequestMethod.POST)
    public HttpStatus addInventoryList(@RequestBody Map<String, String> inventoryPayload, Authentication auth)
        throws JsonProcessingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        List<Inventory> inventoryList =
                Arrays.asList(mapper.readValue(inventoryPayload.get("inventoryList"), Inventory[].class));
        String user = auth.getName();
        if(user == null || user.isEmpty())
        {
            return HttpStatus.FORBIDDEN;
        }
        for(Inventory eaInv : inventoryList)
        {
            eaInv.setLastUpdBy(user);
            this.processInventoryParams(eaInv, null);
        }
        return HttpStatus.OK;

    }


    private void processInventoryParams(Inventory inventory, AdjustmentBean params)
    {

        if(inventory.getLiteralItemName() != null)
        {

            inventory.setItem(this.itemCoord.createOrFindItemByName(inventory.getLiteralItemName()));

            this.inventoryCoord.saveInventory(inventory, params);

        }
    }

}
