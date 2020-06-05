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
import javax.persistence.EntityManager;
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
    public void remove(Member entity) {
        memberRepo.remove(entity);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Member findById(Long paramLong) {
        return memberRepo.findById(paramLong);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Member findByName(String namaVisit) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Member> listMember() {
        return memberRepo.listMember();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Member> listMemberPaging(int max, int start) {
        return memberRepo.listMemberPaging(max, start);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Member> listActive(Boolean isActive) {
        return memberRepo.listActive(isActive);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public List<Member> findByEmployee(int max, int start, String param) {
        return memberRepo.findByEmployee(max, start, param);
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public Integer count() {
        return memberRepo.count();
    }

    @Override
    @Transactional(Constants.TRANSACTION_MANAGER_CHAINED)
    public EntityManager getEntityManager() {
        return memberRepo.getEntityManager();
    }

}
