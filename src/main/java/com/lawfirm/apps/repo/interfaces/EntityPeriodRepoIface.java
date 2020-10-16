/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.EntityPeriod;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface EntityPeriodRepoIface {

    EntityPeriod create(EntityPeriod entity);

    EntityPeriod update(EntityPeriod entity);

    EntityPeriod delete(EntityPeriod entity);

    EntityPeriod findById(Integer number, String taxYear);

    EntityPeriod findBy(Long userId, String caseId, String taxYear);

    Double previousDisbursement(Long userId, String taxYear);

    Double incomeTaxPaidOnPriorPeriod(Integer numberDisbursement, Long userId, String taxYear);
}