/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.EntityPTKP;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface PtkpRepoIface {
    
    EntityPTKP create(EntityPTKP entity);

    EntityPTKP update(EntityPTKP entity);

    EntityPTKP delete(EntityPTKP entity);

    void remove(EntityPTKP entity);

    EntityPTKP findById(Long paramLong);

    EntityPTKP findPTKPByTaxStatus(String param);
}
