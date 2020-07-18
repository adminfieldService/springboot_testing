/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xss.filter.service;

import com.xss.filter.iface.RansackXss;
import static com.xss.filter.util.ConstantUtil.*;
import java.util.regex.Pattern;

/**
 *
 * @author newbiecihuy
 */
public class DefaultRansackXssImpl implements RansackXss {
    @Override
    public String ransackXss(String value) {
        if (value != null) {
           for (Pattern pattern : FILTER_PATTERNS) {
               value = pattern.matcher(value).replaceAll(EMPTY);
           }
        }
        return value;
    }
}