/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.security.jwt;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

/**
 *
 * @author newbiecihuy
 */
@Component
public class AuthEntryPointJwt implements AuthenticationEntryPoint {

    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException authException) throws IOException, ServletException {
//		logger.error("Unauthorized error: {}", authException.getMessage());
//                response.setContentType("application/json");
//		response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Error: Unauthorized");
        final Map<String, Object> mapBodyException = new HashMap<>();

        mapBodyException.put("error", "Error from AuthenticationEntryPoint");
        mapBodyException.put("message", "Message from AuthenticationEntryPoint");
        mapBodyException.put("exception", "My stack trace exception");
        mapBodyException.put("path", request.getServletPath());
        mapBodyException.put("timestamp", (new Date()).getTime());

        response.setContentType("application/json");
        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);

        final ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(response.getOutputStream(), mapBodyException);

    }

}
