/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.utils;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.io.FileNotFoundException;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.File;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author newbiecihuy
 */
public class CreateLog {

    public CreateLog() {
    }

    public static String createJson(Object object, String namaFile) {
//        Samba samba = new Samba();
        String json = null;
        DateTimeFormatter outputDateFormat = DateTimeFormatter.ofPattern("dd_MM_yyyy");
        DateTimeFormatter lineDateFormat = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        byte[] input_file = null;
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime line = LocalDateTime.now();
        String path = "/opt/lawfirm_log/";
        String isi_namaFile = namaFile + "_" + outputDateFormat.format(now) + ".log";
        String isifile = path + isi_namaFile;
        ObjectMapper mapper = new ObjectMapper();
        try {
            json = mapper.writeValueAsString(object);
            File f = new File(isifile);
            PrintWriter writer = null;
//            input_file = Files.readAllBytes(Paths.get(isifile));
            writer = new PrintWriter(new FileWriter(f, true));
            input_file = Files.readAllBytes(Paths.get(isifile));
            if (f.exists()) {

                writer.println("#" + lineDateFormat.format(line) + "::" + json);
//
//                samba.write(input_file, path + isi_namaFile);
                System.out.println("JSON1 = " + json);
            } else {
                writer.println("#" + lineDateFormat.format(line) + "::" + json);
//             
//                samba.write(input_file, path + isi_namaFile);
                System.out.println("JSON2 = " + json);
            }

            writer.close();
        } catch (IOException ex) {
            System.out.println("ERROR: " + ex.getMessage());
        }
        return json;
    }
}
