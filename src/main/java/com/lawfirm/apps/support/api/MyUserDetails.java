/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.support.api;

import com.lawfirm.apps.model.Employee;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 *
 * @author newbiecihuy
 */
public class MyUserDetails implements UserDetails {

    private static final long serialVersionUID = 1L;
    private int id;
    private String username;
    private String password;
    private boolean active;
    private String employeeId;
    private String roleName;
    private Double loanLimit;
    private List<GrantedAuthority> authorities;
    //new fields for the info
//    private String company;
//    private String department;

    public MyUserDetails(Employee user) {
        this.id = (int) (long) user.getIdEmployee();
        this.username = user.getUserName();
        this.password = user.getPassword();
        this.active = user.IsActive();
        this.employeeId = user.getEmployeeId();
        this.roleName = user.getRoleName();
        this.loanLimit = user.getLoanAmount();
         //new fields addition
        //        this.company = user.getCompany();
        //        this.department = user.getDepartment();
        this.authorities = Arrays.stream(user.getRoleName().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public String getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(String employeeId) {
        this.employeeId = employeeId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public Double getLoanLimit() {
        return loanLimit;
    }

    public void setLoanLimit(Double loanLimit) {
        this.loanLimit = loanLimit;
    }

   
    

}
