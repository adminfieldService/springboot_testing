/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.config;

import com.lawfirm.apps.security.jwt.AuthEntryPointJwt;
import com.lawfirm.apps.security.jwt.AuthTokenFilter;
import com.lawfirm.apps.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

/**
 *
 * @author newbiecihuy
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserServiceImpl userDetailsService;

    @Autowired
    private AuthEntryPointJwt unauthorizedHandler;

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
        auth
                .eraseCredentials(true)
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder());
    }

    @Bean
    public AuthTokenFilter authenticationJwtTokenFilter() {
        return new AuthTokenFilter();
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {//.exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint()).and() 

        http.cors().and().csrf().disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST,"/saveUploadedFile/**").access("hasRole('admin') or hasRole('sysadmin')or hasRole('lawyer')")
                .antMatchers(HttpMethod.POST,"/approvedByAdmin/**").access("hasRole('admin') or hasRole('sysadmin')")
                .antMatchers(HttpMethod.POST, "/createEmployee/**").access("hasRole('admin') or hasRole('sysadmin')")//.permitAll()
                .antMatchers(HttpMethod.GET, "/viewEmployeeByAdmin/**").access("hasRole('admin') or hasRole('sysadmin')")//.permitAll()
                .antMatchers(HttpMethod.GET, "/viewEmployeeByFinance/**").access("hasRole('finance') or hasRole('sysadmin')")//.permitAll()
                .antMatchers(HttpMethod.PUT, "/updateEmployee/**").access("hasRole('lawyer') or hasRole('admin')or hasRole('dpm')")//.permitAll()
                .antMatchers(HttpMethod.PUT, "/updateAccount/**").access("hasRole('lawfirm') or hasRole('admin')or hasRole('dpm')or hasRole('finance')")//.permitAll()   
                .antMatchers(HttpMethod.PUT, "/approved/**").access("hasRole('sysadmin') or hasRole('admin')")//.permitAll()   
                .antMatchers(HttpMethod.DELETE, "/deleteEmployee/**").access("hasRole('admin')")//.permitAll()   
                .antMatchers("/loan/**").permitAll()
                .antMatchers("/engagement/**").permitAll()
                .anyRequest().authenticated()
                .and()
                .sessionManagement()
                .maximumSessions(1);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
}
