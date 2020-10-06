/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.TeamMember;
import com.lawfirm.apps.repo.interfaces.TeamMemberRepoIface;
import com.lawfirm.apps.service.interfaces.TeamMemberServiceIface;
import java.util.List;
import java.util.Optional;
import javax.persistence.EntityManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class TeamMemberService implements TeamMemberServiceIface {

    @Autowired
    TeamMemberRepoIface teamMemberRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember create(TeamMember entity) {
        return teamMemberRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember update(TeamMember entity) {
        return teamMemberRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember delete(TeamMember entity) {
        return teamMemberRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(TeamMember entity) {
        teamMemberRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember findById(Long paramLong) {
        return teamMemberRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember findByEngId(Long engID) {
        return teamMemberRepo.findByEngId(engID);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember findByName(String namaVisit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<TeamMember> listTeamMember() {
        return teamMemberRepo.listTeamMember();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<TeamMember> listActive(Boolean param) {
        return teamMemberRepo.listActive(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<TeamMember> byName(Boolean isActive) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return teamMemberRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return teamMemberRepo.getEntityManager();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<TeamMember> findByDmp(Long dmpId) {
        return teamMemberRepo.findByDmp(dmpId);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<TeamMember> listTeamMemberByEngagement(Long param) {
        return teamMemberRepo.listTeamMemberByEngagement(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<TeamMember> generateTeamCaseId(String param1) {
        return teamMemberRepo.generateTeamCaseId(param1);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember findByTeamCaseId(String teamcaseID, String paramY) {
        return teamMemberRepo.findByTeamCaseId(teamcaseID, paramY);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public TeamMember updateFeeDmp(Long engagementId, Long dmpId, Double feeShare) {
        return teamMemberRepo.updateFeeDmp(engagementId, dmpId, feeShare);
    }

}
