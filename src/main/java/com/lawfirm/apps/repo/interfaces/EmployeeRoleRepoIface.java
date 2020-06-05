/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.EmployeeRole;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface EmployeeRoleRepoIface {

    EmployeeRole create(EmployeeRole entity);

    EmployeeRole update(EmployeeRole entity);

    EmployeeRole delete(EmployeeRole entity);

    void remove(EmployeeRole entity);

    EmployeeRole findById(Long paramLong);

    List<EmployeeRole>  findByRoleName(String paramString);

    List<EmployeeRole> listRole();

    List<EmployeeRole> listActive();

    List<EmployeeRole> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
