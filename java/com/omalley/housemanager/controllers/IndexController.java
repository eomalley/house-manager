package com.omalley.housemanager.controllers;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.omalley.housemanager.dao.UserInfoDao;

@Controller
public class IndexController
{
	@Autowired
	UserInfoDao userInfoDao;

    @RequestMapping("/")
    public String index()
    {

        return "index";
    }


    @RequestMapping("/login")
    public String login()
    {
        return "login";
    }
    
    @ResponseBody
    @RequestMapping(value = "/username", method = RequestMethod.GET)
    public Map<String, String> getUser(Authentication auth) {
    	return Collections.singletonMap("name", auth.getName());
    }
    
    @ResponseBody
    @RequestMapping(value = "/username/getusers", method = RequestMethod.GET)
    public List<String> getUserNames(@RequestParam String hint)
    {
        return this.userInfoDao.findLikeUserName(hint);
    }
}

//TODO
// when marking off an item, search for inventory w/ item/brand pairing to add
// to existing (include measurement type) or add new, then delete entry
// grocery object refers to item id
//TODO
//need actual methods for adding from item adding from item essential
//adding from inventory adding from missing ingredients marking off
//TODO
//can't do less than 1 ingredient amounts create grocery list from missing ingredients


