/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Employee;
import com.lawfirm.apps.repo.interfaces.EmployeeRepoIface;
import com.lawfirm.apps.service.interfaces.EmployeeServiceIface;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class EmployeeService implements EmployeeServiceIface {

    @Autowired
    EmployeeRepoIface employeeRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee create(Employee entity) {
        return employeeRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee update(Employee entity) {
        return employeeRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee approved(Employee entity) {
        return employeeRepo.approved(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee delete(Employee entity) {
        return employeeRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Employee entity) {
        employeeRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee findById(Long paramLong) {
        return employeeRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee findByEmployee(String paramString) {
        return employeeRepo.findByEmployee(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listEmployee() {
        return employeeRepo.listEmployee();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listEmployeePaging(int max, int start) {
        return employeeRepo.listEmployeePaging(max, start);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listActive(Boolean isActive) {
        return employeeRepo.listActive(isActive);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> findByApproved(String paramString) {
        return employeeRepo.findByApproved(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return employeeRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return employeeRepo.getEntityManager();
    }

}
