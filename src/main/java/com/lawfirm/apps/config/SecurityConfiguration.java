/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.config;

import com.lawfirm.apps.security.jwt.AuthEntryPointJwt;
import com.lawfirm.apps.security.jwt.AuthTokenFilter;
import com.lawfirm.apps.service.UserServiceImpl;
import java.util.Arrays;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author newbiecihuy
 */
@Configuration
@EnableAutoConfiguration
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

        http.cors().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/managed-employee/**").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/find-by-id/**").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.GET, "/employe-role/**").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/cv/managed-cv/**").hasAnyRole("admin", "sysadmin", "lawyer")
                .antMatchers(HttpMethod.POST, "/approved/by-admin/**").hasAnyRole("admin", "sysadmin")
                .antMatchers(HttpMethod.POST, "/approved/by-admin/**").hasAnyRole("admin", "sysadmin")
                .antMatchers(HttpMethod.GET, "/view/list-by-admin/**").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.GET, "/view/list-by-finance/**").hasAnyRole("finance", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.PUT, "/managed-employee/**").hasAnyRole("lawyer", "admin", "dpm")//.permitAll()
                .antMatchers(HttpMethod.PUT, "/updateAccount/**").hasAnyRole("lawyer", "admin", "dpm", "finance")//.permitAll()    
                .antMatchers(HttpMethod.DELETE, "/managed-employee/**").hasRole("admin")//.permitAll()   
                .antMatchers("/loan/**").hasRole("lawyer")//.permitAll()
                .antMatchers("/engagement/**").permitAll()
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling().accessDeniedPage("/403")
                .and()
                .sessionManagement()
                .maximumSessions(1);

        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
    }
//    @Override
//    protected void configure(HttpSecurity http) throws Exception {//.exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint()).and() 
//
//        http.cors().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable()
//                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
//                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
//                .authorizeRequests()
//                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
//                .antMatchers("/auth/**").permitAll()
//                .antMatchers("/employee/**").hasAnyRole("admin", "sysadmin", "lawyer")
//                //                .antMatchers("/approvedByAdmin").hasAnyRole("admin", "sysadmin")
//                //                .antMatchers("/createEmployee").hasAnyRole("admin", "sysadmin")//.permitAll()
//                //                .antMatchers("/viewEmployeeByAdmin").hasAnyRole("admin", "sysadmin")//.permitAll()
//                //                .antMatchers("/viewEmployeeByFinance").hasAnyRole("finance", "sysadmin")//.permitAll()
//                //                .antMatchers("/updateEmployee").hasAnyRole("lawyer", "admin", "dpm")//.permitAll()
//                //                .antMatchers("/updateAccount").hasAnyRole("lawyer", "admin", "dpm", "finance")//.permitAll()   
//                //                .antMatchers("/approved").hasAnyRole("sysadmin", "admin")//.permitAll()   
//                //                .antMatchers("/deleteEmployee").hasAnyRole("admin")//.permitAll()   
//                .antMatchers("/loan/**").hasAnyRole("lawyer", "admin", "dpm", "finance")
//                .antMatchers("/engagement/**").hasAnyRole("lawyer", "admin", "dpm", "finance")
//                .anyRequest().fullyAuthenticated()//.authenticated()                
//                .and()
//                .sessionManagement()
//                .maximumSessions(1);
//
//        http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
//    }
//https://stackoverflow.com/questions/51719889/spring-boot-cors-issue

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOrigins(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "PATCH", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("authorization", "content-type", "x-auth-token"));
        configuration.setExposedHeaders(Arrays.asList("x-auth-token"));
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

}
