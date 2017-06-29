package com.omalley.housemanager.controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.Collections;
import java.util.List;

import javax.imageio.ImageIO;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.omalley.housemanager.coordinators.IInventoryCoordinator;
import com.omalley.housemanager.dao.RecipeDao;
import com.omalley.housemanager.domain.Inventory;
import com.omalley.housemanager.domain.Recipe;

@Controller
public class FileController
{
    @Autowired
    RecipeDao recipeDao;

    @Autowired
    IInventoryCoordinator inventoryCoord;


    @RequestMapping(value = "/upload/recipe/{id}", method = RequestMethod.GET)
    public @ResponseBody byte[] getRecipeImage(@PathVariable("id") String id)
    {
        Recipe recipe = this.recipeDao.findOne(Long.valueOf(id));
        if(recipe != null && recipe.getImage() != null && !recipe.getImage().isEmpty())
        {

            try
            {
            	String sep = File.separator;
                String path = System.getProperty("user.dir");
                BufferedImage image = ImageIO.read(new File(path + sep + "images" + sep + recipe.getImage()));
                int fileTypeStart = recipe.getImage().lastIndexOf('.');
                String fileType = recipe.getImage().substring(fileTypeStart + 1);
                ByteArrayOutputStream bao = new ByteArrayOutputStream();
                ImageIO.write(image, fileType, bao);

                byte[] imageBytes = bao.toByteArray();
                return imageBytes;

            }
            catch(Exception e)
            {

            }
        }
        return null;
    }


    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody HttpStatus handleFileUpload(@RequestParam("id") String id,
                                                     @RequestParam("file") MultipartFile file)
    {
        if(!file.isEmpty())
        {
            Recipe recipe = this.recipeDao.findOne(Long.valueOf(id));
            if(recipe != null)
            {
                try
                {
                    byte[] bytes = file.getBytes();
                    int fileTypeStart = file.getOriginalFilename().lastIndexOf('.');
                    String fileType = file.getOriginalFilename().substring(fileTypeStart);
                    String path = System.getProperty("user.dir");
                    String sep = File.separator;
                    File newFolder = new File(path + sep + "images");
                    newFolder.mkdirs();
                    BufferedOutputStream stream =
                            new BufferedOutputStream(new FileOutputStream(new File(path + sep + "images" + sep 
                                                                                   + recipe.getName() + recipe.getId()
                                                                                   + fileType)));
                    stream.write(bytes);
                    stream.close();
                    recipe.setImage(recipe.getName() + recipe.getId() + fileType);
                    this.recipeDao.updateRecipe(recipe);
                    return HttpStatus.ACCEPTED;
                }
                catch(Exception e)
                {
                    return HttpStatus.INTERNAL_SERVER_ERROR;
                }
            }

        }
        return HttpStatus.FAILED_DEPENDENCY;
    }


    @SuppressWarnings("unchecked")
    @RequestMapping(value = "/upload/csv", method = RequestMethod.POST)
    public @ResponseBody ResponseEntity<?> handleCsvFileUpload(@RequestParam("file") MultipartFile file)
    {
        if(!file.isEmpty())
        {

            try
            {

                List<Inventory> inventories = this.inventoryCoord.parseCsvFile(file);
                return new ResponseEntity<List<Inventory>>(inventories, HttpStatus.ACCEPTED);
            }
            catch(Exception e)
            {
                return new ResponseEntity<Exception>(e, HttpStatus.INTERNAL_SERVER_ERROR);
            }

        }
        return new ResponseEntity<List<Inventory>>(Collections.EMPTY_LIST, HttpStatus.FAILED_DEPENDENCY);
    }

}
