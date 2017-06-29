package com.omalley.housemanager.controllers;

import java.io.IOException;
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
import com.omalley.housemanager.coordinators.IToDoCoordinator;
import com.omalley.housemanager.dao.ScratchDao;
import com.omalley.housemanager.dao.ToDoDao;
import com.omalley.housemanager.domain.Scratch;
import com.omalley.housemanager.domain.ToDo;

@Controller
public class ToDoController
{

    @Autowired
    ToDoDao toDoDao;

    @Autowired
    IToDoCoordinator toDoCoord;

    @Autowired
    ScratchDao scratchDao;


    @ResponseBody
    @RequestMapping(value = "/todo/getAll", method = RequestMethod.GET)
    public List<ToDo> getAllToDos()
    {
        return this.toDoDao.returnAllItems();
    }


    @ResponseBody
    @RequestMapping(value = "/todo", method = RequestMethod.GET)
    public ToDo getToDoDetails(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        return this.toDoDao.findOne(convertedId);
    }


    @ResponseBody
    @RequestMapping(value = "/todo", method = RequestMethod.POST)
    public HttpStatus updateToDo(@RequestBody Map<String, String> toDoPayload, Authentication auth)
        throws JsonProcessingException, IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        ToDo todo = mapper.readValue(toDoPayload.get("toDoParams"), ToDo.class);
        String user = auth.getName();
        if(user == null || user.isEmpty())
        {
            return HttpStatus.FORBIDDEN;
        }
        todo.setEnteredBy(user);
        this.toDoCoord.createOrUpdateToDo(todo);
        return HttpStatus.OK;
    }


    @ResponseBody
    @RequestMapping(value = "/todo/getAllComplete", method = RequestMethod.GET)
    public List<ToDo> getAllCompleteToDos()
    {
        return this.toDoDao.getCompletedToDos();
    }


    @ResponseBody
    @RequestMapping(value = "/todo/getAllIncomplete", method = RequestMethod.GET)
    public List<ToDo> getAllIncompleteToDos()
    {
        return this.toDoDao.getIncompleteToDos();
    }


    @ResponseBody
    @RequestMapping(value = "/todo", method = RequestMethod.DELETE)
    public void deleteToDo(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        ToDo todo = this.toDoDao.findOne(convertedId);
        if(todo != null)
        {
            this.toDoDao.delete(convertedId);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/todo/scratch", method = RequestMethod.POST)
    public void updateScratchToDo(@RequestBody Map<String, String> scratchPayload) throws JsonProcessingException,
        IOException
    {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        Scratch scratch = mapper.readValue(scratchPayload.get("scratch"), Scratch.class);
        this.toDoCoord.createOrUpdateScratch(scratch);
    }


    @ResponseBody
    @RequestMapping(value = "/todo/scratch", method = RequestMethod.GET)
    public Scratch getScratch()
    {
        Scratch scratch = this.scratchDao.getScratch();
        if(scratch != null)
        {
            return scratch;
        }
        else
        {
            scratch = new Scratch();
            scratch.setNote("");
            return scratch;
        }
    }

}
