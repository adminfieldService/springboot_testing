/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.Professional;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface ProfessionalRepoIface {

    Professional create(Professional entity);

    Professional update(Professional entity);

    Professional delete(Professional entity);

    void remove(Professional entity);

    Professional findById(Long paramLong);

    List<Professional> findByEmployee(String param);
    
    List<Professional> listProfessional();

    List<Professional> listProfessionalPaging(int max, int start);

    List<Professional> listActive(int max, int start, String param);

    List<Professional> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
