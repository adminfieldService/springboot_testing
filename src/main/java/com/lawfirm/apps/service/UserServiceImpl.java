/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.support.api.MyUserDetails;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import lombok.extern.slf4j.Slf4j;
import static org.hibernate.annotations.common.util.impl.LoggerFactory.logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service(value = "userService")
@Slf4j
public class UserServiceImpl implements UserDetailsService {

    @Autowired
    private EmployeeService employeeRepo;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        Optional<Employee> user = employeeRepo.findByUsername(userName);
        user.orElseThrow(() -> new UsernameNotFoundException("Not found: " + userName));
        return user.map(MyUserDetails::new).get();
        //return new User("foo","foo",new ArrayList<>());
    }
//    @Override
//    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Employee user = employeeRepo.findByUserName(userName);
//        String roles = user.getRoleName();
//        if (user == null) {
//            throw new UsernameNotFoundException("Invalid username : " + userName);
//        }
//        Set<GrantedAuthority> authorities = new HashSet<>();
////        for(Em role: roles){
//        authorities.add(new SimpleGrantedAuthority(roles));
//        log.info("role" + roles + " authorities" + (roles));
//
//        return new org.springframework.security.core.userdetails.User(user.getUserName(), user.getPassword(), authorities);
////        return UserServiceImpl;
//    }

//    private List<SimpleGrantedAuthority> getAuthority() {
//        return Arrays.asList(new SimpleGrantedAuthority("ROLE_ADMIN"));//ROLE_ADMIN
//    }
    public List<Employee> findAll() {
        List<Employee> list = new ArrayList<>();
        employeeRepo.listEmployee().iterator().forEachRemaining(list::add);
        return list;
    }

}
