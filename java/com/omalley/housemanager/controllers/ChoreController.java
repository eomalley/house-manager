package com.omalley.housemanager.controllers;

import java.io.IOException;
import java.util.Date;
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
import com.omalley.housemanager.coordinators.IChoreCoordinator;
import com.omalley.housemanager.dao.ChoreDao;
import com.omalley.housemanager.domain.Chore;
import com.omalley.housemanager.domain.ChoreHistory;

@Controller
public class ChoreController
{

    @Autowired
    ChoreDao choreDao;

    @Autowired
    IChoreCoordinator choreCoord;


    @ResponseBody
    @RequestMapping(value = "/chore/getAll", method = RequestMethod.GET)
    public List<Chore> getAllChores()
    {
        List<Chore> chores = this.choreDao.returnAllItems();
        return chores;
    }


    @ResponseBody
    @RequestMapping(value = "/chore", method = RequestMethod.GET)
    public Chore showDetails(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Chore chore = this.choreDao.findOne(convertedId);

        return chore;

    }


    @ResponseBody
    @RequestMapping(value = "/chore/types", method = RequestMethod.GET)
    public List<String> getChoreTypes(@RequestParam String hint)
    {

        return this.choreDao.findLikeType(hint);

    }


    @ResponseBody
    @RequestMapping(value = "/chore", method = RequestMethod.POST)
    public void updateChore(@RequestBody Map<String, String> chorePayload) throws JsonProcessingException, IOException
    {
        if(chorePayload != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Chore chore = mapper.readValue(chorePayload.get("choreParams"), Chore.class);
            
            this.choreCoord.updateChore(chore);
            
        }

    }


    @ResponseBody
    @RequestMapping(value = "/chore", method = RequestMethod.DELETE)
    public void deleteChore(@RequestParam("id") String id)
    {
        long convertedId = Long.valueOf(id);
        Chore chore = this.choreDao.findOne(convertedId);
        if(chore != null)
        {
            this.choreDao.delete(chore);
        }
    }


    @ResponseBody
    @RequestMapping(value = "/chore/complete", method = RequestMethod.POST)
    public HttpStatus completeChore(@RequestBody Map<String, String> chorePayload,  Authentication auth) throws JsonProcessingException,
        IOException
    {
        if(chorePayload != null)
        {
            ObjectMapper mapper = new ObjectMapper();
            mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
            Chore chore = mapper.readValue(chorePayload.get("choreParams"), Chore.class);
            String user = auth.getName();
            if(user == null || user.isEmpty()) {
            	return HttpStatus.FORBIDDEN;
            }
            chore.setLastCompletedBy(user);
            this.choreCoord.completeChore(chore);
            
            
            return HttpStatus.OK;
        }
        return HttpStatus.BAD_REQUEST;

    }


    @ResponseBody
    @RequestMapping(value = "/chore/scores", method = RequestMethod.GET)
    public Map<String, List<ChoreHistory>> getChoreScores(@RequestParam("dateFrom") String fromString,
                                                          @RequestParam("dateTo") String toString)
    {

        return this.choreCoord.getScores(new Date(Long.valueOf(fromString)), new Date(Long.valueOf(toString)));
    }

}
