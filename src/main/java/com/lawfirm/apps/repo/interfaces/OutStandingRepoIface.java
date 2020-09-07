/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.OutStanding;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface OutStandingRepoIface {

    OutStanding create(OutStanding entity);

    OutStanding update(OutStanding entity);

    OutStanding approved(OutStanding entity);

    OutStanding delete(OutStanding entity);

    void remove(OutStanding entity);

    OutStanding findByIdLoan(Long paramLong);
    
    OutStanding findByCaseId(Long paramLong);
}