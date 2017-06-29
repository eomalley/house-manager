package com.omalley.housemanager.coordinators.impl;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.omalley.housemanager.coordinators.IItemCoordinator;
import com.omalley.housemanager.coordinators.IMeasurementCoordinator;
import com.omalley.housemanager.dao.ItemDao;
import com.omalley.housemanager.domain.AdjustmentBean;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;
import com.omalley.housemanager.domain.MeasurementType;

@Component
public class ItemCoordinator implements IItemCoordinator
{

    @Autowired
    ItemDao itemDao;

    @Autowired
    IMeasurementCoordinator measurementCoord;

    public static final class SortByName implements Comparator<Item>
    {
        @Override
        public int compare(Item s1, Item s2)
        {
            return s1.getName().toLowerCase().compareTo(s2.getName().toLowerCase());
        }
    }


    @Override
    public Item createOrFindItemByName(String name)
    {
        List<Item> names = this.itemDao.findByName(name);
        Item item;
        if(!names.isEmpty())
        {
            return names.get(0);
        }
        else
        {
            item = new Item();
            item.setName(name);
            this.itemDao.save(item);
        }
        return item;
    }


    @Override
    public void saveItem(Item item)
    {
        if(item != null)
        {
            Item update = this.itemDao.findOne(item.getId());
            if(update != null)
            {
                if(item.getFavoriteBrand() != null)
                {
                    update.setFavoriteBrand(item.getFavoriteBrand().toUpperCase());
                }
                update.setName(item.getName().toUpperCase());
                update.setEssential(item.isEssential());
                if(item.isEssential())
                {
                    update.setThreshold(item.getThreshold());
                    update.setMeasurement(item.getMeasurement());
                }
                this.itemDao.updateItem(update);
            }
            else
            {
                this.itemDao.save(item);
            }
        }
    }


    @Override
    public List<Item> getBelowThresholdItems(List<Item> items)
    {
        List<Item> setItems = this.getItemInventoryQuantities(items);
                
        List<Item> itemsBelowThreshold = new ArrayList<Item>();
        for(Item eaItem : setItems)
        {
            BigDecimal threshold;
            if(MeasurementCoordinator.convertableTypes.contains(eaItem.getMeasurement()))
            {
                threshold = this.measurementCoord.getMlValue(eaItem.getThreshold(), eaItem.getMeasurement());
                if((threshold.compareTo(eaItem.getConvertableQuantity())) > 0)
                {
                    itemsBelowThreshold.add(eaItem);
                }

            }
            else
            {
                threshold = eaItem.getThreshold();
                BigDecimal nonConvertableQuantity = eaItem.getNonConvertableQuantity().get(eaItem.getMeasurement());
                if(nonConvertableQuantity == null || (threshold.compareTo(nonConvertableQuantity)) > 0)
                {
                    itemsBelowThreshold.add(eaItem);
                }
            }
        }

        return itemsBelowThreshold;
    }


    @Override
    public List<Item> getItemInventoryQuantities(List<Item> items)
    {
        List<Item> setItems = new ArrayList<Item>();
        for(Item eaItem : items)
        {
            Map<MeasurementType, BigDecimal> nonConvertMap = new HashMap<MeasurementType, BigDecimal>();
            BigDecimal convertableQuantity = BigDecimal.ZERO;
            if(eaItem.getInventoryList().isEmpty())
            {
            	this.setItemInventoryQuantities(eaItem, nonConvertMap, convertableQuantity);
            }
            for(Inventory eaInv : eaItem.getInventoryList())
            {
                if(this.measurementCoord.isInventoryConvertable(eaInv))
                {
                    convertableQuantity =
                            convertableQuantity.add(this.measurementCoord.getMlValue(eaInv.getQuantity(),
                                                                                     eaInv.getMeasurement()));
                }
                else
                {
                    BigDecimal type = nonConvertMap.get(eaInv.getMeasurement());
                    if(type == null)
                    {
                        nonConvertMap.put(eaInv.getMeasurement(), eaInv.getQuantity());
                    }
                    else
                    {
                        nonConvertMap.put(eaInv.getMeasurement(), type.add(eaInv.getQuantity()));
                    }

                }
                this.setItemInventoryQuantities(eaItem, nonConvertMap, convertableQuantity);

            }
            setItems.add(eaItem);
        }
        return setItems;
    }


    private void setItemInventoryQuantities(Item eaItem,
                                            Map<MeasurementType, BigDecimal> nonConvertMap,
                                            BigDecimal convertableQuantity)
    {
        AdjustmentBean adjBean = this.measurementCoord.getNewMeasurementValues(convertableQuantity);
        eaItem.setConvertableQuantity(adjBean.getQuantityAdjust());
        eaItem.setConvertableQuantityMeasurement(adjBean.getMeasurementAdjust());
        eaItem.setNonConvertableQuantity(nonConvertMap);
    }

}
