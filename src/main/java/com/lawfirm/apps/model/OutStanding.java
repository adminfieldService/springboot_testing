/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.lawfirm.apps.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import java.io.Serializable;
import java.util.Date;
import java.util.UUID;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.PrePersist;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author newbiecihuy
 */
@Entity
@Table(name = "tbl_outstanding")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "OutStanding.findAll", query = "SELECT o FROM OutStanding o")})
public class OutStanding implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "outstanding_uu_id")
    private String outstandingUUId;

    @Column(name = "outstanding_id")
    private String outstandingId;

    @Column(name = "reimburse_id")
    private Long reimburseId;

//    @Column(name = "loan_id")
//    private Long LoanId;
    @Column(name = "reimburse_amount")
    private Double reimburseAmount;

    @Column(name = "case_id")
    private String caseId;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "tahun_input", length = 10, nullable = true)
    private String tahun_input;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "Asia/Jakarta")
    @Column(name = "tgl_input", nullable = true)
    @Temporal(TemporalType.TIMESTAMP)
    private Date tgInput;

    @ManyToOne(cascade = CascadeType.MERGE, fetch = FetchType.LAZY)
    @JoinColumn(name = "loan_id", referencedColumnName = "id")
    private Loan loan;

    @PrePersist
    public void onCreate() {
        tgInput = new Date();
        isActive = true;
        this.setOutstandingUUId(UUID.randomUUID().toString());
    }

    public OutStanding() {
    }

    public OutStanding(String outstandingUUId, String outstandingId, Long reimburseId, Double reimburseAmount, String caseId, Boolean isActive, Long userId, String tahun_input, Date tgInput, Loan loan) {
        this.outstandingUUId = outstandingUUId;
        this.outstandingId = outstandingId;
        this.reimburseId = reimburseId;
        this.reimburseAmount = reimburseAmount;
        this.caseId = caseId;
        this.isActive = isActive;
        this.userId = userId;
        this.tahun_input = tahun_input;
        this.tgInput = tgInput;
        this.loan = loan;
    }

    public String getOutstandingUUId() {
        return outstandingUUId;
    }

    public void setOutstandingUUId(String outstandingUUId) {
        this.outstandingUUId = outstandingUUId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public String getOutstandingId() {
        return outstandingId;
    }

    public void setOutstandingId(String outstandingId) {
        this.outstandingId = outstandingId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public Long getReimburseId() {
        return reimburseId;
    }

    public void setReimburseId(Long reimburseId) {
        this.reimburseId = reimburseId;
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

    public Date getTgInput() {
        return tgInput;
    }

    public void setTgInput(Date tgInput) {
        this.tgInput = tgInput;
    }

    public Loan getLoan() {
        return loan;
    }

    public void setLoan(Loan loan) {
        this.loan = loan;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Double getReimburseAmount() {
        return reimburseAmount;
    }

    public void setReimburseAmount(Double reimburseAmount) {
        this.reimburseAmount = reimburseAmount;
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }

    public String getCaseId() {
        return caseId;
    }

    public void setCaseId(String caseId) {
        this.caseId = caseId;
    }

}
