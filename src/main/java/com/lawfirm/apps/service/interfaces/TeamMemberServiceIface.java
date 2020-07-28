/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.service.interfaces;

import com.lawfirm.apps.model.TeamMember;
import java.util.List;
import javax.persistence.EntityManager;
import org.springframework.stereotype.Service;

/**
 *
 * @author newbiecihuy
 */
@Service
public interface TeamMemberServiceIface {

    TeamMember create(TeamMember entity);

    TeamMember update(TeamMember entity);

    TeamMember delete(TeamMember entity);

    void remove(TeamMember entity);

    TeamMember findById(Long paramLong);

    TeamMember findByName(String namaVisit);

    List<TeamMember> listTeamMember();

    List<TeamMember> listActive(Boolean param);

    List<TeamMember> byName(Boolean isActive);

    Integer count();

    EntityManager getEntityManager();

    List<TeamMember> findByDmp(Long dmpId);
    
     List<TeamMember> listTeamMemberByEngagement(Long param);
}
