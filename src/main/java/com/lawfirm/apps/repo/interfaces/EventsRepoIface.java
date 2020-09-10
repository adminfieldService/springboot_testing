/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.repo.interfaces;

import com.lawfirm.apps.model.Events;
import java.util.List;
import org.springframework.stereotype.Repository;

/**
 *
 * @author newbiecihuy
 */
@Repository
public interface EventsRepoIface {

    Events create(Events entity);

    Events update(Events entity);

    Events delete(Events entity);

    void remove(Events entity);

    List<Events> findByCaseId(String param);

    List<Events> listEvents(String is_active);
    
    public Events findById(String param);

}
