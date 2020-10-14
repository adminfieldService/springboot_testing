/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.Member;
import java.util.List;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface MemberServiceIface {

    Member create(Member entity);

    Member update(Member entity);

    Member delete(Member entity);

    Integer deleteBy(Long team_member_id);

    void remove(Member entity);

    Member findById(String paramString);

    List<Member> findByIdTeam(Long paramLong);

    List<Member> findByCaseId(String param);

    List<Member> findByEmpId(String param);//@Repository

    Integer updateFeeMember(Long teamMemberId, Long idEmployee, Double feeShare);

    Member findBy(Long idTeamMember, String employeeId);

}
