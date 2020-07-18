/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.xss.filter;

import com.xss.filter.http.wrappers.CaptureRequestWrapper;
import com.xss.filter.iface.RansackXss;

import java.io.IOException;
import java.util.logging.LogRecord;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;

/**
 *
 * @author newbiecihuy
 */
public class CustomXssFilter implements Filter {

    private RansackXss ransackXss;

    public CustomXssFilter(RansackXss ransackXss) {
        if (ransackXss == null) {
            throw new IllegalArgumentException("ransackXss can't be null");
        }
        this.ransackXss = ransackXss;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        filterChain.doFilter(new CaptureRequestWrapper((HttpServletRequest) servletRequest, ransackXss), servletResponse);
    }

    @Override
    public void destroy() {

    }

}
