/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.OneToMany;
import javax.persistence.PrePersist;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_loan_type")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "LoanType.findAll", query = "SELECT lt FROM LoanType lt")})
public class LoanType implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @SequenceGenerator(name = "loantype_seq", sequenceName = "loantype_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "loantype_seq")
    @Column(name = "loan_type_id")
    private Long loantypeId;
    @Column(name = "loan_type")
    private String typeLoan;
    @Column(name = "dsc")
    private String dsc;

    @Column(name = "is_active")
    private Boolean isActive;

//    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
//    @JoinColumn(name = "engagement_id", referencedColumnName = "engagement_id")
//    protected Engagement engagement;
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "loantype")
    private Collection<Loan> laonCollection;

    @PrePersist
    public void onCreate() {
        isActive = true;
    }

    public LoanType() {
    }

    public void addLoan(Loan loan) {
        loan.setLoantype(this);
        laonCollection.add(loan);
    }

    public Long getLoantypeId() {
        return loantypeId;
    }

    public void setLoantypeId(Long loantypeId) {
        this.loantypeId = loantypeId;
    }

    public String getTypeLoan() {
        return typeLoan;
    }

    public void setTypeLoan(String typeLoan) {
        this.typeLoan = typeLoan.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getDsc() {
        return dsc;
    }

    public void setDsc(String dsc) {
        this.dsc = dsc.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

//    public Engagement getEngagementId() {
//        return engagementId;
//    }
//
//    public void setEngagementId(Engagement engagementId) {
//        this.engagementId = engagementId;
//    }
    public Collection<Loan> getLaonCollection() {
        return laonCollection;
    }

    public void setLaonCollection(Collection<Loan> laonCollection) {
        this.laonCollection = laonCollection;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    @Override
    public String toString() {
        return "com.lawfirm.apps.model.LoanType[loantypeId=" + this.loantypeId + " ]";
    }
}
