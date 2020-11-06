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
import java.util.Optional;
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

//    @Override
//    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
//    public Employee findByEmployeeId(String paramString, Long Id) {
//        return employeeRepo.findByEmployeeId(paramString, Id);
//    }
    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee findByEmployeeId(String paramString) {
        return employeeRepo.findByEmployeeId(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listEmployeeId(String param) {
        return employeeRepo.listEmployeeId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listEmployee() {
        return employeeRepo.listEmployee();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listEmployeePaging(String paramString, int max, int start) {
        return employeeRepo.listEmployeePaging(paramString, max, start);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listActive(Boolean isActive) {
        return employeeRepo.listActive(isActive);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> findByApproved(Long paramLong) {
        return employeeRepo.findByApproved(paramLong);
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

//    @Override
//    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
//    public MyUserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
//        Employee userInfo = employeeRepo.findByUserName(userName);
//        GrantedAuthority authority = new SimpleGrantedAuthority(userInfo.getRoleName());
//        System.out.println("loadUserByUsername : " + authority);
//        return new User(userInfo.getUserName(), userInfo.getPassword(), Arrays.asList(authority));
//    }
    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee chekUserName(String paramString) {
        return employeeRepo.chekUserName(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Boolean existsByUsername(String username) {
        return employeeRepo.existsByUsername(username);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Boolean existsByEmail(String email) {
        return employeeRepo.existsByEmail(email);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer generateEmpId(String param) {
        return employeeRepo.generateEmpId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Optional<Employee> findByUsername(String username) {
        return employeeRepo.findByUsername(username);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee cekPass(String password, Long idEmployee) {
        return employeeRepo.cekPass(password,idEmployee);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Employee> listEmployeeByRole(String paramString) {
        return employeeRepo.listEmployeeByRole(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Employee findByEmail(String paramString) {
         return employeeRepo.findByEmail(paramString);
    }

}
