/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;

/**
 *
 * @author newbiecihuy
 */
public class CreateJson {

    public CreateJson() {
    }

    public static String setJson(Object object) {
        String json = null;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(object);
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return json;
    }
}
