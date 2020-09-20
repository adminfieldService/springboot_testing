/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_team_member")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "TeamMember.findAll", query = "SELECT t FROM TeamMember t")})
public class TeamMember implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "team_member_seq", sequenceName = "team_member_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "team_member_seq")
    @Column(name = "team_member_id")
    private Long teamMemberId;
    @Column(name = "is_active")
    private Boolean isActive;
    @Column(name = "description")
    private String description;
    @Column(name = "dmp_id")
    private Long dmpId;
//    @Column(name = "dmp_portion")
//    private Double dmpPortion;
    @Column(name = "fee_share")
    private Double feeShare;
    @Column(name = "tahun_input", length = 10, nullable = true)
    protected String tahun_input;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
    protected Engagement engagement;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "id_employee", referencedColumnName = "id_employee")
//    protected Employee employee;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMember")
////    private Collection<Member> membertCollection;
//    private List<Member> membertCollection = new ArrayList<>();
    @OneToMany(cascade = CascadeType.MERGE, fetch = FetchType.LAZY, mappedBy = "teamMember")
    private List<Member> memberCollection = new ArrayList<>();
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMember")
//    private Collection<Financial> financialCollection;
//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "teamMember")
//    private Collection<Disbursement> disbursementCollection;

    public TeamMember() {
    }

    public TeamMember(Long teamMemberId, Boolean isActive, String description, Long dmpId, Double feeShare, String tahun_input, Engagement engagement) {
        this.teamMemberId = teamMemberId;
        this.isActive = isActive;
        this.description = description;
        this.dmpId = dmpId;
        this.feeShare = feeShare;
        this.tahun_input = tahun_input;
        this.engagement = engagement;
    }

    public void addMember(Member member) {
        member.setTeamMember(this);
        memberCollection.add(member);
    }

//    public void addfee(Double fee) {
//        feeShareCollection.add(fee);
//    }
    public Long getTeamMemberId() {
        return teamMemberId;
    }

    public void setTeamMemberId(Long teamMemberId) {
        this.teamMemberId = teamMemberId;
    }

    public Engagement getEngagement() {
        return engagement;
    }

    public void setEngagement(Engagement engagement) {
        this.engagement = engagement;
    }

//    public Collection<Financial> getFinancialCollection() {
//        return financialCollection;
//    }
//
//    public void setFinancialCollection(Collection<Financial> financialCollection) {
//        this.financialCollection = financialCollection;
//    }
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

//    public List<Double> getFeeShareCollection() {
//        return feeShareCollection;
//    }
//
//    public void setFeeShareCollection(List<Double> feeShareCollection) {
//        this.feeShareCollection = feeShareCollection;
//    }
    public Double getFeeShare() {
        return feeShare;
    }

    public void setFeeShare(Double feeShare) {
        this.feeShare = feeShare;
    }

    public List<Member> getMemberCollection() {
        return memberCollection;
    }

    public void setMemberCollection(List<Member> memberCollection) {
        this.memberCollection = memberCollection;
    }

    public Long getDmpId() {
        return dmpId;
    }

    public void setDmpId(Long dmpId) {
        this.dmpId = dmpId;
    }

    public String getTahun_input() {
        return tahun_input;
    }

    public void setTahun_input(String tahun_input) {
        this.tahun_input = tahun_input.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.TeamMember[teamMemberId=" + this.teamMemberId + " ]";
    }

}
