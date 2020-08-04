/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Events;
import com.lawfirm.apps.repo.interfaces.EventsRepoIface;
import com.lawfirm.apps.service.interfaces.EventServiceIface;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class EventService implements EventServiceIface {

    @Autowired
    private EventsRepoIface eventsRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Events create(Events entity) {
        return eventsRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Events update(Events entity) {
        return eventsRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Events delete(Events entity) {
        return eventsRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Events entity) {
        eventsRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Events> findByCaseId(String param) {
        return eventsRepo.findByCaseId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Events> findByActive(String is_active) {
        return eventsRepo.findByActive(is_active);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Events findById(String param) {
        return eventsRepo.findById(param);
    }

}
