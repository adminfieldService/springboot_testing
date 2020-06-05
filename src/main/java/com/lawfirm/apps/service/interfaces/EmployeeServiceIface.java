/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.Employee;
import java.util.List;
import javax.persistence.EntityManager;
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

    Employee findByEmployee(String paramString);

    List<Employee> listEmployee();

    List<Employee> listEmployeePaging(int max, int start);

    List<Employee> listActive(Boolean isActive);

    List<Employee> findByApproved(String paramString);

    Integer count();

    EntityManager getEntityManager();
}
