/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import java.io.Serializable;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_member")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "Member.findAll", query = "SELECT t FROM Member t")})
public class Member implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "member_seq", sequenceName = "member_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "member_seq")
    @Column(name = "member_id")
    private Long memberId;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "team_member_id", referencedColumnName = "team_member_id")
    private TeamMember teamMember;
    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
    private Employee employee;
    @Column(name = "description")
    private String description;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "feeShare")
    private Double feeShare;

    public Member() {
    }

    public Long getMemberId() {
        return memberId;
    }

    public void setMemberId(Long memberId) {
        this.memberId = memberId;
    }

    public TeamMember getTeamMember() {
        return teamMember;
    }

    public void setTeamMember(TeamMember teamMember) {
        this.teamMember = teamMember;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
                .replaceAll("<script>(.*?)</script>", "")
                .replaceAll("(?i)<.*?javascript:.*?>.*?</.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?/>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>", "")
                .replaceAll("(?i)<.*?\\s+on.*?>.*?</.*?>", "")
                .replaceAll("vbscript", "")
                .replaceAll("encode", "")
                .replaceAll("decode", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\'(.*?)\\\'", "")
                .replaceAll("src[\r\n]*=[\r\n]*\\\"(.*?)\\\"", "")
                .replaceAll("</script>", "")
                .replaceAll("<script(.*?)>", "")
                .replaceAll("eval\\((.*?)\\)", "")
                .replaceAll("expression\\((.*?)\\)", "");
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public Double getFeeShare() {
        return feeShare;
    }

    public void setFeeShare(Double feeShare) {
        this.feeShare = feeShare;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.Member[memberId=" + this.memberId + " ]";
    }
}
