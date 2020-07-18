/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xss.filter.http.wrappers;

import com.xss.filter.iface.RansackXss;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

/**
 *
 * @author newbiecihuy
 */
public class CaptureRequestWrapper extends HttpServletRequestWrapper {

    private RansackXss ransackXss;

    public CaptureRequestWrapper(HttpServletRequest servletRequest, RansackXss ransackXss) {
        super(servletRequest);
        this.ransackXss = ransackXss;
    }

    @Override
    public String[] getParameterValues(String parameter) {
        String[] values = super.getParameterValues(parameter);
        if (values == null) {
            return null;
        }
        int count = values.length;
        String[] encodedValues = new String[count];
        for (int i = 0; i < count; i++) {
            encodedValues[i] = ransackXss.ransackXss(values[i]);
        }
        return encodedValues;
    }

    @Override
    public String getParameter(String parameter) {
        String value = super.getParameter(parameter);
        return ransackXss.ransackXss(value);
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        return ransackXss.ransackXss(value);
    }


}
