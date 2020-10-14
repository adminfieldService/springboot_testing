/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service;

import com.lawfirm.apps.config.Constants;
import com.lawfirm.apps.model.Member;
import com.lawfirm.apps.repo.interfaces.MemberRepoIface;
import com.lawfirm.apps.service.interfaces.MemberServiceIface;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 *
 * @author newbiecihuy
 */
@Service
public class MemberService implements MemberServiceIface {

    @Autowired
    MemberRepoIface memberRepo;

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Member create(Member entity) {
        return memberRepo.create(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Member update(Member entity) {
        return memberRepo.update(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Member delete(Member entity) {
        return memberRepo.delete(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer deleteBy(Long team_member_id) {
        return memberRepo.deleteBy(team_member_id);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public void remove(Member entity) {
        memberRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Member findById(String paramString) {
        return memberRepo.findById(paramString);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Member> findByIdTeam(Long paramLong) {
        return memberRepo.findByIdTeam(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Member> findByCaseId(String param) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Member> findByEmpId(String param) {
        return memberRepo.findByEmpId(param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer updateFeeMember(Long teamMemberId, Long idEmployee, Double feeShare) {
        return memberRepo.updateFeeMember(teamMemberId, idEmployee, feeShare);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Member findBy(Long idTeamMember, String employeeId) {
        return memberRepo.findBy(idTeamMember, employeeId);
    }

}
