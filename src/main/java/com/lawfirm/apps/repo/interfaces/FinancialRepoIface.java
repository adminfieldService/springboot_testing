/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.Financial;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface FinancialRepoIface {

    Financial create(Financial entity);

    Financial update(Financial entity);

    Financial delete(Financial entity);

    void remove(Financial entity);

    Financial findById(Long paramLong);

    List<Financial> findByTeamMember(String param);
    
    List<Financial> findByCaseId (String param);

    List<Financial> listFinancial();

    List<Financial> listActive();

    List<Financial> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
