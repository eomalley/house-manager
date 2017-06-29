package com.omalley.housemanager.coordinators;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import org.junit.Assert;
import org.junit.Ignore;
import org.junit.Test;

import com.omalley.housemanager.coordinators.impl.ItemCoordinator;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Item;
import com.omalley.housemanager.domain.MeasurementType;

@Ignore("something is up with loading the app, see measurement coord test as well")
public class ItemCoordinatorTest
{

    @Test
    public void testGetBelowThresholdItems()
    {

        IItemCoordinator itemCoord = new ItemCoordinator();
        // itemCoord.setMeasurementCoordinator(new MeasurementCoordinator());
        Item item = new Item();
        item.setEssential(false);
        item.setThreshold(BigDecimal.valueOf(1.0));
        item.setFavoriteBrand("BINGBONG");
        item.setId(1);
        item.setName("ANITEM1");

        Inventory inventory = new Inventory();
        inventory.setItem(item);
        inventory.setMeasurement(MeasurementType.CUP);
        inventory.setQuantity(BigDecimal.valueOf(2.0));
        inventory.setBrand("BINGBONG");

        Item item2 = new Item();
        item2.setEssential(true);
        item2.setThreshold(BigDecimal.valueOf(1.0));
        item2.setFavoriteBrand("BINGBONG2");
        item2.setId(1);
        item2.setName("ANITEM2");

        Inventory inventory2 = new Inventory();
        inventory2.setItem(item2);
        inventory2.setMeasurement(MeasurementType.CUP);
        inventory2.setQuantity(BigDecimal.valueOf(2.0));
        inventory2.setBrand("BINGBONG2");

        item.setInventoryList(new HashSet<Inventory>());
        item.getInventoryList().add(inventory);

        item2.setInventoryList(new HashSet<Inventory>());
        item2.getInventoryList().add(inventory2);

        List<Item> newItems = itemCoord.getBelowThresholdItems(Arrays.asList(item, item2));

        Assert.assertTrue(1 == newItems.size());

    }
}
