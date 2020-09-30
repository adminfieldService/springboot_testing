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
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
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
@Table(name = "tbl_outstanding_loan_a")
@NamedQueries({
    @javax.persistence.NamedQuery(name = "OutStandingLoanA.findAll", query = "SELECT o FROM OutStandingLoanA o")})
public class OutStandingLoanA implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Column(name = "outstanding_a_uu_id")
    private String outstandingAUUId;

    @Column(name = "id_employee")
    private Long idEmployee;

    @Column(name = "disburse_id", length = 10)
    private String disburseId;

    @JsonFormat(pattern = "yyyy-MM-dd", timezone = "Asia/Jakarta")
    @Column(name = "cut_off_date", nullable = true)
    @Temporal(TemporalType.DATE)
    private Date cutOffDate;

    @Column(name = "tax_year", length = 10)
    private String taxYear;

    @Column(name = "total_loan_amount")
    private Double loanAmount;

    @Column(name = "disburseable_amount")
    private Double disburseableAmount;

    @Column(name = "outstanding_after_disbursement_amount")
    private Double outstandingADisbursement;

    @Column(name = "is_active")
    private Boolean isActive;

    @PrePersist
    public void onCreate() {
        this.isActive = true;
        this.setOutstandingAUUId(UUID.randomUUID().toString());
    }

    public OutStandingLoanA() {
    }

    public OutStandingLoanA(String outstandingAUUId, Long idEmployee, String disburseId, Date cutOffDate, String taxYear, Double loanAmount, Double disburseableAmount, Double outstandingADisbursement, Boolean isActive) {
        this.outstandingAUUId = outstandingAUUId;
        this.idEmployee = idEmployee;
        this.disburseId = disburseId;
        this.cutOffDate = cutOffDate;
        this.taxYear = taxYear;
        this.loanAmount = loanAmount;
        this.disburseableAmount = disburseableAmount;
        this.outstandingADisbursement = outstandingADisbursement;
        this.isActive = isActive;
    }
    
    

    

    public String getOutstandingAUUId() {
        return outstandingAUUId;
    }

    public void setOutstandingAUUId(String outstandingAUUId) {
        this.outstandingAUUId = outstandingAUUId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public Long getIdEmployee() {
        return idEmployee;
    }

    public void setIdEmployee(Long idEmployee) {
        this.idEmployee = idEmployee;
    }

    public String getDisburseId() {
        return disburseId;
    }

    public void setDisburseId(String disburseId) {
        this.disburseId = disburseId.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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

    public Date getCutOffDate() {
        return cutOffDate;
    }

    public void setCutOffDate(Date cutOffDate) {
        this.cutOffDate = cutOffDate;
    }

    public String getTaxYear() {
        return taxYear;
    }

    public void setTaxYear(String taxYear) {
        this.taxYear = taxYear.replaceAll("(?i)<script.*?>.*?</script.*?>", "")
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
                .replaceAll("expression\\((.*?)\\)", "");;
    }

    public Double getLoanAmount() {
        return loanAmount;
    }

    public void setLoanAmount(Double loanAmount) {
        this.loanAmount = loanAmount;
    }

    public Double getDisburseableAmount() {
        return disburseableAmount;
    }

    public void setDisburseableAmount(Double disburseableAmount) {
        this.disburseableAmount = disburseableAmount;
    }

    public Double getOutstandingADisbursement() {
        return outstandingADisbursement;
    }

    public void setOutstandingADisbursement(Double outstandingADisbursement) {
        this.outstandingADisbursement = Math.max(loanAmount - disburseableAmount, 0);
    }

    public Boolean getIsActive() {
        return isActive;
    }

    public void setIsActive(Boolean isActive) {
        this.isActive = isActive;
    }
    
    

}
