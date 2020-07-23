/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.ClientData;
import com.lawfirm.apps.repo.interfaces.ClientDataRepoIface;
import com.lawfirm.apps.service.interfaces.ClientDataServiceIface;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class ClientDataService implements ClientDataServiceIface {

    @Autowired
    ClientDataRepoIface clientDataRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ClientData create(ClientData entity) {
        return clientDataRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ClientData update(ClientData entity) {
        return clientDataRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ClientData delete(ClientData entity) {
        return clientDataRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(ClientData entity) {
        clientDataRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ClientData findById(Long paramLong) {
        return clientDataRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ClientData findByName(String paramString) {
        return clientDataRepo.findByName(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ClientData findBydataClient(String clientName, String address, String npwp) {
        return clientDataRepo.findBydataClient(clientName, address, npwp);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<ClientData> listClient() {
        return clientDataRepo.listClient();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<ClientData> listActive(String isActive) {
        return clientDataRepo.listActive(isActive);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<ClientData> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer generateCleintId(String npwp) {
        return clientDataRepo.generateCleintId(npwp);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return clientDataRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public ClientData checkCI(String paramString) {
        return clientDataRepo.checkCI(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return clientDataRepo.getEntityManager();
    }

}
