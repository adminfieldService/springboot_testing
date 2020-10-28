/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.controller;

import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author newbiecihuy
 */
@RestController
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class Controller {

    @RequestMapping("/")
    public String welcome() {

        return "index";
    }

    private final org.slf4j.Logger log = LoggerFactory.getLogger(this.getClass());

    public void log() {
        log.info("log4j is work");
    }

}
