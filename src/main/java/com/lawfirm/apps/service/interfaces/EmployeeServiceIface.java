/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.support.api.MyUserDetails;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface EmployeeServiceIface {

    Employee create(Employee entity);

    Employee update(Employee entity);

    Employee approved(Employee entity);

    Employee delete(Employee entity);

    void remove(Employee entity);

    Employee findById(Long paramLong);

    Employee chekUserName(String paramString);

    Employee findByEmployee(String paramString);

    Employee findByEmployeeId(String paramString, Long Id);

    List<Employee> listEmployee();

    List<Employee> listEmployeePaging(String paramString, int max, int start);

    List<Employee> listActive(Boolean isActive);

    List<Employee> findByApproved(Long paramLong);

    Integer count();

    EntityManager getEntityManager();

//    MyUserDetails loadUserByUsername(String userName);
    Boolean existsByUsername(String username);

    Boolean existsByEmail(String email);

    Optional<Employee> findByUsername(String username);
}
