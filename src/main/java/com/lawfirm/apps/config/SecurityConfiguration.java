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
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
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

/**
 *
 * @author newbiecihuy
 */
@Order(Ordered.HIGHEST_PRECEDENCE)
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
        auth.eraseCredentials(true)
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
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/v3/api-docs", "/configuration/**", "/swagger-resources/**", "/swagger-ui.html/..", "/webjars/**", "/api-docs/**")
                .antMatchers("/opt/UploadFile/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
//  .exceptionHandling().authenticationEntryPoint(new AuthExceptionEntryPoint()).and() 
        http.cors().and().csrf().csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse()).disable()
                .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers("/auth/**").permitAll()
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.GET, "/employee/role/").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/update-profile/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitAll()       
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/account/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitAll()       
                .antMatchers(HttpMethod.POST, "/employee/approval/account/").hasRole("admin")//.permitAll()       
                .antMatchers(HttpMethod.DELETE,"/employee/managed-employee/{id_employee}/").hasAnyRole("admin", "sysadmin")//.permitAll()        
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/{id_employee}/set-password/").hasAnyRole("lawyer", "admin", "finance", "dmp", "support")//.permitAll()       
                .antMatchers(HttpMethod.PUT, "/employee/managed-employee/password/").hasAnyRole("lawyer", "admin", "finance", "dmp", "support", "sysadmin")//.permitAll()       
                .antMatchers(HttpMethod.POST, "/employee/find-by-id/").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/{employee_id}/find-by-employee-id/").hasAnyRole("admin", "sysadmin", "lawyer", "support")//.permitAll()
                .antMatchers(HttpMethod.GET, "/employee/employe-role/role-name/").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/{id_employee}/cv/").hasAnyRole("admin", "sysadmin", "lawyer")
                .antMatchers(HttpMethod.GET, "/employee/managed-employee/{id_employee}/download-cv/").hasAnyRole("admin", "sysadmin", "lawyer", "support", "dmp", "finance")// .antMatchers(HttpMethod.POST, "/employee/managed-employee/{id_employee}/download-cv/").hasAnyRole("admin", "sysadmin", "lawyer", "support", "dmp", "finance")
                .antMatchers(HttpMethod.POST, "/employee/approval/by-admin/").hasAnyRole("admin", "sysadmin")
                .antMatchers(HttpMethod.POST, "/employee/managed-account/").hasAnyRole("lawyer", "admin", "dmp", "finance")//.permitAll()    
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/in-active/").hasRole("admin")
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/active/").hasRole("admin")
                .antMatchers(HttpMethod.POST, "/employee/managed-employee/resign/").hasAnyRole("admin")//.permitAll()  
                .antMatchers(HttpMethod.GET, "/employee/view/list-by-admin/").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.GET, "/employee/view/list-by-finance/").hasAnyRole("finance", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/loan/manage-loan/loan-a/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/approval/by-admin/").hasRole("admin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/loan/approval/{id_loan}/by-admin/").hasAnyRole("admin", "sysadmin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/loan/approval/{id_loan}/by-finance/").hasRole("finance")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/view/list-of-loan-a/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitSAll()
                .antMatchers(HttpMethod.GET, "/loan/view/{id_employee}/loans-a/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/view/list-of-loan-a/by-admin/").hasAnyRole("admin", "sysadmin")//.hasRole("admin")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/view/list-of-loan-b/by-admin/").hasAnyRole("admin", "sysadmin")//.hasRole("admin")//.permitAll()
                .antMatchers(HttpMethod.POST, "/loan/manage-loan/loan-b/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/view/list-of-loan-b/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/view/{id_employee}/loans-b/").hasAnyRole("lawyer", "admin", "dmp", "support")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/view/list-of-loan-b/by-admin/").hasAnyRole("admin", "sysadmin")//.hasRole("admin")//.permitAll()
                .antMatchers(HttpMethod.GET, "/loan/{id_loan}/find-by-id/").hasAnyRole("lawyer", "admin", "dmp", "finance", "support")//.permitAll()
                .antMatchers(HttpMethod.POST, "/loan/loan-b/case-id/").hasAnyRole("lawyer", "admin", "dmp", "finance", "support")//.permitAll()
                .antMatchers(HttpMethod.POST, "/engagement/manage-engagement/").hasAnyRole("admin", "sysadmin", "lawyer", "sup")
                .antMatchers(HttpMethod.GET, "/engagement/manage-engagement/list-of-engagement/").hasAnyRole("admin", "sysadmin", "lawyer", "sup")
                .antMatchers(HttpMethod.GET, "/engagement/manage-engagement/view/by-employee/").hasAnyRole("admin", "sysadmin", "lawyer", "sup")
                .antMatchers(HttpMethod.GET, "/engagement/manage-engagement/aproval-list/by-admin/").hasAnyRole("admin", "sysadmin", "lawyer", "sup")
                .antMatchers(HttpMethod.GET, "/engagement/manage-engagement/").hasAnyRole("admin", "sysadmin", "lawyer", "sup")
                .antMatchers(HttpMethod.POST, "/engagement/approval/{engagement_id}/by-admin/").hasRole("admin")
                .antMatchers(HttpMethod.POST, "/engagement/manage-engagement/{engagement_id}/done/").hasRole("dmp")
                .antMatchers(HttpMethod.POST, "/engagement-dto/manage-engagement-dto/").hasAnyRole("admin", "sysadmin", "lawyer", "sup")
                .antMatchers(HttpMethod.POST, "/engagement/manage-engagement/{engagement_id}/event/").hasAnyRole("admin", "dmp", "lawyer")
                .antMatchers(HttpMethod.POST, "/engagement/manage-engagement/event/{event_id}/").hasAnyRole("admin", "dmp", "lawyer")
                .antMatchers(HttpMethod.GET, "/engagement/manage-engagement/{engagement_id}/events/").hasAnyRole("admin", "dmp", "lawyer")
                .antMatchers(HttpMethod.GET, "/engagement/manage-engagement/events/").hasAnyRole("admin", "dmp", "lawyer")
                .antMatchers(HttpMethod.POST, "/engagement/managed-fee-share/{engagement_id}/").hasRole("admin")
                .antMatchers(HttpMethod.POST, "/case/{engagement_id}/document/").hasAnyRole("admin", "dmp", "lawyer")
                .antMatchers(HttpMethod.GET, "/case/documents/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.GET, "/case/{engagement_id}/documents/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.POST, "/case/close/").hasRole("admin")
                .antMatchers(HttpMethod.GET, "/case/document/{case_document_id}/view/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.GET, "/case/case-id/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.POST, "/disburse/{id_loan}/loan-a/").hasRole("finance")
                .antMatchers(HttpMethod.POST, "/disburse/{id_loan}/loan-b/").hasRole("finance")
                .antMatchers(HttpMethod.GET, "/disbursements/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.GET, "/disbursements/loans/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.GET, "/disbursements/loan-a/").hasRole("finance")
                .antMatchers(HttpMethod.GET, "/disbursements/loan-b/").hasRole("finance")
                .antMatchers(HttpMethod.GET, "/disbursement/{id_loan}/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.POST, "/disbursement/case-id/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.POST, "/reimbursement/{id_loan}/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.GET, "/reimbursements/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.POST, "/reimbursement/find-by-id/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.POST, "/reimbursement/{reimburse_id}/approval/").hasRole("admin")
                .antMatchers(HttpMethod.POST, "/reimbursement/{reimburse_id}/reject/").hasRole("admin")
                .antMatchers(HttpMethod.POST, "/reimbursement/{reimburse_id}/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .antMatchers(HttpMethod.GET, "/reimbursements/finance/").hasRole("finance")
                .antMatchers(HttpMethod.GET, "/reimbursements/admin/").hasRole("admin")
                .antMatchers(HttpMethod.GET, "/reimbursements/dmp/").hasAnyRole("dmp", "finance")
                .antMatchers(HttpMethod.GET, "/reimbursement/{reimburse_id}/document/").hasAnyRole("admin", "dmp", "lawyer", "finance")
                .anyRequest()
                .authenticated()
                .and()
                .exceptionHandling()
                .accessDeniedPage("/403")
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
