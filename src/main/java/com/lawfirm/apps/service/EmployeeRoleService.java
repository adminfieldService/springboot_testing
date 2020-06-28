/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.EmployeeRole;
import com.lawfirm.apps.repo.interfaces.EmployeeRoleRepoIface;
import com.lawfirm.apps.service.interfaces.EmployeeRoleServiceIface;
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
public class EmployeeRoleService implements EmployeeRoleServiceIface {

    @Autowired
    EmployeeRoleRepoIface employeeRoleRepoIface;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EmployeeRole create(EmployeeRole entity) {
        return employeeRoleRepoIface.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EmployeeRole update(EmployeeRole entity) {
        return employeeRoleRepoIface.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EmployeeRole delete(EmployeeRole entity) {
        return employeeRoleRepoIface.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(EmployeeRole entity) {
        employeeRoleRepoIface.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EmployeeRole findById(Long paramLong) {
        return employeeRoleRepoIface.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<EmployeeRole> findByRoleName(String paramString) {
        return employeeRoleRepoIface.findByRoleName(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<EmployeeRole> listRole() {
        return employeeRoleRepoIface.listRole();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<EmployeeRole> listActive() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<EmployeeRole> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return employeeRoleRepoIface.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return employeeRoleRepoIface.getEntityManager();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EmployeeRole findByName(String RoleName) {
        return employeeRoleRepoIface.findByName(RoleName);
    }

}
