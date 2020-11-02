/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.utils;

/**
 *
 * @author newbiecihuy
 */
public class CustomErrorType {

//    private String message;
    private String response_code;
    private String response;
    private String info;

    public String getResponse_code() {
        return response_code;
    }

    public void setResponse_code(String response_code) {
        this.response_code = response_code;
    }

    public String getResponse() {
        return response;
    }

    public void setResponse(String response) {
        this.response = response;
    }

    public String getInfo() {
        return info;
    }

    public CustomErrorType(String response_code, String info, String response) {
        this.response_code = response_code;
        this.info = info;
        this.response = response;
    }

//    public CustomErrorType(String response) {
//        this.response = response;
//    }
//
//    public String getMessage() {
//        return message;
//    }
}
