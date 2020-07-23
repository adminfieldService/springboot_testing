/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.CaseDetails;
import com.lawfirm.apps.model.Engagement;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface EngagementRepoIface {

    Engagement create(Engagement entity);

    Engagement update(Engagement entity);

    Engagement delete(Engagement entity);

    void remove(Engagement entity);

    Engagement findById(Long paramLong);

//    Engagement findByName(String namaVisit);
    CaseDetails findByCaseID(String paramString);

    CaseDetails createCaseID(Integer number);

    List<Engagement> listEngagement();

    List<Engagement> listActive();

    List<Engagement> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();
}
